
/*TODO
 * 
 * 
 * cd/shutdown cycle
 * 
 * multithreading? into other
 * or shutdown cycle nonblocking
 * keyliustener?
 * 
 * 
 * 
 * change things into log rather than system print
 * 
 * comment out room methods
 * 
 * prevent machine rooms form opening and closing every time they have to be used as dumps
 * 
 * figure out how to prevent a cool to heat direct switch never passing over nothing asking
 * 
 * rename classes to easier to understand stuff
 * 
 * 
 * MAKE SURE FALLING EDGE/SHUTDOWN CYCLE IS NONBLOCKING
 * 
 * SHOW WHAT ZONES CHANGED DURING LAST CYCLE TO THIS CYCLE
 * new lists of previous and current?
 * 
 * 
 * HOWE TO REMOVE DELAY OPR MULTITHREAD THIS SHIT
 * 
 * figure out where to turn machines off
 * */




import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortException;


public class ACAndLightsV2 
{
	//declarations for easy typing in relayWrite()
	final static String on = "ON";
	final static String off = "OFF";
    private static DecimalFormat df2 = new DecimalFormat("#.##");

	
	public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{
		
		//open up the bms and port 
		BMSMethods bms = new BMSMethods();
		bms.portOpen();

		//room objects representing all of the rooms
		Room[] roomList = new Room[] 
		{
			new Room("Studio 1", 0, 22, "http://192.168.1.208/getData.json", 37, bms, bms.relayRead(37)),//0
			new Room("Studio 2", 0, 22, "http://192.168.1.211/getData.json", 46, bms, bms.relayRead(46)),//1
			new Room("Studio 3", 0, 22, "http://192.168.1.209/getData.json", 42, bms, bms.relayRead(42)),//2

			new Room("Booth 1",  0,  14, "http://192.168.1.207/getData.json", 36, bms, bms.relayRead(36)),//3
			new Room("Booth 2",  0,  14, "http://192.168.1.212/getData.json", 45, bms, bms.relayRead(45)),//4
			new Room("Booth 3",  0,  14, "http://192.168.1.214/getData.json", 43, bms, bms.relayRead(43)),//5

			new Room("Machine Room 1", 0, 11, "http://192.168.1.206/getData.json", 48, bms, bms.relayRead(48)),//6
			new Room("Machine Room 2", 0, 11, "http://192.168.1.206/getData.json", 49, bms, bms.relayRead(49)) //7
		};
		roomList[2].setTargetTemp(80);
		int i = 0;
		while(i==0)
		{
			ACThread(bms, roomList);
			Thread.sleep(100000);
			
			//i++;
		}
		
		
			
			
		bms.portClose();
	}//main end
	
	/**
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * 
	 * 
	 * */
	public static int ACThread(BMSMethods bms, Room[] list) throws SerialPortException, InterruptedException
	{
		
		//initial loop updates
		bms.massRefresh(list);

		//print all temps to see whats going on
		System.out.println("+-+-+-+-+START+-+-+-+-+");
		System.out.println("  CR1, CR2, CR3, BTH1, BTH2, BTH3, MR1, MR2");
		System.out.println(bms.printCurrentTemps(list));
		System.out.println(bms.printTargetTemps(list));
		System.out.println(bms.tempDifference(list));
		System.out.println(bms.printPreviousStates(list));
		System.out.println("----------");
		
		
		//handle to see what is the request
		Room[] requester = {};
		requester = bms.addRoomLists(requester, bms.requestingCold(list));
		requester = bms.addRoomLists(requester, bms.requestingHeat(list));
		requester = bms.addRoomLists(requester, bms.removeFromListPrevious(bms.requestingCutoffCooling(list), -1));
		requester = bms.addRoomLists(requester, bms.removeFromListPrevious(bms.requestingCutoffHeating(list), -1));
		bms.printRoomNames(requester);
		
		int currentRequest = bms.checkForConditioningStill(requester);
		
		
		
		//print out all of the starting lists
		Room[] needCooling = bms.requestingCold(list);
		System.out.print("cool= ");
		bms.printRoomNames(needCooling);
		Room[] needCoolingStill = bms.requestingCutoffCooling(list);
		System.out.print("cool++= ");
		bms.printRoomNames(needCoolingStill);
		Room[] needHeating = bms.requestingHeat(list);
		System.out.print("heat= ");
		bms.printRoomNames(needHeating);
		Room[] needHeatingStill = bms.requestingCutoffHeating(list);
		System.out.print("heat++= ");
		bms.printRoomNames(needHeatingStill);
		Room[] needNothing = bms.requestingNothing(list);
		System.out.print("nothing= ");
		bms.printRoomNames(needNothing);
		
		int neededAirflow = 0;
		int tally48 =0;
		int tally49 =0;
		
		System.out.println("----------Entering Room Decisions----------");
		//if the system is requesting cool
		if(currentRequest == 0)
		{
			//print out that the cooling is determined an starting
			System.out.println("Need Cooling");

			//gather preliminary lists of rooms to open and close
			Room[] openThisTime = needCooling;
			bms.massSetPreviousState(needCooling, 0);
			Room[] closeThisTime = bms.addRoomLists(needNothing, needHeating);
			
			//add to close the room who are in cooling but have a previous of -1
			//these are the rooms who have not started cooling but are still requesting cutOff Cool
			Room[] culledNotCooled = bms.removeFromListPrevious(needCoolingStill, -1);
			//other rooms in the above cull, rooms that are currently requesting cooling
			Room[] culledCooled = bms.removeFromListPrevious(needCoolingStill, 0);
			
			System.out.print("culled -1 = ");
			bms.printRoomNames(culledNotCooled);
			//bms.massUpdatePreviousState(culledNotCooled);
			System.out.print("culled 0 = ");
			bms.printRoomNames(culledCooled);
				
			//add to the close list the rooms who are above the cutoff but havnt started cooling
			closeThisTime = bms.addRoomLists(closeThisTime, culledCooled);
			openThisTime = bms.addRoomLists(openThisTime, culledNotCooled);

			
			//find total airflow before you start messing with MRs
			neededAirflow = bms.collectAirflow(openThisTime);
			System.out.println("neededAirflow= "+neededAirflow);
			
			//remove machine rooms to use as dampers
			//find if mr48 exists in the list
			for(Room i : openThisTime)
			{	
				if(i.getRoomName().equalsIgnoreCase("Machine Room 1"))
				{	
					tally48++;
				}
			}	
			//find if mr49 exists in the list
			for(Room i : openThisTime)
			{	
				if(i.getRoomName().equalsIgnoreCase("Machine Room 2"))
				{
					tally49++;
				}
			}	
			//check to see if MRs need cooling to keep tallied
			openThisTime = bms.removeMRs(openThisTime);
			closeThisTime = bms.removeMRs(closeThisTime);
			
			bms.massSetPreviousState(openThisTime, 0);
			bms.massSetPreviousState(closeThisTime, -1);
			
			System.out.println("48= "+tally48+", 49= "+tally49+"");
			System.out.print("cooling rooms= ");
			bms.printRoomNames(openThisTime);
			System.out.print("closing rooms= ");
			bms.printRoomNames(closeThisTime);
			
			
			
			
			bms.handleHVACMachines(neededAirflow, currentRequest);
			
			//handle neededAirflow changes in relation to machines turning on
			if(neededAirflow > 50)
				neededAirflow -=50;
			if(neededAirflow > 0)
				neededAirflow -=50;
			
			
			//handle dump zones
			bms.handleHVACDumpZones(neededAirflow, currentRequest);
			
			System.out.println("----------\nOpening/Closing Rooms");
			bms.openRoomForHVAC(openThisTime);
			bms.closeRoomForHVAC(closeThisTime);
			
			
			tally48+=bms.mr48;
			tally49+=bms.mr49;
			
			//final open/close check for MRs
			if(tally48 > 0)
			{
				list[6].acceptHVAC();
			}
			else
			{
				list[6].closeHVAC();
			}		
			
			if(tally49 > 0)
			{
				list[7].acceptHVAC();
			}
			else
			{
				list[7].closeHVAC();
			}
			
			//reset the global variable for MRs
			bms.mr48=0;
			bms.mr49=0;
			
			
			//close the ones who dont want 
			//close requesting heat
			//close requesting nothing
			
			
			//open requesting cold
			//open dump zones
			
			
			//turn on machines
			
		}//cool if end
		
		//heat if start
		else if(currentRequest == 1)
		{
			System.out.println("Need heating");
		}//heat if end	
		
		//all good start
		else if(currentRequest == -1)
		{
			System.out.println("Every room is satisfied!");
			//find all rooms, excempt MRs, close all rooms
			Room[] allOff = list;
			allOff = bms.removeMRs(allOff);
			bms.closeRoomForHVAC(allOff);
			bms.massSetPreviousState(list, -1);
			//open machine rooms
			list[6].acceptHVAC();
			list[7].acceptHVAC();
			bms.relayWrite(50, off);
			bms.relayWrite(51, off);
			bms.relayWrite(53, off);
			bms.relayWrite(54, off);
			System.out.println("Turned off all machines, resting");
			Thread.sleep(120000);
			System.out.println("Cooldown extra sleep ended");
			
		}//all good end
		System.out.println("----------Exiting Room Decisions----------");

		
		System.out.print("END= ");
		System.out.println(bms.printPreviousStates(list));

		
		
		
		
		return 1;
	}
	

}

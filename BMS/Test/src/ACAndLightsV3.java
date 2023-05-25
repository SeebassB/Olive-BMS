
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




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jssc.SerialPortException;


public class ACAndLightsV3
{
	//declarations for easy typing in relayWrite()
	final static String on = "ON";
	final static String off = "OFF";

	
	public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{
		
		//open up the bms and port 
		BMSMethods bms = new BMSMethods();
		bms.portOpen();

		//room objects representing all of the rooms
		Room[] roomList = new Room[] 
		{
				//  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
			new Room("CR 1", 0, 15, "http://192.168.1.241/getData.json", 37, bms, bms.relayRead(37)),//0
			new Room("CR 2", 0, 19, "http://192.168.1.211/getData.json", 46, bms, bms.relayRead(46)),//1
			new Room("CR 3", 0, 15, "http://192.168.1.209/getData.json", 42, bms, bms.relayRead(42)),//2

			new Room("Booth 1",  0,  14, "http://192.168.1.208/getData.json", 36, bms, bms.relayRead(36)),//3
			new Room("Booth 2",  0,  14, "http://192.168.1.212/getData.json", 45, bms, bms.relayRead(45)),//4
			new Room("Booth 3",  0,  14, "http://192.168.1.214/getData.json", 43, bms, bms.relayRead(43)),//5

			new Room("Machine Room 1", 0, 11, "http://192.168.1.206/getData.json", 48, bms, bms.relayRead(48)),//6
			new Room("Machine Room 2", 0, 11, "http://192.168.1.206/getData.json", 49, bms, bms.relayRead(49)) //7
		};
		
		
		//TEMP CAPACITY NUM<BERS
		//list of more rooms to use as secondary rooms, they do not ask for cold air, just open when they can accpet
		Room[] secondaryRoomList = new Room[] 
		{
			//  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
			new Room("Edit",        0, 7,  "http://192.168.1.251/getData.json", 41, bms, bms.relayRead(41)),//0
			new Room("Kitchen",     0, 11, "http://192.168.1.213/getData.json", 39, bms, bms.relayRead(39)),//1
			new Room("Hallway",     0, 11, "http://192.168.1.250/getData.json", 44, bms, bms.relayRead(44)),//2
			new Room("Phone Booth", 0, 7,  "http://192.168.1.252/getData.json", 47, bms, bms.relayRead(47)) //3
		};
		

		int i = 0;
		int isNight=0;//if day=0, if night=1
		
		while(i==0)
		{
		
			//setup a date only to use the hour to see if its between 8PM and 8AM (2000-0800)
			DateFormat dateFormat = new SimpleDateFormat("HH");
			Date date = new Date();
			
			//format the date as a timestamp for the log file
			int currentHour = Integer.parseInt(dateFormat.format(date));
			
			//change nightDay according to the results of the date hour
			if(currentHour>=20 || currentHour < 7)
			{
				isNight=1;
			}
			else
			{
				isNight=0;	
			}
			
			System.out.println("isNight="+isNight+", currentHour="+currentHour);
			//daytime
			//daytime set the temps to their base state of 74
			//hardcoded for now as a proof of concept
			if(isNight==0)
			{
				System.out.println("is daytime");
				roomList[0].setTargetTemp(74);
				roomList[1].setTargetTemp(74);
				roomList[2].setTargetTemp(74);
				roomList[3].setTargetTemp(74);
				roomList[4].setTargetTemp(74);
				roomList[5].setTargetTemp(73);
			}
			//night time
			//set all rooms sans MRs to a higher target temp to "turn them off"
			else if(isNight==1)
			{
				System.out.println("is night time");
				roomList[0].setTargetTemp(80);
				roomList[1].setTargetTemp(80);
				roomList[2].setTargetTemp(80);
				roomList[3].setTargetTemp(80);
				roomList[4].setTargetTemp(80);
				roomList[5].setTargetTemp(80);
			}
			
			
			
			
			ACThread(bms, roomList, secondaryRoomList);
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
	public static int ACThread(BMSMethods bms, Room[] list, Room[] dumpZones) throws SerialPortException, InterruptedException
	{
		
		//MR temp list to keep numbers the same
		Room[] list2 = list;
		
		//initial loop updates
		//refreshes all rooms so we have currentTemps and damper states
		bms.massRefresh(list);
		bms.massRefresh(dumpZones);
		
		//print all temps to see whats going on
		System.out.println("+-+-+-+-+START+-+-+-+-+");
		System.out.println("   CR1,  CR2,  CR3, BTH1, BTH2, BTH3,  MR1,  MR2, Edit, Kich, Hall, Phone");
		System.out.println(bms.printCurrentTemps(list)   +""+bms.printCurrentTemps(dumpZones));
		System.out.println(bms.printTargetTemps(list)    +""+bms.printTargetTemps(dumpZones));
		System.out.println(bms.tempDifference(list) 	 +""+bms.tempDifference(dumpZones));
		System.out.println(bms.printPreviousStates(list) +""+bms.printPreviousStates(dumpZones));		
						
		//put together a list of all of the rooms asking for conditioning
		//then run that list through checkForCoditioningStill() to see 
		//whether or not cold/heat is requested, with cold overriding heat
		//the priority is decided in the method checkForConditioningStill
		Room[] requester = {};
		requester = bms.addRoomLists(requester, bms.requestingCold(list));
		requester = bms.addRoomLists(requester, bms.requestingHeat(list));
		requester = bms.addRoomLists(requester, bms.removeFromListPrevious(bms.requestingCutoffCooling(list), -1));
		requester = bms.addRoomLists(requester, bms.removeFromListPrevious(bms.requestingCutoffHeating(list), -1));
		
		int currentRequest = bms.checkForConditioningStill(requester);
		//combine the main list with the secondary list to ease manipulation
		//done after the requester as the secondary rooms are not allowed to request cooling/heating
		list = bms.addRoomLists(list, dumpZones);
		
		//print out all of the starting lists
		Room[] needCooling      = bms.requestingCold(list);
		Room[] needCoolingStill = bms.requestingCutoffCooling(list);
		Room[] needHeating      = bms.requestingHeat(list);
		Room[] needHeatingStill = bms.requestingCutoffHeating(list);
		Room[] needNothing      = bms.requestingNothing(list);
		Room[] openThisTime     = new Room[0];
		Room[] closeThisTime    = new Room[0];
		
		//variables needed in the program
		int neededAirflow = 0;
		int tally48 =0;
		int tally49 =0;
		
		
		System.out.println("----------Entering Room Decisions----------");
		//if the system is requesting cool
		if(currentRequest == 0)
		{
			//print out that the cooling is determined an starting
			System.out.println("COOLING REQUESTED");			
			
			//gather preliminary lists of rooms to open and close
			openThisTime = needCooling;
			bms.massSetPreviousState(needCooling, 0);
			closeThisTime = bms.addRoomLists(needNothing, needHeating);
			
			//add to close the room who are in cooling but have a previous of -1
			//these are the rooms who have not started cooling but are still requesting cutOff Cool
			Room[] culledNotCooled = bms.removeFromListPrevious(needCoolingStill, -1);
			//other rooms in the above cull, rooms that are currently requesting cooling
			Room[] culledCooled = bms.removeFromListPrevious(needCoolingStill, 0);
			
				
			//add to the close list the rooms who are above the cutoff but havnt started cooling
			closeThisTime = bms.addRoomLists(closeThisTime, culledCooled);
			openThisTime = bms.addRoomLists(openThisTime, culledNotCooled);
			
			//find total airflow before you start messing with MRs
			neededAirflow = bms.collectAirflow(openThisTime);
			
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
			
			//update all rooms that are open to the PreviousState of 0, meaning the rooms are currently cooling
			bms.massSetPreviousState(openThisTime, 0);
			
			
		}//cool if end
		
		//heat if start
		else if(currentRequest == 1)
		{
			System.out.println("Need heating");
		}//heat if end	
		
		//handle if all rooms are satisfied, opens the MRs to let air flow, turns off all other rooms
		else if(currentRequest == -1)
		{
			System.out.println("Every room is satisfied!");
			//find all rooms, excempt MRs, close all rooms
			closeThisTime = bms.removeMRs(list);
			//open machine rooms
			tally48++;
			tally49++;
					
		}//all good end
		
		//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ROOM DECISIONS ENDED///////////////////////////////////////
		System.out.println("----------Exiting Room Decisions----------");

	
		//handle turning the machines on/off depending on which rooms request and heat/cool
		bms.handleHVACMachines(neededAirflow, currentRequest);
		
		//handle neededAirflow changes in relation to machines turning on
		//calculated here instead of through the method
		if(neededAirflow > 50)
			neededAirflow -=50;
		if(neededAirflow > 0)
			neededAirflow -=50;
		
		
		//handle dump zones
		bms.handleHVACDumpZones(neededAirflow, currentRequest);
		
		//open dampers according to the rooms in openThisTime
		System.out.println("----------");
		System.out.println("Open Rooms");
		bms.openRoomForHVAC(openThisTime);
		//close dampers not in use, found in closeThisTime
		System.out.println("----------\nClosed Rooms");
		bms.closeRoomForHVAC(closeThisTime);
		bms.massSetPreviousState(closeThisTime, -1);

		
		
		//add in MR flags from BMSMethods
		tally48+=bms.mr48;
		tally49+=bms.mr49;
		
		
		
		//UN-HARDCODE MAYBE???
		//MRS shouldnt be hardcoded as list[6] and 7, when using other lists it could generate issues
		//unsure how to fix
		
		//final open/close check for MRs
		if(tally48 > 0)
		{
			list2[6].acceptHVAC();
		}
		else
		{
			list2[6].closeHVAC();
		}		
		
		if(tally49 > 0)
		{
			list2[7].acceptHVAC();
		}
		else
		{
			list2[7].closeHVAC();
		}
		
		//reset the global variable for MRs
		bms.mr48=0;
		bms.mr49=0;
		
		//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\MACHINE ROOM HANDLING ENDED///////////////////////////////////////

		//list of all rooms in the building 
		//generate a full list of all rooms by adding both arrays into a new full one
		Room[] totalList = new Room[list2.length+dumpZones.length];
		int count=0;
		for(Room i: list2)
		{
			totalList[count] = i;
			count++;
		}
		
		for(Room d : dumpZones)
		{
			totalList[count] = d;
			count++;
		}	
		
		//run the logging method with a full list of all rooms
		bms.logBuildingStatus(totalList, currentRequest);
		
		
		//return the currentRequest for some reason
		return currentRequest;
	}
	

}

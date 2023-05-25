import jssc.SerialPortException;



/*TODO
 * 
 * 
 * FIX THE BUG WHRE SOME ROOMS END UP DUPLICATING
 * UNSURE WHY, FIXED BY ADDING IN THE UNALTERED ROOM LIST AND SETTING OURSIDEROOMLIST TO IT AT EVEY LOOP
 * 
 * probably has something to do with adding rooms into the list, as it wasnt an issue before as it would be remade evry time
 * 
 * 
 * 
 * 
 * */



public class HVACThread extends Thread
{

	Room[] outsideRoomList;
	static Room[] unalteredRoomList;
	Room[] outsideDumpList;
	BMSMethods outsideBMS;
	
	int threadStatus = 0;
	
	public HVACThread(BMSMethods bms, Room[] roomList, Room[] secondaryRoomList) 
	{
			outsideBMS = bms;
			outsideRoomList = roomList;
			outsideDumpList = secondaryRoomList;
			unalteredRoomList = roomList;
	}


	public void run()
	{
		try {
		
		HVACMultiTest.hvacThreadStatus=1;
				
		//MR temp list to keep numbers the same
		outsideRoomList = unalteredRoomList;
		Room[] list2 = outsideRoomList;
		
		//initial loop updates
		//refreshes all rooms so we have currentTemps and damper states
		outsideBMS.massRefresh(outsideRoomList);
		outsideBMS.massRefresh(outsideDumpList);
		
		//print all temps to see whats going on
		System.out.println("+-+-+-+-+START+-+-+-+-+");
		
		outsideBMS.printInfo(outsideRoomList, outsideDumpList);
		
		
		//put together a list of all of the rooms asking for conditioning
		//then run that list through checkForCoditioningStill() to see 
		//whether or not cold/heat is requested, with cold overriding heat
		//the priority is decided in the method checkForConditioningStill
		Room[] requester = {};
		//add to requester(list of rooms asking for HVAC) main rooms asking for cold/heat
		requester = outsideBMS.addRoomLists(requester, outsideBMS.requestingCold(outsideRoomList));
		requester = outsideBMS.addRoomLists(requester, outsideBMS.requestingHeat(outsideRoomList));
		//add to requester secondary rooms asking for cutOff cold/heat that are not currenly requesting
		requester = outsideBMS.addRoomLists(requester, outsideBMS.removeFromListPrevious(outsideBMS.requestingCutoffCooling(outsideRoomList), -1));
		requester = outsideBMS.addRoomLists(requester, outsideBMS.removeFromListPrevious(outsideBMS.requestingCutoffHeating(outsideRoomList), -1));
		
		//add the secondary rooms who are asking for heat
		//essentially promote them to primary rooms for heating only
		requester = outsideBMS.addRoomLists(requester, outsideBMS.requestingHeat(outsideDumpList));
		requester = outsideBMS.addRoomLists(requester, outsideBMS.removeFromListPrevious(outsideBMS.requestingCutoffHeating(outsideDumpList), -1));
		
		int currentRequest = outsideBMS.checkForConditioningStill(requester);
		//combine the main list with the secondary list to ease manipulation
		//done after the requester as the secondary rooms are not allowed to request cooling/heating
		outsideRoomList = outsideBMS.addRoomLists(outsideRoomList, outsideDumpList);
		
		//print out all of the starting lists
		Room[] needCooling      = outsideBMS.requestingCold(outsideRoomList);
		Room[] needCoolingStill = outsideBMS.requestingCutoffCooling(outsideRoomList);
		Room[] needHeating      = outsideBMS.requestingHeat(outsideRoomList);
		Room[] needHeatingStill = outsideBMS.requestingCutoffHeating(outsideRoomList);
		Room[] needNothing      = outsideBMS.requestingNothing(outsideRoomList);
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
			outsideBMS.massSetPreviousState(needCooling, 0);
			closeThisTime = outsideBMS.addRoomLists(needNothing, needHeating);
			closeThisTime = outsideBMS.addRoomLists(needNothing, needHeatingStill);

			
			//add to close the room who are in cooling but have a previous of -1
			//these are the rooms who have not started cooling but are still requesting cutOff Cool
			Room[] culledNotCooled = outsideBMS.removeFromListPrevious(needCoolingStill, -1);
			//other rooms in the above cull, rooms that are currently requesting cooling
			Room[] culledCooled = outsideBMS.removeFromListPrevious(needCoolingStill, 0);
			
				
			//add to the close list the rooms who are above the cutoff but havnt started cooling
			closeThisTime = outsideBMS.addRoomLists(closeThisTime, culledCooled);
			openThisTime = outsideBMS.addRoomLists(openThisTime, culledNotCooled);
			
			//find total airflow before you start messing with MRs
			neededAirflow = outsideBMS.collectAirflow(openThisTime);
			
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
			openThisTime = outsideBMS.removeMRs(openThisTime);
			closeThisTime = outsideBMS.removeMRs(closeThisTime);
			
			//update all rooms that are open to the PreviousState of 0, meaning the rooms are currently cooling
			outsideBMS.massSetPreviousState(openThisTime, 0);
			
			
		}//cool if end
		
		//heat if start
		else if(currentRequest == 1)
		{
			//print out that the cooling is determined an starting
			System.out.println("HEATING REQUESTED AND NO COOLING REQUEST");			
			
			//gather preliminary lists of rooms to open and close
			openThisTime = needHeating;
			outsideBMS.massSetPreviousState(needHeating, 1);
			closeThisTime = outsideBMS.addRoomLists(needNothing, needCooling);
			closeThisTime = outsideBMS.addRoomLists(needNothing, needCoolingStill);
			
			
			//add to close the room who are in cooling but have a previous of -1
			//these are the rooms who have not started heating but are still requesting cutOff Heat
			Room[] culledNotHeated = outsideBMS.removeFromListPrevious(needHeatingStill, -1);
			//other rooms in the above cull, rooms that are currently requesting heating
			Room[] culledHeated = outsideBMS.removeFromListPrevious(needHeatingStill, 1);
			
				
			//add to the close list the rooms who are above the cutoff but havnt started heating
			closeThisTime = outsideBMS.addRoomLists(closeThisTime, culledHeated);
			openThisTime = outsideBMS.addRoomLists(openThisTime, culledNotHeated);
			
			//find total airflow before you start messing with MRs
			neededAirflow = outsideBMS.collectAirflow(openThisTime);
			
			//close both MRs since we dont want heat going into the MR
			tally48=0;
			tally49=0;
			
			//check to see if MRs need cooling to keep tallied
			openThisTime = outsideBMS.removeMRs(openThisTime);
			closeThisTime = outsideBMS.removeMRs(closeThisTime);
			
			//update all rooms that are open to the PreviousState of 1, meaning the rooms are currently heating
			outsideBMS.massSetPreviousState(openThisTime, 1);
			
			
		}//heat if end	
		
		//handle if all rooms are satisfied, opens the MRs to let air flow, turns off all other rooms
		else if(currentRequest == -1)
		{
			System.out.println("Every room is satisfied!");
			//find all rooms, excempt MRs, close all rooms
			closeThisTime = outsideBMS.removeMRs(outsideRoomList);
			//open machine rooms
			tally48++;
			tally49++;
			
			//reset MR temp changes
			list2[6].setTargetTemp(74);
			list2[7].setTargetTemp(74);
			
					
		}//all good end
		
		//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ROOM DECISIONS ENDED///////////////////////////////////////
		System.out.println("----------Exiting Room Decisions----------");

	
		//handle turning the machines on/off depending on which rooms request and heat/cool
		outsideBMS.handleHVACMachines(neededAirflow, currentRequest);
		
		//handle neededAirflow changes in relation to machines turning on
		//calculated here instead of through the method
		if(neededAirflow > 50)
			neededAirflow -=50;
		if(neededAirflow > 0)
			neededAirflow -=50;
		
		
		//handle dump zones
		outsideBMS.handleHVACDumpZones(neededAirflow, currentRequest);
		
		//open dampers according to the rooms in openThisTime
		System.out.println("----------");
		System.out.println("Open Rooms");
		outsideBMS.openRoomForHVAC(openThisTime);
		//close dampers not in use, found in closeThisTime
		System.out.println("----------\nClosed Rooms");
		outsideBMS.closeRoomForHVAC(closeThisTime);
		outsideBMS.massSetPreviousState(closeThisTime, -1);

		
		
		//add in MR flags from BMSMethods
		tally48+=outsideBMS.mr48;
		tally49+=outsideBMS.mr49;
		
		
		
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
		outsideBMS.mr48=0;
		outsideBMS.mr49=0;
		
		//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\MACHINE ROOM HANDLING ENDED///////////////////////////////////////

		//list of all rooms in the building 
		//generate a full list of all rooms by adding both arrays into a new full one
		Room[] totalList = new Room[list2.length+outsideDumpList.length];
		int count=0;
		for(Room i: list2)
		{
			totalList[count] = i;
			count++;
		}
		
		for(Room d : outsideDumpList)
		{
			totalList[count] = d;
			count++;
		}	
		
		//run the logging method with a full list of all rooms
		outsideBMS.logBuildingStatus(totalList, currentRequest);
		
		HVACMultiTest.hvacThreadStatus=2;
		//sleep for 2 min
		Thread.sleep(1000 * 10 * 2 *outsideBMS.doubleOff);
		System.out.println("-");
		Thread.sleep(1000 * 10 * 2 *outsideBMS.doubleOff);
		System.out.println("-");
		Thread.sleep(1000 * 10 * 2 *outsideBMS.doubleOff);
		System.out.println("-");
		Thread.sleep(1000 * 10 * 2 *outsideBMS.doubleOff);
		System.out.println("-");
		Thread.sleep(1000 * 10 * 2 *outsideBMS.doubleOff);
		System.out.println("-");
		Thread.sleep(1000 * 10 * 2 *outsideBMS.doubleOff);
		System.out.println("-");
		HVACMultiTest.hvacThreadStatus=0;
		Thread.sleep(2000);
		
		
		outsideBMS.doubleOff=1;

		
		} catch (SerialPortException e) {
			System.out.println("PORT PROBLEM IN RUN");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("INTERRUPTION IN RUN");
			e.printStackTrace();
		}
		
	}
	
}

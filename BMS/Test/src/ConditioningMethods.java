import jssc.SerialPortException;



public class ConditioningMethods
{

	public void runConditioning(BMSMethods bms, Room[] primaryRoomList, Room[] secondaryRoomList) {
		try
		{
		
			//refreshes all rooms info in order to have currentTemps and damper states
			bms.massRefresh(primaryRoomList);
			bms.massRefresh(secondaryRoomList);
		
			//print all temps to see what's going on
			System.out.println("+-+-+-+-+START+-+-+-+-+");
			bms.printInfo(primaryRoomList, secondaryRoomList);
		

			//get all the starting lists
			Room[] needCooling      = bms.requestingCold(primaryRoomList);
			Room[] needCoolingStill = bms.requestingCutoffCooling(primaryRoomList);
			Room[] needTotalCooling = bms.addRoomLists(needCooling, needCoolingStill);
			Room[] needHeating      = bms.requestingHeat(primaryRoomList);
			Room[] needHeatingStill = bms.requestingCutoffHeating(primaryRoomList);
			Room[] needTotalHeating = bms.addRoomLists(needHeating, needHeatingStill);
			Room[] needNothing      = bms.requestingNothing(primaryRoomList);
			Room[] roomsOpenForThisCycle   = new Room[0]; //rooms that will open for this cycle
			Room[] roomsClosedForThisCycle = new Room[0]; //rooms that will close for this cycle
			Room[] machineRooms     = bms.findMRs(primaryRoomList);

			//keep track of airflow requirements
			char currentConditioningRequest = 'n';



			System.out.println("----------Entering Room Decisions----------");
			//if the system is requesting cool
			if(needCooling.length > 0)//-------------------------------------------------COOLING----------------------
			{
				//print out that the cooling is determined an starting
				System.out.println("COOLING REQUESTED");
				currentConditioningRequest = 'c';

				//gather lists of rooms to open and close

				//generate the open rooms list
				roomsOpenForThisCycle = needCooling; //open the rooms asking for cold
				//add rooms that are below target, above cutoff, and currently cooling
				bms.addRoomLists(roomsOpenForThisCycle, bms.removeFromListPrevious(needCoolingStill, 'n'));
				bms.massSetPreviousState(needCooling, 'c'); //set the rooms asking for cold's previous state to 0 (cooling)

				//generate the closed rooms list
				roomsClosedForThisCycle = bms.addRoomLists(needNothing, needTotalHeating); //close the rooms that are not asking for anything

			}//cool if end
		
			//heat if start
			else if(needHeating.length > 0)
			{
				//log the heating cycle
				System.out.println("HEATING REQUESTED AND NO COOLING REQUEST");

				currentConditioningRequest = 'h';

				//gather preliminary lists of rooms to open and close
				roomsOpenForThisCycle = needHeating;//get the rooms requesting heat
				//add rooms that are above the target, below the cutoff, and currently heating
				bms.addRoomLists(roomsOpenForThisCycle, bms.removeFromListPrevious(needHeatingStill, 'n'));
				bms.massSetPreviousState(needHeating, 'h');

				//generate the closed rooms list
				roomsClosedForThisCycle = bms.addRoomLists(needNothing, needTotalCooling);
			
			}//heat if end

			//handle if all rooms are satisfied, opens the MRs to let air flow, turns off all other rooms
			else
			{
				System.out.println("Every room is satisfied!");
				//open the MRs and close all other rooms
				roomsOpenForThisCycle = machineRooms;
				roomsClosedForThisCycle = bms.addRoomLists(bms.removeMRs(primaryRoomList), secondaryRoomList);

			}//all satisfied end
		
			//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ROOM DECISIONS ENDED///////////////////////////////////////
			System.out.println("----------Exiting Room Decisions----------");

			//get the total airflow needed this cycle
			int airflowRequestedByRooms = bms.addUpRequests(roomsOpenForThisCycle);
			//handle turning the machines on/off depending on which rooms request and heat/cool
			bms.handleHVACMachines(airflowRequestedByRooms, currentConditioningRequest);
		
			//the method handleHVACMachines will turn on one or two machines, depending on airflowRequestedByRooms
			//this block of code mimics the logic inside that method and is used subtract 50 if a machine is on and 100 if two are on
			//this is important to calculate the dump zones
			if(airflowRequestedByRooms > 50)
				airflowRequestedByRooms -= 50;
			if(airflowRequestedByRooms > 0)
				airflowRequestedByRooms -= 50;
		
		
			//handle dump zones
			bms.handleHVACDumpZones(airflowRequestedByRooms, currentConditioningRequest);
		
			//open dampers according to the rooms in openThisTime
			System.out.println("----------");
			System.out.println("Open Rooms");
			bms.openRoomForHVAC(roomsOpenForThisCycle);
			//close dampers not in use, found in closeThisTime
			System.out.println("----------\nClosed Rooms");
			bms.closeRoomForHVAC(roomsClosedForThisCycle);
			bms.massSetPreviousState(roomsClosedForThisCycle, 'n');

		


		
			//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\MACHINE ROOM HANDLING ENDED///////////////////////////////////////


		
			//run the logging method with a full list of all rooms
			bms.logBuildingStatus(bms.addRoomLists(roomsOpenForThisCycle, roomsClosedForThisCycle), currentConditioningRequest);
		
		}
		catch (SerialPortException e)
		{
			System.out.println("PORT PROBLEM IN RUN");
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			System.out.println("INTERRUPTION IN RUN");
			e. printStackTrace();
		}
		
	}//runConditioningEnd
	
}

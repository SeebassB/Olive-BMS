import jssc.SerialPortException;



public class ConditioningMethods
{

	public void runConditioning(BMSMethods bms) {
		try
		{
			Room[] primaryRoomList = bms.getPrimary();
			Room[] secondaryRoomList = bms.getSecondary();


			//refreshes all rooms info in order to have currentTemps and damper states
			bms.refreshAllRooms();

			//print all temps to see what's going on
			System.out.println("+-+-+-+-+START+-+-+-+-+");
			bms.printInfo();
		

			//get all the starting lists
			Room[] needCooling      = bms.requestingCold();//gather all C
			Room[] needCoolingStill = bms.removeFromListPrevious(bms.requestingCutoffCooling(), 'n');//remove from c all prev n
			Room[] needTotalCooling = bms.addRoomLists(needCooling, needCoolingStill);//combine above 2


			Room[] needHeating      = bms.requestingHeat();
			Room[] needHeatingStill = bms.removeFromListPrevious(bms.requestingCutoffHeating(primaryRoomList), 'n');
			Room[] needTotalHeating = bms.addRoomLists(needHeating, needHeatingStill);

			Room[] needNothing      = bms.requestingNothing(primaryRoomList);
			Room[] roomsOpenForThisCycle; //rooms that will open for this cycle
			Room[] roomsClosedForThisCycle; //rooms that will close for this cycle
			Room[] machineRooms = bms.findMRs();

			//keep track of airflow requirements
			char currentConditioningRequest = 'n';



			System.out.println("----------Entering Room Decisions----------");
			//if the system is requesting cool
			if(needTotalCooling.length > 0)//-------------------------------------------------COOLING----------------------
			{
				//print out that the cooling is determined an starting
				System.out.println("COOLING REQUESTED");
				currentConditioningRequest = 'c';

				//gather lists of rooms to open and close

				//generate the open rooms list
				roomsOpenForThisCycle = needTotalCooling; //open the rooms asking for cold
				bms.massSetPreviousState(needCooling, 'C'); //set the rooms asking for cold's previous state to 0 (cooling)
				bms.massSetPreviousState(needCoolingStill, 'c');

				//generate the closed rooms list
				roomsClosedForThisCycle = bms.addRoomLists(needNothing, needTotalHeating); //close the rooms that are not asking for anything
				bms.massSetPreviousState(roomsClosedForThisCycle, 'n');

			}

			else if(needHeating.length > 0)//heating
			{
				//log the heating cycle
				System.out.println("HEATING REQUESTED AND NO COOLING REQUEST");
				currentConditioningRequest = 'h';

				//generate the open rooms list
				roomsOpenForThisCycle = needTotalHeating;//get the rooms requesting heat
				bms.massSetPreviousState(needHeating, 'H');
				bms.massSetPreviousState(needHeatingStill, 'h');

				//generate the closed rooms list
				roomsClosedForThisCycle = bms.addRoomLists(needNothing, needTotalCooling);
				bms.massSetPreviousState(roomsClosedForThisCycle, 'n');

			}//heat if end

			//handle if all rooms are satisfied, opens the MRs to let air flow, turns off all other rooms
			else
			{
				System.out.println("Every room is satisfied!");
				//open the MRs and close all other rooms
				roomsOpenForThisCycle = machineRooms;
				bms.massSetPreviousState(roomsOpenForThisCycle, 'n');
				roomsClosedForThisCycle = bms.addRoomLists(bms.removeMRs(), secondaryRoomList);
				bms.massSetPreviousState(roomsClosedForThisCycle, 'n');

			}//all satisfied end


			//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ROOM DECISIONS ENDED///////////////////////////////////////
			System.out.println("----------Exiting Room Decisions----------");

			//get the total airflow needed this cycle
			int airflowRequestedByRooms = bms.findTotalAirflowRequested(roomsOpenForThisCycle);

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
			bms.openRoomsForHVAC(roomsOpenForThisCycle);
			//close dampers not in use, found in closeThisTime
			System.out.println("----------\nClosed Rooms");
			bms.closeRoomForHVAC(roomsClosedForThisCycle);
			bms.massSetPreviousState(roomsClosedForThisCycle, 'n');

		
			//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\MACHINE ROOM HANDLING ENDED///////////////////////////////////////


		
			//run the logging method with a full list of all rooms
			bms.logBuildingStatus(bms.addRoomLists(roomsOpenForThisCycle, roomsClosedForThisCycle), currentConditioningRequest);
		
		}
		catch (SerialPortException | InterruptedException e)
		{
			e.printStackTrace();
			System.err.println("ConditioningMethods has failed");
		}

		BMSMainController.mainStatusFlag = "normal";
	}//runConditioningEnd
	
}

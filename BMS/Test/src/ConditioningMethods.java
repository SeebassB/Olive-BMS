import jssc.SerialPortException;

public class ConditioningMethods
{

	int currentCoolMachine = 0;
	int currentHeatMachine = 0;
	int mr48 = 0;
	int mr49 = 0;
	int doubleOff =1;


	/**
	 * This method runs the HVAC cycle
	 * It determines which rooms need conditioning
	 * @param bms passed to give information on the building
	 * */
	public void runConditioning(BMSMethods bms) throws SerialPortException, InterruptedException
	{

		//keep track of airflow requirements
		char currentConditioningRequest = 'n';


		//refreshes all rooms info in order to have currentTemps and damper states
		bms.refreshAllRooms();

		//print all temps to see what's going on
		System.out.println("+-+-+-+-+START+-+-+-+-+");
		bms.printInfo();
		

		//collect current room statuses
		Room[] needCooling      = bms.requestingCold();//gather all C
		Room[] needCoolingStill = bms.removeFromListPrevious(bms.requestingCutoffCooling(), 'n');//remove from c all prev n
		Room[] needTotalCooling = bms.addRoomLists(needCooling, needCoolingStill);//combine above 2

		Room[] needHeating      = bms.requestingHeat();
		Room[] needHeatingStill = bms.removeFromListPrevious(bms.requestingCutoffHeating(bms.getPrimary()), 'n');
		Room[] needTotalHeating = bms.addRoomLists(needHeating, needHeatingStill);

		Room[] needNothing      = bms.requestingNothing(bms.getPrimary());
		Room[] roomsOpenForThisCycle; //rooms that will open for this cycle
		Room[] roomsClosedForThisCycle; //rooms that will close for this cycle
		Room[] machineRooms = bms.findMRs();


		System.out.println("----------Entering Room Decisions----------");

		//check if any room needs cooling
		//this is how the system prioritizes cooling over heating
		if(needTotalCooling.length > 0)
		{
			//print out that the cooling is determined an starting
			System.out.println("COOLING REQUESTED");
			currentConditioningRequest = 'c';

			//generate the open rooms list
			roomsOpenForThisCycle = needTotalCooling; //open the rooms asking for cold
			bms.massSetPreviousState(needCooling, 'C'); //set the rooms asking for cold's previous state to 0 (cooling)
			bms.massSetPreviousState(needCoolingStill, 'c');

			//generate the closed rooms list
			roomsClosedForThisCycle = bms.addRoomLists(needNothing, needTotalHeating); //close the rooms that are not asking for anything
			bms.massSetPreviousState(roomsClosedForThisCycle, 'n');
		}
		else if(needHeating.length > 0)//if no rooms need cooling and any need heating
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

		//if no rooms are requesting heat or cooling
		else
		{
			System.out.println("Every room is satisfied!");

			//open the MRs and close all other rooms
			roomsOpenForThisCycle = machineRooms;
			bms.massSetPreviousState(roomsOpenForThisCycle, 'n');
			roomsClosedForThisCycle = bms.addRoomLists(bms.removeMRs(), bms.getSecondary());
			bms.massSetPreviousState(roomsClosedForThisCycle, 'n');

		}//all satisfied end



		//conditioning has been determined, now we will calculate the airflow required
		System.out.println("----------Exiting Room Decisions----------");

		//get the total airflow needed this cycle
		int airflowRequestedByRooms = bms.findTotalAirflowRequested(roomsOpenForThisCycle);

		//handle turning the machines on/off depending on which rooms request and heat/cool
		handleHVACMachines(airflowRequestedByRooms, currentConditioningRequest);
		
		//the method handleHVACMachines will turn on one or two machines, depending on airflowRequestedByRooms
		//this block of code mimics the logic inside that method and is used subtract 50 if a machine is on and 100 if two are on
		//this is important to calculate the dump zones
		if(airflowRequestedByRooms > 50)
			airflowRequestedByRooms -= 50;
		if(airflowRequestedByRooms > 0)
			airflowRequestedByRooms -= 50;
		
		
		//handle dump zones
		handleHVACDumpZones(airflowRequestedByRooms, currentConditioningRequest);
		
		//open dampers according to the rooms in openThisTime
		System.out.println("----------");
		System.out.println("Open Rooms");
		bms.openRoomsForHVAC(roomsOpenForThisCycle);

		//close dampers not in use, found in closeThisTime
		System.out.println("----------\nClosed Rooms");
		bms.closeRoomForHVAC(roomsClosedForThisCycle);
		bms.massSetPreviousState(roomsClosedForThisCycle, 'n');


		//run the logging method with a full list of all rooms
		bms.logBuildingStatus(bms.addRoomLists(roomsOpenForThisCycle, roomsClosedForThisCycle), currentConditioningRequest);
		
		//set the main controller to normal
		BMSMainController.mainStatusFlag = "normal";

	}


	/**
	 * Method used to sort and run the machines based on how much air is needed
	 * @param currentRequested the amount of air needed from the machines
	 * @param hotCold whether you want heat or cold, c being cold, h being heat
	 * */
	public void handleHVACMachines(int currentRequested, char hotCold) throws SerialPortException, InterruptedException
	{

		char previousHVAC = 'n';
		BMSMainController.mainStatusFlag = "reading HVAC machines";

		//50 and 51 are machine 1, the (south)?? machine
		//arrays are both machines followed by their states
		int[][] coolingMachines = {{50, 53} , {0,0}};
		int[][] heatingMachines = {{51, 54} , {0,0}};

		System.out.println("Machine Request: "+currentRequested);

		//something strange is up with read so this is an "empty" read to reset the issue
		BMSMethods.relayRead(50);

		//read the machine's current states and adjust their arrays to match
		//sets the 2d array collingMachines to the current state of the actual machines
		if(BMSMethods.relayRead(50).equalsIgnoreCase("OFF"))
		{
			coolingMachines[1][0] = 0;
			System.out.println("Cooling Machine 50 is off");
		}
		else
		{
			coolingMachines[1][0] = 1;
			System.out.println("Cooling Machine 50 is on");

		}
		//second cooling machine
		if(BMSMethods.relayRead(53).equalsIgnoreCase("OFF"))
		{
			coolingMachines[1][1] = 0;
			System.out.println("Cooling Machine 53 is off");

		}
		else
		{
			coolingMachines[1][1] = 1;
			System.out.println("Cooling Machine 53 is on");

		}
		//first heating machine
		if(BMSMethods.relayRead(51).equalsIgnoreCase("OFF"))
		{
			heatingMachines[1][0] = 0;
			System.out.println("Heating Machine 51 is off");

		}
		else
		{
			heatingMachines[1][0] = 1;
			System.out.println("Heating Machine 51 is on");

		}
		//second heating machine
		if(BMSMethods.relayRead(54).equalsIgnoreCase("OFF"))
		{
			heatingMachines[1][1] = 0;
			System.out.println("Heating Machine 54 is off");

		}
		else
		{
			heatingMachines[1][1] = 1;
			System.out.println("Heating Machine 54 is on");
		}

		//adjust previousHVAC based on the machines that are currently on
		//if either cool machine is on then the previous state is cool or 0
		if(coolingMachines[1][0]+coolingMachines[1][1]>0)
			previousHVAC='c';
			//if either heat machine in on then the previous state is heat or 1
		else if(heatingMachines[1][0]+heatingMachines[1][1]>0)
			previousHVAC='h';

		//handle the purging cycle time
		int purgeTimer=180;//timer for the purge cycle which is 3 minutes 60s * 3 = 180seconds

		//check if current request and previous request are mismatched, if so trigger a purge cycle
		if((hotCold == 'c' && previousHVAC== 'h')||(hotCold == 'h' && previousHVAC== 'c'))
		{
			System.out.println("P U R G I N G");
			BMSMainController.mainStatusFlag = "purging";
			//turn off all units
			BMSMethods.relayWrite(50,"off");
			BMSMethods.relayWrite(53,"off");
			BMSMethods.relayWrite(51,"off");
			BMSMethods.relayWrite(54,"off");


			currentCoolMachine++;
			currentHeatMachine++;

			System.out.println("Turning all machines off for the purge cycle");
			while(purgeTimer>0)
			{
				Thread.sleep(1000 * 10);//10s
				System.out.println("seconds left in purge cycle = "+purgeTimer);
				purgeTimer-=10;
			}
		}

		System.out.println("----------\nMachine Decisions:");

		BMSMainController.mainStatusFlag = "HVAC Deciding";
		//machine decisions time
		//decide if both machines are needed
		//if this number is ever changed form 50, remember to change it in the main method as well
		if(currentRequested > 50)
		{
			//main cooling machine decisions
			if(hotCold == 'c')
			{

				BMSMethods.relayWrite(50, "on");
				BMSMethods.relayWrite(53, "on");
				System.out.println("Turning on both cooling machines!");
				BMSMainController.mainStatusFlag = "COOLING DOUBLE";
			}
			//main heating machine decisions
			else if(hotCold == 'h')
			{
				BMSMethods.relayWrite(51, "on");
				BMSMethods.relayWrite(54, "on");
				System.out.println("Turning on both heating machines!");
				BMSMainController.mainStatusFlag = "HEATING DOUBLE";
			}
		}
		//nothing requested
		else if(currentRequested == 0)
		{
			//all machines off
			BMSMethods.relayWrite(50, "off");
			BMSMethods.relayWrite(51, "off");
			BMSMethods.relayWrite(53,"off");
			BMSMethods.relayWrite(54,"off");
			currentCoolMachine++;
			currentHeatMachine++;
			System.out.println("Turning off all machines");
			BMSMainController.mainStatusFlag = "ALL HAPPY";
			//MRs set to open
			mr48++;
			mr49++;

		}
		//only 1 machine needed
		else if(currentRequested > 0)
		{
			int totalCool = coolingMachines[1][0] + coolingMachines[1][1];
			int totalHeat = heatingMachines[1][0] + heatingMachines[1][1];

			//check cooling
			if(hotCold == 'c')
			{
				//turn one machine on
				if(totalCool == 0)
				{
					BMSMethods.relayWrite(coolingMachines[0][currentCoolMachine%2], "on");//alternating machines
					System.out.println("Turning on 1 cooling machine, number= "+coolingMachines[0][currentCoolMachine%2]);
				}
				//leave as is because 1 machine is already on
				else if(totalCool == 1)
				{
					System.out.println("1 machine is already on");
				}
				//turn one machine off since both are running and only 1 is needed
				else if(totalCool == 2)
				{
					BMSMethods.relayWrite(coolingMachines[0][currentCoolMachine%2], "off");
					System.out.println("Turning off 1 machine, both were running, turning off "+coolingMachines[0][currentCoolMachine%2]);
					currentCoolMachine++;
					Thread.sleep(1000 * 60 * 2);//2m
					//temporary cooldown to prevent the extra pressure from the winding down machines from making noise in the pipes
					System.out.println("Opening MRs to allow for extra airflow temporarily");
					BMSMethods.openDamper(48);
					BMSMethods.openDamper(49);
					doubleOff = 3;
					mr48++;
					mr49++;

				}
				else
				{
					//Shouldnt ever happen, program needs to be fixed if this error shows up
					//maybe upgrade this to some sort of warning
					System.out.println("Problem with the 2d array ");
				}
				BMSMainController.mainStatusFlag = "COOLING SINGLE";
			}
			//check heating
			else if(hotCold == 'h')
			{
				//turn one machine on
				if(totalHeat == 0)
				{
					BMSMethods.relayWrite(heatingMachines[0][currentHeatMachine%2], "on");
					System.out.println("Turning on 1 heating machine, number= "+heatingMachines[0][currentHeatMachine%2]);
				}
				//leave as is cause 1 machine is already on
				else if(totalHeat == 1)
				{
					System.out.println("Requesting 1 machine on, "+heatingMachines[0][currentHeatMachine%2]+" is already on, no change");
				}
				//turn one machine off since both were one already
				else if(totalHeat == 2)
				{
					BMSMethods.relayWrite(heatingMachines[0][currentHeatMachine%2], "off");
					System.out.println("Turning off 1 machine, both were running, turning off "+heatingMachines[0][currentHeatMachine%2]);
					currentHeatMachine++;
				}
				BMSMainController.mainStatusFlag = "HEATING SINGLE";
			}
		}//one machine request if end
	}

	/**
	 * Method used to calculate dump zones, and when to open/close them
	 * @param currentRequested the current Airflow being requested
	 * @param hotCold whether the building is asking for hot/cold
	 * */
	public void handleHVACDumpZones(int currentRequested, char hotCold) throws SerialPortException, InterruptedException
	{
		//adjustment int
		int dumpActivateLevel = -30;

		//handle cold
		if(hotCold == 'c')
		{
			System.out.println("----------\nDumpZones for Cold Air");

			//starting from the top, subtract the dump zone's capacity until it reaches 0
			if(currentRequested < dumpActivateLevel)
			{
				mr48++;
				currentRequested += 13;
				System.out.println("Requesting MR1(48) open, capacity= "+currentRequested);
			}
			//48 not requested
			else
			{
				mr48=0;
				System.out.println("MR1(48) not requested as dump zone");
			}
			//49 requested
			if(currentRequested < dumpActivateLevel)
			{
				mr49++;
				currentRequested += 13;
				System.out.println("Requesting MR2(49) open, capacity= "+currentRequested);
			}
			//49 not requested
			else
			{
				mr49=0;
				System.out.println("MR2(49) not requested as dump zone");
			}

			//35 requested - OUTSIDE DUMP 1
			if(currentRequested < dumpActivateLevel)
			{
				BMSMethods.openDamper(35);
				currentRequested += 13;
				System.out.println("Requesting OUTSIDE 1 open, capacity= "+currentRequested);
			}
			//35 not requested - OUTSIDE DUMP 1
			else
			{
				BMSMethods.closeDamper(35);
				System.out.println("OUTSIDE DUMP 1 not requested");
			}

			//34 requested - OUTSIDE DUMP 2
			if(currentRequested < dumpActivateLevel)
			{
				BMSMethods.openDamper(34);
				currentRequested += 13;
				System.out.println("Requesting OUTSIDE 2 open, capacity= "+currentRequested);
			}
			//34 not requested - OUTSIDE DUMP 2
			else
			{
				BMSMethods.closeDamper(34);
				System.out.println("OUTSIDE 2 not requested as dump zone");
			}

		}

		//handle heat
		else if(hotCold == 'h')
		{
			//47 is the damper for the phone booth

			//Damp_Phone requested, opening
			if((currentRequested < dumpActivateLevel) || (BMSMethods.relayRead(47).equals("on")))
			{
				BMSMethods.openDamper(47);
				currentRequested += 9;
				System.out.println("Opening Phone Booth(47), capacity= "+currentRequested);
			}
			//44 not requested, closing
			else
			{
				BMSMethods.closeDamper(47);
				System.out.println("Phone Booth(47) not requested, final capacity= "+currentRequested);
			}

		}//hot else if end
		else if(hotCold == 'n')
		{

			System.out.println("Leaving MRs open to circulate air and prevent overpressure when the machines are winding down");
			mr48++;
			mr49++;
		}
		else
		{
			System.out.println("Problem with handleHVACDumpZones, reveived a non -1/0/1 hotCold");
		}

		System.out.println("Current dump leftover at="+currentRequested);
	}

}

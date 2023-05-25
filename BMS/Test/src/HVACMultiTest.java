import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jssc.SerialPortException;

public class HVACMultiTest 
{

		static Room[] outerAccessibleRoomList;
		static Room[] outerAccessibleDumpList;
		static int    hvacThreadStatus;
		static int    pause;
		static int	  heatFlag;
		
	public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{
		
		//open up the bms and port 
		BMSMethods bms = new BMSMethods();
		bms.portOpen();

		//room objects representing all of the rooms
		Room[] roomList = new Room[] 
		{
				//  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
			new Room("CR 1", 0, 17, "http://192.168.1.241/getData.json", 37, bms, bms.relayRead(37)),//0
			new Room("CR 2", 0, 21, "http://192.168.1.126/getData.json", 46, bms, bms.relayRead(46)),//1
			new Room("CR 3", 0, 17, "http://192.168.1.209/getData.json", 42, bms, bms.relayRead(42)),//2

			new Room("Booth 1",  0,  16, "http://192.168.1.208/getData.json", 36, bms, bms.relayRead(36)),//3
			new Room("Booth 2",  0,  16, "http://192.168.1.212/getData.json", 45, bms, bms.relayRead(45)),//4
			new Room("Booth 3",  0,  16, "http://192.168.1.214/getData.json", 43, bms, bms.relayRead(43)),//5

			new Room("Machine Room 1", 0, 13, "http://192.168.1.206/getData.json", 48, bms, bms.relayRead(48)),//6
			new Room("Machine Room 2", 0, 13, "http://192.168.1.206/getData.json", 49, bms, bms.relayRead(49)) //7
		};
		
		
		//list of more rooms to use as secondary rooms, they do not ask for cold air, just open when they can accpet
		Room[] secondaryRoomList = new Room[] 
		{
			//  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
			new Room("Edit",        0, 9,  "http://192.168.1.251/getData.json", 41, bms, bms.relayRead(41)),//0
			new Room("Kitchen",     0, 13, "http://192.168.1.213/getData.json", 39, bms, bms.relayRead(39)),//1
			new Room("Hallway",     0, 13, "http://192.168.1.250/getData.json", 44, bms, bms.relayRead(44)),//2
			new Room("Phone Booth", 0, 9,  "http://192.168.1.252/getData.json", 47, bms, bms.relayRead(47)) //3
		};
		outerAccessibleRoomList=roomList;
		outerAccessibleDumpList=secondaryRoomList;
		hvacThreadStatus=0;
		heatFlag=0;
		
		//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		//do the threading stuff here

		HVACThread hvac2;
		TextFieldLabel gui = new TextFieldLabel(bms);
		
		
		
		//timer stuff to actvate the morning routine
		DateFormat hourFormat      = new SimpleDateFormat("HH");
		DateFormat dayNumberFormat = new SimpleDateFormat("u");
  
		
		int timer=0;
		
		
		
		//============================================
		//SPACE FOR INITIAL STARTUP
		
		//outerAccessibleDumpList[0].setCoolHeat(1);
		//outerAccessibleDumpList[0].setTargetTemp(71);
		//outerAccessibleDumpList[1].setCoolHeat(1);
		//outerAccessibleDumpList[1].setTargetTemp(71);
		//outerAccessibleDumpList[2].setCoolHeat(1);
		//outerAccessibleDumpList[2].setTargetTemp(71);
		//outerAccessibleDumpList[3].setCoolHeat(1);
		//outerAccessibleDumpList[3].setTargetTemp(71);
		//gui.commandExecuter("edit 70");

		//gui.commandExecuter("edit heat");
		//
		//=========================================
		
		
		
		
		//main thread management loop
		while(hvacThreadStatus!=-1)//while hvacThreadStatus is no -1 which signifies 
		{
			
			//main thread loop
			while(pause==0)//while pause is off
			{	
				hvac2 = new HVACThread(bms,outerAccessibleRoomList,outerAccessibleDumpList);
				hvac2.start();
				
				//while the thread is alive, keep the main here until the thread ends
				while(hvac2.isAlive())
				{
					Thread.sleep(2000);//1s
				}
				
				
				//AUTOLAUNCH SEQUENCE HANDLING---------------------------
				
				//handle creating the date
				Date date = new Date();
				int hour = Integer.valueOf(hourFormat.format(date));
				int dayNumber = Integer.valueOf(dayNumberFormat.format(date));
				
				//set the timer to 0 when it isnt 7, resetting the one-time use for the below loop
				if(timer==1 && hour!=7)
				{
					roomList[0].setTargetTemp(74);
					roomList[1].setTargetTemp(74);
					roomList[2].setTargetTemp(74);
					roomList[3].setTargetTemp(74);
					roomList[4].setTargetTemp(74);
					roomList[5].setTargetTemp(74);
					roomList[6].setTargetTemp(74);//
					roomList[7].setTargetTemp(74);
					
					secondaryRoomList[0].setTargetTemp(75);
					secondaryRoomList[1].setTargetTemp(75);
					secondaryRoomList[2].setTargetTemp(75);
					secondaryRoomList[3].setTargetTemp(75);
				}
				if(hour!=7)
					timer=0;
					
				if(hour==22)
				{
					secondaryRoomList[0].setTargetTemp(80);
					secondaryRoomList[1].setTargetTemp(80);
					secondaryRoomList[2].setTargetTemp(80);
					secondaryRoomList[3].setTargetTemp(80);
					
				}
				//check for 7AM or 0700 and that it's the first time this is happening
				if(hour==7 && timer==0)
				{
					timer++;
					System.out.println("It is 0700, auto launch sequence initiated");
					//check if it's monday or wednesday to launch the studio for MARIA
					if(dayNumber==4)
					{
						//psuedo launch all studio command
						bms.launchAll();	
						gui.commandExecuter("studio on");

					}
					if(dayNumber!=7)
					{
					//set each of the inner rooms to a high enough temp as to not trigger a cooling cycle
				/*	roomList[0].setTargetTemp(80);
					roomList[1].setTargetTemp(80);
					roomList[2].setTargetTemp(80);
					roomList[3].setTargetTemp(80);
					roomList[4].setTargetTemp(80);
					roomList[5].setTargetTemp(80);
					//including MRs
					roomList[6].setTargetTemp(78);
					roomList[7].setTargetTemp(78);
					secondaryRoomList[0].setTargetTemp(70);
					secondaryRoomList[0].setCoolHeat(1);
					secondaryRoomList[1].setTargetTemp(70);
					secondaryRoomList[1].setCoolHeat(1);
					secondaryRoomList[2].setTargetTemp(70);
					secondaryRoomList[2].setCoolHeat(1);
					secondaryRoomList[3].setTargetTemp(70);
					secondaryRoomList[3].setCoolHeat(1);
					*/
						}
				}			
				
			}//pause=0 end
			

			
			//else
			if (pause!=0)//if pause != 0 then the system is currently paused, will break pause if anyroom gets over 76
			{	
				System.out.println("System PAUSED");
				Thread.sleep(1000 * 15);//15s 
				System.out.println("--");
				Thread.sleep(1000 * 15);//15s
				
				if(bms.findHighestTemp(outerAccessibleRoomList)>76)
				{
					System.out.println("SYSTEM BREAKING STASIS");
					pause=0;
				}
			}//pause!=0 end
		
			

		
		
		
		}//thread status while end
		
		
			
		bms.portClose();
	}//main end
	
	
	
	
	
	
	
}

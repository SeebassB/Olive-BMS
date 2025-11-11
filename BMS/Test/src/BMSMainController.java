import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jssc.SerialPortException;

public class BMSMainController
{


	static int mainStatusFlag = 0;

	public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{
		
		//open up the bms and port 
		BMSMethods bms = new BMSMethods();
		ConditioningMethods cond = new ConditioningMethods();
		bms.portOpen();

		//room objects representing all rooms
		Room[] primaryRoomList = new Room[]
		{
				//  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
			new Room("CR 1", 'n', 17, "http://192.168.1.241/getData.json", 37, bms, bms.relayRead(37)),//0
			new Room("CR 2", 'n', 21, "http://192.168.1.126/getData.json", 46, bms, bms.relayRead(46)),//1
			new Room("CR 3", 'n', 17, "http://192.168.1.209/getData.json", 42, bms, bms.relayRead(42)),//2

			new Room("Booth 1",  'n',  16, "http://192.168.1.208/getData.json", 36, bms, bms.relayRead(36)),//3
			new Room("Booth 2",  'n',  16, "http://192.168.1.212/getData.json", 45, bms, bms.relayRead(45)),//4
			new Room("Booth 3",  'n',  16, "http://192.168.1.214/getData.json", 43, bms, bms.relayRead(43)),//5

			new Room("Machine Room 1", 'n', 13, "http://192.168.1.206/getData.json", 48, bms, bms.relayRead(48)),//6
			new Room("Machine Room 2", 'n', 13, "http://192.168.1.206/getData.json", 49, bms, bms.relayRead(49)),//7
			new Room("Edit",           'n', 9,  "http://192.168.1.251/getData.json", 41, bms, bms.relayRead(41)) //8
		};
		
		
		//list of more rooms to use as secondary rooms, they do not ask for cold air, just open when they can accpet
		Room[] secondaryRoomList = new Room[] 
		{
			//  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
			new Room("Kitchen",     'n', 13, "http://192.168.1.213/getData.json", 39, bms, bms.relayRead(39)),//0
			new Room("Hallway",     'n', 13, "http://192.168.1.250/getData.json", 44, bms, bms.relayRead(44)),//1
			new Room("Phone Booth", 'n', 9,  "http://192.168.1.252/getData.json", 47, bms, bms.relayRead(47)) //2
		};


		//handle the general set up
        V2UITesting gui = new V2UITesting(bms, primaryRoomList, secondaryRoomList);

		//main thread management loop
		while(mainStatusFlag != -1)//while hvacThreadStatus is not -1 which signifies
		{

			switch(mainStatusFlag)//regular operation
			{
				case 0:
					//UPDATE GUI
                    cond.runConditioning(bms,primaryRoomList,secondaryRoomList);
					gui.setBmsInput(bms);//update the GUI's bms to reflect changes in the building
					Thread.sleep(1 * 60 * 1000);//sleep this main thread for X time   gui.setBmsInput(bms);
					gui.setBmsInput(bms);
					Thread.sleep(1 * 60 * 1000);  gui.setBmsInput(bms);
					gui.setBmsInput(bms);
					Thread.sleep(1 * 60 * 1000);   gui.setBmsInput(bms);
					break;

				case 1:
					System.out.println("Entering pause");
					while(mainStatusFlag != -1)
					{
						Thread.sleep(1 * 60 * 1000);//sleep for a minute
					}
					System.out.println("Exiting pause");
					break;

				case 2:
					System.out.println("Shutting down");
					mainStatusFlag = -1;
			}


		}//mainStatusFlag while end


		bms.portClose();
	}//main end
	
}

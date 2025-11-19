import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jssc.SerialPortException;

public class BMSMainController
{

	static BMSMethods bms = new BMSMethods();
	static int mainStatusFlag = 0;

	static Room[] primaryRoomList;//important rooms, can request conditioning
	static Room[] secondaryRoomList;//second class rooms, cannot request, can only open

	//both room list assignments
	static
	{
        try
		{
			primaryRoomList = new Room[]
            {
                //  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
                new Room("CR 1", 'n', 17, "http://192.168.1.241/getData.json", 37, bms, bms.relayRead(37)),//0
                new Room("CR 2", 'n', 21, "http://192.168.1.126/getData.json", 46, bms, bms.relayRead(46)),//1
                new Room("CR 3", 'n', 17, "http://192.168.1.209/getData.json", 42, bms, bms.relayRead(42)),//2

                new Room("Booth 1",  'n',  16, "http://192.168.1.208/getData.json", 36, bms, bms.relayRead(36)),//3
                new Room("Booth 2",  'n',  16, "http://192.168.1.212/getData.json", 45, bms, bms.relayRead(45)),//4
                new Room("Booth 3",  'n',  16, "http://192.168.1.214/getData.json", 43, bms, bms.relayRead(43)),//5

                new Room("Machine Room 1", 'c', 13, "http://192.168.1.206/getData.json", 48, bms, bms.relayRead(48)),//6
                new Room("Machine Room 2", 'c', 13, "http://192.168.1.206/getData.json", 49, bms, bms.relayRead(49)),//7
                new Room("Edit",           'n', 9,  "http://192.168.1.251/getData.json", 41, bms, bms.relayRead(41)) //8
            };

            secondaryRoomList = new Room[]
            {
                //  Room Name, HotCold, percentage, IP, damper number, BMS, damperPosition
                new Room("Kitchen",     'n', 13, "http://192.168.1.213/getData.json", 39, bms, bms.relayRead(39)),//0
                new Room("Hallway",     'n', 13, "http://192.168.1.250/getData.json", 44, bms, bms.relayRead(44)),//1
                new Room("Phone Booth", 'n', 9,  "http://192.168.1.252/getData.json", 47, bms, bms.relayRead(47)) //2
            };
        }
		catch (SerialPortException | InterruptedException e)
		{
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{
		System.out.println("asdasdadsadas");
		//open up the bms and port 
		ConditioningMethods cond = new ConditioningMethods();
		bms.portOpen();

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
					Thread.sleep(3 * 60 * 1000);//sleep this main thread for X time   gui.setBmsInput(bms);
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

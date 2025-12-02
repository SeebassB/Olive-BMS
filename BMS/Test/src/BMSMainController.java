import java.io.IOException;
import java.net.MalformedURLException;

import jssc.SerialPortException;

public class BMSMainController
{


	static BMSMethods bms;

    static {
        try {
            bms = new BMSMethods();
        } catch (SerialPortException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static String mainStatusFlag = "normal";


    public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{


		System.out.println("asdasdadsadas");

		bms.portOpen();
        bms.refreshAllRooms();
        bms.printInfo();

		//open up the bms and port
		ConditioningMethods cond = new ConditioningMethods();

		//handle the general set up
        V2UITesting gui = new V2UITesting(bms);

		//main thread management loop
		while(!mainStatusFlag.equalsIgnoreCase( "QUIT"))//while hvacThreadStatus is not -1 which signifies
		{
			System.out.println("before teh switch = "+mainStatusFlag);
            bms.refreshAllRooms();
			switch(mainStatusFlag)//regular operation
			{
				case "normal":
					//UPDATE GUI
                    cond.runConditioning(bms);
					bms.printInfo();
                    Thread.sleep(3 * 60 * 1000);//sleep this main thread for X time   gui.setBmsInput(bms);
					break;

				case "pause":
					System.out.println("Entering pause");
					while(mainStatusFlag != "ESCAPE")
					{
						Thread.sleep(1 * 60 * 1000);//sleep for a minute
					}
					System.out.println("Exiting pause");
					break;

				case "QUIT":
					System.out.println("Shutting down");
			}


		}//mainStatusFlag while end


		bms.portClose();
	}//main end
	
}

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import jssc.SerialPortException;

public class BMSMainController
{


	static String mainStatusFlag = "normal";
	static BMSMethods bms;

    static
	{
        try
		{
            bms = new BMSMethods();
        }
		catch (SerialPortException | InterruptedException e)
		{
			throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{


		System.out.println("asdasdadsadas");

		//TODO port selector tool if missing port


		//start up BMS
		bms.portOpen();
        bms.refreshAllRooms();
        bms.printInfo();

		ConditioningMethods cond = new ConditioningMethods();

		//startup GUI
        V2UITesting gui = new V2UITesting(bms);

		//main thread management loop
		while(!mainStatusFlag.equalsIgnoreCase( "QUIT"))//while hvacThreadStatus is not -1 which signifies
		{
			System.out.println("before teh switch = "+mainStatusFlag);
            bms.refreshAllRooms();
			gui.update(bms);
			switch(mainStatusFlag)//regular operation
			{
				case "normal":
					//UPDATE GUI
                    cond.runConditioning(bms);
					bms.printInfo();
					Thread.sleep(1 * 60 * 1000);//sleep this main thread for X time, one minute
					gui.update(bms);
					Thread.sleep(1 * 60 * 1000);
					gui.update(bms);
					Thread.sleep(1 * 60 * 1000);
					gui.update(bms);
					break;

				case "pause":
					System.out.println("Entering pause");
					while(!Objects.equals(mainStatusFlag, "normal"))
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

/*TODO
* Make it prettier
* Add pause mode
* Add a direct relay manipulator
* Add tab and space to hit buttons
* Add shortcuts?
* Rework logging
* GUI Timer for power button
* Power off double check
*
*/
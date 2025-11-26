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


		BMSMethods bms = new BMSMethods();
		System.out.println("asdasdadsadas");
		bms.printInfo();
		bms.portOpen();


		//open up the bms and port
		ConditioningMethods cond = new ConditioningMethods();

		//handle the general set up
        V2UITesting gui = new V2UITesting(bms);

		//main thread management loop
		while(mainStatusFlag != -1)//while hvacThreadStatus is not -1 which signifies
		{

			switch(mainStatusFlag)//regular operation
			{
				case 0:
					//UPDATE GUI
                    cond.runConditioning(bms);
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

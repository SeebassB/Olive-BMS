import java.io.IOException;
import java.util.Objects;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.swing.*;

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
			System.out.println("Error in BMSMainController, static try/catch block for bms");
			throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException
	{

		//start up BMS
		bms.portOpen();

		//this is here in case the port is missing (USB is unplugged)
		Object[] options;
		while(!BMSMethods.relayBoard.isOpened())
		{
			System.out.println("Port not opened");
			options = SerialPortList.getPortNames();

			if(options.length == 0)
			{
				System.out.println("NO PORTS IN LIST");
			}
			else if(options.length == 1)
			{
				System.out.println("ONLY ONE PORT, TRYING");
				System.out.println("Port name = "+options[0]);
				BMSMethods.relayBoard = (SerialPort) options[0];
				bms.portOpen();
				//TODO ADD IMPORTANT VERY IMPORTANT LOG
			}
			else {
				BMSMethods.relayBoard = (SerialPort) JOptionPane.showInputDialog(null, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				bms.portOpen();
			}

			Thread.sleep(2000);
		}

		//holds conditioning methods, split for clarity
		ConditioningMethods cond = new ConditioningMethods();

		//startup GUI
        GUIController gui = new GUIController(bms);

		//main thread management loop
		while(!mainStatusFlag.equalsIgnoreCase( "QUIT"))//while hvacThreadStatus is not -1 which signifies
		{
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

				case "maintenance":
					System.out.println("System entering maintenance mode");
					while(!Objects.equals(mainStatusFlag, "normal"))
					{
						Thread.sleep(1 * 30 * 1000);//sleep for a minute
						gui.update(bms);
						bms.printInfo();
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
* Add tab and space to hit buttons
* Add shortcuts?
* Rework logging
*/
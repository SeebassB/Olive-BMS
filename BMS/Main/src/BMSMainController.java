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
			BMSMethods.logInfo("BMS was unable to be created", "ERROR");
			e.printStackTrace();
			throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException
	{

		//start up BMS
		bms.portOpen();



		BMSMethods.logInfo("Beginning of BMSMainController","IMPORTANT");

		//holds conditioning methods, split for clarity
		ConditioningMethods cond = new ConditioningMethods(bms);

		//startup GUI
        GUIController gui = new GUIController(bms);

		//main thread management loop
		while(!mainStatusFlag.equalsIgnoreCase( "QUIT"))//while hvacThreadStatus is not -1 which signifies
		{
			BMSMethods.logInfo("Start BMS main loop","INFO");

			if(!bms.relayBoard.isOpened())
			{
				BMSMethods.logInfo("PORT ERROR, CABLE DISCONNECTED?", "WARNING");
				portCheck();
			}

			bms.refreshAllRooms();
			gui.update(bms);

			BMSMethods.logInfo("BMSController status: "+mainStatusFlag,"INFO");
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


		BMSMethods.logInfo("End of BMSMainController","IMPORTANT");
		bms.portClose();
	}//main end


	/**
	 * Check to see if the port is still connected.
	 * If not then enter a while loop until a port is selected.
	 * Presents a dialog box that will hopefully aid in the rectification of the issue.
	 * */
	public static void portCheck() throws InterruptedException {
		//this is here in case the port is missing (USB is unplugged)
		Object[] options;
		while(!bms.relayBoard.isOpened())
		{
			BMSMethods.logInfo("RUNNING PORT FIXER","WARNING");
			options = SerialPortList.getPortNames();

			if(options.length == 0)
			{
				BMSMethods.logInfo("NO PORTS IN LIST", "WARNING");
				Thread.sleep(3 * 1000);
			}
			else if(options.length == 1)
			{
				BMSMethods.logInfo("Only one COM port found, trying "+options[0], "WARNING");
				bms.setRelayBoard((SerialPort) options[0]);
				bms.portOpen();

			}
			else
			{
				BMSMethods.logInfo("Multiple COM ports found, asking user","WARNING");
				bms.setRelayBoard((SerialPort) JOptionPane.showInputDialog(null, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
				bms.portOpen();

			}
			if(bms.relayBoard.isOpened())
				return;
		}
		return;
	}

}

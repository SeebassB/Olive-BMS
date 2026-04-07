import java.util.Objects;

import com.fazecast.jSerialComm.SerialPort;


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
		catch (InterruptedException e)
		{
			BMSMethods.logInfo("BMS was unable to be created", "ERROR");
			e.printStackTrace();
			throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException
	{

		//start up BMS
		//if(!bms.relayBoard.isOpen()) TO BE WORKED ON
			bms.relayBoard.openPort();



		BMSMethods.logInfo("Beginning of BMSMainController","IMPORTANT");

		//holds conditioning methods, split for clarity
		ConditioningMethods cond = new ConditioningMethods(bms);

		//startup GUI
        GUIController gui = new GUIController(bms);

		//main thread management loop
		while(!mainStatusFlag.equalsIgnoreCase( "QUIT"))//while hvacThreadStatus is not -1 which signifies
		{
			BMSMethods.logInfo("Start BMS main loop","INFO");

			if(!bms.relayBoard.isOpen())
			{
				BMSMethods.logInfo("PORT ERROR, CABLE DISCONNECTED?", "WARNING");
				portCheck();
			}

			bms.refreshAllRooms();
			bms.extremeTempCheck();
			gui.update(bms);

			BMSMethods.logInfo("BMSController status: "+mainStatusFlag,"INFO");
			switch(mainStatusFlag)//regular operation
			{
				case "normal":
					//UPDATE GUI
                    cond.runConditioning(bms);
					bms.printInfo();
					Thread.sleep(60 * 1000);//sleep this main thread for X time, one minute
					bms.refreshAllRooms();
					gui.update(bms);
					Thread.sleep(60 * 1000);
					bms.refreshAllRooms();
					gui.update(bms);
					Thread.sleep(60 * 1000);
					bms.refreshAllRooms();
					gui.update(bms);
					break;

				case "maintenance":
					System.out.println("System entering maintenance mode");
					while(!Objects.equals(mainStatusFlag, "normal"))
					{
						Thread.sleep(1 * 30 * 1000);//sleep for a minute
						bms.refreshAllRooms();
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
		bms.relayBoard.closePort();
	}//main end


	/**
	 * Check to see if the port is still connected.
	 * If not then enter a while loop until a port is selected.
	 * Presents a dialog box that will hopefully aid in the rectification of the issue.
	 * */
	public static void portCheck() throws InterruptedException {
		//this is here in case the port is missing (USB is unplugged)
		Object[] options;
		while(!bms.relayBoard.isOpen())
		{
			BMSMethods.logInfo("RUNNING PORT FIXER","WARNING");
			options = SerialPort.getCommPorts();

			if(options.length == 0)
			{
				BMSMethods.logInfo("NO PORTS IN LIST", "WARNING");
				Thread.sleep(3 * 1000);
			}
			else if(options.length == 1)
			{
				BMSMethods.logInfo("Only one COM port found, trying "+options[0], "WARNING");
				bms.setRelayBoard((SerialPort) options[0]);
				bms.relayBoard.openPort();

			}
			else
			{
				BMSMethods.logInfo("Multiple COM ports found, asking user","WARNING");
				bms.setRelayBoard((SerialPort) JOptionPane.showInputDialog(null, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
				bms.relayBoard.openPort();

			}
			if(bms.relayBoard.isOpen())
				return;
		}
	}//portCheck ending
}

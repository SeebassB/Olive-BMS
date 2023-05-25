import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;


/*  TODO 1/28/20
 * 
 * 	firgure out CR2 Lights, ask carlols to move it to another contacto
 * 
 * 
 *  WarningPrint() make sure the warning little box is apporpriate/not default
 * 
 * 
 * 
 * 
 * gets and sets that chris needs? do those come from the array Obj?
 * 
 * add error handling to portRectifier()
 * 
 * WARNING SUPRESSION??
 * 
 * ADD IMPORTANT LOGS TO LAUNCH/SHUTDOWNS
 * 
 * warning log also does important log?
 * 
 * 
 * 
 * 
 * CHECKLIST OF COMPLETELY DONE METHODS THAT ARE COOL AND DONT NEED TO BE TOUVHED ANYMORE
 * logPrint
 * logImportantPrint
 * debugPrint
 * TODO WARNINGPRINT
 * adcRead
 * relayWrite
 * relayRead
 * formatOutput
 * 
 * 
 * 
 * 
 * 
 */

//nice color #4287f5


public class BMSMethods extends Thread
{

	
	//const list of relays and all of the connections to the boards
	
	final int CR1_Right_Speaker  = 21; //Contactor 1  //Breakers 1  and 3  //to be reviewed
	final int CR1_Middle_Speaker = 22; //Contactor 2  //Breakers 5  and 7  //to be reviewed
	final int CR1_Left_Speaker   = 23; //Contactor 3  //Breakers 9  and 11 //to be reviewed
	final int CR1_Desk           = 24; //Contactor 4  //Breakers 13 and 15 
	final int BTH1_Power         = 25; //Contactor 5  //Breakers 17 and 19
	
	final int CR2_Right_Speaker  = 14; //Contactor 8  //Breakers 29 and 31
	final int CR2_Middle_Speaker = 27; //Contactor 7  //Breakers 25 and 27
	final int CR2_Left_Speaker   = 26; //Contactor 6  //Breakers 21 and 23
	final int CR2_Desk           = 15; //Contactor 9  //Breakers 33 and 35
	final int BTH2_Power         = 16; //Contactor 10 //Breakers 37 and 39
	
	final int CR3_Right_Speaker  = 19; //Contactor 13 //Breakers 14 and 16
	final int CR3_Middle_Speaker = 17; //Contactor 11 //Breakers 6  and 8
	final int CR3_Left_Speaker   = 18; //Contactor 12 //Breakers 10 and 12
	final int CR3_Desk           = 20; //Contactor 14 //Breakers 18 and 20
	final int BTH3_Power         =  7; //Contactor 15 //Breakers 22 and 24
	
	
	final int CR1_Lights         =  8; //Contactor 16  //Breakers 26 and 28
	final int CR2_Lights         = 11; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
	final int CR3_Lights         =  9; //Contactor 17  //Breakers 30 and 32
	final int BthRm_Lights       = 10; //Contactor 18  //Breakers 34 and 36
	//final int MR_Wallmount     = 11; //Contactor 19  //Breakers 38 and 40
	
	
	final int Damp_BTH1   = 36; //Damper for Booth 1
	final int Damp_CR1    = 37; //Damper for Control Room 1
	final int Damp_Bath   = 38; //Damper for Bathrooms
	final int Damp_Lounge = 39; //Damper for Lounge
								//40 is spare
	final int Damp_Office = 41; //Damper for Office
	final int Damp_CR3    = 42; //Damper for Control ROom 3
	final int Damp_BTH3   = 43; //Damper for Booth 3
	final int Damp_Hall   = 44; //Damper for Hallway
	final int Damp_BTH2   = 45; //Damper for Booth 2
	final int Damp_CR2    = 46; //Damper for Control Room 2
	final int Damp_Phone  = 47; //Damper for Phone Booth
	final int Damp_MR1    = 48; //Damper for Machine Room (Left)
	final int Damp_MR2    = 49; //Damper for Machine Room (Right)
	
	final int Damp_Out_Straight = 34;//Damper for the outside dumping, the straight pipe
	final int Damp_Out_Angle    = 35;//damper for the outside dumping, the angled pipe
	
	SerialPort relayBoard = new SerialPort("COM3");
	
	final String on = "on";
	final String off = "off";
    private static DecimalFormat df2 = new DecimalFormat("00.00");
    private static DecimalFormat df2sans0 = new DecimalFormat("0.00");

    int currentCoolMachine = 0;
	int currentHeatMachine = 0;
	int mr48 = 0;
	int mr49 = 0;
	int doubleOff =1;
	int previousHVAC = -1;
	
	int WARNING_SUPPRESSION = 0;
	//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//thread stuff
	

	
	
	
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Used to log almost everything into a log file
	 * log files can be found at C:\\Users\\%USERNAME&\\Documents\\BMS Logs
	 * Each day a new log is created
	 * @param String information to log
	 * */
	public void logPrint(String in)
	{	
		try
		{	
			//debug print so that whatever is logged is also printed out into the console
			//this.debugPrint("LOG: "+in);
			
			//setup a date for the timestamp on the log file and log file name
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			DateFormat currentDay = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			//format the date into just day/month/year
			String smalldate = currentDay.format(date);
			//format the date as a timestamp for the log file
			String logOutput = dateFormat.format(date)+"     "+in+"\n";
			
			
			//set up a writer, that makes a new log file each day
			File target = new File("C:\\Users\\BMS Machine\\Documents\\BMS Logs\\"+smalldate+" log.txt");
			BufferedWriter writer = null;

			//if the file exists, then you dont need to make a new one
			//if it dosn't make a new file with the date in it's title
			if(target.exists())
			{
				writer = new BufferedWriter(new FileWriter(target, true));
				writer.append(logOutput);
			}
			else
			{
				writer = new BufferedWriter(new FileWriter(target));
				writer.write(logOutput);
			}
			writer.close();	
		}//end try
		catch( IOException e)
		{
			this.debugPrint(e.toString()+"\n");
			this.debugPrint("writer is having issues");
		}
	}
	
	/**
	 * Used to log only the important stuff
	 * The default log will still log everything, this one will just filter out the not important stuff 
	 *
	 *	@param String what you want logged
	 */
	public void logImportantPrint(String in)
	{	
		try
		{	
			//anything logged into important log is automatically logged into the normal log
			this.logPrint(in);
			//debug print so that whatever is logged is also printed out into the console
			this.debugPrint("IMPORTANT LOG: "+in);
			
			//setup a date for the timestamp on the log file and log file name
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			DateFormat currentDay = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			//format the date into just day/month/year
			String smalldate = currentDay.format(date);
			//format the date as a timestamp for the log file
			String logOutput = dateFormat.format(date)+"     "+in+"\n";
			
			
			//set up a writer, that makes a new log file each day
			File target = new File("C:\\Users\\BMS Machine\\Documents\\BMS Logs\\"+smalldate+" important log.txt");
			BufferedWriter writer = null;

			//if the file exists, then you dont need to make a new one
			//if it dosn't make a new file with the date in it's title
			if(target.exists())
			{
				writer = new BufferedWriter(new FileWriter(target, true));
				writer.append(logOutput);
			}
			else
			{
				writer = new BufferedWriter(new FileWriter(target));
				writer.write(logOutput);
			}
			writer.close();	
		}//end try
		catch( IOException e)
		{
			this.debugPrint(e.toString()+"\n");
			this.debugPrint("writer is having issues");
		}
	}
	
	/**
	 *	System.out.printlns the input
	 *	@param String inputs what you want to print 
	 */
	public void debugPrint(String in)
	{
		//just a print out to see stuff in the console
		System.out.println(in);
	}
	
	/**
	 *  Displays an important error in a dialogPane
	 *  also writes the message to the log
	 *  if the global int WARNING_SUPPRESSION is non-zero, will suppress warnings
	 * @param String inputs what you want to warn about
	 * @throws InterruptedException 
	 */
	public void warningPrint(String in) throws InterruptedException
	{
		//append in WARNING: so it reads in both the log and the option pane

			in = "WARNING: "+in;
			this.logPrint(in);
			//option pane stuff
			JFrame f = new JFrame();
			JOptionPane.showMessageDialog(f,in);

	}
		
	/**
	 * Used to write the position of a relay from the relay board
	 * Logs each relay write
	 * 
	 * @param int         which relay you want to write
	 * @param String         which state you want the relay to be
	 * @throws SerialPortException 
	 * @throws InterruptedException 
	 * */
	public void relayWrite(int inRelay, String onoff) throws SerialPortException, InterruptedException
	{
		
		//process the inRelay number since it has to be formatted into X0 if less than 10
		String number = "";
		if(inRelay <10)
			number = "0"+inRelay;
		else
		{
			number += inRelay;
		}	
		//send the command to the port, then tell the log it happened
		relayBoard.writeString("relay "+onoff.toLowerCase()+" "+number+"\r");
		Thread.sleep(100);
		relayBoard.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);		
		
		this.logPrint("Relay "+number+" set to "+onoff+". ");
	}	
	
	/**
	 * Used to write any command manually into the board
	 * Logs each relay write
	 * 
	 * @param String         literal input into the board
	 * @throws SerialPortException 
	 * @throws InterruptedException 
	 * */
	public void relayWrite(String rawIn) throws SerialPortException, InterruptedException
	{
		//send the command to the port, then tell the log it happened
		relayBoard.writeString(rawIn+"\r");
		Thread.sleep(100);
		relayBoard.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);		
		
		this.logPrint("raw input "+rawIn+". ");
	}
	
	/**
	 * Used to read the position of a relay from the relay board
	 * Logs each relay read
	 * 
	 * @param int         which relay you want to write
	 * @throws SerialPortException 
	 * @throws InterruptedException 
	 * @return String the result of a formatOutput() relay read XX
	 * */
	public String relayRead(int inRelay) throws SerialPortException, InterruptedException
	{
		
		//process the inRelay number since it has to be formatted into X0 if less than 10
		String number = "";
		if(inRelay < 10)
			number = "0"+inRelay;
		else
		{
			number += inRelay;
		}	
		//send the command to the port, then tell the log it happened
		relayBoard.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);		

		relayBoard.writeString("relay read "+number+"\r");
		Thread.sleep(100);
		relayBoard.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);		
		
		String relayReadResult = relayBoard.readString();
		//System.out.println("RESULT IN METHOD\n"+relayReadResult+"\n---------------");
		String out = this.formatOutput(relayReadResult);
		this.logPrint("Relay "+number+" reads "+out +"  .");
		
		return out;
	}	
	
	/**
	 *	Method used to trim the output of the boards into a usable int
	 *  Looks for both newline characters which only exist in specific spots related to the important numbers 
	 *  Uses those numbers to trim everything down to a simple int
	 * 
	 * @param  String output from the board taht you want formatted into an int
	 * @return String    just the number cut away from all of the other echoed chars 
	 */
	public String formatOutput(String inFromBoard) throws InterruptedException
	{
		//set up the output and return -1 if in error
		String out = "-1";

		//keep track of new line characters' locations
		int firstN  = 0;
		int secondN = 0;
		
		//catch OOB issues for when the output of the baord is blank because of a buggered input
		try
		{	
			//iterate through every character in serialResult
			//we do this to find the newline char ('\n')
			//the newline char appears right before and after the desired number
			for(int i=1;i<inFromBoard.length();i++)
			{			
				if(inFromBoard.charAt(i)=='\n')
				{
					
					if(firstN==0)
						firstN = i;
					else if(secondN==0)
						secondN = i;
				}
			}
			//cut up the main string from '\n' to '\n' and trim the spaces that are left
			out = inFromBoard.substring(firstN,secondN).trim();		
		}
		catch(StringIndexOutOfBoundsException e)
		{
			this.debugPrint("formating failed");
		}	
		return out;	
	}
	/*
	 * Launch all studios
	 * Calls each launchStudio#()
	 * */
	public void launchAll() throws SerialPortException, InterruptedException
	{
		launchStudio1();
		launchStudio2();
		launchStudio3();
		System.out.println("All Studios Launched");
	}
	/*
	 * Shutdown all studios
	 * Calls each shutdownStudio#() method
	 * */
	public void shutdownAll() throws SerialPortException, InterruptedException
	{
		shutdownStudio1();
		shutdownStudio2();
		shutdownStudio3();
		System.out.println("All Studios shutdown!");
	}
	
	/**
	 * Startup Studio 1 at the beginning of the day
	 * Basically calls relayWrite() for all of Studio 1 with a delay in between
	 */
	public void launchStudio1() throws SerialPortException, InterruptedException
	{
		this.logPrint("Studio 1 Starting up");
		try
		{
		this.relayWrite(CR1_Lights,         off);
			Thread.sleep(1000);
		this.relayWrite( CR1_Middle_Speaker, off);
			Thread.sleep(1000);
		this.relayWrite(CR1_Left_Speaker,   off);
			Thread.sleep(1000);
		this.relayWrite(CR1_Right_Speaker,  off);
			Thread.sleep(1000);
		this.relayWrite(BTH1_Power,         off);
			Thread.sleep(1000);
		this.relayWrite(CR1_Desk,           off);
			Thread.sleep(1000);
		this.logImportantPrint("Studio 1 started up with no issues!");
		}
		catch( InterruptedException | SerialPortException e)
		{
			this.logImportantPrint("Studio 1 Launch Interrupted!");
			this.logPrint(e.toString());
		}
		
	}

	/**
	 * Shutdown Studio 1 at the end of the day
	 * Basically calls relayWrite() for all of Studio 1 with a delay in between
	 */
	public void shutdownStudio1() throws SerialPortException, InterruptedException
	{
		this.logPrint("Studio 1 powering down");
		try
		{
		this.relayWrite(CR1_Lights,         on);
			Thread.sleep(1000);
		this.relayWrite(CR1_Middle_Speaker, on);
			Thread.sleep(1000);
		this.relayWrite(CR1_Left_Speaker,   on);
			Thread.sleep(1000);
		this.relayWrite(CR1_Right_Speaker,  on);
			Thread.sleep(1000);
		this.relayWrite(BTH1_Power,         on);
			Thread.sleep(1000);
		this.relayWrite(CR1_Desk,           on);
			Thread.sleep(1000);
			this.logImportantPrint("Studio 1 shutdown with no issues!");
		}
		catch( InterruptedException | SerialPortException e)
		{
			this.logPrint("Studio 1 shutdown Interrupted!");
			this.logPrint(e.toString());
		}
		
	}
	
	/**
	 * Startup Studio 2 at the beginning of the day
	 * Basically calls relayWrite() for all of Studio 2 with a delay in between
	 */
	public void launchStudio2() throws SerialPortException, InterruptedException
	{
		this.logPrint("Studio 2 Starting up");
		try
		{
			this.relayWrite(CR2_Lights,         off);
				Thread.sleep(1000);
			this.relayWrite(CR2_Middle_Speaker, off);
				Thread.sleep(1000);
			this.relayWrite(CR2_Left_Speaker,   off);
				Thread.sleep(1000);
			this.relayWrite(CR2_Right_Speaker,  off);
				Thread.sleep(1000);
			this.relayWrite(BTH2_Power,         off);
				Thread.sleep(1000);
			this.relayWrite(CR2_Desk,           off);
				Thread.sleep(1000);
			this.logImportantPrint("Studio 2 started up with no issues!");
		}
		catch( InterruptedException | SerialPortException e)
		{
			this.logPrint("Studio 2 Launch Interrupted!");
			this.logPrint(e.toString());
		}
		
	}
	
	/**
	 * Shutdown Studio 2 at the end of the day
	 * Basically calls relayWrite() for all of Studio 2 with a delay in between
	 */	
	public void shutdownStudio2() throws SerialPortException, InterruptedException
	{
		this.logPrint("Studio 2 Powering down");
		try
		{
		this.relayWrite(CR2_Lights,         on);
			Thread.sleep(1000);
		this.relayWrite(CR2_Middle_Speaker, on);
			Thread.sleep(1000);
		this.relayWrite(CR2_Left_Speaker,   on);
			Thread.sleep(1000);
		this.relayWrite(CR2_Right_Speaker,  on);
			Thread.sleep(1000);
		this.relayWrite(BTH2_Power,         on);
			Thread.sleep(1000);
		this.relayWrite(CR2_Desk,           on);
			Thread.sleep(1000);
			this.logImportantPrint("Studio 2 shutdown with no issues!");
		}
		catch( InterruptedException | SerialPortException e)
		{
			this.logPrint("Studio 2 Shutdown Interrupted!");
			this.logPrint(e.toString());
		}
		
	}	

	/**
	 * Startup Studio 3 at the beginning of the day
	 * Basically calls relayWrite() for all of Studio 3 with a delay in between
	 */
	public void launchStudio3() throws SerialPortException, InterruptedException
	{
		this.logPrint("Studio 3 Starting up");
		try
		{
			this.relayWrite(CR3_Lights,         off);
				Thread.sleep(1000);
			this.relayWrite(CR3_Middle_Speaker, off);
				Thread.sleep(1000);
			this.relayWrite(CR3_Left_Speaker,   off);
				Thread.sleep(1000);
			this.relayWrite(CR3_Right_Speaker,  off);
				Thread.sleep(1000);
			this.relayWrite(BTH3_Power,         off);
				Thread.sleep(1000);
			this.relayWrite(CR3_Desk,           off);
				Thread.sleep(1000);
			this.logImportantPrint("Studio 3 started up with no issues!");
		}
		catch( InterruptedException | SerialPortException e)
		{
			this.logPrint("Studio 3 Launch Interrupted!");
			this.logPrint(e.toString());
		}
		
	}
	
	/**
	 * Shutdown Studio 3 at the end of the day
	 * Basically calls relayWrite() for all of Studio 3 with a delay in between
	 */	
	public void shutdownStudio3() throws SerialPortException, InterruptedException
	{
		this.logPrint("Studio 3 Powering down");
		try
		{
		this.relayWrite(CR3_Lights,         on);
			Thread.sleep(1000);
		this.relayWrite(CR3_Middle_Speaker, on);
			Thread.sleep(1000);
		this.relayWrite(CR3_Left_Speaker,   on);
			Thread.sleep(1000);
		this.relayWrite(CR3_Right_Speaker,  on);
			Thread.sleep(1000);
		this.relayWrite(BTH3_Power,         on);
			Thread.sleep(1000);
		this.relayWrite(CR3_Desk,           on);
			Thread.sleep(1000);
			this.logImportantPrint("Studio 3 shutdown with no issues!");
		}
		catch( InterruptedException | SerialPortException e)
		{
			this.logPrint("Studio 3 Shutdown Interrupted!");
			this.logPrint(e.toString());
		}
		
	}

	/**
	 * Method to open a damper
	 * 
	 * @param int which damper to open by number
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 */
	public void openDamper(int inDamper) throws SerialPortException, InterruptedException
	{
		this.relayWrite(inDamper,on);
		this.logPrint("Opened Damper "+inDamper+".");
	}	
	
	/**
	 * Method to close a damper
	 * copy of the above method
	 * 
	 * @param int which damper to open by number
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 */
	public void closeDamper(int inDamper) throws SerialPortException, InterruptedException
	{
		this.relayWrite(inDamper,off);
		this.logPrint("Closed Damper "+inDamper+".");
	}
	
	/** 
	 * Method used to open the port
	 * @throws SerialPortException 
	 * */
	public void portOpen() throws InterruptedException, SerialPortException
	{
		//method used to open ports
		//assumes the ports are in order, which is why it runs the portRectifier() first
		//assumes the 2 ports are the correct ones
		
		//this.portRectifier();
		this.logPrint("Opening the ports!");
		
		try {
			//open relayBoard
			//checks to see if the port is open already
			if(relayBoard.isOpened()==false)
			{
				this.logPrint("Opening relayBoard port!");
				relayBoard.openPort();		
			}	
			else
			{
				this.logPrint("relayBoard was already open!");
			}				
		}//end try 
		catch (SerialPortException e) 
		{
			// catches any port issues
			e.printStackTrace();
			this.warningPrint("PORT NOT OPENED CORRECTLY ISSUE PROBLEM");
		}

		//success message
		this.logImportantPrint("Ports Opened SUCCESFULLY!!");
	}
		
	/**
	 * Method to close the port
	 * handles errors if the port wasn't open
	 * 
	 * @param int if non-zero will suppress the warnings, used in the openPort() method
	 */	
	public void portClose()
	{
		this.logPrint("Closing the port!");
		try 
		{
			//close the ports
			relayBoard.closePort();
		}
		catch (SerialPortException e) 
		{
			//will probably happen a bunch since the port might be open
			//if this comes up the its because the port was already closed/not open
			//final result is still all ports closed
			this.logImportantPrint("Closure of serial port failed, might not have been open, not necessarily bad.");
		}
		this.logImportantPrint("Port succesfully closed!");
	}
	
	/**
	 * Method used to find the temperature of a room given the URL of the sensor 
	 * strips the data from a json found in an IP URL from the sensor
	 * 
	 * @param String the URL of the room's sensor
	 * @return double temperature of the room
	 * @throws MalformedURLException
	 * @throws IOException
	 * */
	public double readSensor(String inURL) throws IOException 
	{
		
		InputStream is;
		
		try {
		
		//preset IP for the machine room thermometer, should never change
		 is = new URL(inURL).openStream();
		}
		catch (IOException e)
		{
			System.out.println(e);
			System.out.println("IOException error in readSensor for "+inURL);
			return 73;
		}


		//set up needed variables to use
		double out =-1;
		String pulledJSON ="";
		String found = "";
		int cp;
		
		//transfer the JSON data from the is to a usable string
		while((cp = is.read()) != -1)
		{
			pulledJSON+=(char)cp;
			
		}
				
		//look for the "ext sensor" which denotes the temp will be found 23-28 char after
		for(int j=0;j<pulledJSON.length()-10;j++)
		{
			if(pulledJSON.substring(j,j+10).equalsIgnoreCase("ext sensor"))
				found = pulledJSON.substring(j+23,j+28);
			if(pulledJSON.substring(j,j+8).equalsIgnoreCase("sensor 2"))
					found = pulledJSON.substring(j+19,j+23);
		}//end for

		//parse the temp found above from string to double and output it
		out = Double.parseDouble(found);
		is.close();
		
		
		return out;
	}


	/**
	 * Method to turn off all of the board relays
	 * employing the writeall command
	 * probably shouldnt be used that often
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * */
	
	public void allOFF() throws SerialPortException, InterruptedException
	{
		this.relayWrite("relay writeall off");
		System.out.println("ALL OFF");
	}

	
///////////////////////////////////////////////////////////////////////////////////////////////////////
	//room methods
	
	/**
	 * Method used to sort and run the machines based on how much air is needed
	 * @param currentCapacity the amount of air needed from the machines 
	 * @param hotCold whether you want heat or cold, 0 being cold, 1 being heat
	 * */
	public void handleHVACMachines(int currentCapacity, int hotCold) throws SerialPortException, InterruptedException
	{
		
		int fanNumber =52;
		//set up variables and arrays of the machines
		int requestedAirflow = currentCapacity;
		//arrays are both machines followed by their states

		//�++=+=+/=+=�/�/�/�/�/�_�_<�>�>�[�[�[�]�[�[>�>�<�<�_�_�_�_��_/�/�
		int[][] coolingMachines = {{50, 53} , {0,0}};
		
		int[][] heatingMachines = {{51, 54} , {0,0}};
		System.out.println("Machine Request: "+requestedAirflow);
		
		//something strange is up with read so this is an "empty" read to reset the issue
		this.relayRead(50);
		
		//adjust the machines' current on/off state and change their respective stats
		//sets the 2d array collingMachines to the current state of the actual machines
		if(this.relayRead(50).equalsIgnoreCase("OFF"))
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
		if(this.relayRead(53).equalsIgnoreCase("OFF"))
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
		if(this.relayRead(51).equalsIgnoreCase("OFF"))
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
		if(this.relayRead(54).equalsIgnoreCase("OFF"))
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
	     	previousHVAC=0;
		//if eather heat machine in on then the previous state is heat or 1
    	if(heatingMachines[1][0]+heatingMachines[1][1]>0)
     		previousHVAC=1;
	
		//handle the purging cycle time
		int purgeTimer=180;//timer for the purge cycle which is 3 minutes 60s * 3 = 180seconds
		if(hotCold + previousHVAC == 1)
		{
			System.out.println("P U R G I N G");
			//turn off all units
			this.relayWrite(50,off);
			this.relayWrite(53,off);
			this.relayWrite(51,off);
			this.relayWrite(54,off);
		
			//turn on the fan for the purge cycle
			//this.relayWrite(fanNumber,on);
			if(fanNumber==55)
				fanNumber-=3;
			else
				fanNumber+=3;
				
			currentCoolMachine++;
			currentHeatMachine++;
			System.out.println("Turning all machines off for the purge cycle");
			while(purgeTimer>0)
			{
				Thread.sleep(1000 * 10);//10s
				System.out.println("seconds left in purge cycle = "+purgeTimer);
				purgeTimer-=10;
			}
			this.relayWrite(52,off);
			this.relayWrite(55,off);
		}
		
		System.out.println("----------\nMachine Decisions:");	
		//machine decisions time
		//decide if both machines are needed
		//if this number is ever changed form 50, remember to change it in the main method as well
		if(requestedAirflow > 50)
		{
			//main cooling machine decisions
			if(hotCold == 0)
			{

				this.relayWrite(50, on);
				this.relayWrite(53, on);
				System.out.println("Turning on both cooling machines!");
			}
			//main heating machine decisions
			else if(hotCold == 1)
			{
				this.relayWrite(51, on);
				this.relayWrite(54, on);
				System.out.println("Turning on both heating machines!");
			}	
		}	
		else if(requestedAirflow == 0)
		{
			//all machines off
			this.relayWrite(50, off);
			this.relayWrite(51, off);
			this.relayWrite(53,off);
			this.relayWrite(54,off);
			currentCoolMachine++;
			currentHeatMachine++;
			System.out.println("Turning off all machines");
			//MRs set to open
			mr48++;
			mr49++;
				
		}	
		//only 1 machine needed
		else if(requestedAirflow > 0)
		{
			int totalCool = coolingMachines[1][0] + coolingMachines[1][1];
			int totalHeat = heatingMachines[1][0] + heatingMachines[1][1];
		
			//check cooling
			if(hotCold == 0)
			{
				//turn one machine on
				if(totalCool == 0)
				{
					this.relayWrite(coolingMachines[0][currentCoolMachine%2], on);
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
					this.relayWrite(coolingMachines[0][currentCoolMachine%2], off);
					System.out.println("Turning off 1 machine, both were running, turning off "+coolingMachines[0][currentCoolMachine%2]);
					currentCoolMachine++;
					Thread.sleep(1000 * 60 * 2);//2m
					//temporary cooldown to prevent the extra pressure from the winding down machines from making noise in the pipes
					System.out.println("Opening MRs to allow for extra airflow temporarily");
					this.openDamper(48);
					this.openDamper(49);
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
			}
			//check heating
			else if(hotCold == 1)
			{
				//turn one machine on
				if(totalHeat == 0)
				{
					this.relayWrite(heatingMachines[0][currentHeatMachine%2], on);
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
					this.relayWrite(heatingMachines[0][currentHeatMachine%2], off);
					System.out.println("Turning off 1 machine, both were running, turning off "+heatingMachines[0][currentHeatMachine%2]);
					currentHeatMachine++;
				}	
			}
		}
		
		
		//set previous hotCold of the system for purge cycle reasons
		previousHVAC = hotCold;
	}
	
	/**
	 * Method used to calculate dump zones, and when to open/close them
	 * @param int the current Airflow being requested
	 * @param int whether you are asking for hot/cold
	 * */
	public void handleHVACDumpZones(int currentCapacity, int hotCold) throws SerialPortException, InterruptedException
	{
		//adjustment int
		int dumpActivateLevel = -30;
		
		//handle cold
		if(hotCold == 0)
		{
			System.out.println("----------\nDumpZones for Cold Air");
		
			//starting from the top, subtract the dump zone's capacity until it reaches 0
			if(currentCapacity < dumpActivateLevel)
			{
				mr48++;		
				currentCapacity += 13;
				System.out.println("Requesting MR1(48) open, capacity= "+currentCapacity);
			}
			//48 not requested
			else
			{
				mr48=0;
				System.out.println("MR1(48) not requested as dump zone");
			}	
			//49 requested
			if(currentCapacity < dumpActivateLevel)
			{
				mr49++;			
				currentCapacity += 13;
				System.out.println("Requesting MR2(49) open, capacity= "+currentCapacity);
			}	
			//49 not requested
			else
			{
				mr49=0;
				System.out.println("MR2(49) not requested as dump zone");
			}	

			//35 requested - OUTSIDE DUMP 1
			if(currentCapacity < dumpActivateLevel)
			{
				this.openDamper(35);			
				currentCapacity += 13;
				System.out.println("Requesting OUTSIDE 1 open, capacity= "+currentCapacity);
			}	
			//35 not requested - OUTSIDE DUMP 1
			else
			{
				this.closeDamper(35);
				System.out.println("OUTSIDE DUMP 1 not requested");
			}	
			
			//34 requested - OUTSIDE DUMP 2
			if(currentCapacity < dumpActivateLevel)
			{
				this.openDamper(34);			
				currentCapacity += 13;
				System.out.println("Requesting OUTSIDE 2 open, capacity= "+currentCapacity);
			}	
			//34 not requested - OUTSIDE DUMP 2
			else
			{
				this.closeDamper(34);
				System.out.println("OUTSIDE 2 not requested as dump zone");
			}	
			
		}

		//handle heat	
		else if(hotCold == 1)
		{
		/*	System.out.println("DumpZones for Heating");
			
			//41 requested, opening
			if((currentCapacity < dumpActivateLevel) || (this.relayRead(41).equals("on")))
			{
				this.openDamper(41);
				currentCapacity += 7;
				System.out.println("Opening Edit(41), capacity= "+currentCapacity);
			}	
			//41 not requested, closing
			else
			{
				this.closeDamper(41);
				System.out.println("Edit(41) not requested, final capacity= "+currentCapacity);
			}
			
			//39 requested, opening
			if((currentCapacity < dumpActivateLevel) || (this.relayRead(39).equals("on")))
			{
				this.openDamper(39);
				currentCapacity +=7;
				System.out.println("Opening Lounge(39), capacity= "+currentCapacity);
			}	
			//39 not requested, closing
			else
			{
				this.closeDamper(39);
				System.out.println("Lounge(39) not requested, capacity= "+currentCapacity);
			}
			//44 requested, opening
			if((currentCapacity < dumpActivateLevel) || (this.relayRead(44).equals("on")))
			{
				this.openDamper(44);
				currentCapacity += 7;
				System.out.println("Opening Hallway(44), capacity= "+currentCapacity);
			}	
			//44 not requested, closing
			else
			{
				this.closeDamper(44);
				System.out.println("Hallway(44) not requested, final capacity= "+currentCapacity);
			}
			
			*/
			//47 requested, opening
			if((currentCapacity < dumpActivateLevel) || (this.relayRead(47).equals("on")))
			{
				this.openDamper(47);
				currentCapacity += 9;
				System.out.println("Opening Phone Booth(47), capacity= "+currentCapacity);
			}	
			//44 not requested, closing
			else
			{
				this.closeDamper(47);
				System.out.println("Phone Booth(47) not requested, final capacity= "+currentCapacity);
			}

		}//hot else if end
		else if(hotCold == -1)
		{
			
			System.out.println("Leaving MRs open to circulate air and prevent overpressure when the machines are winding down");
			mr48++;
			mr49++;
		}	
		else
		{
			System.out.println("Problem with handleHVACDumpZones, reveived a non -1/0/1 hotCold");	
		}
		
		System.out.println("Current dump leftover at="+currentCapacity);
	}
		
	/**
	 * Method used to set the value of previousState in multiple rooms
	 * @param Room[] list the list of rooms you want to update previousState in
	 * @param int in the value of previousState you want to set the rooms to
	 * */
	public void massSetPreviousState(Room[] list, int in)
	{
		for(int i=0;i<list.length;i++)
			list[i].setPreviousState(in);
	}
	
	/**
	 * Method used to cull from a room list where all previousStates are equal to the int given
	 * @param Room[] list of rooms you want to use
	 * @param int all rooms with a previousState value of the param will be culled
	 * @return Room[] list given as a param minus rooms that had previousState eaual to the other param
	 * */
	public Room[] removeFromListPrevious(Room[] list, int cull)
	{
		//arraylist to cull all previousState = the param cull
		ArrayList<Room> totalList = new ArrayList<Room>();
		
		for(Room i : list)
		{
			if(i.getPreviousState()!=cull)
			totalList.add(i);
		}
		
		//use the arraylist to make a new array with the exact length of the 
		Room[] culledList = new Room[totalList.size()];
		for(int i=0;i<culledList.length;i++)
		{
			culledList[i] = totalList.get(i);
		}
		
		return  culledList;
	}	
	
	/**
	 * Method used to see if a room needs conditioning
	 * 0 is cooling
	 * 1 is heating will only display heating if every other room does not need cooling
	 * -1 is every room is satisfied
	 * 
	 * @param Room[] list of all the rooms
	 * @return int aforementioned result if a room needs something
	 * */
	public int checkForConditioning(Room[] list)
	{
		int out = -1;
		int cool = 0;
		int heat = 0;
		
		for(int i : collectRequests(list))
		{	
			if(i==0)
				cool++;
			if(i==1)
				heat++;
		}
		if(cool>0)
			out++;
		else if(heat>0)
			out+=2;
		return out;
	}	
	
	/**
	 * Method used to see if a room still needs conditioning looking at the cutoffTemps, not targetTemp
	 * 0 is cooling
	 * 1 is heating will only display heating if every other room does not need cooling
	 * -1 is every room is satisfied
	 * 
	 * @param Room[] list of all the rooms
	 * @return int aforementioned result if a room needs something
	 * */
	public int checkForConditioningStill(Room[] list)
	{
		//keep track of the amount of rooms in each category
		int out = -1;
		int cool = 0;
		int heat = 0;
		
		//check every room in the Room[] to see what they need
		for(int i : collectCutoffs(list))
		{	
			if(i==0)
				cool++;
			if(i==1)
				heat++;
		}
		
		//logic to output heat or cool or nothing
		//if neither cool nor heat are requested then nothing happens to out, which was initialized as -1
		//if any room needs cool then heat is ignored
		if(cool>0)
			out++;
		else if(heat>0)
			out+=2;
		return out;
	}
	
	/**
	 * Method used to grab all of the room's coolHeat value
	 * 
	 * @param Room[] list of rooms
	 * @return int [] all coolHeats from the rooms, 0 is cool 1, is heat
	 * */
	public int[] collectCoolHeat(Room[] list)
	{
		int[] out = new int[list.length];
		for(int i=0; i<list.length; i++)
		{
			out[i] =list[i].getCoolHeat();
		}

		return out;
	}
	
	/**
	 * Method used to check all of the room's request values
	 * 
	 * @param Room[] list of rooms
	 * @return int [] all of the requestState from the rooms
	 * */
	public int[] collectRequests(Room[] list)
	{
		int[] out = new int[list.length];
		for(int i=0; i<list.length; i++)
		{
			out[i] =list[i].checkRequest();
		}

		return out;
	}		
	
	/**
	 * Method used to check all of the room's request values
	 * 
	 * @param Room[] list of rooms
	 * @return int [] all of the cuttoff results from the given rooms
	 * */
	public int[] collectCutoffs(Room[] list)
	{
		int[] out = new int[list.length];
		for(int i=0; i<list.length; i++)
		{
			out[i] =list[i].checkCutoff();
		}

		return out;
	}
		
	/**
	 * Method to add all capacities of the given rooms
	 * uses percentAirflow from Room
	 * @return int total percent you want to find
	 * @param Room[] list of all of the rooms you want to see
	 * */
	public int collectAirflow(Room[] list)
	{
		int total =0;
		for(Room i : list)
		{
				total += i.getPercentAirflow();
		}	
		return total;
	}
	
	/**
	 * Method to update all room's data
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * */
	public void massRefresh(Room[] list) throws SerialPortException, InterruptedException
	{
		for(int i=0; i<list.length; i++)
		{
			list[i].refresh();
		}	
	}
	
	/**
	 * Method used to see which rooms are requesting cold
	 *@param Room[] list of all rooms
	 *@return Room[] list of rooms who are requesting cold
	 * */
	public Room[] requestingCold(Room[] list)
	{
		//arraylist to sort out the rooms asking for cold
		ArrayList<Room> coldList = new ArrayList<Room>();
		
		for(Room i : list)
		{
			if(i.checkRequest()==0)
			coldList.add(i);
		}
		
		//use the arraylist to make a new array with the exact length of the 
		Room[] cold2List = new Room[coldList.size()];
		for(int i=0;i<cold2List.length;i++)
		{
			cold2List[i] = coldList.get(i);
		}
		
		return  cold2List;
	}	
	
	/**
	 * Method used to see which rooms are requesting heat
	 * Takes a list of rooms and cuts out all of the rooms who are not requesting heat, excluding the MRS
	 *@param Room[] list of all rooms
	 *@return Room[] list of rooms who are requesting heat
	 * */
	public Room[] requestingHeat(Room[] list)
	{
		//arraylist to sort out the rooms asking for cold
		ArrayList<Room> heatList = new ArrayList<Room>();
		
		for(Room i : list)
		{	//add in all room requesting heat except for the machine rooms, they will never receive heat
			if(i.checkRequest()==1 && !i.getRoomName().equalsIgnoreCase("MR1") && !i.getRoomName().equalsIgnoreCase("MR2"))
			heatList.add(i);
		}
		
		//use the arraylist to make a new array with the exact length of the 
		Room[] heat2List = new Room[heatList.size()];
		for(int i=0;i<heat2List.length;i++)
		{
			heat2List[i] = heatList.get(i);
		}
		
		return  heat2List;
	}
	
	/**
	 * Method used to check all rooms in a list if they are below the cutoffTemp
	 * Used in conjunction to keep the rooms cooling after they initially request cold 
	 * through the other method requestingCold()
	 * 
	 * @param Room[] list of the rooms you want to check
	 * @return Room[] list of all rooms that are still above their cutoffTemp
	 * */
	public Room[] requestingCutoffCooling(Room[] list)
	{
		//arraylist to sort out the rooms asking for cold
		ArrayList<Room> stillCold = new ArrayList<Room>();
		
		for(Room i : list)
		{
			if(i.checkCutoff()==0)
			stillCold.add(i);
		}
		
		//use the arraylist to make a new array with the exact length of the 
		Room[] still2Cold = new Room[stillCold.size()];
		for(int i=0;i<still2Cold.length;i++)
		{
			still2Cold[i] = stillCold.get(i);
		}
		
		return  still2Cold;
	}
	
	/**
	 * Method used to check all rooms in a list if they are above the cutoffTemp
	 * Used in conjunction to keep the rooms heating after they initially request heat 
	 * through the other method requestingHeat()
	 * 
	 * @param Room[] list of the rooms you want to check
	 * @return Room[] list of all rooms that are still below their cutoffTemp
	 * */
	public Room[] requestingCutoffHeating(Room[] list)
	{
		//arraylist to sort out the rooms asking for heat
		ArrayList<Room> stillHeat = new ArrayList<Room>();
		
		for(Room i : list)
		{
			if(i.checkCutoff()==1)
			stillHeat.add(i);
		}
		
		//use the arraylist to make a new array with the exact length of the 
		Room[] still2Heat = new Room[stillHeat.size()];
		for(int i=0;i<still2Heat.length;i++)
		{
			still2Heat[i] = stillHeat.get(i);
		}
		
		return  still2Heat;
	}
	
	/**
	 * Method used to find if a room is satisfied
	 * @param Room[] list of rooms you want to check
	 * @return Room[] list of rooms who are satisfied
	 * */
	public Room[] requestingNothing(Room[] list)
	{
		//arraylist to sort out the rooms asking for nothing
		ArrayList<Room> fineList = new ArrayList<Room>();
		
		for(Room i : list)
		{
			if(i.checkCutoff()==-1)
				fineList.add(i);
		}
		
		//use the arraylist to make a new array with the exact length of the arraylist
		Room[] fine2List = new Room[fineList.size()];
		for(int i=0;i<fine2List.length;i++)
		{
			fine2List[i] = fineList.get(i);
		}
		
		return  fine2List;
	}
	
	/**
	 * Method used to remove the MRs from an array by specific name
	 * Removes the 2 MRs then remakes an array without them, which is then returned
	 * @param Room[] array of rooms to work on
	 * @return Room[] array of rooms given minus MR1 and MR2
	 **/
	public Room[] removeMRs(Room[] list)
	{
		//make an arraylist to hold the non machine room rooms
		ArrayList<Room> sansMRList = new ArrayList<Room>();
		
		//go through all of the list and if the room is specifically "MR1" or "MR2" then add it to the arraylist
		for(Room i : list)
		{
			if(!i.getRoomName().equalsIgnoreCase("Machine Room 1") && !i.getRoomName().equalsIgnoreCase("Machine Room 2"))
			{	
				sansMRList.add(i);
			}
		}
		
		//use the arraylist to make a new array with the exact length of the arraylist
		Room[] postMRList = new Room[sansMRList.size()];
		for(int i=0;i<postMRList.length;i++)
		{
			postMRList[i] = sansMRList.get(i);
		}
		
		return  postMRList;
	}
	/**
	 * Method used to find all of the rooms with the first  characters beign exactly "Mach"
	 * Used specifically to find Machine Rooms, which are named as, no other room should start with the word machine for this to work
	 * 
	 * @param	Room[] list of rooms used to find the machine rooms inside of
	 * @return	Room[] list of all of the machine rooms in the list given
	 * 
	 **/
	public Room[] findMRs(Room[] list)
	{
		//make an arraylist to hold the machine rooms to return
		ArrayList<Room> onlyMRs = new ArrayList<Room>();
		
		//go through the list given to find the MRs be looking for specifically "M" then "R"
		for(Room i : list)
		{
			if(i.getRoomName().length()>3)
			{	
				String mach = i.getRoomName().substring(0,4);
				if(mach.contentEquals("Mach"))
				{
					onlyMRs.add(i);
				}
			}
		}
		//convert the above awaylist into an array to return
		Room[] postCull = new Room[onlyMRs.size()];
		for(int i=0; i<postCull.length;i++)
		{
			postCull[i] = onlyMRs.get(i);
		}
		return postCull;
	}
	
	/**
	 * Method to print a room array simply
	 * Intended to be used in conjunction with the 4 different prints: RoomNames, CurrentTemps, TargetTemps, PreviousStates, and tempDifference
	 * @param Room[] list of rooms you want printed
	 * @return String all of the roomNames from list
	 * */
	public String printRoomNames(Room[] list)
	{
		String out = "";
		//iterate through each element in the array given and print the roomName
		for(Room i : list)
		{	
				out += " "+i.getRoomName();
		}
		return out;
	}
	
	/**
	 * Method used to find the current temps of a list of rooms
	 * Intended to be used in conjunction with the 4 different prints: RoomNames, CurrentTemps, TargetTemps, PreviousStates, and tempDifference
	 * @param Room[] list of rooms you want to get the currentTemps of
	 * @return String return a line of all of the currentTemp of the given lsit
	 * */
	public String printCurrentTemps(Room[] list)
	{
		String out ="";
		for(Room i : list)
			out+=" "+df2.format(i.getCurrentTemp());
		return out;
	}
	
	/**
	 * Method used to find targetTemps fo the list of rooms given
	 * Intended to be used in conjunction with the 4 different prints: RoomNames, CurrentTemps, TargetTemps, PreviousStates, and tempDifference
	 * @param Room[] list of rooms you want to get target Temps of
	 * @return String return a line of all of the list's TargetTemp
	 * */
	public String printTargetTemps(Room[] list)
	{
		String out ="";
		for(Room i : list)
			out+=" "+df2.format(i.getTargetTemp());
		return out;
	}	

	/**
	 * Method used to find previousStates of the list of rooms given	 
	 * Intended to be used in conjunction with the 4 different prints: RoomNames, CurrentTemps, TargetTemps, PreviousStates, and tempDifference
	 * @param Room[] list of rooms you want to get data from
	 * @return String returns a line of all of the list's PreviousState
	 * */
	public String printPreviousStates(Room[] list)
	{
		String out ="";
		//go through each room and add the previousState to the out line
		for(Room i : list)
		{
			//formatting stuff so it looks nicer
			if(i.getPreviousState()>=0)
				out+=" ";
			out+="    "+i.getPreviousState();
		}	
		return out;
	}
	
	/**
	 * Method used to find hotCOld of the list of rooms given	 
	 * Intended to be used in conjunction with the 4 different prints: RoomNames, CurrentTemps, TargetTemps, PreviousStates, and tempDifference
	 * @param Room[] list of rooms you want to get data from
	 * @return String returns a line of all of the list's hotCOld
	 * */
	public String printCurrentRequest(Room[] list)
	{
		String out ="";
		//go through each room and add the current hotCold to the out line
		for(Room i : list)
		{
			out+="     "+i.getCoolHeat();
		}	
		return out;
	}
	
	/**
	 * Method used to show the temperature difference for the rooms who need HVAC
	 * Intended to be used in conjunction with the 4 different prints: RoomNames, CurrentTemps, TargetTemps, PreviousStates, and tempDifference
	 * @param Room[] list of rooms you want to know about
	 * @return String all of the differences in one line
	 * */
	public String tempDifference(Room[] list)
	{
		//return this string, build it up in the for each loop be printing out the difference in the desired and current temps
		String out ="";
		for(Room i : list)
		{	double x = i.getCurrentTemp()-i.getTargetCutoffTemp();
			if(x>0)
				out+=" ";
			out+=" "+ df2sans0.format(x);
		}
		return out;
	}

	/**
	 * Method to close a Room list for HVAC
	 * @param Room[] list of rooms you want to close for HVAC
	 * @return int number of rooms closed
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * */
	public void closeRoomForHVAC(Room[] list) throws SerialPortException, InterruptedException
	{
		for(Room i : list)
		{	
			i.closeHVAC();
			Thread.sleep(200);
		}
	}
	
	/**
	 * Method to open a Room list for HVAC
	 * opens the damper for the rooms
	 * @param Room[] list of rooms you want to open for HVAC
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * */
	public void openRoomForHVAC(Room[] list) throws SerialPortException, InterruptedException
	{
		for(Room i : list)
		{	
			i.acceptHVAC();
			Thread.sleep(200);
		}
	}
	
	/**
	 * Method to add 2 Room arrays together
	 * additionally trims the repeats
	 * additionally sorts the room names by roomName in alphabetical order
	 * @param Room[] first list of rooms
	 * @param Room[] second list of rooms
	 * @return Room[] a combined list of the above rooms, sorts them by room name
	 * */
	public Room[] addRoomLists(Room[] list1, Room[] list2)
	{
		//find total number of array slots
		int totalLength = list1.length + list2.length;
		
		//combine them list1 then lsit2 into 1 array
		Room[] out = new Room[totalLength];
		for(int i=0;i<list1.length;i++)
		{
			out[i]=list1[i];
		}	
		for(int j=0;j<list2.length;j++)
		{
			out[j+list1.length] = list2[j];
		}
		
		
		Room temp;
		//sort the array by roomname
		for(int k=0;k<out.length;k++)
		{
			for(int l=k+1;l<out.length;l++)
			{
				if((out[k].getRoomName()).compareTo(out[l].getRoomName()) > 0)
				{
					temp = out[k];
					out[k] = out[l];
					out[l] = temp;
				}	
			}
		}
		
		
		//arraylist to sort the added array by roomname, also remove duplicates
		ArrayList<Room> sortedList = new ArrayList<Room>();
		
		String previous = "";
		
		for(Room i : out)
		{
			if(i.getRoomName() != previous)
			sortedList.add(i);
			previous = i.getRoomName();
		}		
		
		//convert the arraylist into an new array with no dupes
		Room[] sortedNoDupes = new Room[sortedList.size()];
		for(int i=0;i<sortedNoDupes.length;i++)
		{
			sortedNoDupes[i] = sortedList.get(i);
		}

		return sortedNoDupes;
	}

	/**
	 * Method used to log the buildings current state into a log
	 * Takes a list of Rooms and prints out a bunch of statuses from the building
	 * Log can be found at Documents\BMS Logs\Temperature Logs
	 * 
	 * @param Room[] list of all of the rooms in the building to log
	 * @param int coolHeat, the current state of the building, with 0 being cool and 1 being heat, -1 is satisfied
	 * */
	public void logBuildingStatus(Room[] list, int coolHeat) throws SerialPortException, InterruptedException
	{
	

		//set up the current date and time
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm,");
		Date date = new Date();
		String currentDate = dateFormat.format(date);
	
		//setup a shorter date for the log
		DateFormat dateShort = new SimpleDateFormat("yyyy MM dd");
		String shortDate = dateShort.format(date);
	
		//handle the string that is added into the log, all of the current stats of the building
		//year, month, day, hour:minute, coolheat, 50, 53, 51, 54, cr1 temp, cr1 damper,-,-,-,-,-, edit, -, -, -
	
		//empty string for output to file
		String out="";
	
		//add the current day into the log
		out+=currentDate;
	
		//empty relayRead becuase somehting is up
		this.relayRead(50);
	
		//add in coolHeat then the status of each machine
		out+= coolHeat+","+this.relayRead(50)+","+this.relayRead(53)+","+this.relayRead(51)+","+this.relayRead(54)+",";
		
		//add in each rooms name, currentTemp, damperState
		for(Room i : list)
		{
			out+=i.getCurrentTemp()+","+i.getDamperState()+",";
		}
	
		//strip the last comma out of the output
		out = out.substring(0,out.length()-1);
	
		//add outside dump zones to the log
		out += ", "+this.relayRead(34)+", "+this.relayRead(35);
		
		//newline for the next entry
		out+="\n";
	
		//set up a writer, that makes a new log file each day
		File target = new File("C:\\Users\\BMS Machine\\Documents\\BMS Logs\\Temperature Logs\\"+ shortDate+" templog.txt");
		BufferedWriter writer = null;
		try 
		{	
			//if the file exists, then you dont need to make a new one
			//if it dosn't make a new file with the date in it's title
			if(target.exists())
			{
				writer = new BufferedWriter(new FileWriter(target, true));
				writer.append(out);
			}
			else
			{
				//if the file dosnt exist, create the file and initialize the first line for a CSV
				writer = new BufferedWriter(new FileWriter(target));
				writer.append("date,time,current_request_state,machine_state_50,machine_state_53,machine_state_51,machine_state_54,"
							 +"cr1_temp,cr1_damper,cr2_temp,cr2_damper,cr3_temp,cr3_damper,"
							 +"mr1_temp,mr1_damper,mr2_temp,mr2_damper,"
							 +"bth1_temp,bth1_damper,bth2_temp,bth2_damper,bth3_temp,bth3_damper,"
						     +"edit_temp,edit_damper,hall_temp,hall_damper,kitchen_temp,kitchen_damper,"
						     +"phone_temp,phone_damper, "
						     + "outside_dump_1, outside_dump_2\n");
				writer.write(out);
			}
			writer.close();	
		}//end try
		catch( IOException e)
		{
			System.out.println(e.toString()+"\n");
			System.out.println("writer is having issues");
		}
		System.out.println("Updated the temperature log");
	}
	
	public void stopHVAC()
	{
		//simply turns off all HVAC machines
		try 
		{
			this.relayWrite(50, off);
			this.relayWrite(51, off);
			this.relayWrite(53, off);
			this.relayWrite(54, off);	
		}
		catch (SerialPortException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void printInfo(Room[] primary, Room[] secondary)
	{
		System.out.println("          CR1,  CR2,  CR3, BTH1, BTH2, BTH3,  MR1,  MR2, Edit, Kich, Hall, Phone");
		System.out.println("current"+this.printCurrentTemps(primary)   +""+this.printCurrentTemps(secondary));
		System.out.println("target "+this.printTargetTemps(primary)    +""+this.printTargetTemps(secondary));
		System.out.println("diff   "+this.tempDifference(primary) 	   +""+this.tempDifference(secondary));
		System.out.println("states "+this.printPreviousStates(primary) +""+this.printPreviousStates(secondary));		
		System.out.println("hotCold"+this.printCurrentRequest(primary)   +""+this.printCurrentRequest(secondary));
	}

	
	public double findHighestTemp(Room[] allRooms)
	{
		double out=0;
		
		for(Room i : allRooms)
		{
			if(i.getCurrentTemp()>out)
				out = i.getCurrentTemp();
		}
		
		return out;
	}

	public double findLowestTemp(Room[] allRooms)
	{
		double out=100;
		
		for(Room i : allRooms)
		{
			if(i.getCurrentTemp()<out)
				out = i.getCurrentTemp();
		}
		
		return out;
	}
	
}//big end


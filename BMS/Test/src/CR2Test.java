import java.io.IOException;
import java.util.Scanner;
import jssc.SerialPort;
import jssc.SerialPortException;

/*
 *	tool used to test/find thermistors
 * 	1/7/2020
 * 	1.1
 */
 


public class CR2Test 
{
	public static void main(String[] args)
	{
		
	
		
		//11, 14, 15, 16, 26, 27
		
		
		
		//open the port using COM3 which I know is the relay board
		SerialPort board = new SerialPort("COM3");
		
		
		//close port if i fucked up earlier
		try {
			board.closePort();
		} catch (SerialPortException e)
		{
			System.out.println("Closed port left open from last time");
		}		
		String input;
		
		try {
		
		  //open the port	
		  if((board.openPort() == true)) 
		  {
			  System.out.println("Port has opened succesfully!\n.\n.\n.");
		  }
		  else
		  {
			  //No port no program
			  System.exit(0);
		  }
	
        	//first
		  //print 
		  //sleep
		  
		  relayOn("14",board);
		  System.out.println("14 turning on 830");
		  Thread.sleep(100060*30);
		  relayOff("14",board);
		  System.out.println("14 turning off");

		  Thread.sleep(1000*10);
		  relayOn("15",board);
		  System.out.println("15 turning on 900");
		  Thread.sleep(100060*30);
		  relayOff("15",board);
		  System.out.println("15 turning off");

		  Thread.sleep(1000*10);
		  relayOn("16",board);
		  System.out.println("16 turning on 930");
		  Thread.sleep(1000*60*30);
		  relayOff("16",board);
		  System.out.println("16 turning off");

		  Thread.sleep(1000*10);
		  relayOn("11",board);
		  System.out.println("11 turning on 1000");
		  Thread.sleep(1000*60*30);
		  relayOff("11",board);
		  System.out.println("11 turning off");

		  Thread.sleep(1000*10);
		  relayOn("26",board);
		  System.out.println("26 turning on 1030");
		  Thread.sleep(1000*60*30);
		  relayOff("26",board);
		  System.out.println("26 turning off");		  
		  
		  Thread.sleep(1000*10);
		  relayOn("27",board);
		  System.out.println("27 turning on 1100");
		  Thread.sleep(1000*60*30);
		  relayOff("27",board);
		  System.out.println("27 turning off");		  
		  
		  
		  
		  
        	//close port at the end
        	board.closePort();
		
		}//end try
	    catch (SerialPortException | InterruptedException ex)
		{
	      System.out.println(ex);
		} 
        System.out.println();
		for(int i=0;i<15;i++)
			System.out.print(i+", ");
		
	
	}

	static String relayOn(String in, SerialPort board) throws SerialPortException, InterruptedException
	{
		//-------------------------------------------------------------------------------------------------------
		  board.writeString("relay on "+in+"\r");
		  //board.writeString("relay off 38 \r");
		//  board.writeString("relay writeall 0000000000000000\r");
		//--------------------------------------------------------------------------------------------------------
	      	Thread.sleep(100);	
	      	System.out.println("---");
	      //	board.writeString("relay readall\r");
	      	Thread.sleep(100);
	  	
	  	
	  	board.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
	  	Thread.sleep(100);
		return in;
	}
	
	static String relayOff(String in, SerialPort board) throws SerialPortException, InterruptedException
	{
		//-------------------------------------------------------------------------------------------------------
		  board.writeString("relay off "+in+"\r");
		  //board.writeString("relay off 38 \r");
		//  board.writeString("relay writeall 0000000000000000\r");
		//--------------------------------------------------------------------------------------------------------
	      	Thread.sleep(100);	
	      	System.out.println("---");
	      //	board.writeString("relay readall\r");
	      	Thread.sleep(100);
	  	
	  	
	  	board.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
	  	Thread.sleep(100);
		return in;
	}
  	
}

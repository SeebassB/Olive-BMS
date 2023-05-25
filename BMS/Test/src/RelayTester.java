import java.io.IOException;
import java.util.Scanner;
import jssc.SerialPort;
import jssc.SerialPortException;

/*
 *	tool used to test/find thermistors
 * 	1/7/2020
 * 	1.1
 */
 


public class RelayTester 
{
	public static void main(String[] args)
	{
		
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
		//-------------------------------------------------------------------------------------------------------
		  board.writeString("relay off 50\r");
		  //board.writeString("relay off 38 \r");
		//  board.writeString("relay writeall 0000000000000000\r");
		  
		  //44 is the hallwayu\ vent for checking
		  
		  //34 and 35 are the new dampers
		//--------------------------------------------------------------------------------------------------------
	        	Thread.sleep(100);	
	        	System.out.println("---");
	        //	board.writeString("relay readall\r");
	        	Thread.sleep(100);
        
        	String readIn = board.readString();
        	System.out.println(readIn);
        	
        	System.out.println("----------");
        	
        	char[] charOut = readIn.toCharArray();
        	for(int i=0;i<charOut.length;i++) 
        	{
        		System.out.print(charOut[i]+", ");
        		if(charOut[i]=='\n')
        			System.out.print("<-\n");
        	}

        	board.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
        	Thread.sleep(100);

        	
        	
        	//close port at the end
        	board.closePort();
		
		}//end try
	    catch (SerialPortException|InterruptedException ex)
		{
	      System.out.println(ex);
		} 
        System.out.println();
		for(int i=0;i<15;i++)
			System.out.print(i+", ");
		
	}
}


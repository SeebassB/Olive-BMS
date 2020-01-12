import com.fazecast.jSerialComm.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JOptionPane;

public class test 
{
	public static void main(String[] args)
	{
		
		//set up the serial ports
		
		//find all of the serial ports connected to the computer
		SerialPort[] ports = SerialPort.getCommPorts();

		//in case there are no ports
		if(ports.length == 0 )
		{
			warning("No Serial Ports found!!");
		}
				
		
		//to hold the port numbers
		int portForUSBRelays = 2;
		int portForGPIO =-1;
		
		//
		String[] result = new String[ports.length];
		for(int i=0; i < ports.length; i++)
		{
			result[i] = ports[i].getSystemPortName();
			result[i] = ports[i].getDescriptivePortName();
			warning(result[i].toString());

		}
	
		
		try
		{
			
			SerialPort porterino = ports[2];

		porterino.setComPortParameters(19200,8,1,0); 
		porterino.openPort();
		System.out.println(porterino.getDescriptivePortName());
		
		
		System.out.println("open or no = "+porterino.isOpen());
		System.out.println("------------------------------");
		
		
		
		

		
		
		

			
		PrintStream os = new PrintStream(porterino.getOutputStream(), true);
		
		
		
		
		os.print("relay on 10");
		os.print((char)13);
		//os.flush();
		os.print("relay read 10");
		os.print((char)13);
		
		
		InputStream in = porterino.getInputStream();
		try 
		{
			for(int i = 0; i <1000; i++)
			{
				System.out.print((char)in.read());
			}	
			in.close();
		}
		catch (Exception e)
		{
			debug(" done reading inputStream");
		}
		
		
	/*	
		byte[] readBuffer = new byte[porterino.bytesAvailable()];
		int numRead = porterino.readBytes(readBuffer, readBuffer.length);
		debug("Reading " +numRead+" bytes.");
		debug(readBuffer.toString());
	*/
		
		
		//os.print("relay off 10");
		//os.print("\r\n");
	//	os.print("relay read 10");
		//os.print("\r\n");
	//	warning(is.readLine());
			
		//	if(is != null) is.close();
			if(os != null) os.close();
			
			
			
			
		porterino.closePort();

		}
		catch(Exception e)
		{
			debug("first closing error");
		}
		
		
		
	}

	
	
	
	
	
	
	
//shows an error message
public static void warning(String input)
{
	JOptionPane.showMessageDialog(null,input);
	debug(input);
}

//prints out debug info for me to see
public static void debug(String input)
{
	System.out.println(input);
}



}
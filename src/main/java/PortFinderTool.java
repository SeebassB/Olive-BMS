import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class PortFinderTool 
{
	public static void main(String[] args) throws InterruptedException
	{
		String lineIn = "off";
		Scanner scanner = new Scanner(System.in);
		while(!lineIn.equals("quit"))
		{
			//make a list of ports
        	SerialPort[] portNames = SerialPort.getCommPorts();
	
			System.out.println("Here's a list of all of the serial ports\n----------");
			//detect if the list of ports is 0 and send a message saying no ports
			if(portNames.length ==0)
			{
				System.out.println("No ports found!");
			}

			//run through the list of serial ports and print them out
        	for (SerialPort port : portNames)
			{
            	System.out.println(port.toString());
        	}
			lineIn = scanner.nextLine();
		}
	}
}
 
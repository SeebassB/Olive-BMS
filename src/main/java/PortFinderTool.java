import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class PortFinderTool 
{

	public static void main(String[] args)
	{
		portFinderTool();
	}

	static void portFinderTool()
	{
		Scanner scan = new Scanner(System.in);
		String scannerInput = "";

		while(!scannerInput.equals("quit"))
		{
			printPorts();
			scannerInput = scan.nextLine();
		}

	}

	static void printPorts()
	{
		SerialPort[] portNames = SerialPort.getCommPorts();

		if(portNames.length ==0)
			System.out.println("NO PORTS CURRENTLY FOUND");

		for(SerialPort p : portNames)
		{
			System.out.println("Port = "+ p.getSystemPortName());
		}
	}


}
 
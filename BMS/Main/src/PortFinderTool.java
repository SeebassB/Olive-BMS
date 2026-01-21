import jssc.SerialPortList; //import jssc SerialPortList

public class PortFinderTool 
{
	public static void main(String[] args) throws InterruptedException
	{
		int onoff=0;
	while(onoff==0)
	{
		//make a list of ports
        String[] portNames = SerialPortList.getPortNames();
	
		System.out.println("Here's a list of all of the serial ports\n----------");	
		//detect if the list of ports is 0 and send a message saying no ports
		if(portNames.length ==0)
			System.out.println("No ports found!");
		
		//run through the list of serial ports and print them out
		for(int i =0; i < portNames.length; i++)
		{
			System.out.println(portNames[i]);
		}
	Thread.sleep(2000);	
	}
	}
}

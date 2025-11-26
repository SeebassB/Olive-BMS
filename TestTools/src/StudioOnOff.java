// TEST PROGRAM TO MAKE SURE EACH STUDIO CAN TURN ON
// NOT PART OF THE MAIN PROGRAM



import java.util.Scanner;

import jssc.SerialPortException;

public class StudioOnOff 
{
	public static void main(String[] args) throws SerialPortException, InterruptedException
	{
		BMSMethods bms = new BMSMethods();
		//bms.portRectifier();
		bms.portClose();
		bms.portOpen();
	
		Scanner scan = new Scanner(System.in);
		String input = "";
		
		
		
		while(!input.equalsIgnoreCase("QUIT"))
		{
			System.out.println("enter \"help\" for help");
			System.out.print("Enter command: ");
			input = scan.nextLine();
			
			//all on
			if(input.equalsIgnoreCase("STUDIO ON"))
			{
				System.out.println("STUDIOS LAUNCHING");
				bms.launchStudio1();
				bms.launchStudio2();
				bms.launchStudio3();
				System.out.println("STUDIOS ON");
			}
			//all off
			else if(input.equalsIgnoreCase("STUDIO OFF"))
			{
				System.out.println("STUDIOS SHUTTING DOWN");
				bms.shutdownStudio1();
				bms.shutdownStudio2();
				bms.shutdownStudio3();
				System.out.println("STUDIOS OFF");
			}
			//1 on
			else if(input.equalsIgnoreCase("STUDIO 1 ON"))
			{
				System.out.println("STUDIO 1 LAUNCHING");
				bms.launchStudio1();
				System.out.println("STUDIO 1 ON");
			}
			//1 off
			else if(input.equalsIgnoreCase("STUDIO 1 OFF"))
			{
				System.out.println("STUDIO 1 SHUTTING DOWN");
				bms.shutdownStudio1();
				System.out.println("STUDIO 1 OFF");
			}
			//2 on
			else if(input.equalsIgnoreCase("STUDIO 2 ON"))
			{
				System.out.println("STUDIO 2 LAUNCHING");
				bms.launchStudio2();
				System.out.println("STUDIO 2 ON");
			}
			//2 off
			else if(input.equalsIgnoreCase("STUDIO 2 OFF"))
			{
				System.out.println("STUDIO 2 SHUTTING DOWN");
				bms.shutdownStudio2();
				System.out.println("STUDIO 2 OFF");
			}		
			//3 on
			else if(input.equalsIgnoreCase("STUDIO 3 ON"))
			{
				System.out.println("STUDIO 3 LAUNCHING");
				bms.launchStudio3();
				System.out.println("STUDIO 3 ON");
			}
			//3 off
			else if(input.equalsIgnoreCase("STUDIO 3 OFF"))
			{
				System.out.println("STUDIO 3 SHUTTING DOWN");
				bms.shutdownStudio3();
				System.out.println("STUDIO 3 OFF");
			}
			
			//help
			else if(input.equalsIgnoreCase("HELP"))
			{
				System.out.println("-+-+-HELP-+-+-");
				System.out.println("enter on of the following commands without spaces");
				System.out.println("STUDIO ON = turns all studios on");
				System.out.println("STUDIO OFF = turns all studios off");
				System.out.println("STUDIO X ON = turns studio x on");
				System.out.println("STUDIO X OFF = turns studio X off");
				System.out.println("STUDIO X ## COOL = cool room X down to ##");
				System.out.println("STUDIO X ## HEAT = heat room X up to ##");
				System.out.println("QUIT = quits the program");
			}
			
			else if(input.equalsIgnoreCase("QUIT"))
			{
				System.out.println("QUITTING");
				bms.portClose();
				System.exit(1);
			}	
			else
			{
				System.out.println("command not recognized");
			}
			
		}
		
		
		bms.portClose();
	}
}

import java.io.IOException;
import java.net.MalformedURLException;

import jssc.SerialPort;
import jssc.SerialPortException;


public class RoomAlertAC 
{
	final static String on = "ON";
	final static String off = "OFF";
	
	
	public static void main(String[] args) throws SerialPortException, InterruptedException, MalformedURLException, IOException
	{
		BMSMethods bms = new BMSMethods();
		bms.portOpen();

		int i=0;
		int[] ACMachine = new int[]{50,53};
		int currentMachine =0;
		int cooling =1;
		
		while(i==0)
		{
			double studio1CR = bms.readSensor("http://192.168.1.208/getData.json");
			double studio1BTH = bms.readSensor("http://192.168.1.207/getData.json");
			double studioMR = bms.readSensor("http://192.168.1.206/getData.json");
			int state =0;
			int requestC =0;
			int requestB =0;
			int requestM =0;
			if(studio1CR > 73)
			{	
				System.out.println("1CR asking cold");
				bms.relayWrite(37, on);
				requestC+=1;
			}	
			if(studio1BTH > 73)
			{
				System.out.println("1BTH asking cold");
				bms.relayWrite(36 , on);			
				requestB+=1;
			}	
			if(studioMR > 73)
			{
				System.out.println("MR asking cold");
				bms.relayWrite(48 , on);			
				bms.relayWrite(49 , on);			
				requestM+=1;
			}
			
			System.out.println("Right before if stats\nrequestC="+requestC
					+"\nrequestB="+requestB
					+"\nstudio1CR= "+studio1CR
					+"\nstudioMR= "+studioMR
					+"\nstudio1BTH= "+studio1BTH);
			int requestA =requestB+requestC+requestM;
			//someone is asking for cold
			if(requestA != 0)
			{
				//machine decider, alternates between machines
				int machine = ACMachine[currentMachine%2];
				//decide which machine to turn on and then turn it on
				bms.relayWrite(machine , on);
				System.out.println("Turning on machine "+machine+". ");
				
				//cool until all requests are satisfied
				while(requestA != 0)
				{


					
					//check if cr1 is cool enough
					if(studio1CR < 71)
					{
						System.out.println("1CR  satisfied");
						bms.relayWrite(37,off);
						requestC=0;
					}
					if(studio1BTH < 71)	
					{	
						System.out.println("1BTH satisfied");
						bms.relayWrite(36,off);
						requestB=0;
					}
					if(studioMR < 71)	
					{	
						System.out.println("MR satisfied");
					
						bms.relayWrite(49,off);
						requestM=0;
					}
					if(studioMR > 73)
					{	
						System.out.println("MR asking cold");
						bms.relayWrite(48,on);
						bms.relayWrite(49,on);
						requestM+=1;
					}	
					if(studio1CR > 73)
					{	
						System.out.println("1CR asking cold");
						bms.relayWrite(37, on);
						requestC+=1;
					}	
					
					if(studio1BTH > 73)
					{
						System.out.println("1BTH asking cold");
						bms.relayWrite(36 , on);			
						requestB+=1;
					}
					System.out.println("Temp check;\nCR1="+studio1CR
							+"\nBTH1="+studio1BTH
							+"\nMR1="+studioMR
							+"\nrequestC"+requestC
							+"\nrequestB"+requestB
							+"\nrequestM"+requestM);
					if((requestM == 0)&&(requestA == 1))
					{
						bms.relayWrite(48, on);
					}	
					else
					{
					}
					requestA =requestB+requestC+requestM;
					Thread.sleep(120000);
					studio1CR = bms.readSensor("http://192.168.1.208/getData.json");
					studio1BTH = bms.readSensor("http://192.168.1.207/getData.json");
					studioMR = bms.readSensor("http://192.168.1.206/getData.json");
					
				}//while end	
				
				//turn off ac and handle chain
				//take care of alternating the ac machine and turning it off
				bms.relayWrite(machine , off);
				currentMachine++;
				bms.relayWrite(49, on);
				Thread.sleep(120000);
				bms.relayWrite(37, off);
				bms.relayWrite(36, off);
				bms.relayWrite(49,  on);
			}//if end	
			else
			{	

				bms.relayWrite(50,off);
				bms.relayWrite(53,off);
				Thread.sleep(120000);
			}	
			//slight delay to main while to not fry stuff
			Thread.sleep(200);
		}//while end

		
		
		
	}
}
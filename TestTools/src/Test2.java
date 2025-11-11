
/*
License
-------
This code is published and shared by Numato Systems Pvt Ltd under GNU LGPL 
license with the hope that it may be useful. Read complete license at 
http://www.gnu.org/licenses/lgpl.html or write to Free Software Foundation,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
Simplicity and understandability is the primary philosophy followed while
writing this code. Sometimes at the expence of standard coding practices and
best practices. It 
This demo code uses "java Simple Serial Connector" library from 
https://code.google.com/p/java-simple-serial-connector/. jSSC Documentation is 
available at https://code.google.com/p/java-simple-serial-connector/w/list
is your responsibility to independantly assess and implement
coding practices that will satisfy safety and security necessary for your final
application.
This demo code demonstrates how to turn a USB Relay Module's relay & IOs to on/off (logic high/low) state and 
demonstrates how to read an analog channel.
This demo code uses "java Simple Serial Connector" or "JSSC" library from 
https://code.google.com/p/java-simple-serial-connector/. jSSC Documentation is 
available at https://code.google.com/p/java-simple-serial-connector/w/list */


import jssc.SerialPort;  /* Calls the respective serial port */
import jssc.SerialPortException;
import jssc.SerialPortList; /* initializes unmathced catagories as string */

public class Test2
{

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) 
   {
   
	   
	   //get a list of all serial ports to try to figure out which is ehich
	   String[] portNames = SerialPortList.getPortNames();
	   
	   
	   if(portNames.length == 0)
	   {
		   System.out.println("No Ports Found");
		   System.exit(0);
	   }
	   else
	   {
		   for(int i=0;i<portNames.length;i++)
		   {
			   System.out.println(portNames[i]);
		   }
	   }
	   //-----------------------------------------------------------------------------------------------
	   //hard coded open serial port that i know is 3 in this case
	   SerialPort porterino = new SerialPort("COM6");
	   try
	   {
		   if(porterino.openPort() == true)
		   {
			   System.out.println("Porterino has been opened");
		   }
		   else
		   {
               System.exit(0);
           }
		   
                /*Set flow control to None*/
                porterino.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

                porterino.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);

               // porterino.writeString("\r");
                porterino.writeString("gpio read 1\r");
                // porterino.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
               Thread.sleep(100);
                System.out.println("pre");

               // byte[] buffer = porterino.readBytes(6);
               // String v = new String(buffer);

               // System.out.println(v);
                
                System.out.println(porterino.readString());
     
                System.out.println("post");

    
              //  porterino.writeString("\r");
               // porterino.writeString("relay read 1\r");
               // System.out.println(porterino.readString());
              //  System.out.println("read 1");

           

                porterino.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
                
                porterino.closePort();
                
                
	   }
       catch (SerialPortException ex)
	   {
    	   System.out.println(ex);
       } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   		
    
    }    
}



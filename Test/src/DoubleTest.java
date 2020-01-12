import jssc.SerialPort;  /* Calls the respective serial port */
import jssc.SerialPortException;
import jssc.SerialPortList; /* initializes unmathced catagories as string */
import jssc.SerialPortTimeoutException;

//test class to see how to interact with both boards at the same time

public class DoubleTest 
{
	   public static void main(String[] args)
	   {
		   
		   SerialPort one = new SerialPort("COM3");
		   SerialPort two = new SerialPort("COM6");
		   
		   
		   try {
		   
			   if((one.openPort() == true)&&(two.openPort() == true))
			   {
				   System.out.println("Porterino has been opened");
			   }
			   else
			   {
	               System.exit(0);
	           }
			   
			   
			   
		   
           one.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
           two.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		   

           one.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);

           one.writeString("relay read 10\r");
          
           System.out.println("pre1-------------");
           Thread.sleep(100);
          
           
           
           String test =   one.readString();
           System.out.println(test);
           
   
           System.out.println("post1------------------");
		   
           one.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
          
           
           two.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);

          // two.writeString("\r");
           two.writeString("gpio read 1\r");
           Thread.sleep(100);
           System.out.println("pre2--------------");
           
  
          System.out.println(  two.readString());

           System.out.println("post2-------------------");
           two.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);

           
           one.closePort();
           two.closePort();
           
           
		   }//end try
	       catch (SerialPortException|InterruptedException ex)
		   {
	    	   System.out.println(ex);
	       }
	   
	   }
		
}

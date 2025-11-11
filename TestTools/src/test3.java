import jssc.SerialPort;  /* Calls the respective serial port */
import jssc.SerialPortException;
import jssc.SerialPortList; /* initializes unmathced catagories as string */
import jssc.SerialPortTimeoutException;

//test class to see how to interact with both boards at the same time

public class test3 
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

          // one.writeString("\r");
           one.writeString("relay read 10\r");
          
           Thread.sleep(100); 
           String test =   one.readString();
  
           one.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);
          
           
           two.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);

           two.writeString("gpio read 1\r");
           Thread.sleep(100);
           String test2 = two.readString();

           two.purgePort(SerialPort.PURGE_RXCLEAR & SerialPort.PURGE_TXCLEAR);

           
           one.closePort();
           two.closePort();
           
           
           System.out.println("------------------------------");

           System.out.println(test);  
           
           System.out.println("------------------------------");

           System.out.println(test2);
           
           System.out.println("------------------------------");

           
           String newtest = test.substring(15,18);
           
           String newtest2 = test2.substring(13,14);
           
           
           
           
           System.out.println("------------------------------");

           System.out.println(newtest);  
           
           System.out.println("------------------------------");

           System.out.println(newtest2);
          
           System.out.println("------------------------------");

           
  
           
           
		   }//end try
	       catch (SerialPortException|InterruptedException ex)
		   {
	    	   System.out.println(ex);
	       }
	   
	   }
		
}

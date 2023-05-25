import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegexPractice 
{
	public static void main (String[] args)
	{
		
		
		 String input = "CONTROL ROOM 3 45";
		 String wrong = "CONTROLER @ ##";
		
		
		 char roomChar = input.charAt(13);
		 int roomInt = Character.getNumericValue(input.charAt(13));
		 System.out.println(roomChar);
		 System.out.println(roomInt);
		 
		if(input.matches("CONTROL ROOM \\d \\d{2}")) 
		System.out.println("yeye");	
		else
		{
			System.out.println("wrong");
		}		 
				 


	
	
		//timer stuff to actvate the morning routine
		DateFormat hourFormat      = new SimpleDateFormat("HH");
		DateFormat dayNumberFormat = new SimpleDateFormat("u");
		Date date = new Date();
		int hour = Integer.valueOf(hourFormat.format(date));
		int dayNumber = Integer.valueOf(dayNumberFormat.format(date));
		
		
		//touterAccessibleRoomList[4].setTargetTemp(71);
		int timer=0;
		System.out.println("hour="+hour);
		System.out.println("dayNumber="+dayNumber);
		
					//if it's monday or wednesday at 0700
					if(hour==16 && timer==0 && (dayNumber==1 || dayNumber==3))
					{
						System.out.println("It is 0700, auto launch sequence initiated");
						timer++;
					}	
				
		
			
	

		
		
		
		
		
	
	
	
	
	
	
	
	
	
	
	}
	
	
	
}

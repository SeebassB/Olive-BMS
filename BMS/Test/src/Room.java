import java.io.IOException;
import java.net.MalformedURLException;

import jssc.SerialPortException;

public class Room
{


	//variables that are given once and never change
	String roomName;//name of the room
	double targetTemp;//target temperature of the room
	int coolHeat;//does the client want the room heated or cooled, 0 is cool and 1 is heat
	int percentAirflow; //percentage of airflow that the room can take
	String sensorURL;//URL of the sensor for the room
	BMSMethods bms;//gives access to the BMSMethods methods, used in finding relay stuff

	//variables found and altered as the program runs
	double currentTemp;//current temperature of the room
	double targetCutoffTemp;//temperature where the system will stop heating/cooling
	String damperState;//whether the damper is open or closed
	int requestState;//how the room is feeling, -1 is satisfied, 0 is cooling, 1 is heating
	int damperNumber;//number of relay for the damper of the room
	int previousState;
	
	//constructor
	public Room(String inName, int cH,int pAir, String inURL, int dampNum, BMSMethods BMSin, String dampIn) throws SerialPortException, InterruptedException
	{
		roomName         = inName;
		currentTemp      =  72;
		targetTemp       = 74.00;
		coolHeat         = cH;
		targetCutoffTemp = targetTemp - (Math.pow(-1, cH))*1.5; //calculate cutoffTemp
		percentAirflow   = pAir;
		requestState     = -1;
		sensorURL        = inURL;
		damperNumber     = dampNum;
		damperState      = dampIn;
		bms              = BMSin;
		previousState    = -1;
		refresh();
	//	bms.relayRead(dampNum);
	}

	//default constructor
	public Room() throws SerialPortException, InterruptedException
	{
		//variables
		roomName ="Default";//name of the room
		currentTemp = updateTemp();//current temperature of the room
		targetTemp = 74.00;//target temperature of the room
		coolHeat =0;//does the client want the room heated or cooled, 0 is cool and 1 is heat
		targetCutoffTemp = 72.50;//tempreature where the system will stop heating/cooling
		percentAirflow =3; //percentage of airflow that the room can take
		damperState ="off";//whether the damper is open or closed
		requestState =0;//how the room is feeling, -1 is satisfied, 0 is cooling, 1 is heating
		sensorURL = "http://192.168.1.208/getData.json";
		damperNumber = 6;//number of the damper associated with the room
		previousState=-1;//previous requestState of the room 
		BMSMethods bms;
		refresh();
	}

	//gets and sets-----------------------------------------------------------------------------------
	
	//gets and set for roomName
	public void setRoomName(String in)
	{
		roomName = in;
	}	
	public String getRoomName()
	{
		return roomName;
	}

	//get and set for currentTemp
	public void setCurrentTemp(double in)
	{
		currentTemp = in;
	}
	public double getCurrentTemp()
	{
		return currentTemp;
	}

	//set and get for targetTemp
	public void setTargetTemp(int in)
	{
		targetTemp = (double)in;
		fixTargetCutoffTemp();
	}
	public double getTargetTemp()
	{
		return targetTemp;
	}

	//set and get for coolHeat, has to also set the targetCutoffTemp based on heat/cool
	public void setCoolHeat(int in)
	{
		coolHeat = in;
		fixTargetCutoffTemp();
	}
	public int getCoolHeat()
	{
		return coolHeat;
	}

	//get for targetCutoffTemp
	public double getTargetCutoffTemp()
	{
		return targetCutoffTemp;
	}

	/**
	 * Method used to fix targetCuttoffTemp, depending on how coolHeat is set
	 * will set target to +/- 1.5 from the target temp
	 *
	 * @return return the new targetCuttoffTemp
	 * */	
	public double fixTargetCutoffTemp()
	{
		double out = targetTemp - (Math.pow(-1, coolHeat))*1.5;
		targetCutoffTemp = out;
		return out;
	}

	//get and set for percentAirflow
	public void setPercentAirflow(int in)
	{
		percentAirflow = in;
	}
	public int getPercentAirflow()
	{
		return percentAirflow;
	}
	
	//get and set for damperState
	public void setDamperState(String in) throws SerialPortException, InterruptedException
	{
		damperState = in;
		bms.relayWrite(damperNumber,in);

	}
	public String getDamperState()
	{
		return damperState;
	}

	//get and set for satisfiedState
	public void setRequestState(int in)
	{
		requestState = in;
	}
	public int getRequestState()
	{
		return requestState;
	}

	//get and set the URL for the sensor for this room
	public void setSensorURL(String in)
	{
		sensorURL = in;
	}
	public String getSensorURL()
	{
		return sensorURL;
	}

	//get and set for damper number
	public void setDampNumer(int in)
	{
		damperNumber = in;
	}
	public int getDampNumber()
	{
		return damperNumber;
	}

	//get and set for previous
	public void setPreviousState()
	{
		previousState=getRequestState();
	}
	public void setPreviousState(int in)
	{
		previousState = in;
	}
	public int getPreviousState()
	{
		return previousState;
	}

	///////////////////////////////////////////////////////////////////////////////////
	//METHODS FOR THE ROOMS
	
	//refresh all of the important stats
	public void refresh() 
	{
		updateTemp();
		
		fixTargetCutoffTemp();
		
		try 
		{
			setDamperState(bms.relayRead(damperNumber));
		} 
		catch (SerialPortException | InterruptedException e)
		{
			System.out.println("MAJOR PROBLEM, MOST LIKELY A SENSOR IS MISSING");
			e.printStackTrace();
		}

		setRequestState(checkRequest());
	}

	/**
	 * Method used to check to see if the room has passed the targetCutoffTemp
	 * this will tell the room that it has cooled enough
	 * @return int returns whether the room has passed the extra degrees of leeway
	 * */
	public int checkCutoff()
	{
		//check cooling
		if(coolHeat == 0)
		{
			if(currentTemp>=targetCutoffTemp)
			{
				requestState = 0;
			}
			else
				requestState = -1;
		}
		//check heating
		else if(coolHeat == 1)
		{
			if(currentTemp<=targetCutoffTemp)
			{
				requestState = 1;
			}
			else
				requestState = -1;
		}
		return requestState;
	}
	
	/**
	 * Method used to to check on the state of the room, if it needs cooling/heating
	 *
	 * @return int returns the state of the room, -1 is no request, 0 is asking for cool, 1 is asking for heat
	 * */
	public int checkRequest()
	{
		//check cooling
		if(coolHeat == 0)
		{
			if(currentTemp>=targetTemp)
			{
				requestState = 0;
			}
			else
				requestState = -1;
		}
		//check heating
		else if(coolHeat == 1)
		{
			if(currentTemp<=targetTemp)
			{
				requestState = 1;
			}
			else
				requestState = -1;
		}
		return requestState;
	}

	/**
	 * Method used to update the temperature of the room
	 * Updates currentTemp, you dont need to read it to do stuff, the return is just nice to have data
	 * @return double returns the updated temperature
	 * */
	public double updateTemp()
	{
		try 
		{
			setCurrentTemp(bms.readSensor(sensorURL));
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
			System.out.println("Problem with MalformedURLException");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("Problem with IOException");
		}
		return getCurrentTemp();
	}

	/** Method used to set variables in response to a temp you want it to
	 *
	 * @param double input of temp you want
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 */
	public void conditionTo(double in) throws SerialPortException, InterruptedException
	{
		targetTemp = in;
		coolHeat = 0;//guessing that you want to cool over heat
		refresh();
	}

	/** Method used to set variables in response to a temp you want it to
	 * OVERLOADED FROM ABOVE METHOD
	 *
	 * @param int input of hot/cold 0 being cold, 1 being hot
	 * @param double input of temp you want
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 */
	public void conditionTo(double in, int cH) throws SerialPortException, InterruptedException
	{
		targetTemp = in;
		coolHeat = cH;
		refresh();
		String coHe = "cool";
		if(coolHeat == 1)
			coHe = "heat";
		System.out.println("UPDATED "+roomName+" to "+coHe+" to "+targetTemp+", currently at "+currentTemp);
	}

	/**
	 * Method used to prepare the room to accept HVAC
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * */
	public void acceptHVAC() throws SerialPortException, InterruptedException
	{
		updateTemp();
		fixTargetCutoffTemp();
		setDamperState("on");
		bms.openDamper(damperNumber);
		System.out.println(roomName+" ready to accept HVAC.");
	}
	
	/**
	 * Method used to close up the room after it satisfies its cooling
	 * @throws InterruptedException 
	 * @throws SerialPortException 
	 * */
	public void closeHVAC() throws SerialPortException, InterruptedException
	{
		setDamperState("off");
		bms.closeDamper(damperNumber);
		System.out.println(roomName+" is closed");
	}
	
	/**
	 * Method used to keep track of a room's current vs cutoff temps
	 * intended to return 1 when the room is satisfied, 0 if its still cooling/heating
	 * @return int 0 if satisfied, 1 if not
	 * */
	public int satisfiedCheck()
	{
		int out =0;
		//cooling
		if(coolHeat==0)
		{	if(currentTemp>targetCutoffTemp)
				return 1;
		}
		//heating
		else if(coolHeat==1)
		{
			if(currentTemp<targetCutoffTemp)
				return 1;
		}	
		return out;	
	}
		
	//TODO UPDATE INTO A LOG
	/**
	 * Method used to print out the variables of the class in a readable form
	 * System.out.prints the rooms data
	 * */
	public void printRoom()
	{
		System.out.println("+-+-+-+-+\n"+roomName+", Current Temp= "+currentTemp+", targetTemp= "+targetTemp+"");
		System.out.println(" coolHeat= "+coolHeat+", targetCutoffTemp= "+targetCutoffTemp+", percentAirflow= "+percentAirflow);
		System.out.println(" damperState= "+damperState+", requestState= "+requestState+", sensorURL= "+sensorURL+", damperNumber= "+damperNumber);
	}

	/**
	 * Method used to print an abbreviated data of the room
	 * */
	public void printRoomShort()
	{
		System.out.println(roomName+" "+currentTemp+" "+targetTemp+" "+targetCutoffTemp+" "+requestState+" "+ damperState);
	}
}
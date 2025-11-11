import java.io.IOException;
import java.net.MalformedURLException;

import jssc.SerialPortException;

public class Room
{


	//variables that are given once and never change
	final String roomName;//name of the room
	double targetTemp;//target temperature of the room
	char coolHeat;//does the client want the room heated or cooled, 0 is cool and 1 is heat
	final int percentAirflow; //percentage of airflow that the room can take
	final String sensorURL;//URL of the sensor for the room
	final BMSMethods bms;//gives access to the BMSMethods methods, used in finding relay stuff

	//variables found and altered as the program runs
	double currentTemp;//current temperature of the room
	double targetCutoffTemp;//temperature where the system will stop heating/cooling
	String damperState;//whether the damper is open or closed
	char requestState;//how the room is feeling, -1 is satisfied, 0 is cooling, 1 is heating
	final int damperNumber;//number of relay for the damper of the room
	char previousState;
	
	//constructor
	public Room(String inName, char cH,int pAir, String inURL, int dampNum, BMSMethods BMSin, String dampIn) throws SerialPortException, InterruptedException
	{
		roomName         = inName;
		currentTemp      = 72;
		targetTemp       = 74.00;
		coolHeat         = cH;
		targetCutoffTemp = targetTemp - (Math.pow(-1, cH))*1.5; //calculate cutoffTemp
		percentAirflow   = pAir;
		requestState     = 'c';
		sensorURL        = inURL;
		damperNumber     = dampNum;
		damperState      = dampIn;
		bms              = BMSin;
		previousState    = 'n';
		refresh();
	}

	//gets and sets-----------------------------------------------------------------------------------

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
		targetTemp = in;
		fixTargetCutoffTemp();
	}
	public double getTargetTemp()
	{
		return targetTemp;
	}

	//set and get for coolHeat, has to also set the targetCutoffTemp based on heat/cool
	public void setCoolHeat(char in)
	{
		coolHeat = in;
		fixTargetCutoffTemp();
	}
	public char getCoolHeat()
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
	 * */
	public void fixTargetCutoffTemp()
	{
        targetCutoffTemp = targetTemp - (Math.pow(-1, coolHeat))*1.5;
	}

	//get for percentAirflow
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
	public void setRequestState(char in)
	{
		requestState = in;
	}
	public char getRequestState()
	{
		return requestState;
	}

	//get and set the URL for the sensor for this room
	/*public void setSensorURL(String in)
	{
		sensorURL = in;
	}
	public String getSensorURL()
	{
		return sensorURL;
	}
*/

	//get for damper number
	public int getDampNumber()
	{
		return damperNumber;
	}

	//get and set for previousState
	public void setPreviousState(char in)
	{
		previousState = in;
	}
	public char getPreviousState()
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
	 * Checks whether the room is requesting a cutoffTemp conditioning
	 * @return  returns whether the room has passed the extra degrees of leeway
	 * */
	public char checkCutoff()
	{
		//check cooling
		if(coolHeat == 'c')
		{
			if(currentTemp>=targetCutoffTemp)
			{
				requestState = 'c';
			}
			else
				requestState = 'n';
		}
		//check heating
		else if(coolHeat == 'h')
		{
			if(currentTemp<=targetCutoffTemp)
			{
				requestState = 'h';
			}
			else
				requestState = 'n';
		}
		return requestState;
	}
	
	/**
	 * Checks the room's current conditioning request and updates it's requestState to match
	 *
	 * @return returns the state of the room, n is no request, c is asking for cool, h is asking for heat
	 * */
	public char checkRequest()
	{
		//check cooling
		if(coolHeat == 'c')
		{
			if(currentTemp>=targetTemp)
			{
				requestState = 'c';
			}
			else
				requestState = 'n';
		}
		//check heating
		else if(coolHeat == 'h')
		{
			if(currentTemp<=targetTemp)
			{
				requestState = 'h';
			}
			else
				requestState = 'n';
		}
		return requestState;
	}

	/**
	 * Method used to update the temperature of the room
	 * Updates currentTemp, you dont need to read it to do stuff, the return is just nice to have data
	 */
	public void updateTemp()
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
	}

	/**
	 * Method used to prepare the room to accept HVAC
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
	 * */
	public void closeHVAC() throws SerialPortException, InterruptedException
	{
		setDamperState("off");
		bms.closeDamper(damperNumber);
		System.out.println(roomName+" is closed");
	}

}
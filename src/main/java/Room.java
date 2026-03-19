import java.io.IOException;

public class Room
{
	double CUTOFF_AMOUNT = 1.5;//adjust this to alter the cutoff temp

	final String roomName;//name of the room
	final int percentAirflow; //percentage of airflow that the room can take
	final String sensorURL;//URL of the sensor for the room
	final int damperNumber;//number of relay for the damper of the room

	double targetTemp;//target temperature of the room
	double currentTemp;//current temperature of the room
	char coolHeat;//does the client want the room heated or cooled, c/C is cool and h/H is heat
	double targetCutoffTemp;//temperature where the system will stop heating/cooling
	String damperState;//whether the damper is open or closed
	char requestState;//how the room is feeling, -1 is satisfied, 0 is cooling, 1 is heating
	char previousState;


	/**
	 * Represents a room in the building
	 * @param inName the name of the room
	 * @param cH coolHeat, whether the room is set to cool or heat
	 * @param pAir the percentage airflow the room takes while open
	 * @param inURL the url for the room's thermometer
	 * @param dampNum the number of the relay that controls the damper to the room
	 * */
	public Room(String inName, char cH,int pAir, String inURL, int dampNum)
	{
		roomName         = inName;//final
		currentTemp      = 72;
		targetTemp       = 74.00;
		coolHeat         = cH;
		fixTargetCutoffTemp(); //calculate cutoffTemp
		percentAirflow   = pAir;//final
		updateRequestState();
		sensorURL        = inURL;//final
		damperNumber     = dampNum;//final
		previousState    = 'n';
	}

	//gets and sets-----------------------------------------------------------------------------------

	public String getRoomName()
	{
		return roomName;
	}

	public void setCurrentTemp(double currentTemp)
	{
		this.currentTemp = currentTemp;
	}
	public double getCurrentTemp()
	{
		return currentTemp;
	}

	public void setTargetTemp(double targetTemp)
	{
		this.targetTemp = targetTemp;
		fixTargetCutoffTemp();//whenever you set the temp you should update the cutoff
	}
	public double getTargetTemp()
	{
		return targetTemp;
	}

	public void setCoolHeat(char coolHeat)
	{
		this.coolHeat = coolHeat;
		fixTargetCutoffTemp();//whenever you change coolHeat you should update the cutoff
	}
	public char getCoolHeat()
	{
		return coolHeat;
	}

	public double getTargetCutoffTemp()
	{
		return targetCutoffTemp;
	}

	public int getPercentAirflow()
	{
		return percentAirflow;
	}

	public void setDamperState(String damperState)
	{
		this.damperState = damperState;
	}
	public String getDamperState()
	{
		return damperState;
	}

	public int getDamperNumber() {
		return damperNumber;
	}

	public void setRequestState(char requestState)
	{
		this.requestState = requestState;
	}
	public char getRequestState()
	{
		return requestState;
	}

	public void setPreviousState(char previousState)
	{
		this.previousState = previousState;
	}
	public char getPreviousState()
	{
		return previousState;
	}


	/**
	 * Fixes targetCutoffTemp based on coolHeat<br>
	 * Adjusts this room's targetCutoffTemp based on CUTOFF_AMOUNT up or down<br>
	 * Used when adjusting the temp of the room
	 * */
	private void fixTargetCutoffTemp()
	{
		if(coolHeat == 'c' || coolHeat == 'C' || coolHeat == 'n')
			targetCutoffTemp = targetTemp + CUTOFF_AMOUNT;
		else if(coolHeat == 'h' || coolHeat == 'H')
			targetCutoffTemp = targetTemp - CUTOFF_AMOUNT;
		BMSMethods.logInfo("Fixing temp for "+ roomName +" to "+targetCutoffTemp, "DEBUG");
	}

	/**
	 * Updates the temperature of the room, reads from the sensor
	 * This method also calls fixTargetCutoffTemp() which adjust the value for this room
	 * This is done since the cutoff should always be reflected off of the temp
	 */
	public void updateTemp()
	{
		try 
		{
			setCurrentTemp(BMSMethods.readSensor(sensorURL));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Room.updateTemp() failed");
		}
		BMSMethods.logInfo(" UpdateTemp for room " + roomName,"DEBUG");
		fixTargetCutoffTemp();
	}

	/**
	 * Updates the current room's requestState based on the room's temp.<br>
	 * Usually you will run this right after updating the temp of the room for more accurate results
	 * <p>
	 * FOR COOLHEAT = c<br>
	 * request C = above cutoff<br>
	 * request c = above target and below cutoff<br>
	 * n = nothing
	 * <p>
	 * FOR COOLHEAT = h<br>
	 * request H = below cutoff<br>
	 * request h = below target and above cutoff<br>
	 * request n = nothing
	 */
	public void updateRequestState()
	{
		if(coolHeat == 'c' || coolHeat == 'C')
		{
			if (currentTemp >= targetCutoffTemp)
				setRequestState('C');
			else if (currentTemp >= targetTemp)
				setRequestState('c');
			else
				setRequestState('n');
		}
		else if(coolHeat == 'h' || coolHeat == 'H')
		{
			if(currentTemp <= targetCutoffTemp)
				setRequestState('H');
			else if(currentTemp <= targetTemp)
				setRequestState('h');
			else
				setRequestState('n');
		}
		BMSMethods.logInfo(" UpdateRequestState for room " + roomName + " request state set to "+ requestState, "DEBUG");
	}



}
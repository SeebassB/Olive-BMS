import java.io.IOException;
import jssc.SerialPortException;

public class Room
{
	double CUTOFF_AMOUNT = 1.5;//adjust this to alter the cutoff temp


	//variables that are given once and never change
	final String roomName;//name of the room
	final int percentAirflow; //percentage of airflow that the room can take
	final String sensorURL;//URL of the sensor for the room
	final int damperNumber;//number of relay for the damper of the room


	//variables found and altered as the program runs
	double targetTemp;//target temperature of the room
	double currentTemp;//current temperature of the room
	char coolHeat;//does the client want the room heated or cooled, 0 is cool and 1 is heat
	double targetCutoffTemp;//temperature where the system will stop heating/cooling
	String damperState;//whether the damper is open or closed
	char requestState;//how the room is feeling, -1 is satisfied, 0 is cooling, 1 is heating
	char previousState;
	
	/**
	 * A Room is designed to mimic one of the rooms in the building, holding information relevant to the HVAC
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
		refresh();
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

	private void setDamperState(String damperState) throws SerialPortException, InterruptedException
	{
		this.damperState = damperState;
		BMSMethods.relayWrite(damperNumber,this.damperState);
	}
	public String getDamperState()
	{
		return damperState;
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
	 * Refreshes the information of the room, updates the temperature then adjusts the request state
	 * Then sets the damper state for the room to whatever it physically is
	 * */
	public void refresh()
	{

		updateTemp();
		updateRequestState();

		//update the damper state as well
		try 
		{
			setDamperState(BMSMethods.relayRead(damperNumber));
		} 
		catch (SerialPortException | InterruptedException e)
		{
			e.printStackTrace();
			System.err.println("Room.refresh(): failed");
		}
	}


	/**
	 * Fixes targetCutoffTemp based on coolHeat
	 * Adjusts this room's targetCutoffTemp based on CUTOFF_AMOUNT up or down
	 * */
	private void fixTargetCutoffTemp()
	{
		if(coolHeat == 'c')
			targetCutoffTemp = targetTemp + CUTOFF_AMOUNT;
		if(coolHeat == 'h')
			targetCutoffTemp = targetTemp - CUTOFF_AMOUNT;

	}

	/**
	 * Updates the temperature of the room, reads from the sensor
	 * This method also calls fixTargetCutoffTemp() which adjust the value for this room
	 * This is done since the cutoff should always be reflected off of the temp
	 */
	private void updateTemp()
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
	 *
	 */
	public void updateRequestState()
	{
		if(coolHeat == 'c')
		{
			if (currentTemp >= targetCutoffTemp)
				setRequestState('C');
			else if (currentTemp >= targetTemp)
				setRequestState('c');
			else
				setRequestState('n');
		}
		else if(coolHeat == 'h')
		{
			if(currentTemp <= targetCutoffTemp)
				setRequestState('H');
			else if(currentTemp <= targetTemp)
				setRequestState('h');
			else
				setRequestState('n');
		}
	}

	/**
	 * Used to open the room to receive conditioning
	 * */
	public void acceptHVAC() throws SerialPortException, InterruptedException
	{
		setDamperState("on");
		BMSMethods.openDamper(damperNumber);
		System.out.println(roomName+" is open");
	}
	
	/**
	 * Used to close the room after it has received conditioning
	 * */
	public void closeHVAC() throws SerialPortException, InterruptedException
	{
		setDamperState("off");
		BMSMethods.closeDamper(damperNumber);
		System.out.println(roomName+" is closed");
	}

}
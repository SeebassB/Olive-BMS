
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jssc.SerialPortException;


public class TextFieldLabel extends JFrame
{

	String[] commanded = {"\n","\n","\n","\n","studio off"
			+ "\n"};
	String commandAdd="";
	
	BMSMethods bms;	

	
	public TextFieldLabel(BMSMethods bms2) 
	{
		super("HVAC and Light Controller");
		
		JTextField textField = new JTextField("", 20);
		JTextArea textArea = new JTextArea("",7,20);
		JLabel label = new JLabel();
		JLabel label2 = new JLabel();
		JButton button = new JButton("OK");
		JButton helpButton = new JButton("HELP");
		bms = bms2;
		
		setLayout(new FlowLayout());

		// customizes appearance: font, foreground, background
		textField.setFont(new java.awt.Font("Arial", Font.ITALIC | Font.BOLD, 12));
		textArea.setFont(new java.awt.Font("Arial", Font.ITALIC | Font.BOLD, 12));
		textField.setToolTipText("Please enter your command here");
		//textField.setForeground(Color.BLUE);
		//textField.setBackground(Color.YELLOW);
		
		// customizes text selection
		//textField.setSelectionColor(Color.CYAN);
		//textField.setSelectedTextColor(Color.RED);



		
		label.setText("Enter your command: ");
		label2.setText("");
		
		
		// adds event listener which listens to Enter key event
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String fromText = textField.getText();
				commandAdd = fromText;
				textField.setEnabled(false);
				textField.setText("EXECUTING COMMAND");				
				try 
				{
					commandExecuter(fromText);
				}
				catch (SerialPortException e) 
				{
					System.out.println("Serial problem in textField1");
					e.printStackTrace();
				}
				catch (InterruptedException e) 
				{
					System.out.println("INTERRUPTION problem in textField");
					e.printStackTrace();
				}
			
				textField.setText("");
				commandLister();
				label2.setText(printableArray(commanded));
				textField.setEnabled(true);
				textField.grabFocus();
			}
		});
		
		//keyListener used to hear an escape key hit
		//hitting escape deletes the text in the textField, if that text is empty it quits the program
		textField.addKeyListener
		( new KeyListener()
				{
				public void keyTyped(KeyEvent event)
				{
				
				}

				@Override
				public void keyPressed(KeyEvent event)
				{
					//System.out.println(event.getKeyCode());
					if(event.getKeyCode()==27)
					{
						if(textField.getText().isEmpty())
						{
							System.exit(0);
						}
						else
						{
							textField.setText("");
						}
					}	
				}

				@Override
				public void keyReleased(KeyEvent event) 
				{
					
				}
				
				});
		
		
		// adds action listener for the OK button, basically does the same thing as if enter was hit
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String fromText = textField.getText();	
				commandAdd = fromText;
				textField.setText("EXECUTING COMMAND");
				textField.setEnabled(false);
				try {
					commandExecuter(fromText);
				} catch (SerialPortException e) {
					System.out.println("Serial problem in textField2");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("INTERUPTION problem in textField2");
					e.printStackTrace();
				}			
				textField.setText("");
				commandLister();
				label2.setText(printableArray(commanded));
				textField.setEnabled(true);
				textField.grabFocus();	
			}
		});		
		
		// my listener for the help button, when pressed gives a dialog box that lists all commands
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(TextFieldLabel.this, 
						"STUDIO ON = will turn on all rooms\n"
						+"STUDIO ALL OFF = will turn off all rooms\n"
						+"STUDIO x ON = will turn on studio x\n"
						+"STUDIO x OFF = will turn off studio x\n"
						+"CONTROL ROOM x ## = will set control room x to ##\n"
						+"BOOTH x ## = will set booth x to ##\n"
						+"CONTROL ROOM x COOL    = will set CR x to cool\n"
						+"CONTROL ROOM x HEAT    = will set CR x to heat\n"
						+"BOOTH x COOL     = will set Booth x to cool\n"
						+"BOOTH x HEAT     = will set Booth x to heat\n"
						+"EDIT COOL        = will set EDIT/OUTSIDE to cool\n"
						+"EDIT HEAT        = will set EDIT/OUTSIDE to heat\n"
					//	+"CONTROL ROOM x ## COOL = will cool CR x to ## degrees\n"
					//	+"BOOTH x ## COOL = will cool Booth x to ## degrees\n"
					//	+"CONTROL ROOM x ## HEAT = will heat CR x to ## degrees\n"
					//	+"BOOTH x ## HEAT = will heat Booth x to ## degrees\n"
						+"BLANKET [+|-]# = will raise(+) or drop(-) the temp in all CRs and BTHs by # degrees\n"
						+"RESET = will reset the rooms to cool at 74\n"
						+"OFFICE ## = will set the office's temp to ##\n"
						+"KITCHEN ## = will set the kitchen's temp to ##\n"
						+"PHONE ## = will set the phone booth's temp to ##\n"
						+"EDIT ## = will set the outside rooms to ##\n"
						+"QUIT = will quit the program\n"
						+"To quit hit the x in the top right corner");

			}
		});
		
		
		add(label);
		add(textField);
		add(button);
		add(helpButton);
		//add(textArea);
		add(label2);
		
		setSize(500, 200);		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setLocationRelativeTo(null);	
		setLocation(350,80);
		setVisible(true);
	}
	

	/*
	 * Method used to add into the commanded string
	 * keeps a max of 5 lines, removing the top one
	 * 
	 * */
	
	public void commandLister()
	{
		
		//shift all array lines down
	
		for(int i = commanded.length-1; i>0; i--)
		{
			commanded[i] = commanded[i-1];
		}
		
		commanded[0] = commandAdd+"\n";
						
	}
	
	public String printableArray(String[] input)
	{
		String out = "<html>";
		
		for(String x : input)
			out += x+"<br>";
		out+="</html>";
		return out;
	}
	
	
	/**
	 * Method used to execute command that affect the building, by entering a command correctly it executes
	 * @param String input the command you want to execute
	 * @param BMSMethod bms the building you want to affect
	 * 
	 * */
	public void commandExecuter(String input) throws SerialPortException, InterruptedException
	{
		
		//make sure you dont throw in any commands while the other thread is operating
		int clearanceOnce =0;
		while(HVACMultiTest.hvacThreadStatus==1)
		{
			//printout the waiting for Clearance 2
			if(clearanceOnce<=4)
			{	
				System.out.println("-=-=-=-=Waiting for clearance: "+HVACMultiTest.hvacThreadStatus);
				clearanceOnce++;
			}
			Thread.sleep(1000);
		}
		
		//make the input all uppercase
		input = input.toUpperCase();
		
		
		
	//if else ladder to encompass all commands---------------------------------------------------------

		//turn all studios on
		if(input.equalsIgnoreCase("STUDIO ON") || input.equals("MORNING"))
		{
			System.out.println("STUDIOS LAUNCHING");
			bms.launchAll();
			
			//run the heat cycle if needed
			
			
			
			//lower the temps of the HVACs bac to 74(baseline)
			HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(74);
			System.out.println("STUDIOS ON");
		}		
		
		//turn off all studios
		else if(input.equalsIgnoreCase("STUDIO OFF") || input.equals("NIGHT"))
		{
			
			String message = "CONFIRM STUDIO SHUTDOWN???"; 
	        ImageIcon icon = new ImageIcon("C:\\Users\\BMS Machine\\Pictures\\Saved Pictures/IglooLogo.png");
			if((int)(Math.random()*19 + 1) == 7)
			{
				message = "Do you like Turtles?";
				icon = new ImageIcon("C:\\Users\\BMS Machine\\Pictures\\Saved Pictures/turtle.png");
			}	
			
	        int confirmInput = JOptionPane.showConfirmDialog(null, message,
	        												"SHUTDOWN CONFIRMER", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE, icon);
	        
	        if(confirmInput == 0)
	        {
	        
			System.out.println("STUDIOS SHUTTING DOWN");
			bms.shutdownAll();
			
			//raise the target temps of the rooms to effectively turn off the HVAC
			HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(80);
			HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(80);
			HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(80);
			HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp(80);
			HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(80);
			HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(80);
			System.out.println("STUDIOS OFF");
	        //test
			}
	        else if(confirmInput !=0)
	        {
	        	System.out.println("STUDIO SHUTDOWN AVERTED");
	        }
		}
		
		//turn on a specific studio, CR+BTH
		else if(input.matches("STUDIO \\d ON"))
		{
			//get info from the command
			int roomNumber = Character.getNumericValue(input.charAt(7));
			
			System.out.println("STUDIO "+roomNumber+" LAUNCHING");
			if(roomNumber==1)
			{
				HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(74);
				HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp(74);
				bms.launchStudio1();
				System.out.println("STUDIO 1 ON");
			}	
			else if(roomNumber==2)
			{
				HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(74);
				HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(80);
				bms.launchStudio2();
				System.out.println("STUDIO 2 ON");
			}
			else if(roomNumber==3)
			{
				HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(74);
				HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(74);
				bms.launchStudio3();
				System.out.println("STUDIO 3 ON");
			}

		}
		
		//turn off a specified studio, CR+BTH
		else if(input.matches("STUDIO \\d OFF"))
		{
			//get the room number
			int roomNumber = Character.getNumericValue(input.charAt(7));
			
			System.out.println("STUDIO "+roomNumber+" SHUTTING DOWN");
			
			if(roomNumber==1)
			{
				HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(80);
				HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp(80);
				bms.shutdownStudio1();
				System.out.println("STUDIO 1 OFF");
			}
			else if(roomNumber==2)
			{
				HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(80);
				HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(80);
				bms.shutdownStudio2();
				System.out.println("STUDIO 2 OFF");
			}
			else if(roomNumber==3)
			{
				HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(80);
				HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(80);
				bms.shutdownStudio3();
				System.out.println("STUDIO 3 OFF");
			}	

		}
		
		//condition control room x to ## HHHH, room x to degrees ## and HHHH cool/heat
		else if(input.matches("CONTROL ROOM \\d \\d{2} [C|H].{3}") || input.equalsIgnoreCase("CR\\d \\d{2} [C|H].{3}"))
		{
			
			//replace CR with CONTROL ROOM
			if(input.matches("CR\\d \\d{2} [C|H].{3}"))
			{
				input = "CONTROL ROOM "+input.substring(2);
			}
			
			//get studio #
			int roomNumber = Character.getNumericValue(input.charAt(13));
			//get temp ##
			int tempNumber = Integer.parseInt(input.substring(15,17));
			//get cool/heat
			int coolHeat = -1;
			System.out.println(input.charAt(18));
			//set coolHeat to the correct input based on the first character
			if(input.charAt(18)=='C')
				coolHeat=0;
			else if(input.charAt(18)=='H')
				coolHeat=1;
				
			//apply it to the correct studio
			System.out.println("CONTROL ROOM "+roomNumber+" TEMPERATURE SET TO "+tempNumber+" C/H SET TO "+coolHeat);
			if(roomNumber==1)
			{
				HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(tempNumber);
				HVACMultiTest.outerAccessibleRoomList[0].setCoolHeat(coolHeat);
			}	
			else if(roomNumber==2)
			{
				HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(tempNumber);
				HVACMultiTest.outerAccessibleRoomList[1].setCoolHeat(coolHeat);
			}
			else if(roomNumber==3)
			{
				HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(tempNumber);
				HVACMultiTest.outerAccessibleRoomList[2].setCoolHeat(coolHeat);
			}
			
		}

		//condition booth x to ## HHHH, room x to degrees ## and HHHH cool/heat
		else if(input.matches("BOOTH \\d \\d{2} [C|H].{3}") || input.equalsIgnoreCase("BTH\\d \\d{2} [C|H].{3}"))
		{
			
			//replace CR with CONTROL ROOM
			if(input.matches("BTH\\d \\d{2} [C|H].{3}"))
			{
				input = "BOOTH "+input.substring(3);
			}
			
			//get studio #
			int roomNumber = Character.getNumericValue(input.charAt(6));
			//get temp ##
			int tempNumber = Integer.parseInt(input.substring(8,10));
			//get cool/heat
			int coolHeat = -1;
			//set coolHeat to the correct input based on the first character
			if(input.charAt(19)=='C')
				coolHeat=0;
			else if(input.charAt(19)=='H')
				coolHeat=1;
				
			//apply it to the correct studio
						
			System.out.println("BOOTH "+roomNumber+" TEMPERATURE SET TO "+tempNumber+" C/H SET TO "+coolHeat);
			if(roomNumber==1)
			{
				HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(tempNumber);
				HVACMultiTest.outerAccessibleRoomList[4].setCoolHeat(coolHeat);
			}	
			else if(roomNumber==2)
			{
				HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(tempNumber);
				HVACMultiTest.outerAccessibleRoomList[5].setCoolHeat(coolHeat);
			}
			else if(roomNumber==3)
			{
				HVACMultiTest.outerAccessibleRoomList[6].setTargetTemp(tempNumber);
				HVACMultiTest.outerAccessibleRoomList[6].setCoolHeat(coolHeat);
			}
			
		}
		
		//condition control room x to ##, keeping current cool/heat
		else if(input.matches("CONTROL ROOM \\d \\d{2}") || input.matches("CR\\d \\d{2}"))
		{
			//handle the CR input
			//replace CR with CONTROL ROOM
			if(input.matches("CR\\d \\d{2}"))
			{
				input = "CONTROL ROOM "+input.substring(2);
			}
			
			//get studio #
			int roomNumber = Character.getNumericValue(input.charAt(13));
			//get temp ##
			int tempNumber = Integer.parseInt(input.substring(15,17));
			//apply it to the correct studio
						
			System.out.println("CONTROL ROOM "+roomNumber+" TEMPERATURE SET TO "+tempNumber);
			if(roomNumber==1)
			{
				HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(tempNumber);
			}	
			else if(roomNumber==2)
			{
				HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(tempNumber);
			}
			else if(roomNumber==3)
			{
				HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(tempNumber);
			}
			
		}
		
		//condition booth x to ##, keeping current cool/heat
		else if(input.matches("BOOTH \\d \\d{2}") || input.matches("BTH\\d \\d{2}"))
		{
			//handle the BTH input
			//replace CR with CONTROL ROOM
			if(input.matches("BTH\\d \\d{2}"))
			{
				input = "BOOTH "+input.substring(3);
			}
			
			//get studio #
			int roomNumber = Character.getNumericValue(input.charAt(6));
			//get temp ##
			int tempNumber = Integer.parseInt(input.substring(8,10));
			//apply it to the correct studio
						
			System.out.println("BOOTH "+roomNumber+" TEMPERATURE SET TO "+tempNumber);
			if(roomNumber==1)
			{
				HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp(tempNumber);
			}	
			else if(roomNumber==2)
			{
				HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(tempNumber);
			}
			else if(roomNumber==3)
			{
				HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(tempNumber);
			}
			
		}
		
		//info print
		else if(input.matches("INFO"))
		{
			
			bms.printInfo(HVACMultiTest.outerAccessibleRoomList, HVACMultiTest.outerAccessibleDumpList);
		}	
		
		//system standby
		else if(input.matches("STANDBY")||input.matches("PAUSE"))
		{
			HVACMultiTest.pause=1;
			System.out.println("System in stasis");
		}
		
		//system resume
		else if(input.matches("RESUME"))
		{
			HVACMultiTest.pause=0;
			System.out.println("System resumed");
		}
		
		//let me work, stops all machines, disables itself if MRs get too hot
		else if(input.matches("ZA WARUDO"))
		{
			System.out.println("TOKI WO TOMARE");
			HVACMultiTest.pause=115;
			bms.stopHVAC();
		}
		
		//reset to default, default being 74 degrees cool
		else if(input.matches("RESET")||input.matches("DEFAULT"))
		{
			HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp(74);
			HVACMultiTest.outerAccessibleRoomList[0].setCoolHeat(0);
			HVACMultiTest.outerAccessibleRoomList[1].setCoolHeat(0);
			HVACMultiTest.outerAccessibleRoomList[2].setCoolHeat(0);
			HVACMultiTest.outerAccessibleRoomList[3].setCoolHeat(0);
			HVACMultiTest.outerAccessibleRoomList[4].setCoolHeat(0);
			HVACMultiTest.outerAccessibleRoomList[5].setCoolHeat(0);
			
			//reset the outer rooms to the heat standard
			HVACMultiTest.outerAccessibleDumpList[0].setCoolHeat(1);
			HVACMultiTest.outerAccessibleDumpList[0].setTargetTemp(72);
			HVACMultiTest.outerAccessibleDumpList[1].setCoolHeat(1);
			HVACMultiTest.outerAccessibleDumpList[1].setTargetTemp(72);
			HVACMultiTest.outerAccessibleDumpList[2].setCoolHeat(1);
			HVACMultiTest.outerAccessibleDumpList[2].setTargetTemp(72);
			HVACMultiTest.outerAccessibleDumpList[3].setCoolHeat(1);
			HVACMultiTest.outerAccessibleDumpList[3].setTargetTemp(72);
		}
		
		//blanket drop all inportant rooms by 1 degree
		else if(input.matches("BLANKET [+-]\\d"))
		{
			int blanketAmount = Character.getNumericValue(input.charAt(9));
			int blanketHC = 1;
			if(input.charAt(8)=='-')
				blanketAmount =-1*blanketAmount;
			
			System.out.println("CHANGING ALL CRs and BTHs by "+blanketAmount);
			
			HVACMultiTest.outerAccessibleRoomList[0].setTargetTemp((int) (HVACMultiTest.outerAccessibleRoomList[0].getTargetTemp()+blanketAmount));
			HVACMultiTest.outerAccessibleRoomList[1].setTargetTemp((int) (HVACMultiTest.outerAccessibleRoomList[1].getTargetTemp()+blanketAmount));
			HVACMultiTest.outerAccessibleRoomList[2].setTargetTemp((int) (HVACMultiTest.outerAccessibleRoomList[2].getTargetTemp()+blanketAmount));
			HVACMultiTest.outerAccessibleRoomList[3].setTargetTemp((int) (HVACMultiTest.outerAccessibleRoomList[3].getTargetTemp()+blanketAmount));
			HVACMultiTest.outerAccessibleRoomList[4].setTargetTemp((int) (HVACMultiTest.outerAccessibleRoomList[4].getTargetTemp()+blanketAmount));
			HVACMultiTest.outerAccessibleRoomList[5].setTargetTemp((int) (HVACMultiTest.outerAccessibleRoomList[5].getTargetTemp()+blanketAmount));

		}
		
		//set all of the outside rooms to ## input
		else if(input.matches("EDIT \\d{2}"))
		{
			
			int tempNumber = Integer.parseInt(input.substring(5));
			System.out.println("SET EDIT, HALLWAY, KITCHEN, AND PHONE BOOTH TO ="+tempNumber);
			
			HVACMultiTest.outerAccessibleDumpList[0].setTargetTemp(tempNumber);
			HVACMultiTest.outerAccessibleDumpList[1].setTargetTemp(tempNumber);
			HVACMultiTest.outerAccessibleDumpList[2].setTargetTemp(tempNumber);
			HVACMultiTest.outerAccessibleDumpList[3].setTargetTemp(tempNumber);

		}
		
		//set the temperature of the Office or EDIT to ## input
		else if(input.matches("OFFICE \\d{2}"))
		{
					
			int tempNumber = Integer.parseInt(input.substring(7));
			System.out.println("OFFICE(EDIT) TO ="+tempNumber);
					
			HVACMultiTest.outerAccessibleDumpList[0].setTargetTemp(tempNumber);

		}

		//set the temperature of the Office or EDIT to ## input
		else if(input.matches("KITCHEN \\d{2}"))
		{
					
			int tempNumber = Integer.parseInt(input.substring(8));
			System.out.println("SETTING KITHCEN TO ="+tempNumber);
					
			HVACMultiTest.outerAccessibleDumpList[1].setTargetTemp(tempNumber);

		}
		
		//set the temperature of the Office or EDIT to ## input
		else if(input.matches("PHONE \\d{2}"))
		{
					
			int tempNumber = Integer.parseInt(input.substring(6));
			System.out.println("SETTING PHONE BOOTH TO ="+tempNumber);
					
			HVACMultiTest.outerAccessibleDumpList[3].setTargetTemp(tempNumber);

		}	
		
		
		//set a specific CONTROL ROOM to Cool
		else if(input.matches("CONTROL ROOM \\d COOL"))
		{
			//get info from the command
			int roomNumber = Character.getNumericValue(input.charAt(13));
			if(roomNumber < 4 && roomNumber > 0)
			{
				System.out.println("CONTROL ROOM "+roomNumber+" SET TO COOL");
				roomNumber-=1;
			
				HVACMultiTest.outerAccessibleRoomList[roomNumber].setCoolHeat(0);
			}
			else
			System.out.println("CONTROL ROOM NUMBER NOT RECOGNIZED");	
		}
		
		//set a specific CONTORL ROOM to Heat
		else if(input.matches("CONTROL ROOM \\d HEAT"))
		{
			//get info from the command
			int roomNumber = Character.getNumericValue(input.charAt(13));
			if(roomNumber < 4 && roomNumber > 0)
			{
				System.out.println("CONTROL ROOM "+roomNumber+" SET TO HEAT");
				roomNumber-=1;

				HVACMultiTest.outerAccessibleRoomList[roomNumber].setCoolHeat(1);
			}
			else
			System.out.println("CONTROL ROOM NUMBER NOT RECOGNIZED");	
		}
		
		//set a specific BOOTH to Cool
		else if(input.matches("BOOTH \\d COOL"))
		{
			//get info from the command
			int roomNumber = Character.getNumericValue(input.charAt(6));
			if(roomNumber < 4 && roomNumber > 0)
			{
				System.out.println("BOOTH "+roomNumber+" SET TO COOL");
				roomNumber-=1;

				HVACMultiTest.outerAccessibleRoomList[roomNumber+3].setCoolHeat(0);
			}
			else
			System.out.println("BOOTH NUMBER NOT RECOGNIZED");	
		}
		
		//set a specific BOOTH to Heat
		else if(input.matches("BOOTH \\d HEAT"))
		{
			//get info from the command
			int roomNumber = Character.getNumericValue(input.charAt(6));

			if(roomNumber < 4 && roomNumber > 0)
			{
				System.out.println("BOOTH "+roomNumber+" SET TO HEAT");
				roomNumber-=1;

				HVACMultiTest.outerAccessibleRoomList[roomNumber+3].setCoolHeat(1);
			}
			else
			System.out.println("BOOTH NUMBER NOT RECOGNIZED");	
		}
		
		//set EDIT or the OUTSIDE ROOMS to COOL
		else if(input.matches("EDIT COOL"))
		{
			
				System.out.println("EDIT/OUTSIDE SET TO COOL");
			
				HVACMultiTest.outerAccessibleDumpList[0].setCoolHeat(0);
				HVACMultiTest.outerAccessibleDumpList[1].setCoolHeat(0);
				HVACMultiTest.outerAccessibleDumpList[2].setCoolHeat(0);
				HVACMultiTest.outerAccessibleDumpList[3].setCoolHeat(0);			
			
		}	
		
		//set EDIT or the OUTSIDE ROOMS to HEAT
		else if(input.matches("EDIT HEAT"))
		{
			
				System.out.println("EDIT/OUTSIDE SET TO HEAT");
			
				HVACMultiTest.outerAccessibleDumpList[0].setCoolHeat(1);
				HVACMultiTest.outerAccessibleDumpList[1].setCoolHeat(1);
				HVACMultiTest.outerAccessibleDumpList[2].setCoolHeat(1);
				HVACMultiTest.outerAccessibleDumpList[3].setCoolHeat(1);			
			
		}
		
		//quit
		else if(input.equalsIgnoreCase("QUIT"))
		{
			System.out.println("Quitting");
			System.exit(0);
		}	
		
		else
		{
			System.out.println("command not recognized");
		}
		
	}//method end
	
	
	
}
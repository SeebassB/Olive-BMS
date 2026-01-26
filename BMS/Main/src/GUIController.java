import jssc.SerialPortException;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;


public class GUIController
{
    //all
    static JToggleButton allLightsButton;
    static JToggleButton allPowerButton;

    //cr1
    JButton cr1HeatButton;
    JButton cr1CoolButton;
    JButton cr1ConditioningOffButton;

    JLabel cr1CurrentTemp;
    JLabel cr1TargetTemp;
    JLabel cr1ConditioningStatus;

    static JToggleButton cr1LightsButton;
    static JToggleButton cr1PowerButton;

    //bth1
    JButton bth1HeatButton;
    JButton bth1CoolButton;
    JButton bth1ConditioningOffButton;

    JLabel bth1CurrentTemp;
    JLabel bth1TargetTemp;
    JLabel bth1ConditioningStatus;

    //cr2
    JButton cr2HeatButton;
    JButton cr2CoolButton;
    JButton cr2ConditioningOffButton;

    JLabel cr2CurrentTemp;
    JLabel cr2TargetTemp;
    JLabel cr2ConditioningStatus;

    static JToggleButton cr2LightsButton;
    static JToggleButton cr2PowerButton;

    //bth2
    JButton bth2HeatButton;
    JButton bth2CoolButton;
    JButton bth2ConditioningOffButton;

    JLabel bth2CurrentTemp;
    JLabel bth2TargetTemp;
    JLabel bth2ConditioningStatus;

    //cr3
    JButton cr3HeatButton;
    JButton cr3CoolButton;
    JButton cr3ConditioningOffButton;

    JLabel cr3CurrentTemp;
    JLabel cr3TargetTemp;
    JLabel cr3ConditioningStatus;

    static JToggleButton cr3LightsButton;
    static JToggleButton cr3PowerButton;

    //bth3
    JButton bth3HeatButton;
    JButton bth3CoolButton;
    JButton bth3ConditioningOffButton;

    JLabel bth3CurrentTemp;
    JLabel bth3TargetTemp;
    JLabel bth3ConditioningStatus;

    //edit
    JButton editHeatButton;
    JButton editCoolButton;
    JButton editConditioningOffButton;

    JLabel editCurrentTemp;
    JLabel editTargetTemp;
    JLabel editConditioningStatus;

    //other
    JLabel kitchenCurrentTemp;
    JLabel hallwayCurrentTemp;
    JLabel phoneBoothCurrentTemp;
    JLabel mrCurrentTemp;

    //HVAC Machine Status
    JLabel HVACMachine1Status;
    JLabel HVACMachine2Status;

    static Color onColor = new Color(37, 170, 5);
    static Color offColor = new Color(47,79,143);
    static Color disabledColor = new Color(202,196,206);

    static boolean itemListenerFlag = false;

    //TODO machine status to show more clearly when blue and red/orange

    //TODO figure out fonts and stuff

    //TODO redo logging

    //TODO add a shortcut to make it easier for cleaning?

    public GUIController(BMSMethods bms){

        UIManager.put("ToggleButton.select", onColor);

        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);

        Font serif = new Font("Serif", Font.BOLD, 14);
        Font small = new Font("Serif", Font.BOLD, 12);


        //set up the frame
        JFrame frame = new JFrame("Olive Building Management System");
        frame.setIconImage(new ImageIcon("BMS//Test//IglooLogo.png").getImage());
        Color mainBackgroundColor = new Color(84, 151, 167);
        frame.getContentPane().setBackground(mainBackgroundColor);
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when you hit the X button
        frame.setLocation(350,80); //where is the window going to open
        frame.setSize(895, 860); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);



        //y levels for each room
        int allYLevel  = 10;
        int cr1YLevel  = 100;
        int bth1YLevel = 190;
        int cr2YLevel  = 280;
        int bth2YLevel = 370;
        int cr3YLevel  = 460;
        int bth3YLevel = 550;
        int editYLevel = 640;
        int otherYLevel = 730;

        //x levels for panels
        int labelXLevel = 10;
        int firstBoxXLevel = 100;
        int tempInfoBoxXLevel = 330;
        int tempControlBoxXLevel = 560;
        int powerControlBoxXLevel = 720;


        //ALL ROOMS----------------------------------------------------------------------------------------------------

        //all label
        frame.add(GUIHelperMethods.createLabel("ALL ROOMS", labelXLevel, allYLevel, 80, 60, lineBorder2, small, Color.GRAY, Color.BLACK));

        //all lights
        JPanel allLightsBox = GUIHelperMethods.createPanel(firstBoxXLevel, allYLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(allLightsBox);

            //all lights on button
            allLightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10,  60, 60, lineBorder2, small, offColor, null);
            allLightsBox.add(allLightsButton);

            //all lights off button
            allPowerButton = GUIHelperMethods.createToggleButton("POWER+LIGHTS", 80, 10, 60, 60, lineBorder2, small, offColor, null);
            allLightsBox.add(allPowerButton);

                allLightsButton.addItemListener(e ->
                {
                    //if you are doing this programmatically then do nothing
                    if(itemListenerFlag)
                        return;
                    itemListenerFlag = true;

                    //disable all light buttons while executing this
                    GUIHelperMethods.buttonDisabler(allLightsButton);
                    GUIHelperMethods.buttonDisabler(cr1LightsButton);
                    GUIHelperMethods.buttonDisabler(cr2LightsButton);
                    GUIHelperMethods.buttonDisabler(cr3LightsButton);

                    if(e.getStateChange() == ItemEvent.SELECTED)
                        new GUIHelperMethods.allLightsOnWorker().execute();
                    else if(e.getStateChange() == ItemEvent.DESELECTED)
                        new GUIHelperMethods.allLightsOffWorker().execute();

                });

                allPowerButton.addItemListener(e ->
                {
                    //if you are doing this programmatically then do nothing
                    if(itemListenerFlag)
                        return;
                    itemListenerFlag = true;

                    //disable all power buttons while executing this
                    GUIHelperMethods.buttonDisabler(allPowerButton);
                    GUIHelperMethods.buttonDisabler(cr1PowerButton);
                    GUIHelperMethods.buttonDisabler(cr2PowerButton);
                    GUIHelperMethods.buttonDisabler(cr3PowerButton);

                    //disable all lights buttons as well
                    GUIHelperMethods.buttonDisabler(allLightsButton);
                    GUIHelperMethods.buttonDisabler(cr1LightsButton);
                    GUIHelperMethods.buttonDisabler(cr2LightsButton);
                    GUIHelperMethods.buttonDisabler(cr3LightsButton);

                    //power turned on
                    if (e.getStateChange() == ItemEvent.SELECTED)
                        new GUIHelperMethods.allPowerOnWorker(bms).execute();

                    else if(e.getStateChange() == ItemEvent.DESELECTED)
                        new GUIHelperMethods.allPowerOffWorker(bms).execute();

                });


        //CR1---------------------------------------------------------------------------------------------------------



        //room name label
        JLabel cr1NameLabel = GUIHelperMethods.createLabel("<html>Control<br>Room 1</html>", labelXLevel, cr1YLevel+10, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr1NameLabel);

        //cr1 conditioning control box
        JPanel cr1ConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, cr1YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(cr1ConditioningBox);

            //heat button
            cr1HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr1ConditioningBox.add(cr1HeatButton);

            //cool button
            cr1CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr1ConditioningBox.add(cr1CoolButton);

            //off button
            cr1ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr1ConditioningBox.add(cr1ConditioningOffButton);


                //cr1 heat button action listener
                cr1HeatButton.addActionListener(_ -> {
                    bms.findRoom("CR 1").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 1", cr1HeatButton, cr1CoolButton, cr1ConditioningOffButton);
                    System.out.println("Current st1 conditioning set to heat");
                });

                //cool button action listener
                cr1CoolButton.addActionListener(_ -> {
                    bms.findRoom("CR 1").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 1", cr1HeatButton, cr1CoolButton, cr1ConditioningOffButton);
                    System.out.println("Current st1 conditioning set to cool ");
                });

                //off button action listener
                cr1ConditioningOffButton.addActionListener(_ -> {
                    bms.findRoom("CR 1").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 1", cr1HeatButton, cr1CoolButton, cr1ConditioningOffButton);
                    System.out.println("Current st1 conditioning set to none");
                });


        // cr1 information box
        JPanel cr1TemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, cr1YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(cr1TemperatureInfoBox);

            //current temp display
            cr1CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 1").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1CurrentTemp);

            //current temp text
            JLabel cr1CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1CurrentLabel);

            //cr1 target temp display
            cr1TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 1").getTargetTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1TargetTemp);

            //current temp text
            JLabel cr1TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1TargetTempLabel);

            //cr1 current conditioning display
            cr1ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("CR 1").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1ConditioningStatus);

            //cr1 current conditioning label
            JLabel cr1ConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1ConditioningStatusLabel);

        //CR1 temp box-------------------------------------------------------------------------------------------
        JPanel cr1TempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, cr1YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        cr1TempBox.setLayout(null);
        frame.add(cr1TempBox);

            //temp up
            JButton cr1TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            cr1TempBox.add(cr1TempUpButton);

            //temp down
            JButton cr1TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, Color.BLUE, Color.YELLOW);
            cr1TempBox.add(cr1TempDownButton);

                //cr1 temp down button action listener
                cr1TempDownButton.addActionListener(_ -> {
                    double tempTemp = bms.findRoom("CR 1").getTargetTemp()-1;
                    bms.findRoom("CR 1").setTargetTemp(tempTemp);
                    cr1TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current st1 target temperature: " + bms.findRoom("CR 1").getTargetTemp());
                });

                //cr1 temp up button action listener
                cr1TempUpButton.addActionListener(_ -> {
                    double tempTemp = bms.findRoom("CR 1").getTargetTemp()+1;
                    bms.findRoom("CR 1").setTargetTemp(tempTemp);
                    cr1TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current st1 target temperature: " + bms.findRoom("CR 1").getTargetTemp());
                });

        //cr1 power box
        JPanel cr1PowerBox = GUIHelperMethods.createPanel(powerControlBoxXLevel, cr1YLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(cr1PowerBox);


            //cr 1lights on button
            cr1LightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10, 60, 60, lineBorder2, serif, offColor, Color.BLACK);
            cr1PowerBox.add(cr1LightsButton);

            //lights off button
            cr1PowerButton = GUIHelperMethods.createToggleButton("POWER", 80, 10, 60, 60, lineBorder2, serif, offColor, Color.BLACK);
            cr1PowerBox.add(cr1PowerButton);

            //st1 lights off button action listener
            cr1LightsButton.addItemListener(e ->
            {
                //if you are doing this programmatically then do nothing
                if(itemListenerFlag)
                    return;
                itemListenerFlag = true;


                GUIHelperMethods.buttonDisabler(cr1LightsButton);

                if (e.getStateChange() == ItemEvent.SELECTED)
                    new GUIHelperMethods.singleRoomLightsWorker(1, true, cr1LightsButton).execute();
                else if(e.getStateChange() == ItemEvent.DESELECTED)
                    new GUIHelperMethods.singleRoomLightsWorker(1, false, cr1LightsButton).execute();

            });


            //st1 power button action listener
            cr1PowerButton.addItemListener(e ->
            {
                //if you are doing this programmatically then do nothing
                if(itemListenerFlag)
                    return;
                itemListenerFlag = true;


                GUIHelperMethods.buttonDisabler(cr1PowerButton);
                GUIHelperMethods.buttonDisabler(cr1LightsButton);

                if (e.getStateChange() == ItemEvent.SELECTED)
                    new GUIHelperMethods.singleRoomPowerWorker(bms, 1, true, cr1LightsButton, cr1PowerButton).execute();

                else if(e.getStateChange() == ItemEvent.DESELECTED)
                    new GUIHelperMethods.singleRoomPowerWorker(bms, 1, false, cr1LightsButton, cr1PowerButton).execute();
            });



        //booth  1--------------------------------------------------------------------------------------------------



        //room name label
        JLabel bth1NameLabel = GUIHelperMethods.createLabel("Booth 1", labelXLevel, bth1YLevel+30, 80, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth1NameLabel);

        //bth1 conditioning box
        JPanel bth1ConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, bth1YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(bth1ConditioningBox);

            //heat button
            bth1HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth1HeatButton.setFocusPainted(false);
            bth1ConditioningBox.add(bth1HeatButton);

            //cool button
            bth1CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth1CoolButton.setFocusPainted(false);
            bth1ConditioningBox.add(bth1CoolButton);

            //off button
            bth1ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth1ConditioningOffButton.setFocusPainted(false);
            bth1ConditioningBox.add(bth1ConditioningOffButton);

                //cr1 heat button action listener
                bth1HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 1").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 1", bth1HeatButton, bth1CoolButton, bth1ConditioningOffButton);
                    System.out.println("Current bth1 conditioning set to heat");
                });

                //cool button action listener
                bth1CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 1").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 1", bth1HeatButton, bth1CoolButton, bth1ConditioningOffButton);
                    System.out.println("Current bth1 conditioning set to cool ");
                });

                //off button action listener
                bth1ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 1").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 1", bth1HeatButton, bth1CoolButton, bth1ConditioningOffButton);
                    System.out.println("Current bth1 conditioning set to none");
                });


        //booth 1 temp status box
        JPanel bth1TemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, bth1YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(bth1TemperatureInfoBox);

            //bth1 current temp display
            bth1CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Booth 1").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1CurrentTemp);

            //bth1 current temp text
            JLabel bth1CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color. BLACK);
            bth1TemperatureInfoBox.add(bth1CurrentLabel);

            //bth1 target temp display
            bth1TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Booth 1").getCurrentTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1TargetTemp);

            //bth1 current temp text
            JLabel bth1TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1TargetTempLabel);

            //bth1 current conditioning display
            bth1ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("Booth 1").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1ConditioningStatus);

            //bth1 current conditioning label
            JLabel bth1ConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1ConditioningStatusLabel);


        //CR1 temp box-------------------------------------------------------------------------------------------
        JPanel bth1TempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, bth1YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        bth1TempBox.setLayout(null);
        frame.add(bth1TempBox);

            //temp up
            JButton bth1TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            bth1TempBox.add(bth1TempUpButton);

            //temp down
            JButton bth1TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            bth1TempBox.add(bth1TempDownButton);

                //cr1 temp down button action listener
                bth1TempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Booth 1").getTargetTemp()-1;
                    bms.findRoom("Booth 1").setTargetTemp(tempTemp);
                    bth1TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth1 target temperature: " + bms.findRoom("Booth 1").getTargetTemp());
                });

                //cr1 temp up button action listener
                bth1TempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Booth 1").getTargetTemp()+1;
                    bms.findRoom("Booth 1").setTargetTemp(tempTemp);
                    bth1TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth1 target temperature: " + bms.findRoom("Booth 1").getTargetTemp());
                });



        //CR2-------------------------------------------------------------------------------------



        //room name label
        JLabel cr2NameLabel = GUIHelperMethods.createLabel("<html>Control<br>Room 2</html>", labelXLevel, cr2YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr2NameLabel);

        //cr2 conditioning box
        JPanel cr2ConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, cr2YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(cr2ConditioningBox);

            //heat button
            cr2HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr2ConditioningBox.add(cr2HeatButton);

            //cool button
            cr2CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr2ConditioningBox.add(cr2CoolButton);

            //off button
            cr2ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr2ConditioningBox.add(cr2ConditioningOffButton);


                //cr2 heat button action listener
                cr2HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 2").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 2", cr2HeatButton, cr2CoolButton, cr2ConditioningOffButton);
                    System.out.println("Current st2 conditioning set to heat");
                });

                //cr2 cool button action listener
                cr2CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 2").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 2", cr2HeatButton, cr2CoolButton, cr2ConditioningOffButton);
                    System.out.println("Current st2 conditioning set to cool ");
                });

                //cr2 off button action listener
                cr2ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 2").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 2", cr2HeatButton, cr2CoolButton, cr2ConditioningOffButton);
                    System.out.println("Current st2 conditioning set to none");
                });



        // cr2 information box
        JPanel cr2TemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, cr2YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(cr2TemperatureInfoBox);

            //current temp display
            cr2CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 2").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2CurrentTemp);

            //current temp text
            JLabel cr2CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2CurrentLabel);

            //cr2 target temp display
            cr2TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 2").getTargetTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2TargetTemp);

            //current temp text
            JLabel cr2TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2TargetTempLabel);

            //cr2 current conditioning display
            cr2ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("CR 2").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2ConditioningStatus);

            //cr2 current conditioning label
            JLabel cr2ConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2ConditioningStatusLabel);

        //CR2 temp box-------------------------------------------------------------------------------------------
        JPanel cr2TempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, cr2YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        frame.add(cr2TempBox);

            //temp up
            JButton cr2TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            cr2TempBox.add(cr2TempUpButton);

            //temp down
            JButton cr2TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            cr2TempBox.add(cr2TempDownButton);

                //cr2 temp down button action listener
                cr2TempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("CR 2").getTargetTemp()-1;
                    bms.findRoom("CR 2").setTargetTemp(tempTemp);
                    cr2TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current cr2 target temperature: " + bms.findRoom("CR 2").getTargetTemp());
                });

                //cr2 temp up button action listener
                cr2TempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("CR 2").getTargetTemp()+1;
                    bms.findRoom("CR 2").setTargetTemp(tempTemp);
                    cr2TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current cr2 target temperature: " + bms.findRoom("CR 2").getTargetTemp());
                });

        //cr2 power box
        JPanel cr2PowerBox = GUIHelperMethods.createPanel(powerControlBoxXLevel, cr2YLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(cr2PowerBox);


            //cr 2 lights button
            cr2LightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10, 60, 60, lineBorder2, serif, offColor, Color.BLACK);
            cr2PowerBox.add(cr2LightsButton);

            //lights off button
            cr2PowerButton = GUIHelperMethods.createToggleButton("POWER", 80, 10, 60, 60, lineBorder2, serif, offColor, Color.BLACK);
            cr2PowerBox.add(cr2PowerButton);




                //st2 power button action listener
                cr2LightsButton.addItemListener(e ->
                {
                    //if you are doing this programmatically then do nothing
                    if(itemListenerFlag)
                        return;
                    itemListenerFlag = true;

                    GUIHelperMethods.buttonDisabler(cr2LightsButton);

                    if (e.getStateChange() == ItemEvent.SELECTED)
                        new GUIHelperMethods.singleRoomLightsWorker(2, true, cr2LightsButton).execute();
                    else if(e.getStateChange() == ItemEvent.DESELECTED)
                        new GUIHelperMethods.singleRoomLightsWorker(2, false, cr2LightsButton).execute();

                });


        //st2 power on button action listener
        cr2PowerButton.addItemListener(e ->
        {
            //if you are doing this programmatically then do nothing
            if(itemListenerFlag)
                return;
            itemListenerFlag = true;


            GUIHelperMethods.buttonDisabler(cr2PowerButton);
            GUIHelperMethods.buttonDisabler(cr2LightsButton);

            if (e.getStateChange() == ItemEvent.SELECTED)
                new GUIHelperMethods.singleRoomPowerWorker(bms, 2, true, cr2LightsButton, cr2PowerButton).execute();
            else if(e.getStateChange() == ItemEvent.DESELECTED)
                new GUIHelperMethods.singleRoomPowerWorker(bms, 2, false, cr2LightsButton, cr2PowerButton).execute();

        });


        //BTH2-------------------------------------------------------------------------------------


        //room name label
        JLabel bth2NameLabel = GUIHelperMethods.createLabel("<html>Booth 2</html>", labelXLevel, bth2YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth2NameLabel);

        //cr2 conditioning box
        JPanel bth2ConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, bth2YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(bth2ConditioningBox);

            //heat button
            bth2HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth2ConditioningBox.add(bth2HeatButton);

            //cool button
            bth2CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth2ConditioningBox.add(bth2CoolButton);

            //off button
            bth2ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth2ConditioningBox.add(bth2ConditioningOffButton);


                //bth2 heat button action listener
                bth2HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 2").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 2", bth2HeatButton, bth2CoolButton, bth2ConditioningOffButton);
                    System.out.println("Current bth2 conditioning set to heat");
                });

                //cool button action listener
                bth2CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 2").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 2", bth2HeatButton, bth2CoolButton, bth2ConditioningOffButton);
                    System.out.println("Current bth2 conditioning set to cool ");
                });

                //off button action listener
                bth2ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 2").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 2", bth2HeatButton, bth2CoolButton, bth2ConditioningOffButton);
                    System.out.println("Current bth2 conditioning set to none");
                });

        //temp info box
        JPanel bth2TemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, bth2YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(bth2TemperatureInfoBox);

            //bth2 current temp display
            bth2CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Booth 2").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth2TemperatureInfoBox.add(bth2CurrentTemp);

            //bth2 current temp text
            JLabel bth2CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color. BLACK);
            bth2TemperatureInfoBox.add(bth2CurrentLabel);

            //bth2 target temp display
            bth2TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Booth 2").getCurrentTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth2TemperatureInfoBox.add(bth2TargetTemp);

            //bth2 current temp text
            JLabel bth2TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth2TemperatureInfoBox.add(bth2TargetTempLabel);

            //bth2 current conditioning display
            bth2ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("Booth 2").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth2TemperatureInfoBox.add(bth2ConditioningStatus);

            //bth2 current conditioning label
            JLabel bth2ConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth2TemperatureInfoBox.add(bth2ConditioningStatusLabel);

        //BTH2 temp box
        JPanel bth2TempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, bth2YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        frame.add(bth2TempBox);

            //temp up
            JButton bth2TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            bth2TempBox.add(bth2TempUpButton);

            //temp down
            JButton bth2TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            bth2TempBox.add(bth2TempDownButton);

                //bth2 temp down button action listener
                bth2TempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Booth 2").getTargetTemp()-1;
                    bms.findRoom("Booth 2").setTargetTemp(tempTemp);
                    bth2TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth2 target temperature: " + bms.findRoom("Booth 2").getTargetTemp());
                });

                //bth2 temp up button action listener
                bth2TempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Booth 2").getTargetTemp()+1;
                    bms.findRoom("Booth 2").setTargetTemp(tempTemp);
                    bth2TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth2 target temperature: " + bms.findRoom("Booth 2").getTargetTemp());
                });


//CR3-------------------------------------------------------------------------------------



        //room name label
        JLabel cr3NameLabel = GUIHelperMethods.createLabel("<html>Control<br>Room 3</html>", labelXLevel, cr3YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr3NameLabel);

        //cr3 conditioning box
        JPanel cr3ConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, cr3YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(cr3ConditioningBox);

            //heat button
            cr3HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr3ConditioningBox.add(cr3HeatButton);

            //cool button
            cr3CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr3ConditioningBox.add(cr3CoolButton);

            //off button
            cr3ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr3ConditioningBox.add(cr3ConditioningOffButton);

                //cr3 heat button action listener
                cr3HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 3").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 3", cr3HeatButton, cr3CoolButton, cr3ConditioningOffButton);
                    System.out.println("Current cr3 conditioning set to heat");
                });

                //cool button action listener
                cr3CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 3").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 3", cr3HeatButton, cr3CoolButton, cr3ConditioningOffButton);
                    System.out.println("Current cr3 conditioning set to cool ");
                });

                //off button action listener
                cr3ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 3").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 3", cr3HeatButton, cr3CoolButton, cr3ConditioningOffButton);
                    System.out.println("Current cr3 conditioning set to none");
                });



        // cr3 information box
        JPanel cr3TemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, cr3YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(cr3TemperatureInfoBox);

            //current temp display
            cr3CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 3").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr3TemperatureInfoBox.add(cr3CurrentTemp);

            //current temp text
            JLabel cr3CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr3TemperatureInfoBox.add(cr3CurrentLabel);

            //cr3 target temp display
            cr3TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 3").getTargetTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr3TemperatureInfoBox.add(cr3TargetTemp);

            //current temp text
            JLabel cr3TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr3TemperatureInfoBox.add(cr3TargetTempLabel);

            //cr3 current conditioning display
            cr3ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("CR 3").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr3TemperatureInfoBox.add(cr3ConditioningStatus);

            //cr3 current conditioning label
            JLabel cr3ConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr3TemperatureInfoBox.add(cr3ConditioningStatusLabel);

        //CR3 temp box
        JPanel cr3TempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, cr3YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        frame.add(cr3TempBox);

            //temp up
            JButton cr3TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            cr3TempBox.add(cr3TempUpButton);

            //temp down
            JButton cr3TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            cr3TempBox.add(cr3TempDownButton);

                //cr3 temp down button action listener
                cr3TempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("CR 3").getTargetTemp()-1;
                    bms.findRoom("CR 3").setTargetTemp(tempTemp);
                    cr3TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current cr3 target temperature: " + bms.findRoom("CR 3").getTargetTemp());
                });

                //cr2 temp up button action listener
                cr3TempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("CR 3").getTargetTemp()+1;
                    bms.findRoom("CR 3").setTargetTemp(tempTemp);
                    cr3TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current st3 target temperature: " + bms.findRoom("CR 3").getTargetTemp());
                });

        //cr3 power box
        JPanel cr3PowerBox = GUIHelperMethods.createPanel(powerControlBoxXLevel, cr3YLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(cr3PowerBox);


            //cr 3 lights button
            cr3LightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10, 60, 60, lineBorder2, serif, offColor, Color.BLACK);
            cr3PowerBox.add(cr3LightsButton);

            //lights off button
            cr3PowerButton = GUIHelperMethods.createToggleButton("POWER", 80, 10, 60, 60, lineBorder2, serif, offColor, Color.BLACK);
            cr3PowerBox.add(cr3PowerButton);



                //st3 lights button action listener
                cr3LightsButton.addItemListener(e ->
                {
                    //if you are doing this programmatically then do nothing
                    if(itemListenerFlag)
                        return;
                    itemListenerFlag = true;


                    GUIHelperMethods.buttonDisabler(cr3LightsButton);

                    if(e.getStateChange() == ItemEvent.SELECTED)
                        new GUIHelperMethods.singleRoomLightsWorker(3, true, cr3LightsButton).execute();
                    else if(e.getStateChange() == ItemEvent.DESELECTED)
                        new GUIHelperMethods.singleRoomLightsWorker(3, false, cr3LightsButton).execute();

                });

                    //st3 lights on button action listener
                    cr3PowerButton.addItemListener(e ->
                    {
                        //if you are doing this programmatically then do nothing
                        if(itemListenerFlag)
                            return;
                        itemListenerFlag = true;


                        GUIHelperMethods.buttonDisabler(cr3LightsButton);
                        GUIHelperMethods.buttonDisabler(cr3PowerButton);

                        if (e.getStateChange() == ItemEvent.SELECTED)
                            new GUIHelperMethods.singleRoomPowerWorker(bms, 3, true, cr3LightsButton, cr3PowerButton).execute();
                        else if(e.getStateChange() == ItemEvent.DESELECTED)
                            new GUIHelperMethods.singleRoomPowerWorker(bms, 3, false, cr3LightsButton, cr3PowerButton).execute();

                    });





        //BTH3-------------------------------------------------------------------------------------


        //room name label
        JLabel bth3NameLabel = GUIHelperMethods.createLabel("<html>Booth 3</html>", labelXLevel, bth3YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth3NameLabel);

        //cr2 conditioning box
        JPanel bth3ConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, bth3YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(bth3ConditioningBox);

            //heat button
            bth3HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth3ConditioningBox.add(bth3HeatButton);

            //cool button
            bth3CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth3ConditioningBox.add(bth3CoolButton);

            //off button
            bth3ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth3ConditioningBox.add(bth3ConditioningOffButton);


                //cr1 heat button action listener
                bth3HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 3").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 3", bth3HeatButton, bth3CoolButton, bth3ConditioningOffButton);
                    System.out.println("Current bth3 conditioning set to heat");
                });

                //cool button action listener
                bth3CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 3").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 3", bth3HeatButton, bth3CoolButton, bth3ConditioningOffButton);
                    System.out.println("Current bth3 conditioning set to cool ");
                });

                //off button action listener
                bth3ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 3").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 3", bth3HeatButton, bth3CoolButton, bth3ConditioningOffButton);
                    System.out.println("Current bth3 conditioning set to none");
                });


        //temp info box
        JPanel bth3TemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, bth3YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(bth3TemperatureInfoBox);

            //bth3 current temp display
            bth3CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Booth 3").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth3TemperatureInfoBox.add(bth3CurrentTemp);

            //bth2 current temp text
            JLabel bth3CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color. BLACK);
            bth3TemperatureInfoBox.add(bth3CurrentLabel);

            //bth3 target temp display
            bth3TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Booth 3").getCurrentTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth3TemperatureInfoBox.add(bth3TargetTemp);

            //bth3 current temp text
            JLabel bth3TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth3TemperatureInfoBox.add(bth3TargetTempLabel);

            //bth3 current conditioning display
            bth3ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("Booth 3").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth3TemperatureInfoBox.add(bth3ConditioningStatus);

            //bth3 current conditioning label
            JLabel bth3ConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth3TemperatureInfoBox.add(bth3ConditioningStatusLabel);

        //BTH2 temp box
        JPanel bth3TempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, bth3YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        frame.add(bth3TempBox);

            //temp up
            JButton bth3TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            bth3TempBox.add(bth3TempUpButton);

            //temp down
            JButton bth3TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            bth3TempBox.add(bth3TempDownButton);

                //bth3 temp down button action listener
                bth3TempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Booth 3").getTargetTemp()-1;
                    bms.findRoom("Booth 3").setTargetTemp(tempTemp);
                    bth3TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth3 target temperature: " + bms.findRoom("Booth 3").getTargetTemp());
                });

                //bth3 temp up button action listener
                bth3TempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Booth 3").getTargetTemp()+1;
                    bms.findRoom("Booth 3").setTargetTemp(tempTemp);
                    bth3TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth3 target temperature: " + bms.findRoom("Booth 3").getTargetTemp());
                });




        //EDIT-------------------------------------------------------------------------------------



        //room name label
        JLabel editNameLabel = GUIHelperMethods.createLabel("<html>EDIT</html>", labelXLevel, editYLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(editNameLabel);

        //edit conditioning box
        JPanel editConditioningBox = GUIHelperMethods.createPanel(firstBoxXLevel, editYLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(editConditioningBox);

            //heat button
            editHeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            editConditioningBox.add(editHeatButton);

            //cool button
            editCoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            editConditioningBox.add(editCoolButton);

            //off button
            editConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            editConditioningBox.add(editConditioningOffButton);


                //edit heat button action listener
                editHeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Edit").setCoolHeat('h');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Edit", editHeatButton, editCoolButton, editConditioningOffButton);
                    System.out.println("Current edit conditioning set to heat");
                });

                //cool button action listener
                editCoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Edit").setCoolHeat('c');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Edit", editHeatButton, editCoolButton, editConditioningOffButton);
                    System.out.println("Current edit conditioning set to cool ");
                });

                //off button action listener
                editConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Edit").setCoolHeat('n');
                    GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Edit", editHeatButton, editCoolButton, editConditioningOffButton);
                    System.out.println("Current edit conditioning set to none");
                });



        //temp info box
        JPanel editTemperatureInfoBox = GUIHelperMethods.createPanel(tempInfoBoxXLevel, editYLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(editTemperatureInfoBox);

            //edit current temp display
            editCurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Edit").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            editTemperatureInfoBox.add(editCurrentTemp);

            //edit current temp text
            JLabel editCurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color. BLACK);
            editTemperatureInfoBox.add(editCurrentLabel);

            //bth2 target temp display
            editTargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("Edit").getCurrentTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            editTemperatureInfoBox.add(editTargetTemp);

            //edit current temp text
            JLabel editTargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            editTemperatureInfoBox.add(editTargetTempLabel);

            //edit current conditioning display
            editConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("Booth 2").getCoolHeat()), 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            editTemperatureInfoBox.add(editConditioningStatus);

            //edit current conditoning label
            JLabel editConditioningStatusLabel = GUIHelperMethods.createLabel("Status", 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            editTemperatureInfoBox.add(editConditioningStatusLabel);

        //edit temp box
        JPanel editTempBox = GUIHelperMethods.createPanel(tempControlBoxXLevel, editYLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        frame.add(editTempBox);

            //temp up
            JButton editTempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, new Color(224, 51, 31), Color.YELLOW);
            editTempBox.add(editTempUpButton);

            //temp down
            JButton editTempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            editTempBox.add(editTempDownButton);

                //edit temp down button action listener
                editTempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Edit").getTargetTemp()-1;
                    bms.findRoom("Edit").setTargetTemp(tempTemp);
                    editTargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current edit target temperature: " + bms.findRoom("Edit").getTargetTemp());
                });

                //edit temp up button action listener
                editTempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("Edit").getTargetTemp()+1;
                    bms.findRoom("Edit").setTargetTemp(tempTemp);
                    editTargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current edit target temperature: " + bms.findRoom("Edit").getTargetTemp());
                });









        //OTHER ROOMS-------------------------------------------------------------------------------------


        //room name label
        JLabel otherNameLabel = GUIHelperMethods.createLabel("<html>OTHER</html>", 10, otherYLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(otherNameLabel);

        //other conditioning box
        JPanel otherConditioningBox = GUIHelperMethods.createPanel(100, otherYLevel, 290, 80, lineBorder3, new Color(100, 101, 66));
        frame.add(otherConditioningBox);

            //kitchen
            JLabel kitchenLabel = GUIHelperMethods.createLabel("Kitchen", 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(kitchenLabel);

            kitchenCurrentTemp = GUIHelperMethods.createLabel(Double.toString(bms.findRoom("Kitchen").getCurrentTemp()), 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(kitchenCurrentTemp);

            //hallway
            JLabel hallwayLabel = GUIHelperMethods.createLabel("Hallway", 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(hallwayLabel);

            hallwayCurrentTemp = GUIHelperMethods.createLabel(Double.toString(bms.findRoom("Hallway").getCurrentTemp()), 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(hallwayCurrentTemp);

            //phone booth
            JLabel phoneBoothLabel = GUIHelperMethods.createLabel("Phone Booth", 150, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(phoneBoothLabel);

            phoneBoothCurrentTemp = GUIHelperMethods.createLabel(Double.toString(bms.findRoom("Phone Booth").getCurrentTemp()), 150, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(phoneBoothCurrentTemp);

            //machine room
            JLabel mrConditioningLabel = GUIHelperMethods.createLabel("Machine Room", 220, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(mrConditioningLabel);

            mrCurrentTemp = GUIHelperMethods.createLabel(Double.toString(bms.findRoom("Machine Room 1").getCurrentTemp()), 220, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            otherConditioningBox.add(mrCurrentTemp);

        //hvac info
        JPanel HVACStatusBox = GUIHelperMethods.createPanel(400, otherYLevel, 150, 80, lineBorder3, new Color(100, 150, 66));
        frame.add(HVACStatusBox);

            JLabel machineOne = GUIHelperMethods.createLabel("HVAC 1", 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            HVACStatusBox.add(machineOne);

            HVACMachine1Status = GUIHelperMethods.createLabel(ConditioningMethods.getCurrentConditioningState(), 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            HVACStatusBox.add(HVACMachine1Status);

            JLabel machineTwo = GUIHelperMethods.createLabel("HVAC 2", 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            HVACStatusBox.add(machineTwo);

            HVACMachine2Status = GUIHelperMethods.createLabel(ConditioningMethods.getCurrentConditioningState(), 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            HVACStatusBox.add(HVACMachine2Status);

        //debug button
        JPanel debugBox = GUIHelperMethods.createPanel(790, otherYLevel, 80, 80, lineBorder3, Color.RED);
        frame.add(debugBox);

            JButton debugOpenButton = GUIHelperMethods.createButton("MAINT", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            debugBox.add(debugOpenButton);

                //debug screen open button
                debugOpenButton.addActionListener(_ ->
                {
                   System.out.println("debug start");
                   BMSMainController.mainStatusFlag = "maintenance";
                   new DebugGUI();
                });






        //final GUI setup
        //update(bms); //initial update



        frame.setVisible(true);

    }


    public void update(BMSMethods bms) throws SerialPortException, InterruptedException {



        //do the lights and stuff


        //CR1 update cool/heat buttons
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 1", cr1HeatButton, cr1CoolButton, cr1ConditioningOffButton);

        //CR1 update temp info box
        cr1CurrentTemp.setText(Double.toString(bms.findRoom("CR 1").getCurrentTemp()));
        cr1TargetTemp.setText(Double.toString(bms.findRoom("CR 1").getTargetTemp()));
        cr1ConditioningStatus.setText(Character.toString(bms.findRoom("CR 1").getRequestState()));

        //set the color of the temp status
        if(cr1ConditioningStatus.getText().equals("h"))
            cr1ConditioningStatus.setBackground(Color.ORANGE);
        else if(cr1ConditioningStatus.getText().equals("c"))
            cr1ConditioningStatus.setBackground(Color.BLUE);
        else
            cr1ConditioningStatus.setBackground(Color.GRAY);


        //bth 1
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 1", bth1HeatButton, bth1CoolButton, bth1ConditioningOffButton);

        bth1CurrentTemp.setText(Double.toString(bms.findRoom("Booth 1").getCurrentTemp()));
        bth1TargetTemp.setText(Double.toString(bms.findRoom("Booth 1").getTargetTemp()));
        bth1ConditioningStatus.setText(Character.toString(bms.findRoom("Booth 1").getRequestState()));

        //set the color of the temp status
        if(bth1ConditioningStatus.getText().equals("h"))
            bth1ConditioningStatus.setBackground(Color.ORANGE);
        else if(bth1ConditioningStatus.getText().equals("c"))
            bth1ConditioningStatus.setBackground(Color.BLUE);
        else
            bth1ConditioningStatus.setBackground(Color.GRAY);


        //CR 2
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 2", cr2HeatButton, cr2CoolButton, cr2ConditioningOffButton);

        cr2CurrentTemp.setText(Double.toString(bms.findRoom("CR 2").getCurrentTemp()));
        cr2TargetTemp.setText(Double.toString(bms.findRoom("CR 2").getTargetTemp()));
        cr2ConditioningStatus.setText(Character.toString(bms.findRoom("CR 2").getRequestState()));

        if(cr2ConditioningStatus.getText().equals("h"))
            cr2ConditioningStatus.setBackground(Color.ORANGE);
        else if(cr2ConditioningStatus.getText().equals("c"))
            cr2ConditioningStatus.setBackground(Color.BLUE);
        else
            cr2ConditioningStatus.setBackground(Color.GRAY);


        //bth2
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 2", bth2HeatButton, bth2CoolButton, bth2ConditioningOffButton);

        bth2CurrentTemp.setText(Double.toString(bms.findRoom("Booth 2").getCurrentTemp()));
        bth2TargetTemp.setText(Double.toString(bms.findRoom("Booth 2").getTargetTemp()));
        bth2ConditioningStatus.setText(Character.toString(bms.findRoom("Booth 2").getRequestState()));

        if(bth2ConditioningStatus.getText().equals("h"))
            bth2ConditioningStatus.setBackground(Color.ORANGE);
        else if(bth2ConditioningStatus.getText().equals("c"))
            bth2ConditioningStatus.setBackground(Color.BLUE);
        else
            bth2ConditioningStatus.setBackground(Color.GRAY);


        //CR3
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "CR 3", cr3HeatButton, cr3CoolButton, cr3ConditioningOffButton);

        cr3CurrentTemp.setText(Double.toString(bms.findRoom("CR 3").getCurrentTemp()));
        cr3TargetTemp.setText(Double.toString(bms.findRoom("CR 3").getTargetTemp()));
        cr3ConditioningStatus.setText(Character.toString(bms.findRoom("CR 3").getRequestState()));

        if(cr3ConditioningStatus.getText().equals("h"))
            cr3ConditioningStatus.setBackground(Color.ORANGE);
        else if(cr3ConditioningStatus.getText().equals("c"))
            cr3ConditioningStatus.setBackground(Color.BLUE);
        else
            cr3ConditioningStatus.setBackground(Color.GRAY);


        //bth3
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Booth 3", bth3HeatButton, bth3CoolButton, bth3ConditioningOffButton);

        bth3CurrentTemp.setText(Double.toString(bms.findRoom("Booth 3").getCurrentTemp()));
        bth3TargetTemp.setText(Double.toString(bms.findRoom("Booth 3").getTargetTemp()));
        bth3ConditioningStatus.setText(Character.toString(bms.findRoom("Booth 3").getRequestState()));

        if(bth3ConditioningStatus.getText().equals("h"))
            bth3ConditioningStatus.setBackground(Color.ORANGE);
        else if(bth3ConditioningStatus.getText().equals("c"))
            bth3ConditioningStatus.setBackground(Color.BLUE);
        else
            bth3ConditioningStatus.setBackground(Color.GRAY);

        //edit
        GUIHelperMethods.updateRoomCoolHeatButtons(bms, "Edit", editHeatButton, editCoolButton, editConditioningOffButton);

        editCurrentTemp.setText(Double.toString(bms.findRoom("Edit").getCurrentTemp()));
        editTargetTemp.setText(Double.toString(bms.findRoom("Edit").getTargetTemp()));
        editConditioningStatus.setText(Character.toString(bms.findRoom("Edit").getRequestState()));

        if(editConditioningStatus.getText().equals("h"))
            editConditioningStatus.setBackground(Color.ORANGE);
        else if(editConditioningStatus.getText().equals("c"))
            editConditioningStatus.setBackground(Color.BLUE);
        else
            editConditioningStatus.setBackground(Color.GRAY);


        //others

        kitchenCurrentTemp.setText(Double.toString(bms.findRoom("Kitchen").getCurrentTemp()));
        hallwayCurrentTemp.setText(Double.toString(bms.findRoom("Hallway").getCurrentTemp()));
        phoneBoothCurrentTemp.setText(Double.toString(bms.findRoom("Phone Booth").getCurrentTemp()));
        mrCurrentTemp.setText(Double.toString(bms.findRoom("Machine Room 1").getCurrentTemp()));


        //HVAC machine state
        if(BMSMethods.relayRead(50).equals("on"))
            HVACMachine1Status.setText("Cool");
        else if(BMSMethods.relayRead(51).equals("on"))
            HVACMachine1Status.setText("Heat");
        else
        {
            HVACMachine1Status.setText("None");
        }

        if(BMSMethods.relayRead(53).equals("on"))
            HVACMachine2Status.setText("Cool");
        else if(BMSMethods.relayRead(54).equals("on"))
            HVACMachine2Status.setText("Heat");
        else
        {
            HVACMachine2Status.setText("None");
        }



    }






}



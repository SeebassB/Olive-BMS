import jssc.SerialPortException;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;


public class V2UITesting
{
    //all
    JToggleButton allLightsButton;
    JToggleButton allPowerButton;

    //cr1
    JButton cr1HeatButton;
    JButton cr1CoolButton;
    JButton cr1ConditioningOffButton;

    JLabel cr1CurrentTemp;
    JLabel cr1TargetTemp;
    JLabel cr1ConditioningStatus;

    JToggleButton cr1LightsButton;
    JToggleButton cr1PowerButton;

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

    JToggleButton cr2LightsButton;
    JToggleButton cr2PowerButton;

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

    JToggleButton cr3LightsButton;
    JToggleButton cr3PowerButton;

    //bth3
    JButton bth3HeatButton;
    JButton bth3CoolButton;
    JButton bth3ConditioningOffButton;

    JLabel bth3CurrentTemp;
    JLabel bth3TargetTemp;
    JLabel bth3ConditioningStatus;

    //edit
    JButton cr4HeatButton;
    JButton cr4CoolButton;
    JButton cr4ConditioningOffButton;



    //other

    public V2UITesting(BMSMethods bms)
    {

        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);

        Font serif = new Font("Serif", Font.BOLD, 14);

        Timer timer = new Timer(3000, null);

        //set up the frame
        JFrame frame = new JFrame("Olive Building Management System");
        frame.setIconImage(new ImageIcon("BMS//Test//IglooLogo.png").getImage());
        Color mainBackgroundColor = new Color(84, 151, 167);
        frame.getContentPane().setBackground(mainBackgroundColor);
        frame.getContentPane().setLayout(null);




        ImageIcon onBulbOriginal = new ImageIcon("BMS//Test//OnBulb.png");
        ImageIcon onBulb = new ImageIcon(onBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        ImageIcon offBulbOriginal = new ImageIcon("BMS//Test//OffBulb.png");
        ImageIcon offBulb = new ImageIcon(offBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        //frame attributes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when you hit the X button
        frame.setLocation(350,80); //where is the window going to open
        frame.setSize(895, 860); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);

        //mouse point for me
        JLabel positionLabel = new JLabel("Move the mouse...");
        positionLabel.setBounds(700, 10, 200, 20);  // position + size
        frame.add(positionLabel);
        // Add mouse motion listener to frame’s content pane
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                positionLabel.setText("X: " + e.getX() + ", Y: " + e.getY());
            }
        });



        //ALL ROOMS----------------------------------------------------------------------------------------------------

        int allYLevel = 10;

        //all label
        frame.add(GUIHelperMethods.createLabel("ALL ROOMS", 10, allYLevel, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK));

        //all lights
        JPanel allLightsBox = GUIHelperMethods.createPanel(100, allYLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(allLightsBox);

            //all lights on button
            allLightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10,  60, 60, lineBorder2, serif, Color.GRAY, null);
            allLightsBox.add(allLightsButton);

            //all lights off button
            allPowerButton = GUIHelperMethods.createToggleButton("POWER+LIGHTS", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, null);
            allLightsBox.add(allPowerButton);

                allLightsButton.addItemListener(e ->
                {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                    {
                        //bms.allLightsOn();
                        System.out.println("Button is ON");
                        allLightsButton.setEnabled(false);
                        allLightsButton.setBackground(Color.DARK_GRAY);
                        allLightsButton.setText("ACTIVATING");

                        timer.addActionListener( _ -> {
                            System.out.println("in time stop");
                            allLightsButton.setBackground(Color.GRAY);
                            allLightsButton.setEnabled(true);
                            allLightsButton.setText("On");
                            timer.stop();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    else
                    {
                        int dialogOut = JOptionPane.showConfirmDialog(null, "Are you sure about that", "WARNING", JOptionPane.YES_NO_OPTION);
                        if(dialogOut == JOptionPane.NO_OPTION)
                        {
                            System.out.println("NO DONT shutdown");

                        }
                        else
                        {
                            allLightsButton.setText("Off");

                            System.out.println("Button is OFF");
                        }
                    }
                });

                allPowerButton.addItemListener(e ->
                {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                    {
                        allPowerButton.setText("On");
                        System.out.println("Button is ON");
                    }
                    else
                    {
                        allPowerButton.setText("Off");
                        System.out.println("Button is OFF");
                    }
                });


        //CR1---------------------------------------------------------------------------------------------------------

        int cr1YLevel = 100;

        //room name label
        JLabel cr1NameLabel = GUIHelperMethods.createLabel("<html>Control<br>Room 1</html>", 10, cr1YLevel+10, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr1NameLabel);

        //cr1 conditioning control box
        JPanel cr1ConditioningBox = GUIHelperMethods.createPanel(100, cr1YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(cr1ConditioningBox);

            //heat button
            cr1HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr1HeatButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1HeatButton);

            //cool button
            cr1CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr1CoolButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1CoolButton);

            //off button
            cr1ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr1ConditioningOffButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1ConditioningOffButton);


                //cr1 heat button action listener
                cr1HeatButton.addActionListener(_ -> {
                    bms.findRoom("CR 1").setCoolHeat('h');
                    cr1HeatButton.setBackground(Color.WHITE);
                    cr1CoolButton.setBackground(Color.GRAY);
                    cr1ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current st1 conditioning set to heat");
                });

                //cool button action listener
                cr1CoolButton.addActionListener(_ -> {
                    bms.findRoom("CR 1").setCoolHeat('c');
                    cr1HeatButton.setBackground(Color.GRAY);
                    cr1CoolButton.setBackground(Color.WHITE);
                    cr1ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current st1 conditioning set to cool ");
                });

                //off button action listener
                cr1ConditioningOffButton.addActionListener(_ -> {
                    bms.findRoom("CR 1").setCoolHeat('n');
                    cr1HeatButton.setBackground(Color.GRAY);
                    cr1CoolButton.setBackground(Color.GRAY);
                    cr1ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current st1 conditioning set to none");
                });


        // cr1 information box
        JPanel cr1TemperatureInfoBox = GUIHelperMethods.createPanel(330, cr1YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
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
            cr1ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("CR 1").getCoolHeat()), 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TemperatureInfoBox.add(cr1ConditioningStatus);


        //CR1 temp box-------------------------------------------------------------------------------------------
        JPanel cr1TempBox = GUIHelperMethods.createPanel(560, cr1YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        cr1TempBox.setLayout(null);
        frame.add(cr1TempBox);

            //temp up
            JButton cr1TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, Color.ORANGE, Color.YELLOW);
            cr1TempUpButton.setFocusPainted(false);
            cr1TempBox.add(cr1TempUpButton);

            //temp down
            JButton cr1TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            cr1TempDownButton.setFocusPainted(false);
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
        JPanel cr1PowerBox = GUIHelperMethods.createPanel(720, cr1YLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(cr1PowerBox);


            //cr 1lights on button
            cr1LightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1PowerBox.add(cr1LightsButton);

            //lights off button
            cr1PowerButton = GUIHelperMethods.createToggleButton("POWER", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1PowerBox.add(cr1PowerButton);

                //st1 lights on button action listener
                cr1LightsButton.addActionListener(_ -> {
                    try
                    {
                        BMSMethods.relayWrite(bms.CR1_Lights, "on");
                        BMSMethods.relayWrite(bms.BTH1_Power, "on");
                    }
                    catch (SerialPortException | InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    cr1LightsButton.setBackground(new Color(153, 144, 14));
                    cr1PowerButton.setBackground(Color.DARK_GRAY);
                    System.out.println("Current st1 lights are on");
                });

                //st1 lights off button action listener
                cr1LightsButton.addActionListener(_ -> {
                    try
                    {
                        BMSMethods.relayWrite(bms.CR1_Lights, "off");
                    }
                    catch (SerialPortException | InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                cr1LightsButton.setBackground(Color.DARK_GRAY);
                cr1PowerButton.setBackground(new Color(27, 59, 135));
                System.out.println("Current st1 lights are off");
                });





        //booth  1--------------------------------------------------------------------------------------------------

        int bth1YLevel = 190;

        //room name label
        JLabel bth1NameLabel = GUIHelperMethods.createLabel("Booth 1", 10, bth1YLevel+30, 80, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth1NameLabel);



        //bth1 conditioning box
        JPanel bth1ConditioningBox = GUIHelperMethods.createPanel(100, bth1YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
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
                    bth1HeatButton.setBackground(Color.WHITE);
                    bth1CoolButton.setBackground(Color.GRAY);
                    bth1ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current bth1 conditioning set to heat");
                });

                //cool button action listener
                bth1CoolButton.addActionListener(_ ->
                {
                bms.findRoom("Booth 1").setCoolHeat('c');
                bth1HeatButton.setBackground(Color.GRAY);
                bth1CoolButton.setBackground(Color.WHITE);
                bth1ConditioningOffButton.setBackground(Color.GRAY);
                System.out.println("Current bth1 conditioning set to cool ");
                });

                //off button action listener
                bth1ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 1").setCoolHeat('n');
                    bth1HeatButton.setBackground(Color.GRAY);
                    bth1CoolButton.setBackground(Color.GRAY);
                    bth1ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current bth1 conditioning set to none");
                });


        //booth 1 temp status box
        JPanel bth1TemperatureInfoBox = GUIHelperMethods.createPanel(330, bth1YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
        frame.add(bth1TemperatureInfoBox);

            //bth1 current temp display
            bth1CurrentTemp = GUIHelperMethods.createLabel("##", 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1CurrentTemp);

            //bth1 current temp text
            JLabel bth1CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color. BLACK);
            bth1TemperatureInfoBox.add(bth1CurrentLabel);

            //bth1 target temp display
            bth1TargetTemp = GUIHelperMethods.createLabel("##", 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1TargetTemp);

            //bth1 current temp text
            JLabel bth1TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1TargetTempLabel);

            //bth1 current conditioning display
            JLabel bth1ConditioningStatus = GUIHelperMethods.createLabel("NONE", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1ConditioningStatus);


        //CR1 temp box-------------------------------------------------------------------------------------------
        JPanel bth1TempBox = GUIHelperMethods.createPanel(560, bth1YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        bth1TempBox.setLayout(null);
        frame.add(bth1TempBox);

            //temp up
            JButton bth1TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, Color.ORANGE, Color.YELLOW);
            bth1TempUpButton.setFocusPainted(false);
            bth1TempBox.add(bth1TempUpButton);

            //temp down
            JButton bth1TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            bth1TempDownButton.setFocusPainted(false);
            bth1TempBox.add(bth1TempDownButton);

                //cr1 temp down button action listener
                bth1TempDownButton.addActionListener(_ -> {
                    double tempTemp = bms.findRoom("Booth 1").getTargetTemp()-1;
                    bms.findRoom("Booth 1").setTargetTemp(tempTemp);
                    bth1TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth1 target temperature: " + bms.findRoom("Booth 1").getTargetTemp());
                });

                //cr1 temp up button action listener
                bth1TempUpButton.addActionListener(_ -> {
                    double tempTemp = bms.findRoom("Booth 1").getTargetTemp()+1;
                    bms.findRoom("Booth 1").setTargetTemp(tempTemp);
                    bth1TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current bth1 target temperature: " + bms.findRoom("Booth 1").getTargetTemp());
                });



        //CR2-------------------------------------------------------------------------------------

        int cr2YLevel = 280;

        //room name label
        JLabel cr2NameLabel = GUIHelperMethods.createLabel("<html>Control<br>Room 2</html>", 10, cr2YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr2NameLabel);

        //cr2 conditioning box
        JPanel cr2ConditioningBox = GUIHelperMethods.createPanel(100, cr2YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
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


                //cr1 heat button action listener
                cr2HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 2").setCoolHeat('h');
                    cr2HeatButton.setBackground(Color.WHITE);
                    cr2CoolButton.setBackground(Color.GRAY);
                    cr2ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current st2 conditioning set to heat");
                });

                //cool button action listener
                cr2CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 2").setCoolHeat('c');
                    cr2HeatButton.setBackground(Color.GRAY);
                    cr2CoolButton.setBackground(Color.WHITE);
                    cr2ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current st2 conditioning set to cool ");
                });

                //off button action listener
                cr2ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 2").setCoolHeat('n');
                    cr2HeatButton.setBackground(Color.GRAY);
                    cr2CoolButton.setBackground(Color.GRAY);
                    cr2ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current st2 conditioning set to none");
                });



        // cr2 information box
        JPanel cr2TemperatureInfoBox = GUIHelperMethods.createPanel(330, cr2YLevel, 220, 80, lineBorder3, new Color(59, 138, 81));
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
            cr2ConditioningStatus = GUIHelperMethods.createLabel(Character.toString(bms.findRoom("CR 2").getCoolHeat()), 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2TemperatureInfoBox.add(cr2ConditioningStatus);


        //CR2 temp box-------------------------------------------------------------------------------------------
        JPanel cr2TempBox = GUIHelperMethods.createPanel(560, cr2YLevel, 150, 80, lineBorder3, new Color(62, 94, 173));
        frame.add(cr2TempBox);

            //temp up
            JButton cr2TempUpButton = GUIHelperMethods.createButton("↑", 10, 10, 60, 60, lineBorder2, serif, Color.ORANGE, Color.YELLOW);
            cr1TempUpButton.setFocusPainted(false);
            cr2TempBox.add(cr2TempUpButton);

            //temp down
            JButton cr2TempDownButton = GUIHelperMethods.createButton("↓", 80, 10, 60, 60, lineBorder2, serif, new Color(29, 127, 184), Color.YELLOW);
            cr2TempDownButton.setFocusPainted(false);
            cr2TempBox.add(cr2TempDownButton);

                //cr2 temp down button action listener
                cr2TempDownButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("CR 2").getTargetTemp()-1;
                    bms.findRoom("CR 2").setTargetTemp(tempTemp);
                    cr2TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current st2 target temperature: " + bms.findRoom("CR 2").getTargetTemp());
                });

                //cr2 temp up button action listener
                cr2TempUpButton.addActionListener(_ ->
                {
                    double tempTemp = bms.findRoom("CR 2").getTargetTemp()+1;
                    bms.findRoom("CR 2").setTargetTemp(tempTemp);
                    cr2TargetTemp.setText(String.valueOf(tempTemp));
                    System.out.println("Current st2 target temperature: " + bms.findRoom("CR 2").getTargetTemp());
                });

        //cr1 power box
        JPanel cr2PowerBox = GUIHelperMethods.createPanel(720, cr2YLevel, 150, 80, lineBorder3, new Color(232, 207, 67));
        frame.add(cr2PowerBox);


            //cr 2 lights button
            cr2LightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2PowerBox.add(cr2LightsButton);

            //lights off button
            cr2PowerButton = GUIHelperMethods.createToggleButton("POWER", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr2PowerBox.add(cr2PowerButton);

                //cr2 lights on button action listener
                cr1LightsButton.addActionListener(_ ->
                {
                    System.out.println("Current cr2 lights are on");
                });

                //st1 lights off button action listener
                cr1LightsButton.addActionListener(_ ->
                {

                    System.out.println("Current cr2 lights are off");
                });









        //BTH2-------------------------------------------------------------------------------------

        int bth2YLevel = 370;

        //room name label
        JLabel bth2NameLabel = GUIHelperMethods.createLabel("<html>Booth 2</html>", 10, bth2YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth2NameLabel);

        //cr2 conditioning box
        JPanel bth2ConditioningBox = GUIHelperMethods.createPanel(100, bth2YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(bth2ConditioningBox);

            //heat button
            JButton bth2HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth2ConditioningBox.add(bth2HeatButton);

            //cool button
            JButton bth2CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth2ConditioningBox.add(bth2CoolButton);

            //off button
            JButton bth2ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth2ConditioningBox.add(bth2ConditioningOffButton);


                //cr1 heat button action listener
                bth2HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 2").setCoolHeat('h');
                    bth2HeatButton.setBackground(Color.WHITE);
                    bth2CoolButton.setBackground(Color.GRAY);
                    bth2ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current bth2 conditioning set to heat");
                });

                //cool button action listener
                bth2CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 2").setCoolHeat('c');
                    bth2HeatButton.setBackground(Color.GRAY);
                    bth2CoolButton.setBackground(Color.WHITE);
                    bth2ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current bth2 conditioning set to cool ");
                });

                //off button action listener
                bth2ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 2").setCoolHeat('n');
                    bth2HeatButton.setBackground(Color.GRAY);
                    bth2CoolButton.setBackground(Color.GRAY);
                    bth2ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current bth2 conditioning set to none");
                });



//CR3-------------------------------------------------------------------------------------

        int cr3YLevel = 460;

        //room name label
        JLabel cr3NameLabel = GUIHelperMethods.createLabel("<html>Control<br>Room 3</html>", 10, cr3YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr3NameLabel);

        //cr2 conditioning box
        JPanel cr3ConditioningBox = GUIHelperMethods.createPanel(100, cr3YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(cr3ConditioningBox);

            //heat button
            JButton cr3HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr3ConditioningBox.add(cr3HeatButton);

            //cool button
            JButton cr3CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr3ConditioningBox.add(cr3CoolButton);

            //off button
            JButton cr3ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr3ConditioningBox.add(cr3ConditioningOffButton);

                //cr3 heat button action listener
                cr3HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 3").setCoolHeat('h');
                    cr3HeatButton.setBackground(Color.WHITE);
                    cr3CoolButton.setBackground(Color.GRAY);
                    cr3ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current cr3 conditioning set to heat");
                });

                //cool button action listener
                cr3CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 3").setCoolHeat('c');
                    cr3HeatButton.setBackground(Color.GRAY);
                    cr3CoolButton.setBackground(Color.WHITE);
                    cr3ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current cr3 conditioning set to cool ");
                });

                //off button action listener
                cr3ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("CR 3").setCoolHeat('n');
                    cr3HeatButton.setBackground(Color.GRAY);
                    cr3CoolButton.setBackground(Color.GRAY);
                    cr3ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current cr3 conditioning set to none");
                });


        //BTH3-------------------------------------------------------------------------------------

        int bth3YLevel = 550;

        //room name label
        JLabel bth3NameLabel = GUIHelperMethods.createLabel("<html>Booth 3</html>", 10, bth3YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth3NameLabel);

        //cr2 conditioning box
        JPanel bth3ConditioningBox = GUIHelperMethods.createPanel(100, bth3YLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(bth3ConditioningBox);

            //heat button
            JButton bth3HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth3ConditioningBox.add(bth3HeatButton);

            //cool button
            JButton bth3CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth3ConditioningBox.add(bth3CoolButton);

            //off button
            JButton bth3ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth3ConditioningBox.add(bth3ConditioningOffButton);


                //cr1 heat button action listener
                bth3HeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 3").setCoolHeat('h');
                    bth3HeatButton.setBackground(Color.WHITE);
                    bth3CoolButton.setBackground(Color.GRAY);
                    bth3ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current bth3 conditioning set to heat");
                });

                //cool button action listener
                bth3CoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 3").setCoolHeat('c');
                    bth3HeatButton.setBackground(Color.GRAY);
                    bth3CoolButton.setBackground(Color.WHITE);
                    bth3ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current bth3 conditioning set to cool ");
                });

                //off button action listener
                bth3ConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Booth 3").setCoolHeat('n');
                    bth3HeatButton.setBackground(Color.GRAY);
                    bth3CoolButton.setBackground(Color.GRAY);
                    bth3ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current bth3 conditioning set to none");
                });






        //EDIT-------------------------------------------------------------------------------------

        int editYLevel = 640;

        //room name label
        JLabel editNameLabel = GUIHelperMethods.createLabel("<html>EDIT</html>", 10, editYLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(editNameLabel);

        //cr2 conditioning box
        JPanel editConditioningBox = GUIHelperMethods.createPanel(100, editYLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(editConditioningBox);

            //heat button
            JButton editHeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            editConditioningBox.add(editHeatButton);

            //cool button
            JButton editCoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            editConditioningBox.add(editCoolButton);

            //off button
            JButton editConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            editConditioningBox.add(editConditioningOffButton);


                //edit heat button action listener
                editHeatButton.addActionListener(_ ->
                {
                    bms.findRoom("Edit").setCoolHeat('h');
                    editHeatButton.setBackground(Color.WHITE);
                    editCoolButton.setBackground(Color.GRAY);
                    editConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current edit conditioning set to heat");
                });

                //cool button action listener
                editCoolButton.addActionListener(_ ->
                {
                    bms.findRoom("Edit").setCoolHeat('c');
                    editHeatButton.setBackground(Color.GRAY);
                    editCoolButton.setBackground(Color.WHITE);
                    editConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current edit conditioning set to cool ");
                });

                //off button action listener
                editConditioningOffButton.addActionListener(_ ->
                {
                    bms.findRoom("Edit").setCoolHeat('n');
                    editHeatButton.setBackground(Color.GRAY);
                    editCoolButton.setBackground(Color.GRAY);
                    editConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current bth3 conditioning set to none");
                });



        //OTHER ROOMS-------------------------------------------------------------------------------------

        int otherYLevel = 730;

        //room name label
        JLabel otherNameLabel = GUIHelperMethods.createLabel("<html>OTHER</html>", 10, otherYLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(otherNameLabel);

        //cr2 conditioning box
        JPanel otherConditioningBox = GUIHelperMethods.createPanel(100, otherYLevel, 220, 80, lineBorder3, new Color(255, 101, 66));
        frame.add(otherConditioningBox);

        //heat button
        JLabel otherHeatLabel = GUIHelperMethods.createLabel("Kitchen", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
        otherConditioningBox.add(otherHeatLabel);

        //cool button
        JLabel otherCoolLabel = GUIHelperMethods.createLabel("Hallway", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
        otherConditioningBox.add(otherCoolLabel);

        //off button
        JLabel otherConditioningOffLabel = GUIHelperMethods.createLabel("Phone Booth", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
        otherConditioningBox.add(otherConditioningOffLabel);



        frame.setVisible(true);


    }





    public void update(BMSMethods bms) throws SerialPortException, InterruptedException {


        //checking lights and all lights toggle
        int howManyLights = 0;//track for all lights toggle

        if(BMSMethods.relayRead(8).equalsIgnoreCase("on"))//CR1_Lights
        {
            howManyLights++;
            cr1LightsButton.setSelected(true);
        }
        else
        {
            cr1LightsButton.setSelected(false);
        }

        if(BMSMethods.relayRead(11).equalsIgnoreCase("on"))//CR2_Lights
        {
            howManyLights++;
            //cr2LightsButton.setSelected(true);
        }
        else
        {
            //cr2LightsButton.setSelected(false);
        }





    }






}



import jssc.SerialPortException;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;


public class V2UITesting
{

    public void update()
    {



    }




    public V2UITesting(BMSMethods bms)
    {

        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);

        Font serif = new Font("Serif", Font.BOLD, 16);

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
        frame.setSize(1055, 860); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);

        //mouse point for me
        JLabel positionLabel = new JLabel("Move the mouse...");
        positionLabel.setBounds(800, 10, 200, 20);  // position + size
        frame.add(positionLabel);
        // Add mouse motion listener to frame’s content pane
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                positionLabel.setText("X: " + e.getX() + ", Y: " + e.getY());
            }
        });



        //ALL LIGHTS----------------------------------------------------------------------------------------------------

        int allYLevel = 10;

        //all label
        frame.add(GUIHelperMethods.createLabel("ALL ROOMS", 10, allYLevel, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK));

        //all lights
        JPanel allLightsBox = GUIHelperMethods.createPanel(100, allYLevel, 150, 80, new Color(232, 207, 67), lineBorder3);
        frame.add(allLightsBox);

            //all lights on button
            JToggleButton allLightsButton = GUIHelperMethods.createToggleButton("LIGHTS", 10, 10,  60, 60, Color.GRAY, lineBorder2, serif);
            allLightsBox.add(allLightsButton);

            //all lights off button
            JToggleButton allPowerButton = GUIHelperMethods.createToggleButton("POWER+LIGHTS", 80, 10, 60, 60, Color.GRAY, lineBorder2, serif);
            allLightsBox.add(allPowerButton);

                allLightsButton.addItemListener(e ->
                {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                    {
                        //bms.allLightsOn();
                        System.out.println("Button is ON");
                        allLightsButton.setEnabled(false);
                        allLightsButton.setBackground(Color.DARK_GRAY);
                        allLightsButton.setText("PAUSE");

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
                        allLightsButton.setText("Off");
                        System.out.println("Button is OFF");
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
        JPanel cr1ConditioningBox = GUIHelperMethods.createPanel(100, cr1YLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(cr1ConditioningBox);

            //heat button
            JButton cr1HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr1HeatButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1HeatButton);

            //cool button
            JButton cr1CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr1CoolButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1CoolButton);

            //off button
            JButton cr1ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
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
        JPanel cr1TemperatureInfoBox = GUIHelperMethods.createPanel(330, cr1YLevel, 220, 80, new Color(59, 138, 81), lineBorder3);
        frame.add(cr1TemperatureInfoBox);

            //current temp display
            JLabel cr1CurrentTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 1").getCurrentTemp()), 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1CurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1CurrentTemp);

            //current temp text
            JLabel cr1CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1CurrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1CurrentLabel);

            //cr1 target temp display
            JLabel cr1TargetTemp = GUIHelperMethods.createLabel(String.valueOf(bms.findRoom("CR 1").getTargetTemp()), 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TargetTemp.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1TargetTemp);

            //current temp text
            JLabel cr1TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TargetTempLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1TargetTempLabel);

            //cr1 current conditioning display
            JLabel cr1ConditioningStatus = GUIHelperMethods.createLabel("NONE", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1ConditioningStatus.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1ConditioningStatus);


        //CR1 temp box-------------------------------------------------------------------------------------------
        JPanel cr1TempBox = GUIHelperMethods.createPanel(560, cr1YLevel, 150, 80, new Color(62, 94, 173), lineBorder3);
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

        //cr1 light box
        JPanel cr1LightsBox = GUIHelperMethods.createPanel(720, cr1YLevel, 150, 80, new Color(232, 207, 67), lineBorder3);
        frame.add(cr1LightsBox);


            //cr 1lights on button
            JButton cr1LightsOnButton = GUIHelperMethods.createButton(onBulb, 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1LightsBox.add(cr1LightsOnButton);

            //lights off button
            JButton cr1LightsOffButton = GUIHelperMethods.createButton(offBulb, 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1LightsBox.add(cr1LightsOffButton);

                //st1 lights on button action listener
                cr1LightsOnButton.addActionListener(_ -> {
                    try
                    {
                        BMSMethods.relayWrite(bms.CR1_Lights, "on");
                        BMSMethods.relayWrite(bms.BTH1_Power, "on");
                    }
                    catch (SerialPortException | InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    cr1LightsOnButton.setBackground(new Color(153, 144, 14));
                    cr1LightsOffButton.setBackground(Color.DARK_GRAY);
                    System.out.println("Current st1 lights are on");
                });

                //st1 lights off button action listener
                cr1LightsOffButton.addActionListener(_ -> {
                    try
                    {
                        BMSMethods.relayWrite(bms.CR1_Lights, "off");
                    }
                    catch (SerialPortException | InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                cr1LightsOnButton.setBackground(Color.DARK_GRAY);
                cr1LightsOffButton.setBackground(new Color(27, 59, 135));
                System.out.println("Current st1 lights are off");
                });

        //turn on/off box
        JPanel cr1OnOffBox = GUIHelperMethods.createPanel(880, cr1YLevel, 150, 80, new Color(59,204,177), lineBorder3);
        frame.add(cr1OnOffBox);

            //lights on button
            JButton cr1AllOnButton = GUIHelperMethods.createButton("STUDIO ON", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1OnOffBox.add(cr1AllOnButton);

            //lights off button
            JButton cr1AllOffButton = GUIHelperMethods.createButton("TURN STUDIO OFF", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1OnOffBox.add(cr1AllOffButton);

                //all all on button action listener
                cr1AllOnButton.addActionListener(_ -> {
                cr1AllOnButton.setBackground(new Color(22, 99, 45));
                cr1AllOffButton.setBackground(Color.DARK_GRAY);
                System.out.println("ST1 POWER OFF");
                });

                //all all off button action listener
                cr1AllOffButton.addActionListener(_ -> {
                cr1AllOnButton.setBackground(Color.DARK_GRAY);
                cr1AllOffButton.setBackground(new Color(99, 22, 30));
                System.out.println("ST1 POWER ON");
                });





        //booth  1--------------------------------------------------------------------------------------------------

        int bth1YLevel = 190;

        //room name label
        JLabel bth1NameLabel = GUIHelperMethods.createLabel("Booth 1", 10, bth1YLevel+30, 80, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
        bth1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(bth1NameLabel);



        //bth1 conditioning box
        JPanel bth1ConditioningBox = GUIHelperMethods.createPanel(100, bth1YLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(bth1ConditioningBox);

            //heat button
            JButton bth1HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth1HeatButton.setFocusPainted(false);
            bth1ConditioningBox.add(bth1HeatButton);

            //cool button
            JButton bth1CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth1CoolButton.setFocusPainted(false);
            bth1ConditioningBox.add(bth1CoolButton);

            //off button
            JButton bth1ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
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
        JPanel bth1TemperatureInfoBox = GUIHelperMethods.createPanel(330, bth1YLevel, 220, 80, new Color(59, 138, 81), lineBorder3);
        frame.add(bth1TemperatureInfoBox);

            //bth1 current temp display
            JLabel bth1CurrentTemp = GUIHelperMethods.createLabel("##", 10, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1CurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TemperatureInfoBox.add(bth1CurrentTemp);

            //bth1 current temp text
            JLabel bth1CurrentLabel = GUIHelperMethods.createLabel("Current", 10, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color. BLACK);
            bth1CurrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TemperatureInfoBox.add(bth1CurrentLabel);

            //bth1 target temp display
            JLabel bth1TargetTemp = GUIHelperMethods.createLabel("##", 80, 10, 60, 40, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TargetTemp.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TemperatureInfoBox.add(bth1TargetTemp);

            //bth1 current temp text
            JLabel bth1TargetTempLabel = GUIHelperMethods.createLabel("Target", 80, 48, 60, 22, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1TemperatureInfoBox.add(bth1TargetTempLabel);

            //bth1 current conditioning display
            JLabel bth1ConditioningStatus = GUIHelperMethods.createLabel("NONE", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1ConditioningStatus.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TemperatureInfoBox.add(bth1ConditioningStatus);


        //CR1 temp box-------------------------------------------------------------------------------------------
        JPanel bth1TempBox = GUIHelperMethods.createPanel(560, bth1YLevel, 150, 80, new Color(62, 94, 173), lineBorder3);
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
        JPanel cr2ConditioningBox = GUIHelperMethods.createPanel(100, cr2YLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(cr2ConditioningBox);

            //heat button
            JButton cr2HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr2HeatButton.setFocusPainted(false);
            cr2ConditioningBox.add(cr2HeatButton);

            //cool button
            JButton cr2CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr2CoolButton.setFocusPainted(false);
            cr2ConditioningBox.add(cr2CoolButton);

            //off button
            JButton cr2ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr2ConditioningOffButton.setFocusPainted(false);
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


        //BTH2-------------------------------------------------------------------------------------

        int bth2YLevel = 370;

        //room name label
        JLabel bth2NameLabel = GUIHelperMethods.createLabel("<html>Booth 2</html>", 10, bth2YLevel+15, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(bth2NameLabel);

        //cr2 conditioning box
        JPanel bth2ConditioningBox = GUIHelperMethods.createPanel(100, bth2YLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(bth2ConditioningBox);

            //heat button
            JButton bth2HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth2HeatButton.setFocusPainted(false);
            bth2ConditioningBox.add(bth2HeatButton);

            //cool button
            JButton bth2CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth2CoolButton.setFocusPainted(false);
            bth2ConditioningBox.add(bth2CoolButton);

            //off button
            JButton bth2ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth2ConditioningOffButton.setFocusPainted(false);
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
        JPanel cr3ConditioningBox = GUIHelperMethods.createPanel(100, cr3YLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(cr3ConditioningBox);

            //heat button
            JButton cr3HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr3HeatButton.setFocusPainted(false);
            cr3ConditioningBox.add(cr3HeatButton);

            //cool button
            JButton cr3CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr3CoolButton.setFocusPainted(false);
            cr3ConditioningBox.add(cr3CoolButton);

            //off button
            JButton cr3ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            cr3ConditioningOffButton.setFocusPainted(false);
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
        JPanel bth3ConditioningBox = GUIHelperMethods.createPanel(100, bth3YLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(bth3ConditioningBox);

            //heat button
            JButton bth3HeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            bth3HeatButton.setFocusPainted(false);
            bth3ConditioningBox.add(bth3HeatButton);

            //cool button
            JButton bth3CoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            bth3CoolButton.setFocusPainted(false);
            bth3ConditioningBox.add(bth3CoolButton);

            //off button
            JButton bth3ConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            bth3ConditioningOffButton.setFocusPainted(false);
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
        JPanel editConditioningBox = GUIHelperMethods.createPanel(100, editYLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(editConditioningBox);

            //heat button
            JButton editHeatButton = GUIHelperMethods.createButton("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            editHeatButton.setFocusPainted(false);
            editConditioningBox.add(editHeatButton);

            //cool button
            JButton editCoolButton = GUIHelperMethods.createButton("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            editCoolButton.setFocusPainted(false);
            editConditioningBox.add(editCoolButton);

            //off button
            JButton editConditioningOffButton = GUIHelperMethods.createButton("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
            editConditioningOffButton.setFocusPainted(false);
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
        JPanel otherConditioningBox = GUIHelperMethods.createPanel(100, otherYLevel, 220, 80, new Color(255, 101, 66), lineBorder3);
        frame.add(otherConditioningBox);

        //heat button
        JLabel otherHeatLabel = GUIHelperMethods.createLabel("Heat", 10, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
        otherConditioningBox.add(otherHeatLabel);

        //cool button
        JLabel otherCoolLabel = GUIHelperMethods.createLabel("Cool", 80, 10, 60, 60, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
        otherConditioningBox.add(otherCoolLabel);

        //off button
        JLabel otherConditioningOffLabel = GUIHelperMethods.createLabel("OFF", 150, 10, 60, 60, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
        otherConditioningBox.add(otherConditioningOffLabel);












        frame.setVisible(true);


    }

}



import jssc.SerialPortException;

import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;


public class V2UITesting
{




    public JButton createButton(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBorder(border);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);

        button.setOpaque(true);

        return button;
    }

     //   @overload
    public JButton createButton(ImageIcon icon, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JButton button = new JButton(icon);
        button.setBounds(x, y, width, height);
        button.setBorder(border);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setHorizontalTextPosition(SwingConstants.CENTER);


        button.setOpaque(true);

        return button;
    }
    public JLabel createLabel(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setBorder(border);
        label.setFont(font);
        label.setBackground(background);
        label.setForeground(foreground);
        label.setHorizontalTextPosition(SwingConstants.CENTER);

        label.setOpaque(true);

        return label;
    }

    public JPanel createPanel(int x, int y, int width, int height, Color color ,Border border)
    {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBorder(border);
        panel.setBackground(color);
        panel.setLayout(null);

        return panel;
    }

    public V2UITesting(BMSMethods bms)
    {

        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);

        Font serif = new Font("Serif", Font.BOLD, 16);


        //set up the frame
        JFrame frame = new JFrame("Olive Building Management System");
        frame.setIconImage(new ImageIcon("BMS//Test//IglooLogo.png").getImage());

        ImageIcon onBulbOriginal = new ImageIcon("BMS//Test//OnBulb.png");
        ImageIcon onBulb = new ImageIcon(onBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        ImageIcon offBulbOriginal = new ImageIcon("BMS//Test//OffBulb.png");
        ImageIcon offBulb = new ImageIcon(offBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        //frame attributes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when you hit the X button
        frame.setLocation(350,80); //where is the window going to open
        frame.setSize(1295, 800); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);

        //mouse point for me
        JLabel positionLabel = new JLabel("Move the mouse...");
        positionLabel.setBounds(100, 10, 200, 20);  // position + size
        frame.add(positionLabel);
        // Add mouse motion listener to frame’s content pane
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                positionLabel.setText("X: " + e.getX() + ", Y: " + e.getY());
            }
        });



        //ALL STUDIO CONTROL BOX-----------------------------------------------------------

        int allControlYLevel = 30;




        //all lights
        JPanel allLightsBox = createPanel(880, 30, 190, 100, new Color(232, 207, 67), lineBorder3);
        frame.add(allLightsBox);

        //label
        frame.add(createLabel("ALL ROOMS", 755, 55, 120, 40, lineBorder2, serif, Color.GRAY, Color.BLACK));

            //all lights on button
            JButton allLightsOnButton = createButton(onBulb, 10, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.RED);
            allLightsBox.add(allLightsOnButton);

            //all lights off button
            JButton allLightsOffButton = createButton(offBulb, 100, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.RED);
            allLightsBox.add(allLightsOffButton);

                //all lights on button action listener
                allLightsOnButton.addActionListener(_ ->
                {

                    bms.allLightsOn();

                    allLightsOnButton.setBackground(new Color(153, 144, 14));
                    allLightsOffButton.setBackground(Color.DARK_GRAY);
                    System.out.println("ALL LIGHTS ON");
                });

                //all lights off button action listener
                allLightsOffButton.addActionListener(_ ->
                {
                    bms.allLightsOff();

                    allLightsOnButton.setBackground(Color.DARK_GRAY);
                    allLightsOffButton.setBackground(new Color(27, 59, 135));
                    System.out.println("ALL LIGHTS OFF");
                });

        //all power box
        JPanel allPowerBox = createPanel(1080, 30, 190, 100, new Color(59,204,177), lineBorder3);
        frame.add(allPowerBox);

            //all power on button
            JButton allPowerOnButton = createButton("<html>STUDIO<br>ON</html>", 10, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.BLACK);
            allPowerBox.add(allPowerOnButton);

            //all power off button
            JButton allPowerOffButton = createButton("<html>TURN<br>STUDIO<br>OFF</html>", 100, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.BLACK);
            allPowerBox.add(allPowerOffButton);

                //all all on button action listener
                allPowerOnButton.addActionListener(_ -> {

                    bms.launchAll();
                    bms.setAllRoomTemps(bms.removeMRs(), 74);//set all rooms except MR to 74
                    bms.setAllRoomsRequest(bms.removeMRs(), 'c');//set all rooms except MR to cool
                    //bms.logPrint();

                allPowerOnButton.setBackground(new Color(22, 99, 45));
                allPowerOffButton.setBackground(Color.DARK_GRAY);
                System.out.println("ALL POWER OFF");
                });

                //all all off button action listener
                allPowerOffButton.addActionListener(_ -> {
                    //studio shutdown, turn off everything, set request to none
                    bms.shutdownAll();
                    bms.setAllRoomsRequest(bms.removeMRs(), 'n');
                    allPowerOnButton.setBackground(Color.DARK_GRAY);
                    allPowerOffButton.setBackground(new Color(99, 22, 30));
                System.out.println("ALL POWER ON");
                });

        //all control outline box
        JPanel allControlBoxOutline = createPanel(750, 25, 525, 110, null, lineBorder2);
        frame.add(allControlBoxOutline);


        //studio 1--------------------------------------------------------------------------------------------------

        int cr1YLevel = 180;


        //room name label
        JLabel cr1NameLabel = createLabel("<html>Control<br>Room 1</html>", 10, cr1YLevel+20, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr1NameLabel);


        //conditioning control box-------------------------------------------------------------

        //conditioning box label
        JLabel cr1ConditioningBoxLabel = createLabel("AC", 104, cr1YLevel-54, 80, 80, null, serif, null, Color.BLACK);
        cr1ConditioningBoxLabel.setOpaque(false);
        frame.add(cr1ConditioningBoxLabel);

        //cr1 conditioning control box
        JPanel cr1ConditioningBox = createPanel(100, cr1YLevel, 280, 100, new Color(255, 101, 66), lineBorder3);
        frame.add(cr1ConditioningBox);


            //heat button
            JButton cr1HeatButton = createButton("Heat", 10, 10, 80, 80, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
            cr1HeatButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1HeatButton);

            //cool button
            JButton cr1CoolButton = createButton("Cool", 100, 10, 80, 80, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
            cr1CoolButton.setFocusPainted(false);
            cr1ConditioningBox.add(cr1CoolButton);

            //off button
            JButton cr1ConditioningOffButton = createButton("OFF", 190, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
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


        //information box
        JPanel cr1TemperatureInfoBox = createPanel(390, cr1YLevel, 280, 100, new Color(59, 138, 81), lineBorder3);
        frame.add(cr1TemperatureInfoBox);

        //information box label
        JLabel cr1InformationBoxLabel = createLabel("STATUS", 396, cr1YLevel-52, 120, 80, null, serif, null, Color.BLACK);
        cr1InformationBoxLabel.setOpaque(false);
        frame.add(cr1InformationBoxLabel);

            //current temp display
            JLabel cr1CurrentTemp = createLabel(String.valueOf(bms.findRoom("CR 1").getCurrentTemp()), 10, 10, 80, 50, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1CurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1CurrentTemp);

            //current temp text
            JLabel cr1CurrentLabel = createLabel("Current", 10, 58, 80, 32, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1CurrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1CurrentLabel);

            //cr1 target temp display
            JLabel cr1TargetTemp = createLabel(String.valueOf(bms.findRoom("CR 1").getTargetTemp()), 100, 10, 80, 50, lineBorder2, serif, Color.GRAY, Color.BLACK);
            cr1TargetTemp.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TemperatureInfoBox.add(cr1TargetTemp);

            //current temp text
            JLabel cr1TargetTempLabel = new JLabel("Target");
            cr1TargetTempLabel.setOpaque(true);
            cr1TargetTempLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TargetTempLabel.setBorder(lineBorder2);
            cr1TargetTempLabel.setBounds(100, 10+48,80,32);
            cr1TargetTempLabel.setBackground(Color.GRAY);
            cr1TemperatureInfoBox.add(cr1TargetTempLabel);

            //cr1 current conditioning display
            JLabel cr1ConditioningStatus = new JLabel("NONE");
            cr1ConditioningStatus.setHorizontalAlignment(SwingConstants.CENTER);
            cr1ConditioningStatus.setOpaque(true);
            cr1ConditioningStatus.setBackground(Color.GRAY);
            cr1ConditioningStatus.setBorder(lineBorder2);
            cr1ConditioningStatus.setForeground(Color.RED);
            cr1ConditioningStatus.setBounds(190, 10,80,80);
            cr1TemperatureInfoBox.add(cr1ConditioningStatus);


        //temp controls box----------------------------------------------------------------
        JPanel cr1TempControlBox = new JPanel();
        cr1TempControlBox.setLayout(null);
        cr1TempControlBox.setBackground(new Color(62, 94, 173));
        cr1TempControlBox.setBounds(680, cr1YLevel, 190, 100);
        cr1TempControlBox.setBorder(lineBorder3);
        frame.add(cr1TempControlBox);

        //temp controls box label
        JLabel cr1TempControlHintLabel = new JLabel("TEMP");
        cr1TempControlHintLabel.setForeground(Color.BLACK);
        cr1TempControlHintLabel.setFont(new Font("Serif", Font.BOLD, 18));
        cr1TempControlHintLabel.setBounds(686 , cr1YLevel-54,80,80);
        frame.add(cr1TempControlHintLabel);


        //temp up
        JButton cr1TempUpButton = new JButton("↑");
        cr1TempUpButton.setFont(new Font("Serif", Font.BOLD, 60));
        cr1TempUpButton.setFocusPainted(false);
        cr1TempUpButton.setOpaque(true);
        cr1TempUpButton.setBorder(lineBorder2);
        cr1TempUpButton.setBackground(new Color(184, 94, 29));
        cr1TempUpButton.setForeground(Color.ORANGE);
        cr1TempUpButton.setBounds(10, 10,80,80);
        cr1TempControlBox.add(cr1TempUpButton);

        //temp down
        JButton cr1TempDownButton = new JButton("↓");
        cr1TempDownButton.setFont(new Font("Serif", Font.BOLD, 60));
        cr1TempDownButton.setFocusPainted(false);
        cr1TempDownButton.setOpaque(true);
        cr1TempDownButton.setBorder(lineBorder2);
        cr1TempDownButton.setBackground(new Color(29, 127, 184));
        cr1TempDownButton.setForeground(Color.YELLOW);
        cr1TempDownButton.setBounds(100, 10,80,80);
        cr1TempControlBox.add(cr1TempDownButton);

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

        //light box
        JPanel cr1LightsBox = new JPanel();
        cr1LightsBox.setLayout(null);
        cr1LightsBox.setBackground(new Color(232, 207, 67));
        cr1LightsBox.setBounds(880, cr1YLevel, 190, 100);
        cr1LightsBox.setBorder(lineBorder2);
        frame.add(cr1LightsBox);

        //light box label
        JLabel cr1LightBoxLabel = new JLabel("LIGHTS");
        cr1LightBoxLabel.setForeground(Color.BLACK);
        cr1LightBoxLabel.setFont(new Font("Serif", Font.BOLD, 12));
        cr1LightBoxLabel.setBounds(886 , cr1YLevel-52,120,80);
        frame.add(cr1LightBoxLabel);

            //lights on button
            JButton cr1LightsOnButton = new JButton(onBulb);
            cr1LightsOnButton.setOpaque(true);
            cr1LightsOnButton.setBackground(Color.GRAY);
            cr1LightsOnButton.setForeground(Color.RED);
            cr1LightsOnButton.setBounds(10, 10,80,80);
            cr1LightsBox.add(cr1LightsOnButton);

            //lights off button
            JButton cr1LightsOffButton = new JButton(offBulb);
            cr1LightsOffButton.setOpaque(true);
            cr1LightsOffButton.setBackground(Color.GRAY);
            cr1LightsOffButton.setForeground(Color.RED);
            cr1LightsOffButton.setBounds(100, 10,80,80);
            cr1LightsBox.add(cr1LightsOffButton);

                //st1 lights on button action listener
                cr1LightsOnButton.addActionListener(_ -> {
                    try
                    {
                        bms.relayWrite(bms.CR1_Lights, "on");
                        bms.relayWrite(bms.BTH1_Power, "on");
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
                        bms.relayWrite(bms.CR1_Lights, "off");
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
        JPanel cr1OnOffBox = new JPanel();
        cr1OnOffBox.setLayout(null);
        cr1OnOffBox.setBackground(new Color(59,204,177));
        cr1OnOffBox.setBounds(1080, cr1YLevel, 190, 100);
        cr1OnOffBox.setBorder(lineBorder2);
        frame.add(cr1OnOffBox);

        //light box label
        JLabel cr1OnOffBoxLabel = new JLabel("POWER");
        cr1OnOffBoxLabel.setForeground(Color.BLACK);
        cr1OnOffBoxLabel.setFont(new Font("Serif", Font.BOLD, 12));
        cr1OnOffBoxLabel.setBounds(1086 , cr1YLevel-52,120,80);
        frame.add(cr1OnOffBoxLabel);

            //lights on button
            JButton cr1AllOnButton = new JButton("STUDIO ON");
            cr1AllOnButton.setOpaque(true);
            cr1AllOnButton.setBackground(Color.GRAY);
            cr1AllOnButton.setForeground(Color.RED);
            cr1AllOnButton.setBounds(10, 10,80,80);
            cr1OnOffBox.add(cr1AllOnButton);

            //lights off button
            JButton cr1AllOffButton = new JButton("TURN STUDIO OFF");
            cr1AllOffButton.setOpaque(true);
            cr1AllOffButton.setBackground(Color.GRAY);
            cr1AllOffButton.setForeground(Color.RED);
            cr1AllOffButton.setBounds(100, 10,80,80);
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

        int bth1YLevel = 290;

        //room name label
        JLabel bth1NameLabel = new JLabel("Booth 1");
        bth1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bth1NameLabel.setOpaque(true);
        bth1NameLabel.setBackground(Color.GRAY);
        bth1NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        bth1NameLabel.setBorder(lineBorder2);
        bth1NameLabel.setBounds(10,bth1YLevel+30,80,40);
        frame.add(bth1NameLabel);


        //booth 1 conditioning control box-------------------------------------------------------------


        //bth1 conditioning control box
        JPanel bth1ConditioningBox = createPanel(100, bth1YLevel, 280, 100, new Color(255, 101, 66), lineBorder3);
        frame.add(bth1ConditioningBox);


        //heat button
        JButton bth1HeatButton = createButton("Heat", 10, 10, 80, 80, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
        bth1HeatButton.setFocusPainted(false);
        bth1ConditioningBox.add(bth1HeatButton);

        //cool button
        JButton bth1CoolButton = createButton("Cool", 100, 10, 80, 80, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
        bth1CoolButton.setFocusPainted(false);
        bth1ConditioningBox.add(bth1CoolButton);

        //off button
        JButton bth1ConditioningOffButton = createButton("OFF", 190, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
        bth1ConditioningOffButton.setFocusPainted(false);
        bth1ConditioningBox.add(bth1ConditioningOffButton);


        //cr1 heat button action listener
        bth1HeatButton.addActionListener(_ -> {
            bms.findRoom("Booth 1").setCoolHeat('h');
            bth1HeatButton.setBackground(Color.WHITE);
            bth1CoolButton.setBackground(Color.GRAY);
            bth1ConditioningOffButton.setBackground(Color.GRAY);
            System.out.println("Current bth1 conditioning set to heat");
        });

        //cool button action listener
        bth1CoolButton.addActionListener(_ -> {
            bms.findRoom("Booth 1").setCoolHeat('c');
            bth1HeatButton.setBackground(Color.GRAY);
            bth1CoolButton.setBackground(Color.WHITE);
            bth1ConditioningOffButton.setBackground(Color.GRAY);
            System.out.println("Current bth1 conditioning set to cool ");
        });

        //off button action listener
        bth1ConditioningOffButton.addActionListener(_ -> {
            bms.findRoom("Booth 1").setCoolHeat('n');
            bth1HeatButton.setBackground(Color.GRAY);
            bth1CoolButton.setBackground(Color.GRAY);
            bth1ConditioningOffButton.setBackground(Color.WHITE);
            System.out.println("Current bth1 conditioning set to none");
        });


        //booth 1 temp status box
        JPanel bth1TemperatureInfoBox = createPanel(390, bth1YLevel, 280, 100, new Color(59, 138, 81), lineBorder3);
        frame.add(bth1TemperatureInfoBox);

            //current temp display
            JLabel bth1CurrentTemp = createLabel("##", 10, 10, 80, 50, lineBorder2, serif, Color.GRAY, Color.BLACK);
            bth1CurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TemperatureInfoBox.add(bth1CurrentTemp);

            //current temp text
            JLabel bth1CurrentLabel = new JLabel("Current");
            bth1CurrentLabel.setOpaque(true);
            bth1CurrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bth1CurrentLabel.setBorder(lineBorder2);
            bth1CurrentLabel.setBounds(10, 10+48,80,32);
            bth1CurrentLabel.setBackground(Color.GRAY);
            bth1TemperatureInfoBox.add(bth1CurrentLabel);

            //cr1 target temp display
            JLabel bth1TargetTemp = new JLabel("##");
            bth1TargetTemp.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TargetTemp.setOpaque(true);
            bth1TargetTemp.setBackground(Color.GRAY);
            bth1TargetTemp.setBorder(lineBorder2);
            bth1TargetTemp.setForeground(Color.RED);
            bth1TargetTemp.setBounds(100, 10,80,50);
            bth1TemperatureInfoBox.add(bth1TargetTemp);

            //current temp text
            JLabel bth1TargetTempLabel = new JLabel("Target");
            bth1TargetTempLabel.setOpaque(true);
            bth1TargetTempLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bth1TargetTempLabel.setBorder(lineBorder2);
            bth1TargetTempLabel.setBounds(100, 10+48,80,32);
            bth1TargetTempLabel.setBackground(Color.GRAY);
            bth1TemperatureInfoBox.add(bth1TargetTempLabel);

            //bth1 current conditioning display
            JLabel bth1ConditioningStatus = new JLabel("COOL");
            bth1ConditioningStatus.setHorizontalAlignment(SwingConstants.CENTER);
            bth1ConditioningStatus.setOpaque(true);
            bth1ConditioningStatus.setBackground(Color.GRAY);
            bth1ConditioningStatus.setBorder(lineBorder2);
            bth1ConditioningStatus.setForeground(Color.RED);
            bth1ConditioningStatus.setBounds(190, 10,80,80);
            bth1TemperatureInfoBox.add(bth1ConditioningStatus);





            //CR2-------------------------------------------------------------------------------------

        int cr2YLevel = 400;

        //room name label
        JLabel cr2NameLabel = createLabel("<html>Control<br>Room 2</html>", 10, cr2YLevel+25, 80, 60, lineBorder2, serif, Color.GRAY, Color.BLACK);
        frame.add(cr2NameLabel);


        //conditioning control box-------------------------------------------------------------


        //cr1 conditioning control box
        JPanel cr2ConditioningBox = createPanel(100, cr2YLevel, 280, 100, new Color(255, 101, 66), lineBorder3);
        frame.add(cr2ConditioningBox);


        //heat button
        JButton cr2HeatButton = createButton("Heat", 10, 10, 80, 80, lineBorder2, serif, Color.GRAY, new Color(163,80,43));
        cr2HeatButton.setFocusPainted(false);
        cr2ConditioningBox.add(cr2HeatButton);

        //cool button
        JButton cr2CoolButton = createButton("Cool", 100, 10, 80, 80, lineBorder2, serif, Color.GRAY, new Color(53, 43, 163));
        cr2CoolButton.setFocusPainted(false);
        cr2ConditioningBox.add(cr2CoolButton);

        //off button
        JButton cr2ConditioningOffButton = createButton("OFF", 190, 10, 80, 80, lineBorder2, serif, Color.GRAY, Color.LIGHT_GRAY);
        cr2ConditioningOffButton.setFocusPainted(false);
        cr2ConditioningBox.add(cr2ConditioningOffButton);


        //cr1 heat button action listener
        cr2HeatButton.addActionListener(_ -> {
            bms.findRoom("CR 2").setCoolHeat('h');
            cr2HeatButton.setBackground(Color.WHITE);
            cr2CoolButton.setBackground(Color.GRAY);
            cr2ConditioningOffButton.setBackground(Color.GRAY);
            System.out.println("Current st2 conditioning set to heat");
        });

        //cool button action listener
        cr2CoolButton.addActionListener(_ -> {
            bms.findRoom("CR 2").setCoolHeat('c');
            cr2HeatButton.setBackground(Color.GRAY);
            cr2CoolButton.setBackground(Color.WHITE);
            cr2ConditioningOffButton.setBackground(Color.GRAY);
            System.out.println("Current st2 conditioning set to cool ");
        });

        //off button action listener
        cr2ConditioningOffButton.addActionListener(_ -> {
            bms.findRoom("CR 2").setCoolHeat('n');
            cr2HeatButton.setBackground(Color.GRAY);
            cr2CoolButton.setBackground(Color.GRAY);
            cr2ConditioningOffButton.setBackground(Color.WHITE);
            System.out.println("Current st2 conditioning set to none");
        });























        //frame and draw shapes----------------------------------------------------
        Color mainBackgroundColor = new Color(84, 151, 167);
        frame.getContentPane().setBackground(mainBackgroundColor);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(new ShapeDrawing());
        ShapeDrawing shapes = new ShapeDrawing();
        shapes.setOpaque(false);
        shapes.setBounds(0,0,frame.getWidth(),frame.getHeight());
        frame.getContentPane().add(shapes);


        frame.setVisible(true);

    }

}

class ShapeDrawing extends JComponent {

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        try
        {

            int st1YLevel = 156;

            //first hint box, conditioning status
            g2.setColor(new Color(255, 101, 66));
            int[] x = {101, 130, 160, 101};
            int[] y = {st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            int numPoints = x.length;
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2)); //set border to 2 thickness
            g2.setColor(Color.BLACK); //set to black to draw the border
            g2.drawPolygon(x, y, numPoints); //draw the border

            //second hint box, temp display
            g2.setColor(new Color(59, 138, 81));
            x = new int[]{391, 450, 480, 391};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);

            //third hint box, temp controls
            g2.setColor(new Color(62, 94, 173));
            x = new int[]{681, 735, 765, 681};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);

            //fourth hint box, light controls
            g2.setColor(new Color(232, 207, 67));
            x = new int[]{881, 925, 955, 881};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);


            //fourth hint box, light controls
            g2.setColor(new Color(59,204,177));
            x = new int[]{1081, 1125, 1155, 1081};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);

        }
        finally
        {
            g2.dispose();
        }
    }
}
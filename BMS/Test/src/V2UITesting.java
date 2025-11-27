import jssc.SerialPortException;

import java.awt.*;
import java.awt.event.*;
import java.io.Serial;

import javax.swing.*;
import javax.swing.border.Border;


public class V2UITesting
{


    public V2UITesting(BMSMethods bms)
    {

        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);

        //set up the frame
        JFrame frame = new JFrame("Olive Building Management System");
        frame.setIconImage(new ImageIcon("Test//IglooLogo.png").getImage());

        ImageIcon onBulbOriginal = new ImageIcon("Test//OnBulb.png");
        ImageIcon onBulb = new ImageIcon(onBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        ImageIcon offBulbOriginal = new ImageIcon("Test//OffBulb.png");
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

        //all label
        JLabel allRoomLabel = new JLabel("ALL ROOMS");
        allRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        allRoomLabel.setOpaque(true);
        allRoomLabel.setBackground(Color.GRAY);
        allRoomLabel.setFont(new Font("Serif", Font.BOLD, 16));
        allRoomLabel.setBorder(lineBorder2);
        allRoomLabel.setBounds(755,allControlYLevel+25,120,40);
        frame.add(allRoomLabel);

        //all control box
        JPanel allControlBox = new JPanel();
        allControlBox.setLayout(null);
        allControlBox.setBackground(new Color(232, 207, 67));
        allControlBox.setBounds(880, allControlYLevel, 190, 100);
        allControlBox.setBorder(lineBorder3);
        frame.add(allControlBox);



            //all lights on button
            JButton allLightsOnButton = new JButton(onBulb);
            allLightsOnButton.setOpaque(true);
            allLightsOnButton.setBackground(Color.GRAY);
            allLightsOnButton.setForeground(Color.RED);
            allLightsOnButton.setBounds(10, 10,80,80);
            allControlBox.add(allLightsOnButton);

            //all lights off button
            JButton allLightsOffButton = new JButton(offBulb);
            allLightsOffButton.setOpaque(true);
            allLightsOffButton.setBackground(Color.GRAY);
            allLightsOffButton.setForeground(Color.RED);
            allLightsOffButton.setBounds(100, 10,80,80);
            allControlBox.add(allLightsOffButton);

                //all lights on button action listener
                allLightsOnButton.addActionListener(_ -> {
                    try
                    {
                        bms.relayWrite(bms.CR1_Lights, "on");//CR1_Lights = 8
                        Thread.sleep(500);
                        bms.relayWrite(bms.CR2_Lights, "on");//CR2_Lights
                        Thread.sleep(500);
                        bms.relayWrite(bms.CR3_Lights, "on");
                        //LOG IT
                    }

                    catch (SerialPortException | InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                    allLightsOnButton.setBackground(new Color(153, 144, 14));
                    allLightsOffButton.setBackground(Color.DARK_GRAY);
                    System.out.println("ALL LIGHTS ON");
                });

                //all lights off button action listener
                allLightsOffButton.addActionListener(_ -> {
                try
                {
                    bms.relayWrite(bms.CR1_Lights, "off");//CR1_Lights = 8
                    Thread.sleep(500);
                    bms.relayWrite(bms.CR2_Lights, "off");//CR2_Lights
                    Thread.sleep(500);
                    bms.relayWrite(bms.CR3_Lights, "off");
                    //LOG IT
                }
                catch (SerialPortException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                allLightsOnButton.setBackground(Color.DARK_GRAY);
                allLightsOffButton.setBackground(new Color(27, 59, 135));
                System.out.println("ALL LIGHTS OFF");
                });

        //all on/off box
        JPanel allOnOffBox = new JPanel();
        allOnOffBox.setLayout(null);
        allOnOffBox.setBackground(new Color(59,204,177));
        allOnOffBox.setBounds(1080, allControlYLevel, 190, 100);
        allOnOffBox.setBorder(lineBorder3);
        frame.add(allOnOffBox);

            //all lights on button
            JButton allAllOnButton = new JButton("STUDIO ON");
            allAllOnButton.setOpaque(true);
            allAllOnButton.setBackground(Color.GRAY);
            allAllOnButton.setForeground(Color.RED);
            allAllOnButton.setBounds(10, 10,80,80);
            allOnOffBox.add(allAllOnButton);

            //all lights off button
            JButton allAllOffButton = new JButton("TURN STUDIO OFF");
            allAllOffButton.setOpaque(true);
            allAllOffButton.setBackground(Color.GRAY);
            allAllOffButton.setForeground(Color.RED);
            allAllOffButton.setBounds(100, 10,80,80);
            allOnOffBox.add(allAllOffButton);

                //all all on button action listener
                allAllOnButton.addActionListener(_ -> {
                try
                {
                    bms.launchAll();
                    bms.setAllRoomTemps(bms.removeMRs(), 74);//set all rooms except MR to 74
                    bms.setAllRoomsRequest(bms.removeMRs(), 'c');//set all rooms except MR to cool
                    //bms.logPrint();
                }
                catch (SerialPortException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                allAllOnButton.setBackground(new Color(22, 99, 45));
                allAllOffButton.setBackground(Color.DARK_GRAY);
                System.out.println("ALL POWER OFF");
                });

                //all all off button action listener
                allAllOffButton.addActionListener(_ -> {
                try
                {
                    //studio shutdown, turn off everything, set request to none
                    bms.shutdownAll();
                    bms.setAllRoomsRequest(bms.removeMRs(), 'n');
                }
                catch (SerialPortException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                allAllOnButton.setBackground(Color.DARK_GRAY);
                allAllOffButton.setBackground(new Color(99, 22, 30));
                System.out.println("ALL POWER ON");
                });

        //all control outline box
        JPanel allControlBoxOutline = new JPanel();
        allControlBoxOutline.setLayout(null);
        allControlBoxOutline.setBounds(750, allControlYLevel-5, 525, 110);
        allControlBoxOutline.setBackground(null);
        allControlBoxOutline.setBorder(lineBorder2);
        frame.add(allControlBoxOutline);


        //studio 1--------------------------------------------------------------------------------------------------

        int cr1YLevel = 180;


        //room name label
        JLabel cr1NameLabel = new JLabel("<html>Control<br>Room 1</html>");
        cr1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cr1NameLabel.setOpaque(true);
        cr1NameLabel.setBackground(Color.GRAY);
        cr1NameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        cr1NameLabel.setBorder(lineBorder2);
        cr1NameLabel.setBounds(10,cr1YLevel+20,80,60);
        frame.add(cr1NameLabel);


        //conditioning control box-------------------------------------------------------------

        //conditioning box label
        JLabel cr1ConditioningBoxLabel = new JLabel("AC");
        cr1ConditioningBoxLabel.setForeground(Color.BLACK);
        cr1ConditioningBoxLabel.setFont(new Font("Serif", Font.BOLD, 20));
        cr1ConditioningBoxLabel.setBounds(104 , cr1YLevel-54,80,80);
        frame.add(cr1ConditioningBoxLabel);

        //cr1 conditioning control box
        JPanel cr1ConditioningBox = new JPanel();
        cr1ConditioningBox.setLayout(null);
        cr1ConditioningBox.setBackground(new Color(255, 101, 66));
        cr1ConditioningBox.setBounds(100, cr1YLevel, 280, 100);
        cr1ConditioningBox.setBorder(lineBorder3);
        frame.add(cr1ConditioningBox);


            //heat button
            JButton cr1HeatButton = new JButton("Heat");
            cr1HeatButton.setFont(new Font("Serif", Font.BOLD, 28));
            cr1HeatButton.setFocusPainted(false);
            cr1HeatButton.setOpaque(true);
            cr1HeatButton.setBorder(lineBorder2);
            cr1HeatButton.setBackground(Color.GRAY);
            cr1HeatButton.setForeground(new Color(163,80,43));
            cr1HeatButton.setBounds(10, 10,80,80);
            cr1ConditioningBox.add(cr1HeatButton);

            //cool button
            JButton cr1CoolButton = new JButton("Cool");
            cr1CoolButton.setFont(new Font("Serif", Font.BOLD, 28));
            cr1CoolButton.setFocusPainted(false);
            cr1CoolButton.setOpaque(true);
            cr1CoolButton.setBorder(lineBorder2);
            cr1CoolButton.setBackground(Color.GRAY);
            cr1CoolButton.setForeground(new Color(53, 43, 163));
            cr1CoolButton.setBounds(100, 10,80,80);
            cr1ConditioningBox.add(cr1CoolButton);

            //off button
            JButton cr1ConditioningOffButton = new JButton("OFF");
            cr1ConditioningOffButton.setFont(new Font("Serif", Font.BOLD, 28));
            cr1ConditioningOffButton.setFocusPainted(false);
            cr1ConditioningOffButton.setOpaque(true);
            cr1ConditioningOffButton.setBorder(lineBorder2);
            cr1ConditioningOffButton.setBackground(Color.GRAY);
            cr1ConditioningOffButton.setForeground(Color.LIGHT_GRAY);
            cr1ConditioningOffButton.setBounds(190, 10,80,80);
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
        JPanel cr1TemperatureInfoBox = new JPanel();
        cr1TemperatureInfoBox.setLayout(null);
        cr1TemperatureInfoBox.setBackground(new Color(59, 138, 81));
        cr1TemperatureInfoBox.setBounds(390, cr1YLevel, 280, 100);
        cr1TemperatureInfoBox.setBorder(lineBorder3);
        frame.add(cr1TemperatureInfoBox);

        //information box label
        JLabel cr1InformationBoxLabel = new JLabel("STATUS");
        cr1InformationBoxLabel.setForeground(Color.BLACK);
        cr1InformationBoxLabel.setFont(new Font("Serif", Font.BOLD, 15));
        cr1InformationBoxLabel.setBounds(396 , cr1YLevel-52,120,80);
        frame.add(cr1InformationBoxLabel);

            //current temp display
            JLabel cr1CurrentTemp = new JLabel(String.valueOf(bms.findRoom("CR 1").getCurrentTemp()));
            cr1CurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
            cr1CurrentTemp.setOpaque(true);
            cr1CurrentTemp.setBorder(lineBorder2);
            cr1CurrentTemp.setBackground(Color.GRAY);
            cr1CurrentTemp.setForeground(Color.RED);
            cr1CurrentTemp.setBounds(10, 10,80,50);
            cr1TemperatureInfoBox.add(cr1CurrentTemp);

            //current temp text
            JLabel cr1CurrentLabel = new JLabel("Current");
            cr1CurrentLabel.setOpaque(true);
            cr1CurrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cr1CurrentLabel.setBorder(lineBorder2);
            cr1CurrentLabel.setBounds(10, 10+48,80,32);
            cr1CurrentLabel.setBackground(Color.GRAY);
            cr1TemperatureInfoBox.add(cr1CurrentLabel);

            //cr1 target temp display
            JLabel cr1TargetTemp = new JLabel(String.valueOf(bms.findRoom("CR 1").getTargetTemp()));
            cr1TargetTemp.setHorizontalAlignment(SwingConstants.CENTER);
            cr1TargetTemp.setOpaque(true);
            cr1TargetTemp.setBackground(Color.GRAY);
            cr1TargetTemp.setBorder(lineBorder2);
            cr1TargetTemp.setForeground(Color.RED);
            cr1TargetTemp.setBounds(100, 10,80,50);
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
            JLabel cr1ConditioningStatus = new JLabel("COOL");
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
                bms.findRoom("CR 1").setTargetTemp(bms.findRoom("CR 1").getTargetTemp()-1);
                System.out.println("Current st1 target temperature: " + bms.findRoom("CR 1").getTargetTemp());
            });

            //cr1 temp up button action listener
            cr1TempUpButton.addActionListener(_ -> {
                bms.findRoom("CR 1").setTargetTemp(bms.findRoom("CR 1").getTargetTemp()+1);
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
        JPanel bth1ConditioningBox = new JPanel();
        bth1ConditioningBox.setLayout(null);
        bth1ConditioningBox.setBackground(new Color(255, 101, 66));
        bth1ConditioningBox.setBounds(100, bth1YLevel, 280, 100);
        bth1ConditioningBox.setBorder(lineBorder3);
        frame.add(bth1ConditioningBox);





        //booth 1 temp status box
        JPanel bth1TemperatureInfoBox = new JPanel();
        bth1TemperatureInfoBox.setLayout(null);
        bth1TemperatureInfoBox.setBackground(new Color(59, 138, 81));
        bth1TemperatureInfoBox.setBounds(390, bth1YLevel, 280, 100);
        bth1TemperatureInfoBox.setBorder(lineBorder3);
        frame.add(bth1TemperatureInfoBox);

            //current temp display
            JLabel bth1CurrentTemp = new JLabel("##");
            bth1CurrentTemp.setHorizontalAlignment(SwingConstants.CENTER);
            bth1CurrentTemp.setOpaque(true);
            bth1CurrentTemp.setBorder(lineBorder2);
            bth1CurrentTemp.setBackground(Color.GRAY);
            bth1CurrentTemp.setForeground(Color.RED);
            bth1CurrentTemp.setBounds(10, 10,80,50);
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

    public void initialization()
    {

    }

}
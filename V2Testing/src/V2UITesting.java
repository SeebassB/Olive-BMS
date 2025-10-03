import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;


public class V2UITesting
{

    private int st1CoolHeat            = -1;
    private int st1CurrentTempData     = 10;
    private int st1TargetTempData      = 10;
    private int st1LightStatus         = 0;
    int st1CurrentLightSetting = 0;
    int st1CurrentOnOff        = 0;



    public V2UITesting()
    {
        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);


        //set up the frame
        JFrame frame = new JFrame("Olive Building Management System");
        frame.setIconImage(new ImageIcon("V2Testing//IglooLogo.png").getImage());

        ImageIcon onBulbOriginal = new ImageIcon("V2Testing//OnBulb.png");
        ImageIcon onBulb = new ImageIcon(onBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        ImageIcon offBulbOriginal = new ImageIcon("V2Testing//OffBulb.png");
        ImageIcon offBulb = new ImageIcon(offBulbOriginal.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH));

        //frame attributes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when you hit the X button
        frame.setLocation(350,80); //where is the window going to open
        frame.setSize(1205, 800); //how large is the window
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
        JLabel allRoomControlLabel = new JLabel("ALL ROOMS");
        allRoomControlLabel.setHorizontalAlignment(SwingConstants.CENTER);
        allRoomControlLabel.setOpaque(true);
        allRoomControlLabel.setBackground(Color.GRAY);
        allRoomControlLabel.setFont(new Font("Serif", Font.BOLD, 16));
        allRoomControlLabel.setBorder(lineBorder2);
        allRoomControlLabel.setBounds(650,allControlYLevel+25,120,40);
        frame.add(allRoomControlLabel);

        //all control box
        JPanel allControlBox = new JPanel();
        allControlBox.setLayout(null);
        allControlBox.setBackground(new Color(232, 207, 67));
        allControlBox.setBounds(790, allControlYLevel, 190, 100);
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

                //st1 lights on button action listener
                allLightsOffButton.addActionListener(_ -> {
                st1LightStatus = 0;
                allLightsOnButton.setBackground(new Color(153, 144, 14));
                allLightsOffButton.setBackground(Color.DARK_GRAY);
                System.out.println("ALL LIGHTS OFF");
                });

                //st1 lights off button action listener
                allLightsOnButton.addActionListener(_ -> {
                st1LightStatus = 0;
                allLightsOnButton.setBackground(Color.DARK_GRAY);
                allLightsOffButton.setBackground(new Color(27, 59, 135));
                System.out.println("ALL LIGHTS ON");
                });

        //all on/off box
        JPanel allOnOffBox = new JPanel();
        allOnOffBox.setLayout(null);
        allOnOffBox.setBackground(new Color(59,204,177));
        allOnOffBox.setBounds(990, allControlYLevel, 190, 100);
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

        //all control outline box
        JPanel allControlBoxOutline = new JPanel();
        allControlBoxOutline.setLayout(null);
        allControlBoxOutline.setBounds(785, allControlYLevel-5, 400, 110);
        allControlBoxOutline.setBackground(null);
        allControlBoxOutline.setBorder(lineBorder2);
        frame.add(allControlBoxOutline);


        //studio 1--------------------------------------------------------------------------------------------------

        int st1YLevel = 180;
        int st1InternalY = 10;


        //room name label
        JLabel st1NameLabel = new JLabel("Studio 1");
        st1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        st1NameLabel.setOpaque(true);
        st1NameLabel.setBackground(Color.GRAY);
        st1NameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        st1NameLabel.setBorder(lineBorder2);
        st1NameLabel.setBounds(10,st1YLevel+30,80,40);
        frame.add(st1NameLabel);


        //status control box-------------------------------------------------------------
        JPanel st1StatusControlBox = new JPanel();
        st1StatusControlBox.setLayout(null);
        st1StatusControlBox.setBackground(new Color(255, 101, 66));
        st1StatusControlBox.setBounds(100, st1YLevel, 280, 100);
        st1StatusControlBox.setBorder(lineBorder3);
        frame.add(st1StatusControlBox);

        //status box label
        JLabel st1StatusBoxLabel = new JLabel("AC");
        st1StatusBoxLabel.setForeground(Color.BLACK);
        st1StatusBoxLabel.setFont(new Font("Serif", Font.BOLD, 20));
        st1StatusBoxLabel.setBounds(104 , st1YLevel-54,80,80);
        frame.add(st1StatusBoxLabel);

            //heat button
            JButton st1HeatButton = new JButton("Heat");
            st1HeatButton.setFont(new Font("Serif", Font.BOLD, 28));
            st1HeatButton.setFocusPainted(false);
            st1HeatButton.setOpaque(true);
            st1HeatButton.setBorder(lineBorder2);
            st1HeatButton.setBackground(Color.GRAY);
            st1HeatButton.setForeground(new Color(163,80,43));
            st1HeatButton.setBounds(10, 10,80,80);
            st1StatusControlBox.add(st1HeatButton);

            //cool button
            JButton st1CoolButton = new JButton("Cool");
            st1CoolButton.setFont(new Font("Serif", Font.BOLD, 28));
            st1CoolButton.setFocusPainted(false);
            st1CoolButton.setOpaque(true);
            st1CoolButton.setBorder(lineBorder2);
            st1CoolButton.setBackground(Color.GRAY);
            st1CoolButton.setForeground(new Color(53, 43, 163));
            st1CoolButton.setBounds(100, 10,80,80);
            st1StatusControlBox.add(st1CoolButton);

            //off button
            JButton st1ConditioningOffButton = new JButton("OFF");
            st1ConditioningOffButton.setFont(new Font("Serif", Font.BOLD, 28));
            st1ConditioningOffButton.setFocusPainted(false);
            st1ConditioningOffButton.setOpaque(true);
            st1ConditioningOffButton.setBorder(lineBorder2);
            st1ConditioningOffButton.setBackground(Color.GRAY);
            st1ConditioningOffButton.setForeground(Color.LIGHT_GRAY);
            st1ConditioningOffButton.setBounds(190, 10,80,80);
            st1StatusControlBox.add(st1ConditioningOffButton);


                //heat button action listener
                st1HeatButton.addActionListener(_ -> {
                    st1CoolHeat = 1;
                    st1HeatButton.setBackground(Color.WHITE);
                    st1CoolButton.setBackground(Color.GRAY);
                    st1ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current st1 heat setting: " + st1CoolHeat);
                });

                //cool button action listener
                st1CoolButton.addActionListener(_ -> {
                    st1CoolHeat = 0;
                    st1HeatButton.setBackground(Color.GRAY);
                    st1CoolButton.setBackground(Color.WHITE);
                    st1ConditioningOffButton.setBackground(Color.GRAY);
                    System.out.println("Current st1 heat setting: " + st1CoolHeat);
                });

                //off button action listener
                st1ConditioningOffButton.addActionListener(_ -> {
                    st1CoolHeat = -1;
                    st1HeatButton.setBackground(Color.GRAY);
                    st1CoolButton.setBackground(Color.GRAY);
                    st1ConditioningOffButton.setBackground(Color.WHITE);
                    System.out.println("Current st1 heat setting: " + st1CoolHeat);
                });




        //temp controls box----------------------------------------------------------------
        JPanel st1TempControlBox = new JPanel();
        st1TempControlBox.setLayout(null);
        st1TempControlBox.setBackground(new Color(62, 94, 173));
        st1TempControlBox.setBounds(390, st1YLevel, 190, 100);
        st1TempControlBox.setBorder(lineBorder3);
        frame.add(st1TempControlBox);

        //temp controls box label
        JLabel st1TempControlHintLabel = new JLabel("TEMP");
        st1TempControlHintLabel.setForeground(Color.BLACK);
        st1TempControlHintLabel.setFont(new Font("Serif", Font.BOLD, 20));
        st1TempControlHintLabel.setBounds(396 , st1YLevel-54,80,80);
        frame.add(st1TempControlHintLabel);


            //temp up
            JButton st1TempUpButton = new JButton("↑");
            st1TempUpButton.setFont(new Font("Serif", Font.BOLD, 60));
            st1TempUpButton.setFocusPainted(false);
            st1TempUpButton.setOpaque(true);
            st1TempUpButton.setBorder(lineBorder2);
            st1TempUpButton.setBackground(new Color(184, 94, 29));
            st1TempUpButton.setForeground(Color.ORANGE);
            st1TempUpButton.setBounds(10, 10,80,80);
            st1TempControlBox.add(st1TempUpButton);

            //temp down
            JButton st1TempDownButton = new JButton("↓");
            st1TempDownButton.setFont(new Font("Serif", Font.BOLD, 60));
            st1TempDownButton.setFocusPainted(false);
            st1TempDownButton.setOpaque(true);
            st1TempDownButton.setBorder(lineBorder2);
            st1TempDownButton.setBackground(new Color(29, 127, 184));
            st1TempDownButton.setForeground(Color.YELLOW);
            st1TempDownButton.setBounds(100, 10,80,80);
            st1TempControlBox.add(st1TempDownButton);


        //information box
        JPanel st1TemperatureInfoBox = new JPanel();
        st1TemperatureInfoBox.setLayout(null);
        st1TemperatureInfoBox.setBackground(new Color(59, 138, 81));
        st1TemperatureInfoBox.setBounds(590, st1YLevel, 190, 100);
        st1TemperatureInfoBox.setBorder(lineBorder3);
        frame.add(st1TemperatureInfoBox);

        //information box label
        JLabel st1InformationBoxLabel = new JLabel("CURRENT TEMP");
        st1InformationBoxLabel.setForeground(Color.BLACK);
        st1InformationBoxLabel.setFont(new Font("Serif", Font.BOLD, 12));
        st1InformationBoxLabel.setBounds(596 , st1YLevel-52,120,80);
        frame.add(st1InformationBoxLabel);

            //current temp display
            JLabel st1CurrentTemp = new JLabel("CURRENT TEMP");
            st1CurrentTemp.setOpaque(true);
            st1CurrentTemp.setBorder(lineBorder2);
            st1CurrentTemp.setBackground(Color.GRAY);
            st1CurrentTemp.setForeground(Color.RED);
            st1CurrentTemp.setBounds(10, st1InternalY,80,50);
            st1TemperatureInfoBox.add(st1CurrentTemp);

            //current temp text
            JLabel st1CurrentLabel = new JLabel("Current");
            st1CurrentLabel.setOpaque(true);
            st1CurrentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            st1CurrentLabel.setBorder(lineBorder2);
            st1CurrentLabel.setBounds(10, 10+48,80,32);
            st1CurrentLabel.setBackground(Color.GRAY);
            st1TemperatureInfoBox.add(st1CurrentLabel);

            //target temp display
            JLabel st1TargetTemp = new JLabel("TARGET TEMP");
            st1TargetTemp.setOpaque(true);
            st1TargetTemp.setBackground(Color.GRAY);
            st1TargetTemp.setBorder(lineBorder2);
            st1TargetTemp.setForeground(Color.RED);
            st1TargetTemp.setBounds(100, 10,80,50);
            st1TemperatureInfoBox.add(st1TargetTemp);

            //current temp text
            JLabel st1TargetTempLabel = new JLabel("Target");
            st1TargetTempLabel.setOpaque(true);
            st1TargetTempLabel.setHorizontalAlignment(SwingConstants.CENTER);
            st1TargetTempLabel.setBorder(lineBorder2);
            st1TargetTempLabel.setBounds(100, 10+48,80,32);
            st1TargetTempLabel.setBackground(Color.GRAY);
            st1TemperatureInfoBox.add(st1TargetTempLabel);

        //light box
        JPanel st1LightsBox = new JPanel();
        st1LightsBox.setLayout(null);
        st1LightsBox.setBackground(new Color(232, 207, 67));
        st1LightsBox.setBounds(790, st1YLevel, 190, 100);
        st1LightsBox.setBorder(lineBorder2);
        frame.add(st1LightsBox);

        //light box label
        JLabel st1LightBoxLabel = new JLabel("LIGHTS");
        st1LightBoxLabel.setForeground(Color.BLACK);
        st1LightBoxLabel.setFont(new Font("Serif", Font.BOLD, 12));
        st1LightBoxLabel.setBounds(796 , st1YLevel-52,120,80);
        frame.add(st1LightBoxLabel);

            //lights on button
            JButton st1LightsOnButton = new JButton(onBulb);
            st1LightsOnButton.setOpaque(true);
            st1LightsOnButton.setBackground(Color.GRAY);
            st1LightsOnButton.setForeground(Color.RED);
            st1LightsOnButton.setBounds(10, 10,80,80);
            st1LightsBox.add(st1LightsOnButton);

            //lights off button
            JButton st1LightsOffButton = new JButton(offBulb);
            st1LightsOffButton.setOpaque(true);
            st1LightsOffButton.setBackground(Color.GRAY);
            st1LightsOffButton.setForeground(Color.RED);
            st1LightsOffButton.setBounds(100, 10,80,80);
            st1LightsBox.add(st1LightsOffButton);

                //st1 lights on button action listener
                st1LightsOnButton.addActionListener(_ -> {
                st1LightStatus = 1;
                st1LightsOnButton.setBackground(new Color(153, 144, 14));
                st1LightsOffButton.setBackground(Color.DARK_GRAY);
                System.out.println("Current st1 light setting: " + st1LightStatus);
                });

                //st1 lights off button action listener
                st1LightsOffButton.addActionListener(_ -> {
                st1LightStatus = 0;
                st1LightsOnButton.setBackground(Color.DARK_GRAY);
                st1LightsOffButton.setBackground(new Color(27, 59, 135));
                System.out.println("Current st1 light setting: " + st1LightStatus);
                });

        //turn on/off box
        JPanel st1OnOffBox = new JPanel();
        st1OnOffBox.setLayout(null);
        st1OnOffBox.setBackground(new Color(59,204,177));
        st1OnOffBox.setBounds(990, st1YLevel, 190, 100);
        st1OnOffBox.setBorder(lineBorder2);
        frame.add(st1OnOffBox);

        //light box label
        JLabel st1OnOffBoxLabel = new JLabel("POWER");
        st1OnOffBoxLabel.setForeground(Color.BLACK);
        st1OnOffBoxLabel.setFont(new Font("Serif", Font.BOLD, 12));
        st1OnOffBoxLabel.setBounds(996 , st1YLevel-52,120,80);
        frame.add(st1OnOffBoxLabel);

            //lights on button
            JButton st1AllOnButton = new JButton("STUDIO ON");
            st1AllOnButton.setOpaque(true);
            st1AllOnButton.setBackground(Color.GRAY);
            st1AllOnButton.setForeground(Color.RED);
            st1AllOnButton.setBounds(10, 10,80,80);
            st1OnOffBox.add(st1AllOnButton);

            //lights off button
            JButton st1AllOffButton = new JButton("TURN STUDIO OFF");
            st1AllOffButton.setOpaque(true);
            st1AllOffButton.setBackground(Color.GRAY);
            st1AllOffButton.setForeground(Color.RED);
            st1AllOffButton.setBounds(100, 10,80,80);
            st1OnOffBox.add(st1AllOffButton);





        JButton button = new JButton("Click Me"); // Create a button with text
        button.setBounds(550, 500, 100, 40); // Set position and size



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


            //second hint box, temp controls
            g2.setColor(new Color(62, 94, 173));
            x = new int[]{391, 450, 480, 391};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);


            //third hint box, temp display
            g2.setColor(new Color(59, 138, 81));
            x = new int[]{591, 690, 720, 591};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);


            //fourth hint box, light controls
            g2.setColor(new Color(232, 207, 67));
            x = new int[]{791, 845, 875, 791};
            y = new int[]{st1YLevel, st1YLevel, st1YLevel+25, st1YLevel+25};
            g2.fillPolygon(x, y, numPoints);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawPolygon(x, y, numPoints);


            //fourth hint box, light controls
            g2.setColor(new Color(59,204,177));
            x = new int[]{991, 1045, 1075, 991};
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
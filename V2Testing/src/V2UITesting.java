import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;


public class V2UITesting
{

    private int st1CoolHeat            = -1;
    private int st1CurrentTempData     = 10;
    private int st1TargetTempData      = 10;
    int st1CurrentLightSetting = 0;
    int st1CurrentOnOff        = 0;



    public V2UITesting()
    {
        //general formatting things
        Border lineBorder3 = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border lineBorder2 = BorderFactory.createLineBorder(Color.BLACK, 2);


        //set up the frame
        JFrame frame = new JFrame("Olive Building Management System");

        //frame attributes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when you hit the X button
        frame.setLocation(350,80); //where is the window going to open
        frame.setSize(1205, 800); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);
        Color mainBackgroundColor = new Color(84, 151, 167);
        frame.getContentPane().setBackground(mainBackgroundColor);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(new ShapeDrawing());

        ShapeDrawing shapes = new ShapeDrawing();
        shapes.setOpaque(false);
        shapes.setBounds(0,0,frame.getWidth(),frame.getHeight());
        frame.getContentPane().add(shapes);


        //mouse point for me


        JLabel positionLabel = new JLabel("Move the mouse...");
        positionLabel.setBounds(10, 10, 200, 20);  // position + size
        frame.add(positionLabel);
        // Add mouse motion listener to frame’s content pane
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                positionLabel.setText("X: " + e.getX() + ", Y: " + e.getY());
            }
        });






        //title label
        JLabel titleLabel = new JLabel("Olive Building Management System");
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.GRAY);
        titleLabel.setForeground(Color.RED);
        titleLabel.setBounds(250,20,200,30);



        //studio 1--------------------------------------------------------------------------------------------------

        int st1YLevel = 90;
        int st1InternalY = 10;



        //room name label
        JLabel st1NameLabel = new JLabel("Studio 1");
        st1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        st1NameLabel.setOpaque(true);
        st1NameLabel.setBackground(Color.GRAY);
        st1NameLabel.setForeground(Color.RED);
        st1NameLabel.setBounds(10,100,80,80);
        frame.add(st1NameLabel);


        //status control box
        JPanel st1StatusControlBox = new JPanel();
        st1StatusControlBox.setLayout(null);
        st1StatusControlBox.setBackground(new Color(255, 101, 66));
        st1StatusControlBox.setBounds(100, st1YLevel, 280, 100);
        st1StatusControlBox.setBorder(lineBorder3);
        frame.add(st1StatusControlBox);

            //label
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



        //temp controls box
        JPanel st1TempControlBox = new JPanel();
        st1TempControlBox.setLayout(null);
        st1TempControlBox.setBackground(Color.BLUE);
        st1TempControlBox.setBounds(390, st1YLevel, 190, 100);
        st1TempControlBox.setBorder(lineBorder2);
        frame.add(st1TempControlBox);

            //temp up
            JButton st1TempUpButton = new JButton("TEMP UP");
            st1TempUpButton.setOpaque(true);
            st1TempUpButton.setBackground(Color.GRAY);
            st1TempUpButton.setForeground(Color.ORANGE);
            st1TempUpButton.setBounds(10, st1InternalY,80,80);
            st1TempControlBox.add(st1TempUpButton);

            //temp down
            JButton st1TempDownButton = new JButton("TEMP DOWN");
            st1TempDownButton.setOpaque(true);
            st1TempDownButton.setBackground(Color.GRAY);
            st1TempDownButton.setForeground(Color.YELLOW);
            st1TempDownButton.setBounds(100, st1InternalY,80,80);
            st1TempControlBox.add(st1TempDownButton);


        //information box
        JPanel st1TemperatureInfoBox = new JPanel();
        st1TemperatureInfoBox.setLayout(null);
        st1TemperatureInfoBox.setBackground(Color.GREEN);
        st1TemperatureInfoBox.setBounds(590, st1YLevel, 190, 100);
        st1TemperatureInfoBox.setBorder(lineBorder2);
        frame.add(st1TemperatureInfoBox);


            //current temp display
            JLabel st1CurrentTemp = new JLabel("CURRENT TEMP");
            st1CurrentTemp.setOpaque(true);
            st1CurrentTemp.setBackground(Color.GRAY);
            st1CurrentTemp.setForeground(Color.RED);
            st1CurrentTemp.setBounds(10, st1InternalY,80,80);
            st1TemperatureInfoBox.add(st1CurrentTemp);

            //target temp display
            JLabel st1TargetTemp = new JLabel("TARGET TEMP");
            st1TargetTemp.setOpaque(true);
            st1TargetTemp.setBackground(Color.GRAY);
            st1TargetTemp.setForeground(Color.RED);
            st1TargetTemp.setBounds(100, st1InternalY,80,80);
            st1TemperatureInfoBox.add(st1TargetTemp);


        //light box
        JPanel st1LightsBox = new JPanel();
        st1LightsBox.setLayout(null);
        st1LightsBox.setBackground(Color.YELLOW);
        st1LightsBox.setBounds(790, st1YLevel, 190, 100);
        st1LightsBox.setBorder(lineBorder2);
        frame.add(st1LightsBox);

            //lights on button
            JButton st1LightsOnButton = new JButton("LIGHTS ON");
            st1LightsOnButton.setOpaque(true);
            st1LightsOnButton.setBackground(Color.GRAY);
            st1LightsOnButton.setForeground(Color.RED);
            st1LightsOnButton.setBounds(10, st1InternalY,80,80);
            st1LightsBox.add(st1LightsOnButton);

            //lights off button
            JButton st1LightsOffButton = new JButton("LIGHTS OFF");
            st1LightsOffButton.setOpaque(true);
            st1LightsOffButton.setBackground(Color.GRAY);
            st1LightsOffButton.setForeground(Color.RED);
            st1LightsOffButton.setBounds(100, st1InternalY,80,80);
            st1LightsBox.add(st1LightsOffButton);

        //turn on/off box
        JPanel st1OnOffBox = new JPanel();
        st1OnOffBox.setLayout(null);
        st1OnOffBox.setBackground(Color.PINK);
        st1OnOffBox.setBounds(990, st1YLevel, 190, 100);
        st1OnOffBox.setBorder(lineBorder2);
        frame.add(st1OnOffBox);

            //lights on button
            JButton st1AllOnButton = new JButton("TURN STUDIO ON");
            st1AllOnButton.setOpaque(true);
            st1AllOnButton.setBackground(Color.GRAY);
            st1AllOnButton.setForeground(Color.RED);
            st1AllOnButton.setBounds(10, st1InternalY,80,80);
            st1OnOffBox.add(st1AllOnButton);

            //lights off button
            JButton st1AllOffButton = new JButton("TURN STUDIO OFF");
            st1AllOffButton.setOpaque(true);
            st1AllOffButton.setBackground(Color.GRAY);
            st1AllOffButton.setForeground(Color.RED);
            st1AllOffButton.setBounds(100, st1InternalY,80,80);
            st1OnOffBox.add(st1AllOffButton);





        JButton button = new JButton("Click Me"); // Create a button with text
        button.setBounds(550, 500, 100, 40); // Set position and size




        frame.add(titleLabel);
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

            //studio 1 first
            g2.setColor(Color.RED);
            int[] x = {100, 150, 190, 100};
            int[] y = {65, 65, 90, 90};
            int numPoints = x.length;
            g.drawPolygon(x, y, numPoints);
            g.fillPolygon(x, y, numPoints);

            //studio 1 second
           // g2.setColor(Color.RED);
            //int[] x = {140, 190, 230, 140};
            //int[] y = {70, 70, 90, 90};
            //int numPoints = x.length;
            //g.drawPolygon(x, y, numPoints);
            //g.fillPolygon(x, y, numPoints);



          //  g.setColor(Color.YELLOW);
           // int[] x = {400, 400, 500};
          //  int[] y = {100, 200, 200};
          //  int numPoints = 3;
          //  g.drawPolygon(x, y, numPoints);
          //  g.fillPolygon(x, y, numPoints);




        }
        finally
        {
            g2.dispose();
        }
    }
}
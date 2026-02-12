import jssc.SerialPortException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DebugGUI
{


    public DebugGUI()
    {

        JFrame frame = new JFrame("Debug");
        frame.setIconImage(new ImageIcon("BMS//Test//IglooLogo.png").getImage());
        frame.getContentPane().setLayout(null);
        frame.setLocation(400,200); //where is the window going to open
        frame.setSize(400, 300); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);


        JLabel instruction = GUIHelperMethods.createLabel("Relay Command: (relay on/off ##) ", 10, 10, 100, 20, null, null, null, null);
        frame.add(instruction);

        //text box for command input
        JTextArea commandInputArea = new JTextArea();
        commandInputArea.setBounds(10, 50, 50, 20);
        frame.add(commandInputArea);

        JTextArea commandSecondArea = new JTextArea();
        commandSecondArea.setBounds(70, 50, 50, 20);
        commandSecondArea.setText("##");
        frame.add(commandSecondArea);

        JButton commandEnterButton = GUIHelperMethods.createButton("Enter Command", 10, 100, 100, 20, null, null, Color.GRAY, Color.BLACK);
        frame.add(commandEnterButton);


            commandEnterButton.addActionListener(_ ->
            {
                BMSMethods.relayWrite(Integer.parseInt(commandInputArea.getText()), commandSecondArea.getText());
                System.out.println("Command run! "+commandInputArea.getText() +" "+ commandSecondArea.getText());
                commandInputArea.setText("");
                commandSecondArea.setText("##");
            });

        //button to stop everything

        //warning for purge cycle
        //dropdown for each hvac machine

        //text label for handy commands


        //when closing this window set the BMS back to normal
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                BMSMainController.mainStatusFlag = "normal";
                super.windowClosing(e);
            }
        });

        frame.setVisible(true);

    }

}

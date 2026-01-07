import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DebugGUI
{


    public DebugGUI(BMSMethods bms)
    {

        JFrame frame = new JFrame("Debug");
        frame.setIconImage(new ImageIcon("BMS//Test//IglooLogo.png").getImage());
        frame.getContentPane().setLayout(null);
        frame.setLocation(400,200); //where is the window going to open
        frame.setSize(400, 300); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setResizable(false);


        JLabel instruction = GUIHelperMethods.createLabel("Relay Command: ", 10, 10, 100, 20, null, null, null, null);
        frame.add(instruction);

        //text box for command input
        JTextArea commandInputArea = new JTextArea();
        commandInputArea.setBounds(10, 50, 100, 20);
        frame.add(commandInputArea);


        //button to stop everything

        //warning for purge cycle
        //dropdown for each hvac machine

        //text label for handy commands


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

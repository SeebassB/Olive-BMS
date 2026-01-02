import javax.swing.*;
import javax.swing.border.Border;

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


        JLabel instruction = GUIHelperMethods.createLabel("Relay Command: ", 10, 10, 80, 80, null, null, null, null);
        frame.add(instruction);

        //toggle button for pause

        frame.setVisible(true);

    }


}

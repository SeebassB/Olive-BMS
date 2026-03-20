import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DebugGUI
{

    public BMSMethods bms;

    public DebugGUI(BMSMethods bms)
    {
        this.bms = bms;

        JFrame frame = new JFrame("Maintenance Window");
        frame.setIconImage(new ImageIcon("BMS//Test//IglooLogo.png").getImage());
        frame.getContentPane().setLayout(null);
        frame.setLocation(400,200); //where is the window going to open
        frame.setSize(400, 300); //how large is the window
        frame.setLayout(null); // allow for absolute positioning of components
        frame.setBackground(Color.GRAY);
        frame.setResizable(false);

        JLabel title = GUIHelperMethods.createLabel("MAINTENANCE MODE", 0, 0, 350, 40, null, new Font("Ubuntu", Font.BOLD, 25), null, null);
        frame.add(title);

        JLabel titleExtras = GUIHelperMethods.createLabel("""
                        <html>This window is to help with maintenance.
                        It allows you to manually change the relays while pausing the HVAC cycles.
                         you will still be able to see the temperatures update on the main GUI.
                        SOME OF THESE MAY BE INVERTED, IDK WHICH</html>""",
                                                                0, 40, 300, 80, null, null, null, null);
        frame.add(titleExtras);


        String[] choices = {
                "21 CR1_RIGHT_SPEAKER", "22 CR1_MIDDLE_SPEAKER", "23 CR1_LEFT_SPEAKER", "24 CR1_DESK", "25 BTH1_POWER",
                "14 CR2_RIGHT_SPEAKER", "27 CR2_MIDDLE_SPEAKER", "26 CR2_LEFT_SPEAKER", "15 CR2_DESK", "16 BTH2_POWER",
                "19 CR3_RIGHT_SPEAKER", "17 CR3_MIDDLE_SPEAKER", "18 CR3_LEFT_SPEAKER", "20 CR3_DESK", "7 BTH3_POWER",
                "8 CR1_LIGHTS", "11 CR2_LIGHTS", "9 CR3_LIGHTS",
                "36 DAMP_BTH1", "37 DAMP_CR1", "38 DAMP_BATH", "39 DAMP_LOUNGE", "41 DAMP_OFFICE", "42 DAMP_CR3",
                "43 DAMP_BTH3", "44 DAMP_HALL", "45 DAMP_BTH2", "46 DAMP_CR2",
                "47 DAMP_PHONE", "48 DAMP_MR1", "49 DAMP_MR2",
                "34 DAMP_OUT_STRAIGHT", "35 DAMP_OUT_ANGLE"
        };

        JComboBox<String> combo = new JComboBox<>(choices);
        combo.setBounds(0, 120, 200, 30);
        frame.add(combo);

        String[] onOff = {"on","off"};
        JComboBox<String> comboOnOff = new JComboBox<>(onOff);
        comboOnOff.setBounds(200, 120, 60, 30);
        frame.add(comboOnOff);


        JButton commandEnterButton = GUIHelperMethods.createButton("RUN", 260, 120, 40, 30, null, null, Color.GRAY, Color.BLACK);
        frame.add(commandEnterButton);


            commandEnterButton.addActionListener(_ ->
            {
                int relayNumber = Integer.parseInt(combo.getSelectedItem().toString().substring(0,2).trim());
                String relayStateRequested = comboOnOff.getSelectedItem().toString();

                bms.relayWrite(relayNumber, relayStateRequested);
                System.out.println("Command run! Relay " + relayNumber + " " + relayStateRequested);
            });

        JLabel labelOne = GUIHelperMethods.createLabel("Machine 1", 10, 160, 80, 40, null, new Font("Ubuntu", Font.PLAIN, 12), null, Color.BLACK);
        frame.add(labelOne);

        JButton coolOne = GUIHelperMethods.createButton("COOL", 100, 160, 40, 40, null, new Font("Ubuntu", Font.PLAIN, 12), Color.GRAY, Color.BLACK);
        frame.add(coolOne);

        JButton heatOne = GUIHelperMethods.createButton("HEAT", 150, 160, 40, 40, null, new Font("Ubuntu", Font.PLAIN, 12), Color.GRAY, Color.BLACK);
        frame.add(heatOne);

        JButton offOne = GUIHelperMethods.createButton("OFF", 200, 160, 40, 40, null, new Font("Ubuntu", Font.PLAIN, 12), Color.GRAY, Color.BLACK);
        frame.add(offOne);

            //set first machine to cool
            coolOne.addActionListener(_ -> {
                bms.relayWrite(53, "off");
                bms.relayWrite(50, "on");
                System.out.println("Maintenance; setting machine 1 to cool");
            });

            //set first machine to heat
            heatOne.addActionListener(_ -> {
                bms.relayWrite(50, "off");
                bms.relayWrite(53, "on");
                System.out.println("Maintenance; setting machine 1 to heat");
            });

            //set first machine to off
            offOne.addActionListener(_ ->
            {
                bms.relayWrite(50, "off");
                bms.relayWrite(53, "off");
                System.out.println("Maintenance; setting machine 1 to off");
            });

        JLabel labelTwo = GUIHelperMethods.createLabel("Machine 2", 10, 210, 80, 40, null, new Font("Ubuntu", Font.PLAIN, 12), null, Color.BLACK);
        frame.add(labelTwo);

        JButton coolTwo = GUIHelperMethods.createButton("COOL", 100, 210, 40, 40, null, new Font("Ubuntu", Font.PLAIN, 12), Color.GRAY, Color.BLACK);
        frame.add(coolTwo);

        JButton heatTwo = GUIHelperMethods.createButton("HEAT", 150, 210, 40, 40, null, new Font("Ubuntu", Font.PLAIN, 12), Color.GRAY, Color.BLACK);
        frame.add(heatTwo);

        JButton offTwo = GUIHelperMethods.createButton("OFF", 200, 210, 40, 40, null, new Font("Ubuntu", Font.PLAIN, 12), Color.GRAY, Color.BLACK);
        frame.add(offTwo);

        //set second machine to cool
        coolTwo.addActionListener(_ -> {
            bms.relayWrite(53, "off");
            bms.relayWrite(54, "on");
            System.out.println("Maintenance; setting machine 2 to cool");
        });

        //set second machine to heat
        heatTwo.addActionListener(_ -> {
            bms.relayWrite(51, "off");
            bms.relayWrite(54, "on");
            System.out.println("Maintenance; setting machine 2 to heat");
        });

        //set second machine to off
        offTwo.addActionListener(_ ->
        {
            bms.relayWrite(51, "off");
            bms.relayWrite(54, "off");
            System.out.println("Maintenance; setting machine 2 to off");
        });





        //when closing this window set the BMS back to normal
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                BMSMainController.mainStatusFlag = "normal";
                BMSMethods.logInfo("Closing DEBUG window","IMPORTANT");
                super.windowClosing(e);
            }
        });

        frame.setVisible(true);

    }

}

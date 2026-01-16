import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

public class GUIHelperMethods
{

    /**
     * Used to make a button with passed attributes.
     * @param text the text on the button
     * @param x the x int location of the top corner of the button
     * @param y the y int location fo the top left corner of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param border the border for the button
     * @param font the font for the button
     * @param background the color of the background of the button
     * @param foreground the color of the foreground
     * @return a JButton that you made
     * */
    public static JButton createButton(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBorder(border);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setOpaque(true);

        return button;
    }

    public static JToggleButton createToggleButton(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JToggleButton tButton = new JToggleButton(text);
        tButton.setBounds(x, y, width, height);
        tButton.setBorder(border);
        tButton.setFont(font);
        tButton.setBackground(background);
        tButton.setForeground(foreground);
        tButton.setUI(new BasicToggleButtonUI());
        tButton.setHorizontalAlignment(SwingConstants.CENTER);
        tButton.setFocusPainted(false);

        return tButton;
    }


    public static JLabel createLabel(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setBorder(border);
        label.setFont(font);
        label.setBackground(background);
        label.setForeground(foreground);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);

        return label;
    }

    public static JPanel createPanel(int x, int y, int width, int height, Border border, Color background)
    {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBorder(border);
        panel.setBackground(background);
        panel.setLayout(null);


        return panel;
    }

    /**
     * Method used to update the color of the backgrounds of sets of three buttons
     * @param bms passed to give context to this method
     * @param roomIn the name of the room you need to find the CoolHeat of
     * @param x first button (COOL
     * @param y second button (HEAT)
     * @param z button (NONE)
     * */
    public static void updateRoomCoolHeatButtons(BMSMethods bms, String roomIn, JButton x, JButton y, JButton z)
    {
        //get coolHeat of the target room
        char currentlyOn = bms.findRoom(roomIn).getCoolHeat();

        //reset to gray
        x.setBackground(Color.GRAY);
        y.setBackground(Color.GRAY);
        z.setBackground(Color.GRAY);

        switch(currentlyOn)
        {
            case 'h':
                x.setBackground(Color.WHITE);
                break;

            case 'c':
                y.setBackground(Color.WHITE);
                break;

            case 'n':
                z.setBackground(Color.WHITE);
                break;
        }

    }

    public static void buttonEnabler(JToggleButton button, boolean selected, String textIn)
    {
        button.setEnabled(true);

        if (selected) {
            System.out.println("IN buttonEnabler true");
            button.setSelected(true);
            button.setBackground(V2UITesting.onColor);
        }
        else
        {
            button.setBackground(V2UITesting.offColor);
            button.setSelected(false);
        }
        button.setText(textIn);
    }

    public static void buttonDisabler(JToggleButton button)
    {
        button.setEnabled(true);
        button.setText("WORKING");
        button.setBackground(V2UITesting.disabledColor);
    }




    static class allLightsOnWorker extends SwingWorker<Void, Void>
    {
        protected Void doInBackground() throws Exception
        {

            BMSMethods.relayWrite(BMSMethods.CR1_Lights, "off");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH1_Power, "off");
            Thread.sleep(500);
            System.out.println("Studio 1 lights are on");

            BMSMethods.relayWrite(BMSMethods.CR2_Lights, "off");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH2_Power, "off");
            Thread.sleep(500);
            System.out.println("Studio 2 lights are on");

            BMSMethods.relayWrite(BMSMethods.CR3_Lights, "off");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH3_Power, "off");
            Thread.sleep(500);
            System.out.println("Studio 3 lights are on");


            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, true, "Lights");
            buttonEnabler(V2UITesting.cr1LightsButton, true, "Lights");
            buttonEnabler(V2UITesting.cr2LightsButton, true, "Lights");
            buttonEnabler(V2UITesting.cr3LightsButton, true, "Lights");

            V2UITesting.itemListenerFlag = false;

            return null;
        }

    }


    static class allLightsOffWorker extends SwingWorker<Void, Void>
    {
        protected Void doInBackground() throws InterruptedException {

            BMSMethods.relayWrite(BMSMethods.CR1_Lights, "on");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH1_Power, "on");
            Thread.sleep(500);
            System.out.println("Studio 1 lights are off");

            BMSMethods.relayWrite(BMSMethods.CR2_Lights, "on");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH2_Power, "on");
            Thread.sleep(500);
            System.out.println("Studio 2 lights are off");

            BMSMethods.relayWrite(BMSMethods.CR3_Lights, "on");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH3_Power, "on");
            Thread.sleep(500);
            System.out.println("Studio 3 lights are off");

            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, false, "Lights");
            buttonEnabler(V2UITesting.cr1LightsButton, false, "Lights");
            buttonEnabler(V2UITesting.cr2LightsButton, false, "Lights");
            buttonEnabler(V2UITesting.cr3LightsButton, false, "Lights");

            V2UITesting.itemListenerFlag = false;

            return null;
        }
    }




    static class allPowerOnWorker extends SwingWorker<Void, Void>
    {

        private final BMSMethods bms;

        public allPowerOnWorker(BMSMethods bms)
        {
            this.bms = bms;
        }

        protected Void doInBackground()
        {
            bms.launchAll();
            System.out.println("All power ON");

            //re-enable all power buttons when done
            buttonEnabler(V2UITesting.allPowerButton, true, "Power");
            buttonEnabler(V2UITesting.cr1PowerButton, true, "Power");
            buttonEnabler(V2UITesting.cr2PowerButton, true, "Power");
            buttonEnabler(V2UITesting.cr3PowerButton, true, "Power");

            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, true, "Lights");
            buttonEnabler(V2UITesting.cr1LightsButton, true, "Lights");
            buttonEnabler(V2UITesting.cr2LightsButton, true, "Lights");
            buttonEnabler(V2UITesting.cr3LightsButton, true, "Lights");

            V2UITesting.itemListenerFlag =false;

            return null;
        }

    }

    static class allPowerOffWorker extends SwingWorker<Void, Void>
    {
        private final BMSMethods bms;

        public allPowerOffWorker(BMSMethods bms)
        {
            this.bms = bms;
        }

        protected Void doInBackground()
        {
            //double check
            int confirmation = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to shutdown all rooms?", "Exit?", JOptionPane.YES_NO_OPTION);


            if(confirmation == JOptionPane.YES_OPTION )
            {
                bms.shutdownAll();
                System.out.println("All power OFF");

            }
            else
            {
                System.out.println("Shutdown AVERTED");
                V2UITesting.allPowerButton.setSelected(true);
            }

            //re-enable all power buttons when done
            buttonEnabler(V2UITesting.allPowerButton, false, "Power");
            buttonEnabler(V2UITesting.cr1PowerButton, false, "Power");
            buttonEnabler(V2UITesting.cr2PowerButton, false, "Power");
            buttonEnabler(V2UITesting.cr3PowerButton, false, "Power");

            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, false, "Lights");
            buttonEnabler(V2UITesting.cr1LightsButton, false, "Lights");
            buttonEnabler(V2UITesting.cr2LightsButton, false, "Lights");
            buttonEnabler(V2UITesting.cr3LightsButton, false, "Lights");

            V2UITesting.itemListenerFlag =false;

            return null;
        }

    }


}

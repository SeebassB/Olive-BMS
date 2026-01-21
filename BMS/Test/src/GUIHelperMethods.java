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

        if (selected)
        {
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
        button.setText("<html>Please<br>Wait</html>");
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
            buttonEnabler(V2UITesting.cr1LightsButton, true, "Lights");
            System.out.println("Studio 1 lights are on");

            BMSMethods.relayWrite(BMSMethods.CR2_Lights, "off");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH2_Power, "off");
            Thread.sleep(500);
            buttonEnabler(V2UITesting.cr2LightsButton, true, "Lights");
            System.out.println("Studio 2 lights are on");

            BMSMethods.relayWrite(BMSMethods.CR3_Lights, "off");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH3_Power, "off");
            Thread.sleep(500);
            buttonEnabler(V2UITesting.cr3LightsButton, true, "Lights");
            System.out.println("Studio 3 lights are on");


            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, true, "Lights");

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
            buttonEnabler(V2UITesting.cr1LightsButton, false, "Lights");
            System.out.println("Studio 1 lights are off");

            BMSMethods.relayWrite(BMSMethods.CR2_Lights, "on");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH2_Power, "on");
            Thread.sleep(500);
            buttonEnabler(V2UITesting.cr2LightsButton, false, "Lights");
            System.out.println("Studio 2 lights are off");

            BMSMethods.relayWrite(BMSMethods.CR3_Lights, "on");
            Thread.sleep(500);
            BMSMethods.relayWrite(BMSMethods.BTH3_Power, "on");
            Thread.sleep(500);
            buttonEnabler(V2UITesting.cr3LightsButton, false, "Lights");
            System.out.println("Studio 3 lights are off");

            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, false, "Lights");

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


            for(Room r : bms.getPrimary())
            {
                r.setTargetTemp(74.00);
                r.setRequestState('c');
            }


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
            boolean shutdownRequested = false;

            if(confirmation == JOptionPane.YES_OPTION )
            {
                bms.shutdownAll();
                System.out.println("All power OFF");

            }
            else
            {
                System.out.println("Shutdown AVERTED");
                shutdownRequested = true;
                V2UITesting.allPowerButton.setSelected(true);
            }

            //re-enable all power buttons when done
            buttonEnabler(V2UITesting.allPowerButton, shutdownRequested, "Power");
            buttonEnabler(V2UITesting.cr1PowerButton, shutdownRequested, "Power");
            buttonEnabler(V2UITesting.cr2PowerButton, shutdownRequested, "Power");
            buttonEnabler(V2UITesting.cr3PowerButton, shutdownRequested, "Power");

            //re-enable all light buttons when done
            buttonEnabler(V2UITesting.allLightsButton, shutdownRequested, "Lights");
            buttonEnabler(V2UITesting.cr1LightsButton, shutdownRequested, "Lights");
            buttonEnabler(V2UITesting.cr2LightsButton, shutdownRequested, "Lights");
            buttonEnabler(V2UITesting.cr3LightsButton, shutdownRequested, "Lights");

            V2UITesting.itemListenerFlag =false;

            for(Room r : bms.getPrimary())
            {
                r.setRequestState('n');
            }


            return null;
        }

    }

    static class singleRoomLightsWorker extends SwingWorker<Void, Void>
    {

        private final int room;
        private final boolean onOff;
        private final JToggleButton tButt;

        public singleRoomLightsWorker(int room, boolean onOff, JToggleButton tButt)
        {
            this.room = room;
            this.onOff = onOff;
            this.tButt = tButt;
        }

        protected Void doInBackground() throws InterruptedException {
            int currentRoomLights = 0;
            int boothLights       = 0;


            if (room == 1) {
                currentRoomLights = BMSMethods.CR1_Lights;
                boothLights = BMSMethods.BTH1_Power;
            } else if (room == 2) {
                currentRoomLights = BMSMethods.CR2_Lights;
                boothLights = BMSMethods.BTH2_Power;
            } else if (room == 3) {
                currentRoomLights = BMSMethods.CR3_Lights;
                boothLights = BMSMethods.BTH3_Power;
            }

            //lights on, these are inverted for some reason
            if(onOff)
            {
                BMSMethods.relayWrite(currentRoomLights, "off");
                Thread.sleep(500);
                BMSMethods.relayWrite(boothLights, "off");
            }
            //lights off
            else
            {
                BMSMethods.relayWrite(currentRoomLights, "on");
                Thread.sleep(500);
                BMSMethods.relayWrite(boothLights, "on");
            }

            buttonEnabler(tButt, onOff, "Lights");

            int runningOnCount =0;

            if(V2UITesting.cr1LightsButton.isSelected())
                runningOnCount++;
            if(V2UITesting.cr2LightsButton.isSelected())
                runningOnCount++;
            if(V2UITesting.cr3LightsButton.isSelected())
                runningOnCount++;

            System.out.println("running lights = "+runningOnCount);

            if(runningOnCount == 3)
                GUIHelperMethods.buttonEnabler(V2UITesting.allLightsButton, true, "Lights");
            else if(runningOnCount == 0)
                GUIHelperMethods.buttonEnabler(V2UITesting.allLightsButton, false, "Lights");


            V2UITesting.itemListenerFlag =false;


            return null;
        }
    }

    static class singleRoomPowerWorker extends SwingWorker<Void, Void>
    {
        private final BMSMethods bms;
        private final int room;
        private final boolean onOff;
        private final JToggleButton lightsButton;
        private final boolean lightsButtonInitialState;
        private final JToggleButton powerButton;


        public singleRoomPowerWorker(BMSMethods bms, int room, boolean onOff, JToggleButton lightsButton,boolean lightsButtonInitialState, JToggleButton powerButton)
        {
            this.bms = bms;
            this.room = room;
            this.onOff = onOff;
            this.lightsButton = lightsButton;
            this.lightsButtonInitialState = lightsButton.isSelected();
            this.powerButton = powerButton;
        }

        protected Void doInBackground()
        {

            //turn the power on
            if(onOff)
            {
                if(room == 1)
                    bms.launchStudio1();
                if(room == 2)
                    bms.launchStudio2();
                if(room == 3)
                    bms.launchStudio3();

                new singleRoomLightsWorker(room, true, lightsButton).execute();
                buttonEnabler(powerButton, true, "Power");

            }
            //power off
            else
            {
                int confirmation = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to shutdown studio " + room + "?", "Exit?", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.NO_OPTION)//check when turning off
                {
                    System.out.println("Shutdown for studio " + room + " averted");
                    buttonEnabler(lightsButton, lightsButtonInitialState, "Lights");
                    buttonEnabler(powerButton, true, "Power");
                    V2UITesting.itemListenerFlag =false;
                    return null;
                }


                if(room == 1)
                    bms.shutdownStudio1();
                if(room == 2)
                    bms.shutdownStudio2();
                if(room == 3)
                    bms.shutdownStudio3();

                new singleRoomLightsWorker(room, false, lightsButton).execute();

                int runningOnCount = 0;

                if(V2UITesting.cr1PowerButton.isSelected())
                    runningOnCount++;
                if(V2UITesting.cr2PowerButton.isSelected())
                    runningOnCount++;
                if(V2UITesting.cr3PowerButton.isSelected())
                    runningOnCount++;


                if(runningOnCount == 3)
                {
                    GUIHelperMethods.buttonEnabler(V2UITesting.allPowerButton, true, "Power");
                    GUIHelperMethods.buttonEnabler(V2UITesting.allLightsButton, true, "Lights");
                }
                else if(runningOnCount == 0)
                {
                    GUIHelperMethods.buttonEnabler(V2UITesting.allPowerButton, false, "Power");
                    GUIHelperMethods.buttonEnabler(V2UITesting.allLightsButton, false, "Lights");
                }

                buttonEnabler(powerButton, false, "Power");
                V2UITesting.itemListenerFlag =false;


            }
            return null;
        }
    }



}

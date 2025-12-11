import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GUIHelperMethods
{

    public static JButton createButton(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBorder(border);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);

        return button;
    }

    //   @overload
    public static JButton createButton(ImageIcon icon, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
    {
        JButton button = new JButton(icon);
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
    public static JLabel createLabel(String text, int x, int y, int width, int height, Border border, Font font, Color background, Color foreground)
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

    public static JPanel createPanel(int x, int y, int width, int height, Color color ,Border border)
    {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBorder(border);
        panel.setBackground(color);
        panel.setLayout(null);

        return panel;
    }

    public static JToggleButton createToggleButton(String text, int x, int y, int width, int height, Color color, Border border, Font font)
    {
        JToggleButton tButton = new JToggleButton(text);
        tButton.setBounds(x, y, width, height);
        tButton.setBackground(color);
        tButton.setFont(font);
        tButton.setBorder(border);
        tButton.setFocusPainted(false);

        return tButton;
    }

}

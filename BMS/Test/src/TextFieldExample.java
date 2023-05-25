import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;  
public class TextFieldExample implements ActionListener{  
    JTextField tf1;  
    JButton b1,b2;  
    TextFieldExample()
    {  
        JFrame f= new JFrame();  
        tf1=new JTextField("", 20);  
        tf1.setBounds(50,50,150,20);  
		tf1.setFont(new java.awt.Font("Arial", Font.ITALIC | Font.BOLD, 12));
		tf1.setToolTipText("Please enter your command here");
        b1=new JButton("OK");  
        //b1.setBounds(50,200,50,50);  
        b2=new JButton("HELP");  
       // b2.setBounds(120,200,50,50);  
        b1.addActionListener(this);  
        b2.addActionListener(this);  
		
        
        //frame stuff
		f.setLayout(new FlowLayout());
        f.setSize(500, 200);		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);		
		f.add(tf1);
        f.add(b1);
        f.add(b2);  
        f.setLayout(null);  
        f.setVisible(true);  
    }         
    public void actionPerformed(ActionEvent e) {  
       
        if(e.getSource()==b1){  
            System.out.println("but1");  
        }else if(e.getSource()==b2){  
            System.out.println("butt2");  
        }    
    }  
 } 
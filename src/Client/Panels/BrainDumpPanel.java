package Client.Panels;

import Client.Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BrainDumpPanel {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public BrainDumpPanel(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,String Username, String tasks) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        if (tasks.isEmpty()) {
            JFrame f = new JFrame("Time Boxing");
            Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
            f.setIconImage(icon);
            Container c = f.getContentPane();
            f.getContentPane().setBackground(new Color(240, 240, 240));
            JLabel label1 = new JLabel("Good Morning " + Username);
            Font font = label1.getFont();
            label1.setForeground(new Color(50, 50, 50));
            label1.setFont(new Font(font.getName(), Font.PLAIN, 40));
            JButton jButton = new JButton("Time_Box_It!");
            jButton.setForeground(Color.white);
            jButton.setBackground(new Color(70, 130, 180));
            jButton.setFocusPainted(false);
            JLabel jLabel = new JLabel("You haven't time box today tasks yet.");
            JLabel jLabel1 = new JLabel("Lets make it happen!");
            jLabel.setForeground(new Color(70, 70, 70));
            jLabel1.setForeground(new Color(70, 70, 70));
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f.dispose();
                    new BrainPanel(socket,objectOutputStream,objectInputStream,Username , "");
                }
            });
            JPanel panel = new JPanel();
            panel.setLayout(null);
            label1.setBounds(50, 30, 450, 50);
            jLabel.setBounds(50, 100, 450, 20);
            jLabel1.setBounds(50, 120, 200, 20);
            jButton.setBounds(50, 150, 150, 40);
            panel.add(label1);
            panel.add(label1);
            panel.add(jLabel);
            panel.add(jButton);
            panel.setBackground(new Color(240, 240, 240));
            panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            f.add(panel);
            f.setSize(550, 250);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } else {
            //new HomeTwoPanel();
        }
    }
}

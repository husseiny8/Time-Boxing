package Client.Panels;

import Client.Panels.BrainDumpPanel;
import Client.Panels.LoginPanel;
import Client.Panels.PriorityPanel;
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

public class BrainPanel {
    private String username;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    public BrainPanel(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,String Username, String s) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        this.username = Username;
        JFrame frame = new JFrame("Brain Dump");
        frame.getContentPane().setBackground(Color.lightGray);
        JLabel l1 = new JLabel("Step 1");
        Font font = l1.getFont();
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        frame.setIconImage(icon);
        Container c = frame.getContentPane();
        l1.setFont(new Font(font.getName(), Font.PLAIN, 40));
        l1.setPreferredSize(new Dimension(200, 70));
        JLabel l2 = new JLabel("Write any tasks you want to do, not just for today. They will be saved and displayed here every");
        JLabel label = new JLabel("time you return to this step until you choose them in step 3.");
        JLabel l3 = new JLabel("Write each task on one line");
        JButton previous = new JButton("Menu");
        previous.setForeground(Color.white);
        JButton next = new JButton("Next");
        JTextArea taskstxt = new JTextArea(s);
        l1.setBounds(10, 5, 200, 70);
        l2.setBounds(10, 70, 550, 40);
        label.setBounds(10, 90, 550, 40);
        taskstxt.setBounds(10, 120, 540, 340);
        l3.setBounds(10, 476, 200, 20);
        previous.setBounds(370, 470, 90, 30);
        previous.setBackground(Color.black);
        next.setBounds(460, 470, 90, 30);
        next.setBackground(Color.white);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new BrainDumpPanel(socket,objectOutputStream,objectInputStream,username, "");
            }
        });


        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String allTasks = taskstxt.getText();
                String[] split = allTasks.split("\n");
                String[] acceptedTasks = new String[split.length];
                int acceptedCount = 0;
                for (int i = 0; i < split.length; i++) {
                    if (!split[i].isEmpty() && split[i].length() > 6) {
                        acceptedTasks[acceptedCount++] = split[i];
                    }
                }
                String[] finalAcceptedTasks = new String[acceptedCount];
                System.arraycopy(acceptedTasks, 0, finalAcceptedTasks, 0, acceptedCount);
                if (finalAcceptedTasks.length >= 3) {
                    frame.dispose();
                    new PriorityPanel(socket,objectOutputStream,objectInputStream,Username, finalAcceptedTasks);
                } else
                    JOptionPane.showMessageDialog(null, "Enter atleast 3 Accepted task", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        taskstxt.setBorder(BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(label);
        frame.add(taskstxt);
        frame.add(previous);
        frame.add(next);
        frame.setLayout(null);
        frame.setSize(570, 550);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

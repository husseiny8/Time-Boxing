package Client.Panels;

import Server.Message.MessageType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class HomeTwoPanel extends JFrame {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private void displayNotification(String taskName) {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Task Notification");
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
            trayIcon.displayMessage("Task Reminder", "Task '" + taskName + "' has reached its end time.", TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public HomeTwoPanel(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String username, String[] all, String[] selected, String[] unselected) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        JFrame frame = new JFrame("Home Two");
        frame.getContentPane().setBackground(Color.lightGray);
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        frame.setIconImage(icon);
        Container c = frame.getContentPane();
        JLabel l1 = new JLabel("Good Morrning " + username);
        Font font = l1.getFont();
        l1.setFont(new Font(font.getName(), Font.PLAIN, 30));
        l1.setPreferredSize(new Dimension(400, 70));
        LocalDate myObj = LocalDate.now();
        JLabel l2 = new JLabel(myObj.toString());
        Font font1 = l2.getFont();
        l2.setFont(new Font(font1.getName(), Font.PLAIN, 15));
        l2.setPreferredSize(new Dimension(400, 70));
        l2.setBounds(15, 40, 500, 70);
        JButton back = new JButton("Back");
        JButton exit = new JButton("Exit");
        back.setForeground(Color.white);
        l1.setBounds(10, 5, 500, 70);
        back.setBounds(370, 470, 90, 30);
        back.setBackground(Color.black);
        if (all[0].equals("null")) {
            back.setEnabled(false);
        }
        exit.setBounds(460, 470, 90, 30);
        exit.setBackground(Color.white);


        try {
            objectOutputStream.writeObject(MessageType.HomeTwoPanel);
            objectOutputStream.flush();
            objectOutputStream.writeObject(username);
            objectOutputStream.flush();
            String[] task = (String[]) objectInputStream.readObject();
            Time[] stimes = (Time[]) objectInputStream.readObject();
            Time[] etimes = (Time[]) objectInputStream.readObject();


            for (int i = 0; i < stimes.length; i++) {
                if (stimes[i] != null) {
                    JLabel stimeLabel = new JLabel("<html><div style='display: flex; justify-content: space-between;'><div style='text-align: left;'>" + task[i] + "</div><div style='text-align: right;'>" + stimes[i].toString() + "   ----   " + etimes[i].toString() + "</div></div></html>");
                    stimeLabel.setPreferredSize(new Dimension(540, 70));

                    LocalTime currentTime = LocalTime.now();
                    if (etimes[i].toLocalTime().isBefore(currentTime)) {
                        Font font11 = stimeLabel.getFont();
                        stimeLabel.setFont(new Font(font11.getName(), Font.ROMAN_BASELINE, 15));
                        stimeLabel.setForeground(Color.darkGray);
                        stimeLabel.setBorder(new LineBorder(new Color(2, 255, 178), 5));
                    } else if (currentTime.isAfter(etimes[i].toLocalTime())) {
                        displayNotification(task[i]);
                    } else {
                        Font font11 = stimeLabel.getFont();
                        stimeLabel.setFont(new Font(font11.getName(), Font.BOLD, 15));
                        stimeLabel.setForeground(Color.BLACK);
                        stimeLabel.setBorder(new LineBorder(new Color(0, 230, 0), 5));
                    }

                    JPanel panel = new JPanel(new BorderLayout());
                    panel.add(stimeLabel, BorderLayout.CENTER);
                    panel.setBounds(10, 100 + i * 70, 540, 70);

                    frame.add(panel);
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TimingPanel(socket, objectOutputStream, objectInputStream, username, all, selected, unselected);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.add(l2);
        frame.add(l1);
        frame.add(back);
        frame.add(exit);
        frame.setSize(570, 550);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}


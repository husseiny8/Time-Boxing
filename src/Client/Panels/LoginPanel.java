package Client.Panels;


import Client.Message.MessageType;
import Client.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class LoginPanel {
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public LoginPanel(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) throws IOException {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        menu();
    }

    public void menu() {
        final JFrame jFrame = new JFrame("Time Boxing");
        jFrame.getContentPane().setBackground(Color.lightGray);
        JTextField tf1 = new JTextField();
        JTextField tf2 = new JPasswordField();
        JLabel l = new JLabel("WELCOME BACK!");
        l.setHorizontalAlignment(JLabel.CENTER);
        l.setForeground(Color.black);
        l.setBounds(205, 15, 105, 20);
        jFrame.add(l);
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        jFrame.setIconImage(icon);
        Container c = jFrame.getContentPane();
        JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(JLabel.CENTER);
        label10.setIcon(new ImageIcon("lable.png"));
        Dimension size = label10.getPreferredSize();
        label10.setBounds(215, 60, size.width, size.height);
        c.add(label10);
        JLabel jLabel = new JLabel("Username:");
        JLabel jLable1 = new JLabel("Password:");
        jLabel.setForeground(Color.black);
        jLable1.setForeground(Color.black);
        jLabel.setBounds(150, 210, 150, 20);
        jLable1.setBounds(150, 270, 150, 20);
        tf1.setBounds(150, 230, 250, 20);
        tf2.setBounds(150, 290, 250, 20);
        JButton login = new JButton("Login");
        login.setBackground(Color.white);
        login.setBounds(150, 350, 250, 40);
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
                try {
                    objectOutputStream.writeObject(MessageType.Login);
                    objectOutputStream.flush();
                    objectOutputStream.writeObject(tf1.getText());
                    objectOutputStream.flush();
                    objectOutputStream.writeObject(tf2.getText());
                    objectOutputStream.flush();
                    if ((int) objectInputStream.readObject() == MessageType.Verified) {
                        String tasks = (String) objectInputStream.readObject();
                        if (tasks.isEmpty()) {
                            new BrainDumpPanel(socket, objectOutputStream, objectInputStream, tf1.getText(), tasks);
                            jFrame.dispose();
                        } else {
                            String[] all = {"null"};
                            String[] selected = {"null"};
                            String[] unselected = {"null"};
                            jFrame.dispose();
                            new HomeTwoPanel(socket,objectOutputStream,objectInputStream,tf1.getText(),all,selected,unselected);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "User Not Found!\nTry Again", "Wrong Data", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        JButton createAccount = new JButton("Create Account");
        createAccount.setBackground(Color.black);
        createAccount.setForeground(Color.white);
        createAccount.setBounds(150, 400, 250, 40);
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
                jFrame.dispose();
                Singup();
            }
        });
        jFrame.add(tf1);
        jFrame.add(tf2);
        jFrame.add(login);
        jFrame.add(createAccount);
        jFrame.add(jLabel);
        jFrame.add(jLable1);
        jFrame.setSize(550, 550);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    public void Singup() {
        final JFrame frame = new JFrame("Time Boxing");
        frame.getContentPane().setBackground(Color.lightGray);
        JTextField passworfiled = new JTextField();
        JTextField nametxt = new JTextField();
        JTextField usernameField = new JTextField();
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        frame.setIconImage(icon);
        Container c = frame.getContentPane();
        JLabel label10 = new JLabel();
        label10.setIcon(new ImageIcon("lable.png"));
        label10.setHorizontalAlignment(JLabel.CENTER);
        Dimension size = label10.getPreferredSize();
        label10.setBounds(215, 60, size.width, size.height);
        c.add(label10);
        JButton menu = new JButton("Menu");
        menu.setForeground(Color.white);
        menu.setBackground(Color.black);
        JButton submit = new JButton("Submit");
        submit.setBackground(Color.white);
        submit.setBounds(140, 350, 250, 40);
        menu.setBounds(140, 400, 250, 40);
        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
                frame.dispose();
                try {
                    new LoginPanel(socket, objectOutputStream, objectInputStream);
                } catch (IOException e) {
                    // closeEveryThing(socket, objectInputStream, objectOutputStream);
                    throw new RuntimeException(e);
                }
            }
        });
        frame.add(menu);
        JLabel jLabel = new JLabel("WELCOME TO Time Boxing!");
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel jLabel2 = new JLabel("Username:");
        JLabel jLabel3 = new JLabel("Password:");
        JLabel jLabel5 = new JLabel("Name:");
        jLabel.setForeground(Color.black);
        jLabel2.setForeground(Color.black);
        jLabel3.setForeground(Color.black);
        jLabel5.setForeground(Color.black);

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
                try {
                    String user = usernameField.getText();
                    String pass = passworfiled.getText();
                    String name = nametxt.getText();
                    if (!user.isEmpty() && !pass.isEmpty() && !name.isEmpty()) {
                        objectOutputStream.writeObject(MessageType.SignUp);
                        objectOutputStream.flush();
                        objectOutputStream.writeObject(user);
                        objectOutputStream.flush();
                        objectOutputStream.writeObject(pass);
                        objectOutputStream.flush();
                        objectOutputStream.writeObject(name);
                        objectOutputStream.flush();
                        int v = (int) objectInputStream.readObject();
                        if (v == MessageType.Verified) {
                            frame.dispose();
                            new BrainDumpPanel(socket, objectOutputStream, objectInputStream, usernameField.getText(), "");
                        } else {
                            JOptionPane.showMessageDialog(null, "This Username is already taken!\n Try Again", "Wrong Data", JOptionPane.ERROR_MESSAGE);
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Fill All TextFiled\n Try Again", "Empty Filed", JOptionPane.ERROR_MESSAGE);
                } catch (IOException | ClassNotFoundException e) {
                    //closeEveryThing(socket, objectInputStream, objectOutputStream);
                    throw new RuntimeException(e);

                }
            }
        });
        frame.add(submit);
        jLabel.setBounds(180, 15, 170, 40);
        jLabel2.setBounds(140, 235, 250, 50);
        jLabel3.setBounds(140, 280, 70, 50);
        jLabel5.setBounds(140, 185, 70, 50);
        usernameField.setBounds(140, 275, 250, 20);
        passworfiled.setBounds(140, 315, 250, 20);
        nametxt.setBounds(140, 225, 250, 20);
        frame.add(jLabel);
        frame.add(jLabel2);
        frame.add(jLabel3);
        frame.add(nametxt);
        frame.add(jLabel5);
        frame.add(passworfiled);
        frame.add(usernameField);
        frame.setLayout(null);
        frame.setSize(550, 550);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

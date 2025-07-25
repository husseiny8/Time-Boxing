package Client.Panels;

import Client.Message.MessageType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TimingPanel extends JFrame {

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    private static int maxmin = 59;
    private static int maxh = 23;
    private static String username;
    private static String[] all;
    private static String[] selected;
    private static String[] unselected;
    private int priority = 0;


    public TimingPanel(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String username, String[] all, String[] selected, String[] unselected) {
        this.username = username;
        this.all = all;
        this.selected = selected;
        this.unselected = unselected;
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;

        String[] b = new String[selected.length + unselected.length];
        System.arraycopy(selected, 0, b, 0, selected.length);
        System.arraycopy(unselected, 0, b, selected.length, unselected.length);

        setTitle("Timing Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel borderPanel = new JPanel();
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        setIconImage(icon);
        Container c = getContentPane();
        borderPanel.setBackground(Color.lightGray);
        borderPanel.setLayout(null);
        borderPanel.setBounds(10, 100, 530, 400);
        borderPanel.setForeground(Color.black);
        JLabel stepLabel = new JLabel("Step3");
        stepLabel.setForeground(Color.black);
        Font font = stepLabel.getFont();
        stepLabel.setFont(new Font(font.getName(), Font.PLAIN, 40));
        stepLabel.setPreferredSize(new Dimension(200, 100));
        stepLabel.setBounds(10, 5, 130, 40);
        borderPanel.add(stepLabel);
        JLabel l2 = new JLabel("Choose the tasks you want to do today and allocate a time box for Each.");
        JLabel label = new JLabel("All your priorities tasks should chosen.");
        JLabel l3 = new JLabel("Click on a task to set start and end time (time box)");
        l2.setBounds(10, 45, 550, 40);
        label.setBounds(10, 65, 550, 40);
        l3.setBounds(10, 523, 300, 20);
        JButton previous = new JButton("Previous");
        previous.setForeground(Color.white);
        JButton next = new JButton("Next");
        next.setForeground(Color.black);
        previous.setBounds(346, 520, 90, 30);
        previous.setBackground(Color.black);
        next.setBounds(436, 520, 90, 30);
        next.setBackground(Color.white);
        borderPanel.add(next);
        borderPanel.add(l2);
        borderPanel.add(l3);
        borderPanel.add(label);
        borderPanel.add(previous);

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int s;
                try {
                    objectOutputStream.writeObject(MessageType.HomeTwo);
                    objectOutputStream.flush();
                    objectOutputStream.writeObject(username);
                    objectOutputStream.flush();
                    s = (int) objectInputStream.readObject();
                } catch (ClassNotFoundException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (s == MessageType.Verified) {
                    dispose();
                    new HomeTwoPanel(socket, objectOutputStream, objectInputStream, username, all, selected, unselected);
                } else {
                    JOptionPane.showMessageDialog(null, "Fill All Tasks Time", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PriorityPanel(socket, objectOutputStream, objectInputStream, username, all);
                dispose();
            }
        });
        createButtons(b, borderPanel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(borderPanel, BorderLayout.CENTER);
        setSize(550, 590);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createButtons(String[] buttonTexts, JPanel panel) {
        int buttonHeight = 400 / buttonTexts.length;

        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = new JButton(buttonTexts[i]);
            button.setBackground(Color.white);
            button.setForeground(Color.darkGray);
            int y = i * buttonHeight + 100;

            if (i < 3) {
                priority = 1;
            } else {
                priority = 2;
            }
            if (i >= 3) {
                y = y + 20;
            }
            button.setBounds(15, y, 530 - 20, buttonHeight);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            JButton source = (JButton) e.getSource();
            String task = source.getText();
            JFrame jFrame = new JFrame("Timing");
            jFrame.getContentPane().setBackground(Color.lightGray);
            Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
            jFrame.setIconImage(icon);
            Container c = jFrame.getContentPane();
            JLabel label10 = new JLabel();
            label10.setIcon(new ImageIcon("lable.png"));
            label10.setHorizontalAlignment(JLabel.CENTER);
            Dimension size = label10.getPreferredSize();
            label10.setBounds(215, 60, size.width, size.height);
            c.add(label10);
            JButton submit = new JButton("Submit");
            JButton Previous = new JButton("Previous");
            submit.setBackground(Color.white);
            Previous.setBackground(Color.black);
            Previous.setForeground(Color.white);
            JLabel start = new JLabel("Start in:");
            JLabel end = new JLabel("End in:");

            SpinnerModel value = new SpinnerNumberModel(0, //initial value
                    0, //minimum value
                    maxh, //maximum value
                    1); //step
            JSpinner starthsp = new JSpinner(value);

            starthsp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int starth = (int) starthsp.getValue();
                }
            });

            SpinnerModel value1 = new SpinnerNumberModel(0, //initial value
                    0, //minimum value
                    maxmin, //maximum value
                    1); //step
            JSpinner startmsp = new JSpinner(value1);

            startmsp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int startm = (int) startmsp.getValue();
                }
            });


            SpinnerModel value3 = new SpinnerNumberModel(0, //initial value
                    0, //minimum value
                    maxh, //maximum value
                    1); //step
            JSpinner endhsp = new JSpinner(value3);

            endhsp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int endh = (int) endhsp.getValue();
                }
            });
            SpinnerModel value11 = new SpinnerNumberModel(0, //initial value
                    0, //minimum value
                    maxmin, //maximum value
                    1); //step
            JSpinner endmsp = new JSpinner(value11);

            endmsp.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int endm = (int) endmsp.getValue();
                }
            });

            Previous.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jFrame.dispose();
                    new TimingPanel(socket, objectOutputStream, objectInputStream, username, all, selected, unselected);
                }
            });
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!starthsp.getValue().equals(0) && !startmsp.getValue().equals(null) && !endhsp.getValue().equals(0) && !endmsp.getValue().equals(null)) {
                        try {
                            objectOutputStream.writeObject(MessageType.Savetime);
                            objectOutputStream.flush();
                            objectOutputStream.writeObject((int) starthsp.getValue());
                            objectOutputStream.flush();
                            objectOutputStream.writeObject((int) startmsp.getValue());
                            objectOutputStream.flush();
                            objectOutputStream.writeObject((int) endhsp.getValue());
                            objectOutputStream.flush();
                            objectOutputStream.writeObject((int) endmsp.getValue());
                            objectOutputStream.flush();
                            objectOutputStream.writeObject(username);
                            objectOutputStream.flush();
                            objectOutputStream.writeObject(task);
                            objectOutputStream.flush();
                            int s = (int) objectInputStream.readObject();
                            if (s == MessageType.Verified) {
                                jFrame.dispose();
                                new TimingPanel(socket, objectOutputStream, objectInputStream, username, all, selected, unselected);
                            } else {
                                JOptionPane.showMessageDialog(null, "Insert corect times", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        source.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Insert corect times", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });


            Previous.setBounds(150, 400, 250, 40);
            submit.setBounds(150, 350, 250, 40);
            start.setBounds(150, 210, 150, 20);
            end.setBounds(150, 270, 150, 20);
            starthsp.setBounds(150, 230, 100, 20);
            startmsp.setBounds(260, 230, 100, 20);
            endhsp.setBounds(150, 290, 100, 20);
            endmsp.setBounds(260, 290, 100, 20);

            jFrame.add(starthsp);
            jFrame.add(startmsp);
            jFrame.add(endhsp);
            jFrame.add(endmsp);
            jFrame.add(start);
            jFrame.add(end);
            jFrame.add(submit);
            jFrame.add(Previous);
            jFrame.setSize(550, 550);
            jFrame.setLayout(null);
            jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            jFrame.setVisible(true);
        }
    }
}


package Client.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class PriorityPanel extends JFrame {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    private static final int SELECTED_COUNT_LIMIT = 3;
    private int selectedCount = 0;
    private JButton next;
    private String username;
    private String[] chekBoxLabels;
    private String[] selectedTasks;
    private String[] unselectedTasks;

    public PriorityPanel(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, String username, String[] checkBoxLabels) {
        this.chekBoxLabels = checkBoxLabels;
        this.username = username;
        this.selectedTasks = new String[SELECTED_COUNT_LIMIT];
        this.unselectedTasks = new String[checkBoxLabels.length - SELECTED_COUNT_LIMIT];
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        initUI(checkBoxLabels);
    }

    private void initUI(String[] checkBoxLabels) {
        setTitle("Priority Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        setIconImage(icon);
        Container c = getContentPane();
        JPanel borderPanel = createBorderPanel();
        setupHeader(borderPanel);
        setupCheckBoxes(checkBoxLabels, borderPanel);
        setupButtons(borderPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(borderPanel, BorderLayout.CENTER);

        setSize(550, 590);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createBorderPanel() {
        JPanel borderPanel = new JPanel();
        borderPanel.setBackground(Color.lightGray);
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBounds(10, 100, 530, 400);
        return borderPanel;
    }

    private void setHeaderLabelFont(JLabel label, int size) {
        Font font = label.getFont();
        label.setFont(new Font(font.getName(), Font.PLAIN, size));
    }

    private void setupHeader(JPanel borderPanel) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.lightGray);

        JLabel stepLabel = new JLabel("Step2");
        stepLabel.setForeground(Color.black);
        setHeaderLabelFont(stepLabel, 40);

        JLabel stepLabel1 = new JLabel("Choose the 3 most important tasks for today. This will help you maintain focus,");
        stepLabel1.setForeground(Color.black);

        JLabel stepLabel11 = new JLabel("clarity, and reduce stress by providing a roadmap for your day");
        stepLabel11.setForeground(Color.black);

        GroupLayout layout = new GroupLayout(headerPanel);
        headerPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(stepLabel)
                        .addComponent(stepLabel1)
                        .addComponent(stepLabel11)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(stepLabel)
                        .addComponent(stepLabel1)
                        .addComponent(stepLabel11)
        );

        borderPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void setupCheckBoxes(String[] checkBoxLabels, JPanel panel) {
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(checkBoxLabels.length, 1));
        for (String label : checkBoxLabels) {
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.setBackground(Color.white);
            checkBox.setForeground(Color.darkGray);
            checkBox.addActionListener(createCheckBoxActionListener());
            checkBoxPanel.add(checkBox);
        }
        panel.add(new JScrollPane(checkBoxPanel), BorderLayout.CENTER);
    }

    private ActionListener createCheckBoxActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                handleCheckBoxSelection(checkBox);
            }
        };
    }

    private void handleCheckBoxSelection(JCheckBox checkBox) {
        String task = checkBox.getText();

        if (checkBox.isSelected() && selectedCount < SELECTED_COUNT_LIMIT) {
            selectedTasks[selectedCount] = task;
            selectedCount++;

        } else {
            boolean wasSelected = Arrays.asList(selectedTasks).contains(task);

            if (wasSelected) {
                int index = Arrays.asList(selectedTasks).indexOf(task);
                if (index >= 0) {
                    selectedTasks[index] = null;
                    selectedCount--;
                }
            }
        }
       // next.setEnabled(selectedCount <= 3);
        next.setEnabled(selectedCount >= 3);
        unselectedTasks = Arrays.stream(chekBoxLabels)
                .filter(label -> !Arrays.asList(selectedTasks).contains(label))
                .toArray(String[]::new);
    }

    private void setupButtons(JPanel borderPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton previous = new JButton("Previous");
        previous.setForeground(Color.white);
        previous.setBackground(Color.black);
        previous.addActionListener(createPreviousButtonActionListener());
        buttonPanel.add(previous);
        next = new JButton("Next");
        next.setForeground(Color.black);
        next.setBackground(Color.white);
        next.setEnabled(false);
        next.addActionListener(createNextButtonActionListener());
        buttonPanel.add(next);
        borderPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public static String printAllElements(String[] array) {
        if (array.length >= 1 && array[0] != null) {
            StringBuilder result = new StringBuilder(array[0].length() * array.length);
            result.append(array[0]);
            for (int i = 1; i < array.length; i++) {
                if (array[i] != null) {
                    result.append("\n").append(array[i]);
                }
            }
            return result.toString();
        } else {
            return "";
        }
    }

    private ActionListener createPreviousButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = printAllElements(chekBoxLabels);
                new BrainPanel(socket, objectOutputStream, objectInputStream, username, s);
                dispose();
            }
        };
    }

    private ActionListener createNextButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    // Copy selected tasks to selectedTasks array
                    System.arraycopy(selectedTasks, 0, selectedTasks, 0, SELECTED_COUNT_LIMIT);

                    // Copy unselected tasks to unselectedTasks array
                    System.arraycopy(unselectedTasks, 0, unselectedTasks, 0, unselectedTasks.length);

                    // Rest of your code...
                    new TimingPanel(socket, objectOutputStream, objectInputStream, username, chekBoxLabels, selectedTasks, unselectedTasks);
                    dispose();
            }
        };
    }
}

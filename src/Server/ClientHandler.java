package Server;

import Client.Person;
import Server.Message.MessageType;

import java.io.*;
import java.util.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalTime;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                int code = (int) objectInputStream.readObject();
                answerMessage(code);
            } catch (IOException | SQLException | ClassNotFoundException e) {
                closeEveryThing(socket, objectInputStream, objectOutputStream);
            }
        }
    }

    private void closeEveryThing(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void answerMessage(int code) throws IOException, ClassNotFoundException, SQLException, RuntimeException {
        if (code == MessageType.Login) {
            String username = (String) objectInputStream.readObject();
            String password = (String) objectInputStream.readObject();
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                String url = "jdbc:mysql://localhost:3306/project4?user=root";
                Connection connect = DriverManager.getConnection(url);
                Statement state = connect.createStatement();
                String query9 = "select * from user where username='%s' and password='%s'";
                query9 = String.format(query9, username, password);
                ResultSet result = state.executeQuery(query9);
                String username1 = null;
                String password2 = null;
                String tasks = null;
                while (result.next()) {
                    username1 = result.getString(1);
                    password2 = result.getString(2);
                    tasks = result.getString(4);
                }
                state.close();
                connect.close();
                if (username1 != null && password2 != null && username1.equals(username) && password2.equals(password)) {
                    objectOutputStream.writeObject(MessageType.Verified);
                    objectOutputStream.flush();
                    if (!tasks.isEmpty()) {
                        objectOutputStream.writeObject(tasks);
                        objectOutputStream.flush();
                    } else if (tasks.isEmpty()) {
                        objectOutputStream.writeObject("");
                        objectOutputStream.flush();
                    }
                } else {
                    objectOutputStream.writeObject(0);
                    objectOutputStream.flush();
                }
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException |
                     SQLException e) {
                e.printStackTrace();
            }
        } else if (code == MessageType.SignUp) {
            String user = (String) objectInputStream.readObject();
            String pass = (String) objectInputStream.readObject();
            String name = (String) objectInputStream.readObject();
            if (tekrari(user) == MessageType.Verified) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/project4?user=root";
                Connection connection = DriverManager.getConnection(url);
                Statement statementa = connection.createStatement();
                String query = "insert into user(username,password,name,tasks) values('%s','%s','%s','%s')";
                query = String.format(query, user, pass, name, "");
                statementa.execute(query);
                statementa.close();
                connection.close();
                objectOutputStream.writeObject(MessageType.Verified);
                objectOutputStream.flush();
            } else {
                objectOutputStream.writeObject(0);
                objectOutputStream.flush();
            }
        } else if (code == MessageType.SaveTime) {
            int starth = (int) objectInputStream.readObject();
            int startm = (int) objectInputStream.readObject();
            int endh = (int) objectInputStream.readObject();
            int endm = (int) objectInputStream.readObject();

            LocalTime starttime = LocalTime.of(starth, startm);
            LocalTime endtime = LocalTime.of(endh, endm);
            java.sql.Time startti = java.sql.Time.valueOf(starttime);
            java.sql.Time endti = java.sql.Time.valueOf(endtime);
            String username = (String) objectInputStream.readObject();
            String task = (String) objectInputStream.readObject();
            if (time(starttime, endtime, username) == MessageType.Verified) {
                objectOutputStream.writeObject(MessageType.Verified);
                objectOutputStream.flush();
                String jdbcUrl = "jdbc:mysql://localhost:3306/project4?user=root";
                try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
                    String insertTaskQuery = "INSERT INTO tasks (username, task, starttime, endtime) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertTaskQuery)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, task);
                        preparedStatement.setTime(3, startti);
                        preparedStatement.setTime(4, endti);
                        preparedStatement.executeUpdate();
                    }

                    String updateQuery = "UPDATE user SET tasks = ? WHERE username = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, "full!");
                        preparedStatement.setString(2, username);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                objectOutputStream.writeObject(0);
                objectOutputStream.flush();
            }
        } else if (code == MessageType.HomeTwo) {
            String user = (String) objectInputStream.readObject();
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                String url = "jdbc:mysql://localhost:3306/project4?user=root";
                Connection connect = DriverManager.getConnection(url);
                Statement state = connect.createStatement();
                String query9 = "select * from tasks where username='%s'";
                query9 = String.format(query9, user);
                ResultSet result = state.executeQuery(query9);
                Time st = null;
                Time et = null;
                while (result.next()) {
                    st = result.getTime(3);
                    et = result.getTime(4);
                    if (st.equals(null) || et.equals(null)) {
                        objectOutputStream.writeObject(0);
                    }
                }
                state.close();
                connect.close();
                objectOutputStream.writeObject(MessageType.Verified);
            } catch (IllegalAccessException | InstantiationException | SQLException e) {
                throw new RuntimeException();
            }
        } else if (code == MessageType.HomeTwoPanel) {
            List<String> tasks = new ArrayList<>();
            List<Time> stimes = new ArrayList<>();
            List<Time> etimes = new ArrayList<>();

            String username = (String) objectInputStream.readObject();
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                String url = "jdbc:mysql://localhost:3306/project4?user=root";
                Connection connect = DriverManager.getConnection(url);
                Statement state = connect.createStatement();

                String query = "SELECT task,starttime,endtime FROM tasks WHERE username='%s'";
                query = String.format(query, username);
                ResultSet result = state.executeQuery(query);


                while (result.next()) {
                    Time dbStart = result.getTime("starttime");
                    Time dbEnd = result.getTime("endtime");
                    String task = result.getString("task");

                    if (dbStart != null && dbEnd != null) {
                        tasks.add(task);
                        stimes.add(dbStart);
                        etimes.add(dbEnd);
                    }
                }

                objectOutputStream.writeObject(tasks.toArray(new String[0]));
                objectOutputStream.flush();
                objectOutputStream.writeObject(stimes.toArray(new Time[0]));
                objectOutputStream.flush();
                objectOutputStream.writeObject(etimes.toArray(new Time[0]));
                objectOutputStream.flush();

                state.close();
                connect.close();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    private int time(LocalTime start, LocalTime end, String username) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/project4?user=root";
            Connection connect = DriverManager.getConnection(url);
            Statement state = connect.createStatement();

            String query = "SELECT starttime, endtime FROM tasks WHERE username='%s'";
            query = String.format(query, username);
            ResultSet result = state.executeQuery(query);

            while (result.next()) {
                Time dbStart = result.getTime("starttime");
                Time dbEnd = result.getTime("endtime");

                if (end.isBefore(start)) {
                    state.close();
                    connect.close();
                    return 0;
                }

                if (isTimeRangeOverlap(start, end, dbStart.toLocalTime(), dbEnd.toLocalTime())) {
                    state.close();
                    connect.close();
                    return 0;
                }
            }

            state.close();
            connect.close();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return MessageType.Verified;
    }

    private boolean isTimeRangeOverlap(LocalTime inputStart, LocalTime inputEnd, LocalTime dbStart, LocalTime dbEnd) {
        // Check if the provided time range overlaps with an existing time range
        // If inputStart is before dbEnd AND inputEnd is after dbStart, it's an overlap
        return inputStart.isBefore(dbEnd) && inputEnd.isAfter(dbStart);
    }


    public static int tekrari(String Username) {
        boolean find = false;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/project4?user=root";
            Connection connect = DriverManager.getConnection(url);
            Statement state = connect.createStatement();
            String query9 = "select * from user where username='%s'";
            query9 = String.format(query9, Username);
            ResultSet result = state.executeQuery(query9);
            String username = null;
            while (result.next()) {
                username = result.getString(1);
            }
            state.close();
            connect.close();

            if (username == null) {
                find = false;
            } else
                find = true;
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (find == true) {
            return 0;
        } else return MessageType.Verified;
    }
}
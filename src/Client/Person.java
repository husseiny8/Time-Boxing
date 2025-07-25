package Client;

import java.io.Serializable;

public class Person implements Serializable {
    private String username;
    private String password;
    private String name;
    private String[] tasks = new String[100];

    public Person(String username, String password, String name, String[] tasks) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.tasks = tasks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTasks() {
        return tasks;
    }

    public void setTasks(String[] tasks) {
        this.tasks = tasks;
    }
}

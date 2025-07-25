package Client;

import Client.Panels.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Client() throws IOException{
        this.socket = new Socket("localHost",8000);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        LoginPanel loginPanel = new LoginPanel(socket, objectOutputStream, objectInputStream);
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}
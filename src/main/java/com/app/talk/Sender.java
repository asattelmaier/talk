package com.app.talk;

import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * A simple sender of network traffic.
 */
public class Sender implements Runnable {

    /**
     * The ip-address of the other Host.
     */
    private String remoteHost;
    /**
     * The port which the addressed host listens to.
     */
    private int port;
    /**
     * A dummy Socket that represents the receiving Socket of the other Host.
     * Contains the ip-address and listening port of the other Host.
     */
    private Socket client = null;
    /**
     * A DataOutputStream containing the OutputStream of the client Socket.
     */
    private DataOutputStream outputStream = null;
    /**
     * The username of the sending host.
     */
    private String userName;
    /**
     * A scanner to receive User keyboard input
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * A sender of information over the network.
     *
     * @param remoteHost - remote machine to talk to.
     * @param port       - remote port to talk to.
     * @param userName   - username of this host.
     */
    Sender(String remoteHost, int port, String userName) {
        this.remoteHost = remoteHost;
        this.port = port;
        this.userName = userName;
    }

    /**
     * The the executing method of the class.
     * This method is being called by the start()-method of a Thread object containing
     * a Sender object to establish the outgoing connection to the other host.
     */
    public void run() {
        try {
            System.out.println("Waiting for connection to: " + this.remoteHost + ":" + this.port + "...");
            this.establishConnection();
            System.out.println("Connection established.");

            this.setOutputStream();

            this.sendUserName();

            this.sendUserInput();

            this.closeConnection();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
    }

    /**
     * A method to send the username of the sending Host.
     */
    private void sendUserName() throws IOException {
        this.sendMsg("?userName&" + this.userName);
    }

    /**
     * A method to establish the connection to another host over the network.
     */
    private void establishConnection() throws IOException {
        try {
            this.connect();
        } catch (IOException e) {
            this.reconnect();
        }
    }

    /**
     * A method that creates a dummy Socket as a receiving end for outgoing communication.
     *
     * @throws IOException IOExceptions
     */
    private void connect() throws IOException {
        this.client = new Socket(this.remoteHost, this.port);
    }

    /**
     * A method that tries to reconnect after 10 seconds
     */
    private void reconnect() throws IOException {
        try {
            TimeUnit.SECONDS.sleep(10);
            this.establishConnection();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Initializes the outputStream of the Sender object
     */
    private void setOutputStream() throws IOException {
        this.outputStream = new DataOutputStream(client.getOutputStream());
    }

    /**
     * A method that creates a loop in which the user keyboard input is being taken in and
     * sent to the other Host.
     */
    private void sendUserInput() throws IOException {
        String userInput;

        while (true) {
            userInput = scanner.nextLine();

            this.sendMsg(userInput);

            if (Objects.equals("exit.", userInput)) {
                break;
            }
        }
    }

    /**
     * A method that receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param msg - message that should be sent to the other host.
     */
    private void sendMsg(String msg) throws IOException {
        this.outputStream.writeBytes(msg + "\n");
        this.outputStream.flush();
    }

    /**
     * Closes the client Socket as well as its OutputStream.
     */
    private void closeConnection() throws IOException {
        this.client.close(); //closes OutputStream as well
        this.scanner.close();
        System.exit(0);
    }
}

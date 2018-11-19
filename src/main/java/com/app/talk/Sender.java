package com.app.talk;

import com.app.talk.command.set.ExitCommand;
import com.app.talk.command.set.MessageCommand;
import com.app.talk.common.Config;
import com.app.talk.common.User;

import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.app.talk.common.SystemExitCode.ABORT;
import static com.app.talk.common.SystemExitCode.NORMAL;

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
    private ObjectOutputStream outputStream = null;
    /**
     * The username of the sending host.
     */
    private User user;
    /**
     * A scanner to receive User keyboard input
     */
    private Scanner scanner = new Scanner(System.in);


    /**
     * A sender of information over the network.
     *
     * @param config Configuration information
     * @param user   User object with user information
     */
    public Sender(Config config, User user) {
        this.remoteHost = config.getRemoteHost();
        this.port = config.getTalkPort();
        this.user = user;
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

            this.sendUser();

            this.sendUserInput();

            this.closeConnection();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("IOException: " + e);
            System.exit(ABORT.ordinal());
        }
    }

    /**
     * A method to send the username of the sending Host.
     */
    private void sendUser() throws IOException {
        send(this.user);
    }

    /**
     * A method to establish the connection to another host over the network.
     */
    private void establishConnection() throws IOException {
        try {
            this.connect();
        } catch (ConnectException e) {
            this.reconnect();
        }
    }

    /**
     * A method that creates a dummy Socket as a receiving end for outgoing communication.
     *
     * @throws IOException IOExceptions
     */
    void connect() throws IOException {
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
            System.exit(ABORT.ordinal());
        }
    }

    /**
     * Initializes the outputStream of the Sender object
     */
    private void setOutputStream() throws IOException {
        this.outputStream = new ObjectOutputStream(client.getOutputStream());
    }

    /**
     * A method that creates a loop in which the user keyboard input is being taken in and
     * sent to the other Host.
     */
    private void sendUserInput() throws IOException {
        String userInput = "";
        ExitCommand exitCommand = new ExitCommand();
        boolean userExits = Objects.equals("exit.", userInput);

        while (true) {
            userInput = scanner.nextLine();

            if (userExits) {
                send(exitCommand);
                break;
            } else {
                this.sendMessage(userInput);
            }
        }
    }

    /**
     * A method that receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param message - message that should be sent to the other host.
     */
    private void sendMessage(String message) throws IOException {
        MessageCommand messageCommand = new MessageCommand(message);
        send(messageCommand);
    }

    /**
     * Sends a object to the output stream.
     *
     * @param object the object to send for.
     * @throws IOException throws an IO Exception
     */
    private void send(Object object) throws IOException {
        this.outputStream.writeObject(object);
        this.outputStream.flush();
    }

    /**
     * Closes the client Socket as well as its OutputStream.
     */
    private void closeConnection() throws IOException {
        this.client.close(); //closes OutputStream as well
        this.scanner.close();
        System.exit(NORMAL.ordinal());
    }
}

package com.app.talk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple receiver of network traffic.
 */
public class Receiver implements Runnable {
	/**
	 * A ServerSockets that serves as the endingpoint of Communication for 
	 * the other Host.
	 */
    private ServerSocket serverSocket;
    /**
     * A buffered Reader for incoming messages.
     */
    private BufferedReader inputReader = null;
    /**
     * The username of the other Host.
     */
    private String remoteUserName = null;

    /**
     * A main.java.Receiver of information from the network.
     *
     * @param port - to listen to.
     */
    Receiver(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
    /**
     * The the executing method of the class.
     * This method is being called by the start()-method of a Thread object containing 
     * a Receiver object to establish the incoming connection from the other host.
     */
    public void run() {
        Socket clientSocket;

        try {
            clientSocket = serverSocket.accept();
            this.inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.receive();
            this.closeConnection();
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
    /**
     * Creates a loop in which the incoming messages are printed to the console.
     * If the incoming message contains "exit." gets an information that the other user
     * disconnected.
     * @throws IOException IOExceptions 
     */
    private void receive() throws IOException {
        String response;

        while ((response = inputReader.readLine()) != null) {
            if (response.contains("?userName&")) {
                this.setRemoteUserName(response);
                System.out.println("New user connected: " + remoteUserName);
            } else {
                System.out.println(remoteUserName + ": " + response);
            }

            if (response.contains("exit.")) {
                System.out.println("User disconnected: " + remoteUserName);
                this.closeConnection();
                break;
            }
        }
    }
    /**
     * A simple setter for the remoteUserName
     * @param response - the username gotten from the InputStream.
     */
    private void setRemoteUserName(String response) {
        String[] splitResponse = response.split("&");
        this.remoteUserName = splitResponse[1];
    }
    /**
     * Closes the inputReader, as well as the serverSocket from the Receiver object.
     * @throws IOException IOExceptions 
     */
    private void closeConnection() throws IOException {
        this.inputReader.close();
        this.serverSocket.close();
        System.exit(0);
    }
}

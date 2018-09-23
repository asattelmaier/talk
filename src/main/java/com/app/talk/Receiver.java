package com.app.talk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A simple receiver of network traffic.
 */
public class Receiver extends Thread {

    private ServerSocket serverSocket;
    private BufferedReader inputReader = null;
    private String remoteUserName = null;

    /**
     * A main.java.Receiver of information from the network.
     * @param port - to listen to.
     */
    Receiver(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    public void run() {
        Socket clientSocket;

        try {
            clientSocket = serverSocket.accept();
            this.inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

        this.receive();
    }

    private void receive() {
        try {
            this.printResponse();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void printResponse() throws IOException {
        String response;

        while ((response = inputReader.readLine()) != null) {
            if (response.contains("?userName&")) {
                this.setRemoteUserName(response);
                System.out.println("New user connected: " + remoteUserName);
            } else {
                System.out.println(remoteUserName + ": " + response);
            }

            if (response.contains("end")) {
                System.out.println("User disconnected: " + remoteUserName);
                break;
            }
        }
    }

    private void setRemoteUserName(String response) {
        String[] splitResponse = response.split("&");
        this.remoteUserName = splitResponse[1];
    }
}

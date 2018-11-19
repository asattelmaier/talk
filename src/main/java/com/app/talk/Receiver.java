package com.app.talk;

import com.app.talk.common.Config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.app.talk.command.RemoteCommand;
import com.app.talk.common.User;

import static com.app.talk.common.SystemExitCode.ABORT;
import static com.app.talk.common.SystemExitCode.NORMAL;

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
    private ObjectInputStream input = null;
    /**
     * The username of the other Host.
     */
    private String remoteUserName = null;

    /**
     * A main.java.Receiver of information from the network.
     *
     * @param config - configuration object.
     */
    Receiver(Config config) {
        try {
            serverSocket = new ServerSocket(config.getListenPort());
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
            System.exit(ABORT.ordinal());
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
            this.input = new ObjectInputStream(clientSocket.getInputStream());
            this.getUserName();
            this.receive();
            this.closeConnection();
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
            System.exit(ABORT.ordinal());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(ABORT.ordinal());
        }
    }

    private void getUserName() throws IOException, ClassNotFoundException {
        User user = (User) this.input.readObject();

        this.setRemoteUserName(user.getName());
    }

    /**
     * Creates a loop in which the incoming messages are printed to the console.
     * If the incoming message contains "exit." gets an information that the other user
     * disconnected.
     *
     * @throws IOException IOExceptions
     */
    private void receive() throws IOException, ClassNotFoundException {
        RemoteCommand response;

        while ((response = (RemoteCommand) input.readObject()) != null) {
            System.out.print(this.remoteUserName + ": ");

            response.execute();
        }
    }

    /**
     * A simple setter for the remoteUserName
     *
     * @param userName - the username gotten from the InputStream.
     */
    private void setRemoteUserName(String userName) {
        this.remoteUserName = userName;
    }

    /**
     * Closes the input, as well as the serverSocket from the Receiver object.
     *
     * @throws IOException IOExceptions
     */
    private void closeConnection() throws IOException {
        this.input.close();
        this.serverSocket.close();
        System.exit(NORMAL.ordinal());
    }
}

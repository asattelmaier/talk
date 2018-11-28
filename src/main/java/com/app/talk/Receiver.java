package com.app.talk;

import com.app.talk.command.RemoteCommand;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import static com.app.talk.common.SystemExitCode.ABORT;
import static com.app.talk.common.SystemExitCode.NORMAL;

/**
 * A simple receiver of network traffic.
 */
public class Receiver implements Runnable {
    /**
     * A buffered Reader for incoming messages.
     */
    private ObjectInputStream input = null;
    /**
     * The communicators socket.
     */
    private Socket socket;

    /**
     * A main.java.Receiver of information from the network.
     *
     * @param socket - configuration object.
     */
    public Receiver(Socket socket) {
        this.socket = socket;
    } //constructor

    /**
     * The the executing method of the class.
     * This method is being called by the start()-method of a Thread object containing
     * a Receiver object to establish the incoming connection from the other host.
     */
    public void run() {

        try {
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.receive();
            this.closeConnection();
        } catch (IOException e) {
        	e.printStackTrace();
            System.exit(ABORT.ordinal());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(ABORT.ordinal());
        } //try-ctach
    } //run

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
            response.execute();
        } //while
    } //receive

    /**
     * Closes the input, as well as the serverSocket from the Receiver object.
     *
     * @throws IOException IOExceptions
     */
    private void closeConnection() throws IOException {
        this.input.close();
        System.exit(NORMAL.ordinal());
    } //closeConnection
}

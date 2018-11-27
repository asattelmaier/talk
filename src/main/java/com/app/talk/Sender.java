package com.app.talk;

import static com.app.talk.common.SystemExitCode.ABORT;
import static com.app.talk.common.SystemExitCode.NORMAL;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import com.app.talk.client.command.set.MessageCommand;

/**
 * A simple sender of network traffic.
 */
public class Sender implements Runnable {
    /**
     * A dummy Socket that represents the receiving Socket of the other Host.
     * Contains the ip-address and listening port of the other Host.
     */
    //private Socket socket = null;
    /**
     * A DataOutputStream containing the OutputStream of the client Socket.
     */
    private ObjectOutputStream outputStream = null;
    /**
     * A scanner to receive User keyboard input
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * The communicators socket.
     */
    private Socket socket;
    
    private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<Object>();

    /**
     * A sender of information over the network.
     *
     * @param socket
     */
    public Sender(Socket socket) throws IOException {
        this.socket = socket;
    }

    /**
     * The the executing method of the class.
     * This method is being called by the start()-method of a Thread object containing
     * a Sender object to establish the outgoing connection to the other host.
     */
    public void run() {
        try {
        	System.out.println("Connection established to remote " + this.socket.getInetAddress() + ":" + this.socket.getPort() + " from local address " + this.socket.getLocalAddress() + ":" + this.socket.getLocalPort());

            this.setOutputStream();
                        
            while(!Thread.currentThread().isInterrupted()) {
            	Object object = commandQueue.take();
            	this.outputStream.writeObject(object);
                this.outputStream.flush();
            } //while
            this.closeConnection();
            System.out.println("Connection closed.");
        } catch (IOException e) {
        	e.printStackTrace();
            System.exit(ABORT.ordinal());
        } catch (InterruptedException e) {
			e.printStackTrace();
		} //try-catch
    } //run

    /**
     * Initializes the outputStream of the Sender object
     */
    private void setOutputStream() throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * A method that receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param message - message that should be sent to the other host.
     */
    public void sendMessage(String message) throws IOException {
        MessageCommand messageCommand = new MessageCommand("[" + TalkClient.user.getName() + "]: " + message);
        send(messageCommand);
    }

    /**
     * Sends a object to the output stream.
     *
     * @param object the object to send for.
     * @throws IOException throws an IO Exception
     */
    void send(Object object) throws IOException {
    	commandQueue.offer(object);
    }

    /**
     * Closes the client Socket as well as its OutputStream.
     */
    private void closeConnection() throws IOException {
        this.socket.close(); //closes OutputStream as well
        this.scanner.close();
        System.exit(NORMAL.ordinal());
    }
} //Sender Class

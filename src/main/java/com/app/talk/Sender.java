package com.app.talk;

import com.app.talk.client.command.set.MessageCommand;
import com.app.talk.command.RemoteCommand;
import com.app.talk.server.command.set.ExitCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.app.talk.common.SystemExitCode.ABORT;
import static com.app.talk.common.SystemExitCode.NORMAL;

/**
 * A simple sender of network traffic.
 */
public class Sender implements Runnable {
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
            System.out.println("Waiting for connection to: " + this.socket.getInetAddress() + ":" + this.socket.getPort() + "...");
            this.establishConnection();
            System.out.println("Connection established to remote " + this.socket.getInetAddress() + ":" + this.socket.getPort() + " from local address " + this.socket.getLocalAddress() + ":" + this.socket.getLocalPort());

            this.setOutputStream();
            
            
            while(true) {
            	Object object = commandQueue.take();
            	this.outputStream.writeObject(object);
                this.outputStream.flush();
            	
            	if(false) {
            		break;
            	}
            }
            
            this.closeConnection();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("IOException: " + e);
            System.exit(ABORT.ordinal());
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        this.client = this.socket;
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
        this.client.close(); //closes OutputStream as well
        this.scanner.close();
        System.exit(NORMAL.ordinal());
    }
}

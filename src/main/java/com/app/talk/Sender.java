package com.app.talk;

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
     * A DataOutputStream containing the OutputStream of the client Socket.
     */
    private ObjectOutputStream outputStream = null;
    /**
     * A scanner to receive User keyboard input
     */
    private Scanner scanner = new Scanner(System.in);
    /**
     * once initialized, establishes connection.
     */
    private Socket socket;
    /**
     * queues the given commands.
     */
    private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<Object>();
        
    /**
     * A sender of information over the network.
     *
     * @param socket
     */
    public Sender(Socket socket) throws IOException {
        this.socket = socket;
    } //constructor

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
            System.out.println("Connection closed.");
        } catch (IOException e) {
        	// This is fine - Server realizes that the client socket is gone
        } catch (InterruptedException e) {
			// This is fine - Thread is interrupted and should be gone
		} 
		
    } //run

    /**
     * initializes the OutputStream of the Sender object
     */
    private void setOutputStream() throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    } //setOutputStream

    /**
     * receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param message - message that should be sent to the other host.
     */
    public void sendMessage(String message) throws IOException {
        MessageCommand messageCommand = new MessageCommand("[" + TalkClient.user.getName() + "]: " + message);
        send(messageCommand);
    } //sendMessage

    /**
     * sends a object to the outputstream.
     *
     * @param object the object to send for.
     * @throws IOException throws an IO Exception
     */
    void send(Object object) throws IOException {
    	commandQueue.offer(object);
    } //send

    /**
     * closes the client Socket as well as its OutputStream.
     */
    public void closeConnection() throws IOException {
    	this.socket.close(); //closes OutputStream as well
        this.scanner.close();
        System.exit(NORMAL.ordinal());
    } //closeConnection
} //Sender Class

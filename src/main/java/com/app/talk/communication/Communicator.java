package com.app.talk.communication;

import com.app.talk.Receiver;
import com.app.talk.Sender;

import java.io.IOException;
import java.net.Socket;

/**
 * A combination of a sender and a receiver threads.
 */
public class Communicator {
	
	private Socket socket;
	private Sender sender;
	private Receiver receiver;

	/**
	 * The constructor creates and activates the two threads. One for the sender (+ given user name), one for the receiver
	 * 
	 * @param socket
	 */
	Communicator(Socket socket) throws IOException {
		this.socket = socket;
		this.start();
	} //constructor
	/**
	 * fetches sender.
	 * @return sender object.
	 */
	public Sender getSender() {
		return sender;
	} 
	/**
	 * fetches receiver.
	 * @return receiver object.
	 */
	public Receiver getReceiver() {
		return receiver;
	} 
	
    /**
     * Creates a Sender and a Receiver object.
     */
    private void start() throws IOException {
    	System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());
    	this.receiver = new Receiver(this.socket);
        Thread receiverThread = new Thread(receiver);

        this.sender = new Sender(this.socket);
        Thread senderThread = new Thread(sender);

        receiverThread.start();
        senderThread.start();
    } //start    
} //Communicator Class

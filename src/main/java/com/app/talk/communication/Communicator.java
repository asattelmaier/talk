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

	/**
	 * The constructor creates and activates the two threads. One for the sender (+ given user name), one for the receiver
	 * 
	 * @param socket
	 */
	public Communicator(Socket socket) throws IOException {
		this.socket = socket;
		this.start();
	}
	
    /**
     * Creates a Sender and a Receiver object.
     */
    private void start() throws IOException {
        Receiver receiver = new Receiver(this.socket);
        Thread receiverThread = new Thread(receiver);

        Sender sender = new Sender(this.socket);
        Thread senderThread = new Thread(sender);

        receiverThread.start();
        senderThread.start();
    }
}

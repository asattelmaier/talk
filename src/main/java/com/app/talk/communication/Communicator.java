package com.app.talk.communication;

import static com.app.talk.common.SystemExitCode.ABORT;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.app.talk.Receiver;
import com.app.talk.Sender;
import com.app.talk.common.Config;
import com.app.talk.common.User;

/**
 * A combination of a sender and a receiver threads.
 */
public class Communicator {
	
	private Config config;
	
	private User user;
	
	/**
	 * The constructor creates and activates the two threads. One for the sender (+ given user name), one for the receiver
	 * 
	 * @param config
	 * @param user
	 */
	public Communicator(Config config, User user) {
		this.config = config;
		this.user = user;
		this.start();
	}
	
    /**
     * Creates a Sender and a Receiver object.
     */
    private void start() {
        Receiver receiver = new Receiver(this.config);
        Thread receiverThread = new Thread(receiver);

        Sender sender = new Sender(this.config, this.user);
        Thread senderThread = new Thread(sender);

        receiverThread.start();
        senderThread.start();

        try {
            senderThread.join();
        } catch (InterruptedException e) {
            System.out.print(e.getMessage());
        }
    }
}

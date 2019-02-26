package com.app.talk.communication;

import com.app.talk.Receiver;
import com.app.talk.Sender;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;
import com.app.talk.command.RemoteCommandProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A combination of a sender and a receiver threads.
 */
public class Communicator {
	Context context;
	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private RemoteCommandProcessor commandProcessor;
	private Thread senderThread;
	private Thread receiverThread;
	private Thread commandProcessorThread;

	private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<Object>();

	/**
	 * The constructor creates and activates the two threads. One for the sender
	 * (+ given user name), one for the receiver
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public Communicator(Socket socket) throws IOException {
		this.socket = socket;
		this.init();
	}

	/**
	 * Fetches socket.
	 * 
	 * @return Socket object.
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Fetches sender.
	 * 
	 * @return Sender object.
	 */
	public Sender getSender() {
		return sender;
	}

	/**
	 * @return the context.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Fetches receiver.
	 * 
	 * @return receiver object.
	 */
	public Receiver getReceiver() {
		return receiver;
	}

	/**
	 * Fetches sender thread.
	 * 
	 * @return thread object.
	 */
	public Thread getSenderThread() {
		return senderThread;
	}

	/**
	 * Creates a Sender and a Receiver object.
	 * 
	 * @throws IOException
	 */
	private void init() throws IOException {
		if (this.socket == null)
			return;

		System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());

		this.sender = new Sender(this.socket, commandQueue);
		senderThread = new Thread(sender);

		this.receiver = new Receiver(this.socket);
		receiverThread = new Thread(receiver);

		receiverThread.setName(this.socket.getLocalPort() + " -> " + this.socket.getPort() + "-Receiver");
		senderThread.setName(this.socket.getLocalPort() + " -> " + this.socket.getPort() + "-Sender");

	}

	/**
	 * Initialised the RemoteCommandProcessor and starts the threads.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		initCommandProcessor();
		commandProcessorThread.start();
		receiverThread.start();
		senderThread.start();
	}

	/**
	 * Initialisation of the RemoteCommandProcessor.
	 */
	private void initCommandProcessor() {
		commandProcessor = new RemoteCommandProcessor(this.context);
		commandProcessorThread = new Thread(commandProcessor);
		Observer observer = new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				RemoteCommand remoteCommand = (RemoteCommand) arg;
				try {
					commandProcessor.put(remoteCommand);
				} catch (InterruptedException e) {
					e.printStackTrace();
					// This is ok
				}
			}
		};

		this.receiver.addObserver(observer);
	}

	/**
	 * 
	 * @param heartbeat
	 *            The timeout to set.
	 */
	void setHeartbeat(RemoteCommand heartbeat) {
		this.sender.setHeartbeat(heartbeat);
	}

	/**
	 * @param timeout
	 *            The timeout to set.
	 */
	void setHeartbeatTimeout(long timeout) {
		this.sender.setTimeout(timeout);
	}

	/**
	 * Closes the socket and the open threads.
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		senderThread.interrupt();
		commandProcessorThread.interrupt();
	}

	/**
	 * Sets the context.
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;

	}

	/**
	 * Sends a object to the output stream.
	 *
	 * @param object
	 *            The object to send for.
	 * @throws IOException
	 *             Throws an IO Exception.
	 */
	public void send(Object object) {
		commandQueue.offer(object);
	}
}

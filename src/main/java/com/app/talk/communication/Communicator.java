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
	public Context context;
	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private RemoteCommandProcessor commandProcessor;
	private Thread senderThread;
	private Thread receiverThread;
	private Thread commandProcessorThread;

	private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<Object>();

	public Communicator(Socket socket) {
		this.socket = socket;
		this.init();
	}

	public Context getContext() {
		return context;
	}

	private void init() {
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

	public void start() throws IOException {
		initCommandProcessor();
		commandProcessorThread.start();
		receiverThread.start();
		senderThread.start();
	}

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

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		senderThread.interrupt();
		commandProcessorThread.interrupt();
	}

	public void setContext(Context context) {
		this.context = context;

	}

	public void send(Object object) {
		commandQueue.offer(object);
	}
}

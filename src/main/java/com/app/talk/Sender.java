package com.app.talk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.app.talk.command.RemoteCommand;

/**
 * A simple sender of network traffic.
 */
public class Sender implements Runnable {
	private final ObjectOutputStream outputStream;
	private Socket socket;
	private LinkedBlockingQueue<Object> commandQueue;
	private long timeout;
	private RemoteCommand heartbeat;

	/**
	 * A sender of information over the network.
	 * 
	 * @param socket
	 * @param commandQueue
	 * @throws IOException
	 */
	public Sender(Socket socket, LinkedBlockingQueue<Object> commandQueue) throws IOException {
		this.socket = socket;
		this.commandQueue = commandQueue;
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		this.outputStream.flush();
	}

	/**
	 * An endless loop checking comandQueue end sending RemoteCommands.
	 */
	public void run() {
		try {
			System.out.println("Connection established to remote " + this.socket.getInetAddress() + ":"
					+ this.socket.getPort() + " from local address " + this.socket.getLocalAddress() + ":"
					+ this.socket.getLocalPort());

			while (!Thread.currentThread().isInterrupted()) {
				Object object = commandQueue.poll(timeout, TimeUnit.MILLISECONDS);
				if (object == null) {
					object = heartbeat;
				}
				this.outputStream.writeObject(object);
				this.outputStream.flush();
			}
			System.out.println("Connection closed.");
		} catch (IOException e) {
			// This is fine - Server realizes that the client socket is gone
		} catch (InterruptedException e) {
			// This is fine - Thread is interrupted and should be gone
		}
	}

	/**
	 * 
	 * @param heartbeat
	 *            The timeout to set.
	 */
	public void setHeartbeat(RemoteCommand heartbeat) {
		this.heartbeat = heartbeat;
	}

	/**
	 * @param timeout
	 *            The timeout to set.
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}

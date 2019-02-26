package com.app.talk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.app.talk.command.HeartbeatCommand;
import com.app.talk.command.RemoteCommand;

public class Sender implements Runnable {
	private ObjectOutputStream outputStream;
	private Socket socket;
	private LinkedBlockingQueue<Object> commandQueue;

	public Sender(Socket socket, LinkedBlockingQueue<Object> commandQueue) {
		this.socket = socket;
		this.commandQueue = commandQueue;
		try {
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println("Connection established to remote " + this.socket.getInetAddress() + ":"
					+ this.socket.getPort() + " from local address " + this.socket.getLocalAddress() + ":"
					+ this.socket.getLocalPort());

			while (!Thread.currentThread().isInterrupted()) {
				Object object = commandQueue.poll(60000, TimeUnit.MILLISECONDS);
				if (object == null) {
					object = new HeartbeatCommand();
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
}

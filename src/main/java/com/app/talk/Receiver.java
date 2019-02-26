package com.app.talk;

import static com.app.talk.common.SystemExitCode.ABORT;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import com.app.talk.command.RemoteCommand;

/**
 * A simple receiver of network traffic.
 */
public class Receiver extends Observable implements Runnable {
	private ObjectInputStream input;
	private Socket socket;

	public Receiver(Socket socket) {
		this.socket = socket;
		InputStream in = null;
		try {
			in = this.socket.getInputStream();
			this.input = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			this.receive();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(ABORT.ordinal());
		}
	}

	private void receive() throws IOException, ClassNotFoundException {
		Object response;

		try {
			while ((response = read()) != null) {
				setChanged();
				notifyObservers(response);
			}
		} catch (EOFException e) {
			// This is fine - nothing more to read
		} catch (SocketException e) {
			// client socket closed
		} finally {
			this.closeConnection();
		}
	}

	private void closeConnection() throws IOException {
		this.input.close();
	}

	private Object read() throws ClassNotFoundException, IOException {
		return input.readObject();
	}
}

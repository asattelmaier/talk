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
	private final ObjectInputStream input;
	private Socket socket;

	/**
	 * A main.java.Receiver of information from the network.
	 *
	 * @param socket
	 *            Configuration object.
	 * @throws IOException
	 */
	public Receiver(Socket socket) throws IOException {
		this.socket = socket;
		InputStream in = this.socket.getInputStream();
		this.input = new ObjectInputStream(in);
	}

	/**
	 * The the executing method of the class. This method is being called by the
	 * start()-method of a Thread object containing a Receiver object to
	 * establish the incoming connection from the other host.
	 */
	public void run() {
		try {
			this.receive();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(ABORT.ordinal());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(ABORT.ordinal());
		}
	}

	/**
	 * Creates a loop in which the incoming messages are printed to the console.
	 * If the incoming message contains "exit." gets an information that the
	 * other user disconnected.
	 *
	 * @throws IOException
	 *             IOExceptions
	 */
	private void receive() throws IOException, ClassNotFoundException {
		RemoteCommand response;

		try {
			while ((response = (RemoteCommand) read()) != null) {
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

	/**
	 * Closes the input, as well as the serverSocket from the Receiver object.
	 *
	 * @throws IOException
	 *             IOExceptions
	 */
	public void closeConnection() throws IOException {
		this.input.close();
	}

	/**
	 * Reads the received object.
	 * 
	 * @return The received object.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object read() throws ClassNotFoundException, IOException {
		return input.readObject();
	}
}

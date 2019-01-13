package com.app.talk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import com.app.talk.client.command.set.MessageCommand;
import com.app.talk.client.command.set.PingCommandClient;
import com.app.talk.command.Context;
import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;

/**
 * The dispatcher waits for clients to connect to its serverSocket and creates a
 * communicator for each connected client.
 */
public class Dispatcher implements Runnable {
	private static ServerSocket server;
	private int port;
	static HashMap<Context, Communicator> clientMap = new HashMap<>();

	private static boolean acceptClients = true;

	/**
	 * Dispatcher constructor.
	 *
	 * @param port
	 *            The port to listen to.
	 */
	Dispatcher(int port) {
		this.port = port;
	}

	/**
	 * Runnable implementation.
	 */
	public void run() {
		try {
			this.listen();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates communicators for connected clients.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void listen() throws IOException, ClassNotFoundException {
		server = new ServerSocket(this.port);
		System.out.println("Server started. Listening for incoming connection requests on port: " + this.port);

		while (acceptClients) {
			try {
				Socket client = server.accept();
				System.out.println(
						"Connection request from " + client.getInetAddress().toString() + ":" + client.getPort());
				Communicator communicator = CommunicatorFactory.getInstance().createCommunicator(client,
						CommunicatorFactory.SERVER);
				Dispatcher.addClient(communicator);
				communicator.start();
			} catch (SocketException e) {
				// this is fine
			}
		}
	}

	/**
	 * Closes the server and disables client acception.
	 */
	public static void close() {
		try {
			acceptClients = false;
			server.close();
		} catch (IOException e) {
			// Doesn't matter if already closed.
		}
	}

	/**
	 * Sends a received message to all known chat clients.
	 * 
	 * @param context
	 *            The current client context.
	 * @param message
	 *            Clients text message.
	 */
	synchronized public static void broadcast(Context context, String message) {
		int counter = 0;
		System.out.println("Message: \"" + message + "\" received.");
		for (Communicator communicator : Dispatcher.clientMap.values()) {
			if (communicator.getContext().getId() != context.getId()) {
				try {
					communicator.send(new MessageCommand(message, context));
					System.out.println(" -> redirect to client " + counter++);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Removes a specific chat client from the list of clients.
	 * 
	 * @param client
	 *            Communicator object representing the specific chat client.
	 */
	synchronized public static void removeClient(Communicator client) {
		Communicator removed = Dispatcher.clientMap.remove(client.getContext());
		if (removed != null) {
			client.close();
		}
		if (Dispatcher.clientMap.size() == 0) {
			System.out.println("No more clients available - shutting down server.");
			Dispatcher.close();
		}
	}

	/**
	 * Adds a chat client to the list of clients.
	 * 
	 * @param client
	 *            Communicator object representing the specific chat client.
	 */
	synchronized public static void addClient(Communicator client) {
		Dispatcher.clientMap.put(client.getContext(), client);
	}

	synchronized public static Communicator getCommunicator(Context context) {
		return clientMap.get(context);
	}

	public static void pingResponse(Context context, long startPingTime) {
		Communicator communicator = getCommunicator(context);
		try {
			communicator.send(new PingCommandClient(startPingTime));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Received ping request from client " + context.getId());

	}
}

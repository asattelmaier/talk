package com.app.talk;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.app.talk.command.RemoteCommand;
import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;
import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;
import com.app.talk.server.command.set.BroadcastCommand;
import com.app.talk.server.command.set.ExitCommand;
import com.app.talk.server.command.set.PingCommandServer;

/**
 * A client to connect to a TalkServer.
 */
public class TalkClient {
	private Socket socket = null;
	static User user = null;
	private Scanner scanner = new Scanner(System.in);
	public static Communicator communicator;

	/**
	 * Client constructor.
	 *
	 * @param config
	 *            client configuration
	 * @throws IOException
	 */
	private TalkClient(Config config) throws InterruptedException {
		user = new User();
		user.setNameFromUserInput();
		connect(config);
	}

	/**
	 * @throws IOException
	 */
	private void init() throws IOException {
		System.out.println("End communication with line = \"exit.\"");
		this.communicator = CommunicatorFactory.getInstance().createCommunicator(socket, CommunicatorFactory.CLIENT);
		this.communicator.start();
	}

	/**
	 * A method that creates a loop in which the user keyboard input is being
	 * taken in and sent to the other Host.
	 */
	private void sendUserInput() throws IOException {
		String userInput;
		boolean userExits;
		boolean userPing;

		while (!Thread.interrupted()) {
			userInput = scanner.nextLine();
			userExits = userInput.equals("exit.");
			userPing = userInput.equals("ping.");

			if (userExits) {
				sendExit();
			} else if (userPing) {
				sendPing();
			} else {
				sendMessage(userInput);
			}
		}
	}

	/**
	 * Sends the exit command.
	 */
	private void sendExit() throws IOException {
		ExitCommand exitCommand = new ExitCommand();
		communicator.send(exitCommand);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread.currentThread().interrupt();
		communicator.close();
		socket.close();
	}

	/**
	 * A method that receives a String message and writes it in sequences of
	 * bytes to the other host.
	 *
	 * @param message
	 *            - message that should be sent to the other host.
	 */
	public void sendMessage(String message) throws IOException {
		BroadcastCommand messageCommand = new BroadcastCommand(message);
		this.communicator.send(messageCommand);
	} // sendMessage

	/**
	 * establishes the connection to server and tries to reconnect.
	 * 
	 * @param config
	 *            includes port and ip of server
	 * @throws InterruptedException
	 *             wont happen
	 */
	private void connect(Config config) throws InterruptedException {
		while (socket == null) {
			try {
				this.socket = new Socket(config.getRemoteHost(), config.getPort());
			} catch (Exception e) {
				TimeUnit.SECONDS.sleep(10);
			}
		}
	}

	/**
	 * Starts the client.
	 *
	 * @param args
	 * @throws ConfigParserException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ConfigParserException, IOException, InterruptedException {
		ConfigParser configParser = new ConfigParser(args);
		Config config = configParser.getConfig();

		TalkClient client = new TalkClient(config);
		client.init();
		client.sendUserInput();
	}

	public void sendPing() {
		try {
			RemoteCommand command = new PingCommandServer();
			communicator.send(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.app.talk.communication;

import java.io.IOException;
import java.net.Socket;

import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;
import com.app.talk.common.SystemExitCode;

public class CommunicatorFactory {
	private static CommunicatorFactory single = new CommunicatorFactory();
	private int clientId;
	private Socket socket;
	private Communicator communicator;
	private final String SERVER = "Server";
	private final String CLIENT = "Client";

	/**
	 * Returns the single factory object.
	 * 
	 * @return CommunicatorFactory the single Factory
	 */
	public static CommunicatorFactory getInstance() {
		return single;
	}

	/**
	 * Creates a Server Communicator.
	 * 
	 * @param socket
	 * @param clientId
	 * @return
	 * @throws IOException
	 */
	public Communicator createCommunicator(Socket socket, int clientId) throws IOException {
		this.clientId = clientId;
		this.socket = socket;

		return createCommunicator(SERVER);
	}

	/**
	 * Creates a Client Communicator.
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	public Communicator createCommunicator(Socket socket) throws IOException {
		this.socket = socket;

		return createCommunicator(CLIENT);
	}

	/**
	 * Creates a Communicator.
	 * 
	 * @param communicatorType
	 * @return
	 * @throws IOException
	 */
	private Communicator createCommunicator(String communicatorType) throws IOException {
		this.communicator = new Communicator(this.socket);

		setContext(communicatorType);

		setParameters();

		communicator.start();

		return communicator;
	}

	/**
	 * Sets the context of Server or Client Communicator.
	 * 
	 * @param communicatorType
	 * @throws IOException
	 */
	private void setContext(String communicatorType) throws IOException {
		switch (communicatorType) {
		case SERVER:
			setServerContext();
			break;
		case CLIENT:
			setClientContext();
			break;
		}
	}

	/**
	 * Sets the Server Communicator context
	 * 
	 * @throws IOException
	 */
	private void setServerContext() throws IOException {
		this.communicator.context = new Context(this.clientId);
		this.communicator.getSender().send(this.communicator.context);
	}

	/**
	 * Sets the Client Communicator context
	 * 
	 * @throws IOException
	 */
	private void setClientContext() throws IOException {
		try {
			Context context = (Context) this.communicator.getReceiver().read();
			this.communicator.context = context;
		} catch (ClassNotFoundException e) {
			System.exit(SystemExitCode.ABORT.ordinal());
		}
	}

	/**
	 * Sets Communicator parameters
	 * 
	 * @throws IOException
	 */
	private void setParameters() {
		RemoteCommand heartbeat = createHeartbeat();

		this.communicator.setHeartbeatTimeout(60000);
		this.communicator.setHeartbeat(heartbeat);
	}

	/**
	 * Creates a new HeartBeat
	 * 
	 * @return a new RemoteCommand Instance
	 */
	private RemoteCommand createHeartbeat() {
		return new RemoteCommand() {
			private static final long serialVersionUID = -1801133200424887639L;

			@Override
			public void execute(Context context) {
			}
		};
	}
}

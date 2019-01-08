package com.app.talk.communication;

import java.io.IOException;
import java.net.Socket;

import com.app.talk.client.command.set.RemoteCommandClient;
import com.app.talk.client.command.set.SetContextCommand;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;
import com.app.talk.command.RemoteCommandProcessor;
import com.app.talk.common.SystemExitCode;

public class CommunicatorFactory {
	private static CommunicatorFactory single = new CommunicatorFactory();
	private Socket socket;
	private Communicator communicator;
	public static final boolean SERVER = true;
	public static final boolean CLIENT = false;

	private static RemoteCommand heartbeat = createHeartbeat();

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
	public Communicator createCommunicator(Socket socket, boolean createServerCommunicator) throws IOException {
		this.socket = socket;
		return createCommunicator(createServerCommunicator);
	}

	/**
	 * Creates a Communicator.
	 * 
	 * @param communicatorType
	 * @return
	 * @throws IOException
	 */
	private Communicator createCommunicator(boolean createServerCommunicator) throws IOException {
		this.communicator = new Communicator(this.socket);

		setContext(createServerCommunicator);

		setParameters();

		return communicator;
	}

	/**
	 * Sets the context of Server or Client Communicator.
	 * 
	 * @param communicatorType
	 * @throws IOException
	 */
	private void setContext(boolean createServerCommunicator) throws IOException {
		if (createServerCommunicator) {
			setServerContext();
		}

	}

	/**
	 * Sets the Server Communicator context
	 * 
	 * @throws IOException
	 */
	private void setServerContext() throws IOException {
		Context context = new Context();
		this.communicator.context = context;
		this.communicator.send(new SetContextCommand(context));
	}

	/**
	 * Sets Communicator parameters
	 * 
	 * @throws IOException
	 */
	private void setParameters() {
		this.communicator.setHeartbeatTimeout(60000);
		this.communicator.setHeartbeat(CommunicatorFactory.heartbeat);
	}

	/**
	 * Creates a new HeartBeat
	 * 
	 * @return a new RemoteCommand Instance
	 */
	private static RemoteCommandClient createHeartbeat() {
		return new RemoteCommandClient() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1958388813301466171L;

			@Override
			public void execute() {
			}
		};
	}
}

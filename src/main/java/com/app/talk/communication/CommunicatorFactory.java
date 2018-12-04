package com.app.talk.communication;

import java.io.IOException;
import java.net.Socket;

import com.app.talk.command.RemoteCommand;

public class CommunicatorFactory {
	
	private static CommunicatorFactory single = new CommunicatorFactory();
	private static RemoteCommand heartbeat = new RemoteCommand() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1801133200424887639L;

		@Override
		public void execute(Communicator command) {
			
		}
	};
	
	private CommunicatorFactory() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the single factory object.
	 * 
	 * @return CommunicatorFactory the single Factory
	 */
	public static CommunicatorFactory getInstance() {
		return single;
	}	
	
	/**
	 * Creates a Communicator object.
	 * 
	 * @param socket
	 * @return Communicator object
	 */
	public Communicator createCommunicator(Socket socket) throws IOException {
		Communicator communicator = new Communicator(socket);
		communicator.setHeartbeatTimeout(60000);
		communicator.setHeartbeat(heartbeat);
		communicator.start();
		return communicator; 
	}
}

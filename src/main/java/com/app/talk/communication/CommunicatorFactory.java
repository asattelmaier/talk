package com.app.talk.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;
import com.app.talk.common.SystemExitCode;

public class CommunicatorFactory {
	private static int idGenerator = 0;
	private static CommunicatorFactory single = new CommunicatorFactory();
	private static RemoteCommand heartbeat = new RemoteCommand() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1801133200424887639L;

		@Override
		public void execute(Context context) {
			
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
	public Communicator createCommunicator(Socket socket, boolean createServerCommunicator) throws IOException {
		Communicator communicator = new Communicator(socket);
		
//		Handshake
		if (createServerCommunicator) {
			communicator.context = new Context(idGenerator++);
			communicator.getSender().send(communicator.context);
//			System.out.println("DEBUG: try to send: " + communicator.context);
		} else {
			try {
				Context context = (Context) communicator.getReceiver().read();
				communicator.context = context;
//				System.out.println("DEBUG: received context: " + communicator.context);
			} catch (ClassNotFoundException e) {
				System.exit(SystemExitCode.ABORT.ordinal());
			}
		}
		
		//Set parameter
		communicator.setHeartbeatTimeout(60000);
		communicator.setHeartbeat(heartbeat);
		communicator.start();
		
		return communicator; 
	}
}

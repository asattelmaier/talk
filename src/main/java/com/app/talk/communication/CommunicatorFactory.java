package com.app.talk.communication;

import java.io.IOException;
import java.net.Socket;

public class CommunicatorFactory {
	
	private static CommunicatorFactory single = new CommunicatorFactory();
	
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
		return new Communicator(socket);
	}
}

package com.app.talk.communication;

import com.app.talk.common.Config;
import com.app.talk.common.User;

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
	 * @param config
	 * @param user
	 * @return Communicator object
	 */
	public Communicator createCommunicator(Config config, User user) {		
		return new Communicator(config, user);
	}
}

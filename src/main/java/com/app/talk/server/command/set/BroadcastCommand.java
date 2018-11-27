/**
 * 
 */
package com.app.talk.server.command.set;

import com.app.talk.TalkServer;
import com.app.talk.command.RemoteCommand;

/**
 * @author Michael
 *
 */
public class BroadcastCommand implements RemoteCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7134775297117601744L;

	
	private String message;
	/**
	 * 
	 */
	
	public BroadcastCommand(String message) {
		this.message = message;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		TalkServer.broadcast(this.message);
	}
	
	
	
	
	
}

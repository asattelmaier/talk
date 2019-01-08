/**
 * 
 */
package com.app.talk.server.command.set;

import com.app.talk.Dispatcher;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;

/**
 * Broadcasts a given textual massage to every known chat client.
 */
public class BroadcastCommand implements RemoteCommandServer{

	private static final long serialVersionUID = -7134775297117601744L;
	
	/**
	 * textual message to be sent.
	 */
	private String message;

	
	private Context context;
	
	/**
	 * Constructs a BroadcastCommand object to be read by the receiver.
	 * @param message textual message to be sent.
	 */
	public BroadcastCommand(String message) {
		this.message = message;
	} //constructor
	
	/**
	 * executes the specified command to broadcast.
	 */
	@Override
	public void execute(Context context) {
		Dispatcher.broadcast(context, this.message);
//		System.out.println("DEBUG: BroadcastCommand");
	} //execute	
} //BroadcastCommand

package com.app.talk.server.command.set;

import com.app.talk.Dispatcher;
import com.app.talk.command.Context;

/**
 * Broadcasts a given textual massage to every known chat client.
 */
public class BroadcastCommand implements RemoteCommandServer {
	private static final long serialVersionUID = 3610762201502268371L;
	private String message;

	/**
	 * Constructs a BroadcastCommand object to be read by the receiver.
	 * 
	 * @param message
	 *            Textual message to be sent.
	 */
	public BroadcastCommand(String message) {
		this.message = message;
	}

	/**
	 * executes the specified command to broadcast.
	 */
	@Override
	public void execute(Context context) {
		Dispatcher.broadcast(context, this.message);
	}
}

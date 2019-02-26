package com.app.talk.client.command.set;

import com.app.talk.command.Context;

/**
 * Represents a command that will write a message to standard output.
 */
public class MessageCommand implements RemoteCommandClient {
	private static final long serialVersionUID = -7385361505683714660L;
	private Context context;
	private String message;

	/**
	 * A command that prints a message.
	 * 
	 * @param message
	 *            The message to send.
	 * @param context
	 *            Clients context.
	 */
	public MessageCommand(String message, Context context) {
		this.message = message;
		this.context = context;
	}

	@Override
	public void execute() {
		System.out.println("[U" + context.getId() + "]" + message);
	}
}

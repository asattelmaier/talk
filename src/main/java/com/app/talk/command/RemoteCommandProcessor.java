package com.app.talk.command;

import java.util.concurrent.ArrayBlockingQueue;

import com.app.talk.client.command.set.RemoteCommandClient;
import com.app.talk.server.command.set.RemoteCommandServer;

public class RemoteCommandProcessor implements Runnable {
	ArrayBlockingQueue<RemoteCommand> commandQueue = new ArrayBlockingQueue<RemoteCommand>(10);
	private final Context context;

	/**
	 * RemoteCommandProcessor constructor.
	 * 
	 * @param context
	 */
	public RemoteCommandProcessor(Context context) {
		this.context = context;
	}

	/**
	 * Puts a command to the command queue.
	 * 
	 * @param command
	 *            The given command for the command queue.
	 * @throws InterruptedException
	 */
	public void put(RemoteCommand command) throws InterruptedException {
		commandQueue.put(command);
	}

	@Override
	public void run() {
		try {
			while (true) {

				RemoteCommand command;
				command = commandQueue.take();
				if (context != null) {
					RemoteCommandServer rcs = (RemoteCommandServer) command;
					rcs.execute(context);
				} else {
					RemoteCommandClient rcc = (RemoteCommandClient) command;
					rcc.execute();
				}
			}
		} catch (InterruptedException e) {
			// This is ok
		}
	}
}

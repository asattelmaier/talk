package com.app.talk.command;

import java.util.concurrent.ArrayBlockingQueue;

public class RemoteCommandProcessor implements Runnable {
	ArrayBlockingQueue<RemoteCommand> commandQueue = new ArrayBlockingQueue<RemoteCommand>(10);
	private final Context context;

	public RemoteCommandProcessor(Context context) {
		this.context = context;
	}

	public void put(RemoteCommand command) throws InterruptedException {
		commandQueue.put(command);
	}

	@Override
	public void run() {
		try {
			while (true) {
				RemoteCommand command;
				command = commandQueue.take();
				command.execute(context);
			}
		} catch (InterruptedException e) {
			//This is ok
		}
	}
}

package main.java.com.app.talk.server.command.set;

import main.java.com.app.talk.Dispatcher;
import main.java.com.app.talk.command.Context;
import main.java.com.app.talk.communication.Communicator;

/**
 * Exit command.
 */
public class ExitCommand implements RemoteCommandServer {
	private static final long serialVersionUID = 8203735131553893997L;

	/**
	 * Executes the ExitCommand.
	 */
	@Override
	public void execute(Context context) {
		Communicator communicator = Dispatcher.getCommunicator(context);
		Dispatcher.removeClient(communicator);
	}
}

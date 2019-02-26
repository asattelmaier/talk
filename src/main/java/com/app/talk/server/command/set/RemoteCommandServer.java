package main.java.com.app.talk.server.command.set;

import main.java.com.app.talk.command.Context;
import main.java.com.app.talk.command.RemoteCommand;

public interface RemoteCommandServer extends RemoteCommand {

	/**
	 * Executes the command.
	 */
	void execute(Context context);
}

package com.app.talk.server.command.set;

import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;

public interface RemoteCommandServer extends RemoteCommand {

	/**
	 * Executes the command.
	 */
	void execute(Context context);
}

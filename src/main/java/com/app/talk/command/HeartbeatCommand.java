package com.app.talk.command;

import com.app.talk.client.command.set.RemoteCommandClient;
import com.app.talk.server.command.set.RemoteCommandServer;

/**
 * Broadcasts a given textual massage to every known chat client.
 */
public class HeartbeatCommand implements RemoteCommandServer, RemoteCommandClient {

	private static final long serialVersionUID = -5681079925200162496L;

	@Override
	public void execute() {

	}

	@Override
	public void execute(Context context) {

	}
}

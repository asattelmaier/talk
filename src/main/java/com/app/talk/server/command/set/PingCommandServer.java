package com.app.talk.server.command.set;

import com.app.talk.Dispatcher;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;

public class PingCommandServer implements RemoteCommand{

	private static final long serialVersionUID = 3041424435415632330L;

	@Override
	public void execute(Context context) {
		Dispatcher.pingResponse(context);
	}

}

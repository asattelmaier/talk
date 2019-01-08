package com.app.talk.client.command.set;

import com.app.talk.TalkClient;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;

public class SetContextCommand implements RemoteCommandClient {

	private static final long serialVersionUID = 4819280009293584588L;

	private Context context;

	public SetContextCommand(Context context) {
		this.context = context;
	}

	@Override
	public void execute() {
		TalkClient.communicator.setContext(this.context);
		System.out.println("Client ID set to " + this.context.getId());
	}

}

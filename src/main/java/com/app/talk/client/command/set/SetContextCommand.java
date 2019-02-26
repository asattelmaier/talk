package com.app.talk.client.command.set;

import com.app.talk.TalkClient;
import com.app.talk.command.Context;

public class SetContextCommand implements RemoteCommandClient {
	private static final long serialVersionUID = -5292609199183740710L;
	private Context context;

	/**
	 * SetContextCommand constructor.
	 * 
	 * @param context
	 */
	public SetContextCommand(Context context) {
		this.context = context;
	}

	@Override
	public void execute() {
		System.out.println("Client ID set to " + this.context.getId());
	}
}

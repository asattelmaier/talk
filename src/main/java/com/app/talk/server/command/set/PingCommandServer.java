package main.java.com.app.talk.server.command.set;

import main.java.com.app.talk.Dispatcher;
import main.java.com.app.talk.command.Context;

public class PingCommandServer implements RemoteCommandServer {
	private static final long serialVersionUID = -6438430634274964628L;
	public final long startPingTime = System.nanoTime();

	@Override
	public void execute(Context context) {
		Dispatcher.pingResponse(context, startPingTime);
	}

}

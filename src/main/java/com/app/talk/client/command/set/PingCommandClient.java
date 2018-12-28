package com.app.talk.client.command.set;

import com.app.talk.TalkClient;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;

public class PingCommandClient implements RemoteCommand{

	private static final long serialVersionUID = -5909494824363162600L;
	
	long oneWayPingTime = System.nanoTime();
	
	@Override
	public void execute(Context context) {	
		long endPingTime = System.nanoTime();
		long pingTime = endPingTime - TalkClient.startPingTime;
		System.out.println("Received ping reply, time required to reach server : " +
				(oneWayPingTime - TalkClient.startPingTime) +
				" nanoseconds, roundtrip time: " +
				pingTime + " nanoseconds"
				);
	}

}

package com.app.talk.client.command.set;

import com.app.talk.TalkClient;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;

public class PingCommandClient implements RemoteCommandClient{

	private static final long serialVersionUID = -5909494824363162600L;
	
	public final long startPingTime;
	long oneWayPingTime = System.nanoTime();
	
	
	public PingCommandClient(long startPingTime) {
		this.startPingTime = startPingTime;
	}	
	
	@Override
	public void execute() {	
		long endPingTime = System.nanoTime();
		long pingTime = endPingTime - this.startPingTime;
		System.out.println("Received ping reply, time required to reach server : " +
				(oneWayPingTime - this.startPingTime) +
				" nanoseconds, roundtrip time: " +
				pingTime + " nanoseconds"
				);
	}

}

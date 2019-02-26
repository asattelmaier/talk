package com.app.talk.client.command.set;

import com.app.talk.command.Context;

public class PingCommandClient implements RemoteCommandClient {
	private static final long serialVersionUID = 7777572474626661378L;
	public final long startPingTime;
	long oneWayPingTime = System.nanoTime();

	/**
	 * PingCommandClient constructor.
	 * 
	 * @param startPingTime
	 */
	public PingCommandClient(long startPingTime) {
		this.startPingTime = startPingTime;
	}

	@Override
	public void execute() {
		long endPingTime = System.nanoTime();
		long pingTime = endPingTime - this.startPingTime;
		System.out.println("Received ping reply, time required to reach server : "
				+ (oneWayPingTime - this.startPingTime) + " nanoseconds, roundtrip time: " + pingTime + " nanoseconds");
	}
}

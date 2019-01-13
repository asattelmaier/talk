package com.app.talk.common;

public class Config {
	private int port = 0;
	private String remoteHost = null;

	/**
	 * Configuration constructor with default values.
	 */
	public Config(int port, String remoteHost) {
		this.port = port;
		this.remoteHost = remoteHost;
	}

	/**
	 * Return the talk port.
	 *
	 * @return The talk port.
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Returns the remote host.
	 *
	 * @return The remote host.
	 */
	public String getRemoteHost() {
		return this.remoteHost;
	}
}

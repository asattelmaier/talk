package com.app.talk.common;

public class Config {
	private int port = 0;
	private String remoteHost = null;

	/**
	 * Config constructor with default values.
	 */
	public Config(int port, String remoteHost) {
		this.port = port;
		this.remoteHost = remoteHost;
	}

	/**
	 * Return the talk port.
	 *
	 * @return the talk port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Returns the remote host.
	 *
	 * @return the remote host
	 */
	public String getRemoteHost() {
		return this.remoteHost;
	}
}

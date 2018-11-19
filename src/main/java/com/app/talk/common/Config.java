package com.app.talk.common;

public class Config {
    private int listenPort = 0;
    private int talkPort = 0;
    private String remoteHost = null;

    /**
     * Config constructor with default values.
     */
    public Config(int talkPort, String remoteHost) {
        this.talkPort = talkPort;
        this.remoteHost = remoteHost;
    }

    /**
     * Config constructor with default values.
     */
    Config(int listenPort, int talkPort, String remoteHost) {
        this.listenPort = listenPort;
        this.talkPort = talkPort;
        this.remoteHost = remoteHost;
    }

    /**
     * Returns the listen port.
     *
     * @return the listen port
     */
    public int getListenPort() {
        return this.listenPort;
    }

    /**
     * Return the talk port.
     *
     * @return the talk port
     */
    public int getTalkPort() {
        return this.talkPort;
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

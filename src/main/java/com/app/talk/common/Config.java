package com.app.talk.common;

import java.util.*;

public class Config {
    private int listenPort = 0;
    private int talkPort = 0;
    private String remoteHost = null;

    public Config(String[] args) {
        parseArgumentStrings(args);
    }

    private void parseArgumentStrings(String[] args) {
        if (args.length > 1)
            this.listenPort = getIntegerFromString(args[0]);
        if (args.length > 2)
            this.talkPort = getIntegerFromString(args[1]);
        if (args.length > 3)
            this.remoteHost = args[2];
        if (args.length > 4)
            System.err.println("blub");

        setDefaultValuesIfRequired();
    }

    private void setDefaultValuesIfRequired() {
        int DEFAULT_LISTEN_PORT = 2048;
        int DEFAULT_TALK_PORT = 2049;
        String DEFAULT_REMOTE_HOST = "localhost";

        if (this.listenPort == 0)
            this.listenPort = DEFAULT_LISTEN_PORT;
        if (this.talkPort == 0)
            this.talkPort = DEFAULT_TALK_PORT;
        if (this.remoteHost == null)
            this.remoteHost = DEFAULT_REMOTE_HOST;
    }

    private int getIntegerFromString(String value) {
        int intValue = 0;

        try {
            intValue = Integer.parseInt(value);

        } catch (NumberFormatException e) {
            System.err.println("blub");
        }

        return intValue;
    }

    public int getListenPort() {
        return this.listenPort;
    }

    public int getTalkPort() {
        return this.talkPort;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

}

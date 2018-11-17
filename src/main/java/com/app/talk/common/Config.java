package com.app.talk.common;

public class Config {
    private int listenPort = 0;
    private int talkPort = 0;
    private String remoteHost = null;

    public Config() {
        this.listenPort = 2048;
        this.talkPort = 2048;
        this.remoteHost = "localhost";
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

    public void parseArgumentStrings(String[] args) {
        if (args.length > 1)
            this.listenPort = getIntegerFromString(args[0]);
        if (args.length > 2)
            this.talkPort = getIntegerFromString(args[1]);
        if (args.length > 3)
            this.remoteHost = args[2];
        if (args.length > 4)
            System.err.println("To many arguments given");
    }

    private int getIntegerFromString(String value) {
        int intValue = 0;

        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Port Number");
        }

        return intValue;
    }
}

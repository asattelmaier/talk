package com.app.talk.common;

public class ConfigParser {
    private int listenPort = 0;
    private int talkPort = 0;
    private String remoteHost = null;

    /**
     * ConfigParser constructor with default values.
     */
    public ConfigParser() {
        this.listenPort = 2048;
        this.talkPort = 2049;
        this.remoteHost = "localhost";
    }

    public Config getConfig() {
        return new Config(this.listenPort, this.talkPort, this.remoteHost);
    }

    /**
     * Parses the given argments.
     *
     * @param args the given Arguments
     */
    public void parseArgumentStrings(String[] args) {
        if (args.length > 0)
            this.listenPort = parseStringToInteger(args[0]);
        if (args.length > 1)
            this.talkPort = parseStringToInteger(args[1]);
        if (args.length > 2)
            this.remoteHost = args[2];
        if (args.length > 3)
            System.err.println("To many arguments given");
    }

    /**
     * Parses a given string to integer.
     *
     * @param value checks if the given value is an integer
     * @return returns the given value as Integer
     */
    private int parseStringToInteger(String value) {
        int intValue = 0;

        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Port Number");
        }

        return intValue;
    }
}

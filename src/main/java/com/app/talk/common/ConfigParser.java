package com.app.talk.common;

import static com.app.talk.common.ConfigParserException.ErrorCode.*;

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
    public void parseArgumentStrings(String[] args) throws ConfigParserException {
        if (args.length > 0)
            this.listenPort = parseStringToInteger(args[0]);
        if (args.length > 1)
            this.talkPort = parseStringToInteger(args[1]);
        if (args.length > 2)
            this.remoteHost = args[2];
        if (args.length > 3)
            throw new ConfigParserException(INVALID_PORT);
    }

    /**
     * Parses a given string to integer.
     *
     * @param value checks if the given value is an integer
     * @return returns the given value as Integer
     */
    private int parseStringToInteger(String value) throws ConfigParserException {
        int intValue;

        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigParserException(INVALID_PORT, value);
        }

        return intValue;
    }
}

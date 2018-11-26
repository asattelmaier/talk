package com.app.talk.common;

import static com.app.talk.common.ConfigParserException.ErrorCode.*;

public class ConfigParser {
    private int port = 0;
    private String remoteHost = null;

    /**
     * ConfigParser constructor with default values.
     */
    public ConfigParser(String[] args) throws ConfigParserException {
        this.port = 2048;
        this.remoteHost = "localhost";
        this.parseArgumentStrings(args);
    }

    /**
     * Return the configuration object, that hold the ports and host
     *
     * @return the configuration object
     */
    public Config getConfig() {
        return new Config(this.port, this.remoteHost);
    }

    /**
     * Parses the given argments.
     *
     * @param args the given Arguments
     */
    private void parseArgumentStrings(String[] args) throws ConfigParserException {
        if (args.length > 0)
            this.port = parseStringToInteger(args[0]);
        if (args.length > 1)
            this.remoteHost = args[1];
        if (args.length > 2)
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

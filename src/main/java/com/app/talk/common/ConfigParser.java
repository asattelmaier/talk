package com.app.talk.common;

import static com.app.talk.common.ConfigParserException.ErrorCode.*;

public class ConfigParser {
    private int port = 0;
    private String remoteHost = null;

    public static Config makeConfig(String[] args) {
        ConfigParser configParser = null;

        try {
            configParser = new ConfigParser(args);
        } catch (ConfigParserException e) {
            e.printStackTrace();
        }

        Config config = configParser.getConfig();

        return config;
    }

    private ConfigParser(String[] args) throws ConfigParserException {
        this.port = 2048;
        this.remoteHost = "localhost";
        this.parseArgumentStrings(args);
    }

    private Config getConfig() {
        return new Config(this.port, this.remoteHost);
    }

    private void parseArgumentStrings(String[] args) throws ConfigParserException {
        if (args.length > 0)
            this.port = parseStringToInteger(args[0]);
        if (args.length > 1)
            this.remoteHost = args[1];
        if (args.length > 2)
            throw new ConfigParserException(INVALID_PORT);
    }

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

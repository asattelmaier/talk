package com.app.talk.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigParserTest {
    private int DEFAULT_TALK_PORT = 2049;
    private String DEFAULT_REMOTE_HOST = "localhost";
    private ConfigParser configParser;
    private String[] args = new String[3];

    @Test
    @DisplayName("Arguments:")
    void setDefaultValues() throws ConfigParserException {
        int DEFAULT_LISTEN_PORT = 2048;
        String[] args = {};

        configParser = new ConfigParser(args);

        assertConfiguration(DEFAULT_LISTEN_PORT, DEFAULT_TALK_PORT, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333")
    void setListenPort() throws ConfigParserException {
        String[] args = {"3333"};

        configParser = new ConfigParser(args);

        assertConfiguration(3333, DEFAULT_TALK_PORT, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333 9999")
    void setListenAndTalkPort() throws ConfigParserException {
        String[] args = {"3333", "9999"};

        configParser = new ConfigParser(args);

        assertConfiguration(3333, 9999, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333 9999 localhost")
    void setPortsAndRemoteHost() throws ConfigParserException {
        String[] args = {"3333", "9999", "localhost"};

        configParser = new ConfigParser(args);

        assertConfiguration(3333, 9999, "localhost");
    }

    @Test
    @DisplayName("Arguments: #+*0")
    void invalidListenPortGiven() throws ConfigParserException {
        String[] args = {"#+*0"};

        Assertions.assertThrows(ConfigParserException.class, () -> new ConfigParser(args));
    }

    @Test
    @DisplayName("Arguments: 33333 #+*0")
    void invalidTalkPortGiven() {
        String[] args = {"#+*0"};

        Assertions.assertThrows(ConfigParserException.class, () -> new ConfigParser(args));
    }

    @Test
    @DisplayName("Arguments: 3333 9999 localhost test")
    void toManyArgumentsGiven() {
        String[] args = {"3333", "9999", "localhost", "test"};

        Assertions.assertThrows(ConfigParserException.class, () -> new ConfigParser(args));
    }

    private void assertConfiguration(int listenPort, int talkPort, String remoteHost) {
        assertAll("Talk configuration",
                () -> assertEquals(listenPort, configParser.getConfig().getListenPort()),
                () -> assertEquals(talkPort, configParser.getConfig().getTalkPort()),
                () -> assertEquals(remoteHost, configParser.getConfig().getRemoteHost())
        );
    }
}

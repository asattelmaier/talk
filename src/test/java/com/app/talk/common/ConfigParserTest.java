package com.app.talk.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigParserTest {
    private int DEFAULT_TALK_PORT = 2049;
    private String DEFAULT_REMOTE_HOST = "localhost";
    private ConfigParser config = new ConfigParser();
    private String[] args = new String[3];

    @Test
    @DisplayName("Arguments:")
    void setDefaultValues() {
        int DEFAULT_LISTEN_PORT = 2048;

        assertConfiguration(DEFAULT_LISTEN_PORT, DEFAULT_TALK_PORT, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333")
    void setListenPort() {
        String[] args = {"3333"};

        config.parseArgumentStrings(args);

        assertConfiguration(3333, DEFAULT_TALK_PORT, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333 9999")
    void setListenAndTalkPort() {
        String[] args = {"3333", "9999"};

        config.parseArgumentStrings(args);

        assertConfiguration(3333, 9999, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333 9999 localhost")
    void setPortsAndRemoteHost() {
        String[] args = {"3333", "9999", "localhost"};

        config.parseArgumentStrings(args);

        assertConfiguration(3333, 9999, "localhost");
    }

    private void assertConfiguration(int listenPort, int talkPort, String remoteHost) {
        assertAll("Talk configuration",
                () -> assertEquals(listenPort, config.getConfig().getListenPort()),
                () -> assertEquals(talkPort, config.getConfig().getTalkPort()),
                () -> assertEquals(remoteHost, config.getConfig().getRemoteHost())
        );
    }
}

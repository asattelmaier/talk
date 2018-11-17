package com.app.talk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TalkTests {
    private int DEFAULT_TALK_PORT = 2049;
    private String DEFAULT_REMOTE_HOST = "localhost";
    private Talk talk = new Talk();
    private String[] args = new String[3];

    @Test
    @DisplayName("Arguments:")
    void setDefaultValues() {
        int DEFAULT_LISTEN_PORT = 2048;
        String[] args = {};

        this.talk.setPortsAndRemoteHost(args);

        assertConfiguration(DEFAULT_LISTEN_PORT, DEFAULT_TALK_PORT, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333")
    void setListenPort() {
        String[] args = {"3333"};

        this.talk.setPortsAndRemoteHost(args);

        assertConfiguration(3333, DEFAULT_TALK_PORT, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333 9999")
    void setListenAndTalkPort() {
        String[] args = {"3333", "9999"};

        this.talk.setPortsAndRemoteHost(args);

        assertConfiguration(3333, 9999, DEFAULT_REMOTE_HOST);
    }

    @Test
    @DisplayName("Arguments: 3333 9999 localhost")
    void setPortsAndRemoteHost() {
        String[] args = {"3333", "9999", "localhost"};

        this.talk.setPortsAndRemoteHost(args);

        assertConfiguration(3333, 9999, "localhost");
    }

    private void assertConfiguration(int listenPort, int talkPort, String remoteHost) {
        assertAll("Talk configuration",
                () -> assertEquals(listenPort, this.talk.getListenPort()),
                () -> assertEquals(talkPort,this.talk.getTalkPort()),
                () -> assertEquals(remoteHost,this.talk.getRemoteHost())
        );
    }
}

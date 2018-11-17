package com.app.talk;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;

import com.app.talk.common.User;
import com.app.talk.common.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Matchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SenderTest {
    private final User user = new User();
    private final int PORT = 2049;
    private final String REMOTE_HOST = "localhost";

    @Test
    @DisplayName("Waiting for Connection")
    void startSender() {
        Config config = new Config(PORT, REMOTE_HOST);
        user.setName("Frank Elstner");

        PrintStream out = mock(PrintStream.class);
        System.setOut(out);

        Sender sender = new Sender(config, user);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        verify(out).println(endsWith(REMOTE_HOST + ":" + PORT + "..."));
    }

    @Test
    @DisplayName("Connection Failed")
    void reconnect() throws IOException {
        Config config = new Config(PORT, REMOTE_HOST);
        user.setName("Frank Elstner");

        Sender sender = new Sender(config, user);

        Assertions.assertThrows(ConnectException.class, sender::connect);
    }
}

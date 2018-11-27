package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;

import static org.mockito.Matchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SenderTest {
    private final User user = new User();
    private final int PORT = 2049;
    private final String REMOTE_HOST = "localhost";

    @Test
    @DisplayName("Waiting for Connection")
    void testSenderStart() throws IOException {
        Config config = new Config(PORT, REMOTE_HOST);
        user.setName("Frank Elstner");

        PrintStream out = mock(PrintStream.class);
        System.setOut(out);

        Sender sender = new Sender(new Socket(config.getRemoteHost(), config.getTalkPort()));
        Thread senderThread = new Thread(sender);
        senderThread.start();

        verify(out).println(endsWith(REMOTE_HOST + ":" + PORT + "..."));
    }

    @Test
    @DisplayName("Connection Failed")
    void testReconnect() throws IOException {
        Config config = new Config(PORT, REMOTE_HOST);
        user.setName("Frank Elstner");

        Sender sender = new Sender(new Socket(config.getRemoteHost(), config.getTalkPort()));

        Assertions.assertThrows(ConnectException.class, sender::connect);
    }
}

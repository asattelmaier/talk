package com.app.talk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Matchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SenderTest {
    private final String USERNAME = "Frank Elstner";
    private final int PORT = 2049;
    private final String REMOTE_HOST = "localhost";

    @Test
    @DisplayName("Waiting for Connection")
    void startSender() {
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);

        Sender sender = new Sender(REMOTE_HOST, PORT, USERNAME);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        verify(out).println(endsWith(REMOTE_HOST + ":" + PORT + "..."));
    }

    @Test
    @DisplayName("Connection Failed")
    void reconnect() throws IOException {
        Sender sender = new Sender(REMOTE_HOST, PORT, USERNAME);

        Assertions.assertThrows(ConnectException.class, sender::connect);
    }
}

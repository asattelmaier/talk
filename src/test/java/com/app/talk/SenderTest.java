package com.app.talk;

import java.io.PrintStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SenderTest {

    @Test
    @DisplayName("Waiting for Connection")
    void startSender() {
        String USERNAME = "Frank Elstner";
        int PORT = 2049;
        String REMOTE_HOST = "localhost";

        PrintStream out = mock(PrintStream.class);
        System.setOut(out);

        Sender sender = new Sender(REMOTE_HOST, PORT, USERNAME);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        verify(out).println(endsWith(REMOTE_HOST + ":" + PORT + "..."));
    }
}

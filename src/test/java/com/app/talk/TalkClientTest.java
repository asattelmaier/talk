package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.communication.Communicator;
import com.app.talk.server.command.set.BroadcastCommand;
import com.app.talk.server.command.set.ExitCommand;
import com.app.talk.server.command.set.PingCommandServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TalkClientTest {
    private final String[] args = new String[0];

    @Test
    @DisplayName("Default connection established")
    void establishDefaultConnectionTest() {
        Config config = ConfigParser.makeConfig(args);
        TalkClient client = new TalkClient();

        startServer();
        client.connect(config);

        assertTrue(client.connected);
        assertEquals(0, client.connectionTry);
    }

    @Test
    @DisplayName("Default connection failed")
    void defaultConnectionFailedTest() {
        Config config = ConfigParser.makeConfig(args);
        TalkClient client = spy(TalkClient.class);

        doNothing().when(client).clientWait();
        client.connect(config);

        assertFalse(client.connected);
        assertEquals(10, client.connectionTry);
    }

    @Test
    @DisplayName("Send exit")
    void sendExitTest() throws IOException {
        TalkClient client = new TalkClient();
        client.communicator = spy(new Communicator(null));
        String userInput = "exit.";

        client.sendUserInput(userInput);

        verify(client.communicator).send(isA(ExitCommand.class));
    }

    @Test
    @DisplayName("Send ping")
    void sendPingTest() throws IOException {
        TalkClient client = new TalkClient();
        client.communicator = spy(new Communicator(null));
        String userInput = "ping.";

        client.sendUserInput(userInput);

        verify(client.communicator).send(isA(PingCommandServer.class));
    }

    @Test
    @DisplayName("Send message")
    void sendMessageTest() throws IOException {
        TalkClient client = new TalkClient();
        client.communicator = spy(new Communicator(null));
        String userInput = "Test";

        client.sendUserInput(userInput);

        verify(client.communicator).send(isA(BroadcastCommand.class));
    }

    private void startServer() {
        try {
            new ServerSocket(2048);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
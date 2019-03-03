package com.app.talk;

import com.app.talk.client.command.set.MessageCommand;
import com.app.talk.client.command.set.RemoteCommandClient;
import com.app.talk.command.Context;
import com.app.talk.command.HeartbeatCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SenderTest {
    private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<>();

    @Test
    @DisplayName("Get next command")
    void getNextCommandTest() {
        startDefaultServer();
        Socket socket = getDefaultSocket();
        Sender sender = Sender.createSender(socket, commandQueue);
        RemoteCommandClient testObject = new MessageCommand("test", new Context());
        commandQueue.offer(testObject);

        Object command = sender.nextCommand();

        assertEquals(testObject, command);
    }

    @Test
    @DisplayName("Get heartbeat command")
    void getHeartbeatCommandTest() {
        startDefaultServer();
        Socket socket = getDefaultSocket();
        Sender sender = Sender.createSender(socket, commandQueue);

        sender.NEXT_COMMAND_TIMEOUT = 10;
        Object command = sender.nextCommand();

        assertTrue(command instanceof HeartbeatCommand);
    }

    private Socket getDefaultSocket() {
        Socket socket = null;

        try {
            socket = new Socket("localhost", 2048);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socket;
    }

    private void startDefaultServer() {
        try {
            new ServerSocket(2048);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

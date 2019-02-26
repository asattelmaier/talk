package com.app.talk;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.app.talk.command.RemoteCommand;
import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;
import com.app.talk.server.command.set.BroadcastCommand;
import com.app.talk.server.command.set.ExitCommand;
import com.app.talk.server.command.set.PingCommandServer;

public class TalkClient {
    boolean connected = false;
    private Socket socket = null;
    private Scanner scanner = new Scanner(System.in);
    Communicator communicator;
    int connectionTry = 0;

    void connect(Config config) {
        try {
            socket = new Socket(config.getRemoteHost(), config.getPort());
            connected = true;
        } catch (Exception e) {
            reconnect(config);
        }
    }

    private void reconnect(Config config) {
        connectionTry++;

        if (connectionTry == 10)
            return;

        clientWait();
        connect(config);
    }

    void clientWait() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startCommunicator() {
        if (!connected)
            return;

        System.out.println("End communication with line = \"exit.\"");
        CommunicatorFactory communicatorFactory = new CommunicatorFactory();
        communicator = communicatorFactory.createCommunicator(socket, CommunicatorFactory.CLIENT);
        communicator.start();
    }

    private void userInputLoop() {
        String userInput;

        while (!Thread.interrupted()) {
            userInput = scanner.nextLine();
            sendUserInput(userInput);
        }
    }

    void sendUserInput(String userInput) {
        boolean isExitCommand = userInput.equals("exit.");
        boolean isPingCommand = userInput.equals("ping.");
        RemoteCommand command;

        if (isExitCommand)
            command = new ExitCommand();
        else if (isPingCommand)
            command = new PingCommandServer();
        else
            command = new BroadcastCommand(userInput);

        communicator.send(command);
    }

    public static void main(String[] args) {
        Config config = ConfigParser.makeConfig(args);

        TalkClient client = new TalkClient();

        client.connect(config);

        client.startCommunicator();

        client.userInputLoop();
    }
}

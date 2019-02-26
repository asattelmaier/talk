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
    public static Communicator communicator;
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

    void startCommunicator() {
        if (!connected)
            return;

        System.out.println("End communication with line = \"exit.\"");
        try {
            communicator = CommunicatorFactory.getInstance().createCommunicator(socket, CommunicatorFactory.CLIENT);
            communicator.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendUserInput() {
        String userInput;
        boolean isExitCommand;
        boolean isPingCommand;
        RemoteCommand command;

        while (!Thread.interrupted()) {
            userInput = scanner.nextLine();
            isExitCommand = userInput.equals("exit.");
            isPingCommand = userInput.equals("ping.");

            if (isExitCommand)
                command = new ExitCommand();
            else if (isPingCommand)
                command = new PingCommandServer();
            else
                command = new BroadcastCommand(userInput);

            communicator.send(command);
        }
    }

    public static void main(String[] args) {
        Config config = ConfigParser.makeConfig(args);

        TalkClient client = new TalkClient();

        client.connect(config);

        client.startCommunicator();

        client.sendUserInput();
    }
}

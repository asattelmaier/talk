package com.app.talk.command;

import java.util.concurrent.ArrayBlockingQueue;

import com.app.talk.client.command.set.RemoteCommandClient;
import com.app.talk.server.command.set.RemoteCommandServer;

public class RemoteCommandProcessor implements Runnable {
    private ArrayBlockingQueue<RemoteCommand> commandQueue = new ArrayBlockingQueue<>(10);
    private final Context context;

    public RemoteCommandProcessor(Context context) {
        this.context = context;
    }

    public void put(RemoteCommand command) {
        try {
            commandQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean isServer = context != null;

        while (true) {
            RemoteCommand command = takeCommand();

            if (isServer) {
                ((RemoteCommandServer) command).execute(context);
            } else {
                ((RemoteCommandClient) command).execute();
            }
        }
    }

    private RemoteCommand takeCommand() {
        RemoteCommand remoteCommand = null;

        try {
            remoteCommand = commandQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return remoteCommand;
    }
}

package com.app.talk.communication;

import java.net.Socket;

import com.app.talk.command.Context;

public class CommunicatorFactory {
    public static final boolean SERVER = true;
    public static final boolean CLIENT = false;

    public Communicator createCommunicator(Socket socket, boolean createServerCommunicator) {
        Communicator communicator = new Communicator(socket);

        if (createServerCommunicator) {
            communicator.context = new Context();
        }

        return communicator;
    }
}

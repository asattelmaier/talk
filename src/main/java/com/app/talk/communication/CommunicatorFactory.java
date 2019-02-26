package com.app.talk.communication;

import java.net.Socket;

import com.app.talk.command.Context;

public class CommunicatorFactory {
    public static final boolean SERVER = true;
    public static final boolean CLIENT = false;

    public Communicator createCommunicator(Socket socket, boolean createServerCommunicator) {
        Communicator communicator;

        if (createServerCommunicator)
            communicator = new Communicator(socket, new Context());
        else
            communicator = new Communicator(socket);

        System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());

        communicator.initialize();

        return communicator;
    }
}

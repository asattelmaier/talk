package com.app.talk;

import com.app.talk.communication.CommunicatorFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The dispatcher waits for clients to connect to its serverSocket and creates a communicator for each connected client.
 */
public class Dispatcher implements Runnable {
    private static ServerSocket server;
    private int port;
    private static boolean acceptClients = true;

    /**
     * Dispatcher constructor.
     *
     * @param port The port to listen to.
     */
    Dispatcher(int port) {
        this.port = port;
    }

    /**
     * Runnable implementation.
     */
    public void run() {
        try {
            this.listen();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    /**
     * Creates communicators for connected clients.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void listen() throws IOException, ClassNotFoundException {
        server = new ServerSocket(this.port);
        System.out.println("Server started. Listening for incoming connection requests on port: " + this.port);


        while (acceptClients) {
            Socket client = server.accept();

            System.out.println("Connection request from " + client.getInetAddress().toString() + ":" + client.getPort());
            CommunicatorFactory.getInstance().createCommunicator(client);
        }
    }
}

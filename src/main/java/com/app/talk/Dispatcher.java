package com.app.talk;

import com.app.talk.communication.CommunicatorFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Dispatcher implements Runnable {
    private static ServerSocket server;
    private static int port;

    public Dispatcher(int port) {
        this.port = port;
        this.run();
    }

    public void run() {
        try {
            this.listen();
        } catch(IOException e) {
            System.err.println(e);
        } catch(ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        server = new ServerSocket(this.port);
        System.out.println("Server started. Listening for incoming connection requests on port: " + this.port);

        while(true) {
            Socket socket = server.accept();
            System.out.println("Connection request from " + socket.getInetAddress().toString() + ":" + socket.getPort());
            CommunicatorFactory.getInstance().createCommunicator(socket);
        }
    }
}

package com.app.talk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.app.talk.command.HeartbeatCommand;
import com.app.talk.command.RemoteCommand;

public class Sender implements Runnable {
    private ObjectOutputStream outputStream;
    private Socket socket;
    private LinkedBlockingQueue<Object> commandQueue;

    public static Sender createSender(Socket socket, LinkedBlockingQueue<Object> commandQueue) {
        Sender sender = new Sender(socket, commandQueue);

        sender.setAndFlushOutputStream();

        return sender;
    }

    public void run() {
        systemOutInfoMessage();

        while (!Thread.currentThread().isInterrupted()) {
            Object command = nextCommand();
            send(command);
        }

        System.out.println("Connection closed.");
    }

    private Sender(Socket socket, LinkedBlockingQueue<Object> commandQueue) {
        this.socket = socket;
        this.commandQueue = commandQueue;
    }

    private void setAndFlushOutputStream() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void systemOutInfoMessage() {
        System.out.println("Connection established to remote " + socket.getInetAddress() + ":"
                + socket.getPort() + " from local address " + socket.getLocalAddress() + ":"
                + socket.getLocalPort());
    }

    private Object nextCommand() {
        Object command = null;

        try {
            command = commandQueue.poll(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (command == null) {
            command = new HeartbeatCommand();
        }

        return command;
    }

    private void send(Object object) {
        try {
            this.outputStream.writeObject(object);
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

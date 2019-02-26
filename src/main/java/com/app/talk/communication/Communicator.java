package com.app.talk.communication;

import com.app.talk.Receiver;
import com.app.talk.Sender;
import com.app.talk.command.Context;
import com.app.talk.command.RemoteCommand;
import com.app.talk.command.RemoteCommandProcessor;

import java.io.IOException;
import java.net.Socket;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

public class Communicator {
    public Context context;
    private Socket socket;
    private RemoteCommandProcessor commandProcessor;
    private Observer observer = (o, arg) -> commandProcessor.put((RemoteCommand) arg);
    private Thread senderThread;
    private Thread receiverThread;
    private Thread remoteCommandProcessorThread;
    private LinkedBlockingQueue<Object> commandQueue = new LinkedBlockingQueue<>();

    public Communicator(Socket socket) {
        this.socket = socket;
    }

    Communicator(Socket socket, Context context) {
        this.socket = socket;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    void initialize() {
        if (socket == null)
            return;

        createSender();
        createReceiver();
        createRemoteCommandProcessor();
    }

    public void start() {
        senderThread.start();
        receiverThread.start();
        remoteCommandProcessorThread.start();
    }

    private void createSender() {
        Sender sender = new Sender(socket, commandQueue);
        senderThread = new Thread(sender);

        senderThread.setName(socket.getLocalPort() + " -> " + socket.getPort() + "-Sender");
    }

    private void createReceiver() {
        Receiver receiver = new Receiver(socket);
        receiverThread = new Thread(receiver);

        receiver.addObserver(observer);

        receiverThread.setName(socket.getLocalPort() + " -> " + socket.getPort() + "-Receiver");
    }

    private void createRemoteCommandProcessor() {
        commandProcessor = new RemoteCommandProcessor(this.context);
        remoteCommandProcessorThread = new Thread(commandProcessor);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        senderThread.interrupt();
        receiverThread.interrupt();
        remoteCommandProcessorThread.interrupt();
    }

    public void send(Object object) {
        commandQueue.offer(object);
    }
}

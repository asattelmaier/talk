package com.app.talk;

import static com.app.talk.TalkException.ErrorCode.*;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
class Talk {
    /**
     * The port this host listens to for incoming messages.
     */
    private int listenPort;
    /**
     * The port this host sends his messages to.
     */
    private int talkPort;
    /**
     * The ip-address of the other host / chatpartner.
     */
    private String remoteHost;
    /**
     * The name chosen by the user.
     */
    private String userName;

    /**
     * A sender of information over the network.
     */
    Talk() {
        this.listenPort = 2048;
        this.talkPort = 2049;
        this.remoteHost = "localhost";
    }

    int getListenPort() {
        return this.listenPort;
    }

    /**
     * A setter for the listening port.
     *
     * @param listenPort - the port to receive messages.
     */
    private void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    int getTalkPort() {
        return this.talkPort;
    }

    /**
     * A setter for the talk port.
     *
     * @param talkPort - the port to send messages.
     */
    private void setTalkPort(int talkPort) {
        this.talkPort = talkPort;
    }

    String getRemoteHost() {
        return this.remoteHost;
    }

    /**
     * A setter for the other hosts ip-address.
     *
     * @param remoteHost - the ip-adress of the other host.
     */
    private void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * A simple method to verify given ports.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     * @return true - if the given ports are valid.
     */
    static boolean validatePorts(String[] args) throws TalkException {
        int argLength = args.length;

        try {
            if (argLength > 0) {
                Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException e) {
            throw new TalkException(INVALID_PORT, args[0]);
        }

        try {
            if (argLength > 1) {
                Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException e) {
            throw new TalkException(INVALID_PORT, args[1]);
        }

        return true;
    }

    /**
     * A simple method to set the given ip-address, source port and destination port if given.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    void setPortsAndRemoteHost(String[] args) {
        int argLength = args.length;

        if (argLength > 0) {
            this.setListenPort(Integer.parseInt(args[0]));
        }
        if (argLength > 1) {
            this.setTalkPort(Integer.parseInt(args[1]));
        }
        if (argLength > 2) {
            this.setRemoteHost(args[2]);
        }
    }

    /**
     * Gets User keyboard input and sets it as the username.
     */
    void setUserNameFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");

        this.userName = scanner.nextLine();
    }

    /**
     * Creates a Sender and a Receiver object.
     */
    void start() {
        Thread receiver = new Thread(new Receiver(this.listenPort));
        Thread sender = new Thread(new Sender(this.remoteHost, this.talkPort, this.userName));

        receiver.start();
        sender.start();

        try {
            sender.join();
        } catch (InterruptedException e) {
            System.out.print(e.getMessage());
        }
    }
}

package com.app.talk;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
    private int listenPort;
    private int talkPort;
    private String remoteHost;
    private String userName;

    /**
     * A sender of information over the network.
     */
    private Talk() {
        this.listenPort = 2048;
        this.talkPort = 2049;
        this.remoteHost = "localhost";
    }

    private void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    private void setTalkPort(int talkPort) {
        this.talkPort = talkPort;
    }

    private void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * Validates given arguments.
     *
     * @param args - arguments transferred from the operating system.
     */
    private static void validateArgs(String[] args) {
        int argLength = args.length;

        if (argLength > 3) {
            System.err.println("To many arguments given. Type 'help' for further Information.");
            System.exit(-1);
        }

        try {
            if (argLength > 0) {
                Integer.parseInt(args[0]);
            }
            if (argLength > 1) {
                Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException e) {
            System.err.println("Given Port is not valid!");
        }
    }

    private void setPortsAndRemoteHost(String[] args) {
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

    private void setUserNameFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");

        this.userName = scanner.nextLine();
    }

    private void start() {
        Receiver receiver = new Receiver(this.listenPort);
        Sender sender = new Sender(this.remoteHost, this.talkPort, this.userName);

        receiver.start();
        sender.start();
    }

    /**
     * A simple talk/chat application.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    public static void main(String[] args) {
        validateArgs(args);

        Talk talk = new Talk();

        talk.setPortsAndRemoteHost(args);

        talk.setUserNameFromUserInput();

        talk.start();
    }
}

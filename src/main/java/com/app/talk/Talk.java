package com.app.talk;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
    private static final int DEFAULT_TALK_PORT = 2049;
    private static final int DEFAULT_LISTEN_PORT = 2048;
    private static final String DEFAULT_IP = "localhost";
    private static int listenPort;
    private static int talkPort;
    private static String remote;

    private static void validateArgs(String[] args) {
        int argLength = args.length;

        if (argLength <= 3) {
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
        } else {
            System.err.println("To many arguments given. Type 'help' for further Information.");
            System.exit(-1);
        }
    }

    private static void setPortsAndRemote(String[] args) {
        listenPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_LISTEN_PORT;
        talkPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_TALK_PORT;
        remote = args.length > 2 ? args[2] : DEFAULT_IP;
    }

    private static String getUserNameFromInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");

        return scanner.nextLine();
    }

    /**
     * A simple talk/chat application.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remote machine to talk to (default: localhost)
     */
    public static void main(String[] args) {
        validateArgs(args);
        setPortsAndRemote(args);

        String userName = getUserNameFromInput();

        Receiver receiver = new Receiver(listenPort);
        Sender sender = new Sender(remote, talkPort, userName);

        receiver.start();
        sender.start();
    }
}

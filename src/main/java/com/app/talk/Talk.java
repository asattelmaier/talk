package com.app.talk;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
    private static final int DEFAULT_TALK_PORT = 2049;
    private static final int DEFAULT_LISTEN_PORT = 2048;
    private static final String DEFAULT_IP = "localhost";

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
        int listenPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_LISTEN_PORT;
        int talkPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_TALK_PORT;
        String remote = args.length > 2 ? args[2] : DEFAULT_IP;

        String userName = getUserNameFromInput();

        Receiver receiver = new Receiver(listenPort);
        Sender sender = new Sender(remote, talkPort, userName);

        receiver.start();
        sender.start();
    }
}

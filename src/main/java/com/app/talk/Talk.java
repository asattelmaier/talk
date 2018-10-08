package com.app.talk;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {

    private Receiver receiver;
    private Sender sender;
    private Scanner scanner = new Scanner(System.in);

    private void start(){
        this.receiver.start();
        this.sender.start();
    }

    private String getUserNameFromInput() {
        return this.scanner.nextLine();
    }

    /**
     * A simple talk/chat application.
     * @param args - arguments transferred from the operating system
     *             args[0]: remote machine to talk to (default: localhost)
     *             args[1]: the port to talk to (default: 2049)            
     *             args[2]: the port to listen to (default: 2048)
     */
    public static void main(String[] args) {
    	String remoteArg = args.length > 0 ? args[0] : "";
    	String talkPortArg = args.length > 1 ? args[1] : "";
    	String listenPortArg = args.length > 2 ? args[2] : "";

    	String remote = remoteArg.length() > 0 ? remoteArg : "localhost";
        int talkPort = talkPortArg.length() > 0 ? Integer.parseInt(talkPortArg) : 2049;
        int listenPort = listenPortArg.length() > 0 ? Integer.parseInt(listenPortArg) : 2048;

        Talk talk = new Talk();

        System.out.print("Enter your username: ");
        String userName = talk.getUserNameFromInput();

        Receiver receiver = new Receiver(listenPort);
        Sender sender = new Sender(remote, talkPort, userName);

        receiver.start();
        sender.start();
    }
}

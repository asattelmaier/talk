package com.app.talk;

import com.app.talk.common.Config;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
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
    private Talk(Config config) {
        this.listenPort = config.getListenPort();
        this.talkPort = config.getTalkPort();
        this.remoteHost = config.getRemoteHost();
    }

    /**
     * A setter for the listening port.
     *
     * @param listenPort - the port to receive messages.
     */
    private void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * A setter for the talk port.
     *
     * @param talkPort - the port to send messages.
     */
    private void setTalkPort(int talkPort) {
        this.talkPort = talkPort;
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
     * Gets User keyboard input and sets it as the username.
     */
    private void setUserNameFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");

        this.userName = scanner.nextLine();
    }

    /**
     * Creates a Sender and a Receiver object.
     */
    private void start() {
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

    /**
     * A simple talk/chat application.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    public static void main(String[] args) {
        Config config = new Config(args);

        Talk talk = new Talk(config);

        talk.setUserNameFromUserInput();

        talk.start();
    }
}

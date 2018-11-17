package com.app.talk;

import com.app.talk.common.Config;

import static com.app.talk.TalkException.ErrorCode.*;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
    private Config config;

    /**
     * The name chosen by the user.
     */
    private String userName;

    /**
     * A sender of information over the network.
     */
    private Talk(Config config) {
        this.config = config;
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
        Receiver receiver = new Receiver(this.config.getListenPort());
        Thread receiverThread = new Thread(receiver);

        Sender sender = new Sender(this.config.getRemoteHost(), this.config.getTalkPort(), this.userName);
        Thread senderThread = new Thread(sender);

        receiverThread.start();
        senderThread.start();

        try {
            senderThread.join();
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
        Config config = new Config();

        config.parseArgumentStrings(args);

        Talk talk = new Talk(config);

        talk.setUserNameFromUserInput();

        talk.start();
    }
}

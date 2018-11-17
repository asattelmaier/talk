package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;

import java.util.Scanner;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
    private Config config;
    private User user;

    /**
     * A sender of information over the network.
     */
    private Talk(Config config, User user) {
        this.config = config;
        this.user = user;
    }

    /**
     * Creates a Sender and a Receiver object.
     */
    private void start() {
        Receiver receiver = new Receiver(this.config.getListenPort());
        Thread receiverThread = new Thread(receiver);

        Sender sender = new Sender(this.config.getRemoteHost(), this.config.getTalkPort(), this.user.getName());
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
        ConfigParser configParser = new ConfigParser();
        User user = new User();
        Config config;
        Talk talk;

        user.setNameFromUserInput();

        try {
            configParser.parseArgumentStrings(args);
        } catch (ConfigParserException e) {
            System.err.println(e.errorMessage());
        }

        config = configParser.getConfig();
        talk = new Talk(config, user);

        talk.start();
    }
}

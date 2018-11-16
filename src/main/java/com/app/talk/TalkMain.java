package com.app.talk;

/**
 * Main Method of a driver for a simple sender of network traffic.
 */
public class TalkMain {
    /**
     * A simple talk/chat application.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    public static void main(String[] args) {
        try {
            Talk.validatePorts(args);

            Talk talk = new Talk();

            talk.setPortsAndRemoteHost(args);

            talk.setUserNameFromUserInput();

            talk.start();
        } catch (TalkException e) {
            System.out.printf("Talk error: %s\n", e.errorMessage());
        }
    }
}

package com.app.talk;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {

    private Receiver receiver;
    private Sender sender;

    private Talk(int listenPort, int talkPort, String remote) {
        this.receiver = new Receiver(listenPort);
        this.sender = new Sender(remote, talkPort);
    }

    private void start(){
        this.receiver.start();
        this.sender.start();
    }

    /**
     * A simple talk/chat application.
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remote machine to talk to (default: localhost)
     */
    public static void main(String[] args) {
        int listenPort = args[0].length() > 0 ? Integer.parseInt(args[0]) : 2048;
        int talkPort = args[1].length() > 0 ? Integer.parseInt(args[1]) : 2049;
        String remote = args[2].length() > 0 ? args[2] : "localhost";

        Talk talk = new Talk(listenPort, talkPort, remote);
        talk.start();
    }
}

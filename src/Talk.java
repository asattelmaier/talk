public class Talk {
    private Talk(int listenPort, int talkPort, String remote) {
        Receiver receiver = new Receiver(listenPort);
        Sender sender = new Sender(remote, talkPort);

        receiver.start();
        sender.start();
    }

    public static void main(String[] args) {
        int listenPort = Integer.parseInt(args[0]);
        int talkPort = Integer.parseInt(args[1]);
        String remote = args[2];

        new Talk(listenPort, talkPort, remote);
    }
}

import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Sender extends Thread {
    private String remote;
    private int port;
    private Socket client = null;
    private DataOutputStream outputStream = null;
    private String userName;
    private Scanner scanner = new Scanner(System.in);

    /**
     * A sender of information over the network.
     *
     * @param remote - - remote machine to talk to.
     * @param port   - - remote port to talk to.
     */
    Sender(String remote, int port) {
        this.remote = remote;
        this.port = port;
    }


    public void run() {
        this.userName = this.getUserNameFromInput();

        System.out.println("Waiting for connection to: " + this.remote + ":" + this.port + "...");
        this.establishConnection();
        System.out.println("Connection established.");

        this.setOutputStream();

        this.sendUserName();

        this.sendUserInput();

        this.closeConnection();
    }

    private String getUserNameFromInput() {
        System.out.print("Enter your username: ");
        return scanner.nextLine();
    }

    private void establishConnection() {
        try {
            this.connect();
        } catch (IOException e) {
            this.reconnect();
        }
    }

    private void connect() throws IOException {
        this.client = new Socket(this.remote, this.port);
    }

    private void reconnect() {
        try {
            TimeUnit.SECONDS.sleep(10);
            this.establishConnection();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void setOutputStream() {
        try {
            this.outputStream = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
    }

    private void sendUserName() {
        this.sendMsg("?userName&" + this.userName);
    }

    private void sendUserInput() {
        String userInput;

        while (true) {
            userInput = scanner.nextLine();

            this.sendMsg(userInput);

            if (Objects.equals("end", userInput)) {
                break;
            }
        }
    }

    private void sendMsg(String msg) {
        try {
            this.outputStream.writeBytes(msg + "\n");
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
    }

    private void closeConnection() {
        try {
            outputStream.close();
            client.close();
            scanner.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
    }
}

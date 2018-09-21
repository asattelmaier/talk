import java.net.*;
import java.io.*;

public class Sender extends Thread {
    private Socket client = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private Boolean connected = false;

    private Sender(String host, int portNumber) {
        try {
            client = new Socket(host, portNumber);
            is = new DataInputStream(client.getInputStream());
            os = new DataOutputStream(client.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }

        connected = client != null && os != null && is != null;
    }

    private void send(String[] args) {
        try {
            for (String s: args) {
                os.writeBytes(s + "\n");
            }
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void receiveData() {
        try {
            String responseLine;
            while ((responseLine = is.readLine()) != null) {
                System.out.println("Server: " + responseLine);
                if (responseLine.contains("Ok")) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void closeConnection()throws IOException {
        is.close();
        os.close();
        client.close();
    }

    public static void main(String[] args) throws IOException {
        Sender sender = new Sender("localhost", 9999);

        if (sender.connected) {
            System.err.println("Connection established.");
            sender.send(args);
            sender.receiveData();
            sender.closeConnection();
        }
    }
}

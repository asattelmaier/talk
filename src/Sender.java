import java.net.*;
import java.io.*;

public class Sender extends Thread {
    private Socket client = null;
    private InputStreamReader inputStream;
    private BufferedReader inputReader = null;
    private DataOutputStream outputStream = null;
    private Boolean connected = false;

    private Sender(String host, int port) {
        try {
            client = new Socket(host, port);
            inputStream = new InputStreamReader(client.getInputStream());
            inputReader = new BufferedReader(inputStream);
            outputStream = new DataOutputStream(client.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }

        connected = client != null && inputReader != null && outputStream != null;
    }

    private void send(String[] args) {
        try {
            for (String s: args) {
                outputStream.writeBytes(s + "\n");
            }
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void receive() {

        try {
            String responseLine;
            while ((responseLine = inputReader.readLine()) != null) {
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
        inputStream.close();
        outputStream.close();
        client.close();
    }

    public static void main(String[] args) throws IOException {
        Sender sender = new Sender("localhost", 9999);

        if (sender.connected) {
            System.err.println("Connection established.");
            sender.send(args);
            sender.receive();
            sender.closeConnection();
        }
    }
}

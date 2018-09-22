import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Sender extends Thread {
    private Socket client = null;
    private InputStreamReader inputStream;
    private BufferedReader inputReader = null;
    private DataOutputStream outputStream = null;
    private Boolean connected = false;
    private String userName;
    private Scanner scanner;

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

    private void handleUserInput() {
        scanner = new Scanner(System.in);
        String response;

        System.out.print("Enter your username: ");

        while (true) {
            if (userName != null) {
                System.out.print(userName + ": ");
            }

            String userInput = scanner.nextLine();

            if (userName == null) {
                setUserName(userInput);
            }

            send(userInput);
            response = receive();
            printResponse(response);

            if (Objects.equals("end", userInput)) {
                break;
            }
        }
    }

    private void setUserName(String newUserName) {
        userName = newUserName;
    }

    private void send(String args) {
        try {
            outputStream.writeBytes(args + "\n");
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private String receive() {
        String serverResponse = "";

        try {
            serverResponse = getServerResponse();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

        return serverResponse;
    }

    private String getServerResponse() throws IOException {
        StringBuilder completeResponse = new StringBuilder();
        String response;

        while ((response = inputReader.readLine()) != null) {
            if (response.contains("Ok")) {
                break;
            }

            completeResponse.append(response);
            completeResponse.append("\n");
        }

        return completeResponse.toString();
    }

    private void printResponse(String response) {
        String[] responseArray = response.split("\n");
        for (String s : responseArray) {
            System.out.println("Server: " + s);
        }
    }

    private void closeConnection() throws IOException {
        inputStream.close();
        outputStream.close();
        client.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Sender sender = new Sender(host, port);

        if (sender.connected) {
            System.out.println("Connection established.");

            sender.handleUserInput();
            sender.closeConnection();
        }
    }
}

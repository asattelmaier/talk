import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Receiver {
    private Boolean connected = false;
    private BufferedReader inputReader = null;
    private PrintStream outputStream = null;

    private Receiver(int port) {
        ServerSocket serverSocket;
        Socket clientSocket = null;
        InputStreamReader inputStream;

        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            inputStream = new InputStreamReader(clientSocket.getInputStream());
            inputReader = new BufferedReader(inputStream);
            outputStream = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

        connected = clientSocket != null && inputReader != null && outputStream != null;
    }

    private void receive() {
        try {
            receiveLoop();
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void receiveLoop() throws IOException {
        while (true) {
            String line = inputReader.readLine();
            System.out.println(line);
            outputStream.println("received");
            outputStream.println("Ok");

            if (Objects.equals(line, "end")) {
                break;
            }
        }
    }

    public static void main(String args[]) {
        Receiver receiver = new Receiver(9999);

        if (receiver.connected) {
            receiver.receive();
        }
    }
}

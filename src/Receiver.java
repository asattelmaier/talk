import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    public static void main(String args[]) {
        ServerSocket echoServer = null;
        String line;
        DataInputStream is;
        PrintStream os;
        Socket clientSocket;

        try {
            echoServer = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            if (echoServer != null) {
                clientSocket = echoServer.accept();
                is = new DataInputStream(clientSocket.getInputStream());
                os = new PrintStream(clientSocket.getOutputStream());

                while (true) {
                    line = is.readLine();
                    System.out.println(line);
                    os.println("received");
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

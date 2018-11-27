package com.app.talk;

import com.app.talk.client.command.set.MessageCommand;
import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;
import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;
import com.app.talk.server.command.set.BroadcastCommand;
import com.app.talk.server.command.set.ExitCommand;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * A client to connect to a TalkServer.
 */
public class TalkClient {
    /**
     * Client socket.
     */
    private Socket socket;
    /**
     * User Object containing username.
     */
    static User user = null;
    
    private Scanner scanner = new Scanner(System.in);
    
    private Communicator client;
    
    /**
     * Client constructor.
     *
     * @param config client configuration
     * @throws IOException
     */
    private TalkClient(Config config) throws IOException {
    	TalkClient.user = new User();
        user.setNameFromUserInput();
        this.socket = new Socket(config.getRemoteHost(), config.getPort());
        
        
    }
    
    /**
     * @throws IOException
     */
    private void init() throws IOException {
        System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());        
        this.client = CommunicatorFactory.getInstance().createCommunicator(socket);
        
    }
    
    /**
     * A method that creates a loop in which the user keyboard input is being taken in and
     * sent to the other Host.
     */
    private void sendUserInput() throws IOException {
        String userInput;
        boolean userExits;

        while (true) {
            userInput = scanner.nextLine();
            userExits = userInput.equals("exit.");

            if (userExits) {
                sendExit();
                break;
            } else {
                sendMessage(userInput);
            }
        }
    }
    
    /**
     * Sends the exit command.
     */
    private void sendExit() throws IOException {
        ExitCommand exitCommand = new ExitCommand();
        this.client.getSender().send(exitCommand);
    }

    /**
     * A method that receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param message - message that should be sent to the other host.
     */
    public void sendMessage(String message) throws IOException {
        BroadcastCommand messageCommand = new BroadcastCommand("[" + TalkClient.user.getName() + "]: " + message);
        this.client.getSender().send(messageCommand);
    }

    
    /**
     * Starts the client.
     *
     * @param args
     * @throws ConfigParserException
     * @throws IOException
     */
    public static void main(String[] args) throws ConfigParserException, IOException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();
        
        
        TalkClient client = new TalkClient(config);
        client.init();
    }
}

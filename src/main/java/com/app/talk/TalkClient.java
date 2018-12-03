package com.app.talk;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;
import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;
import com.app.talk.server.command.set.BroadcastCommand;
import com.app.talk.server.command.set.ExitCommand;

/**
 * A client to connect to a TalkServer.
 */
public class TalkClient {
    /**
     * Client socket.
     */
    private Socket socket = null;
    
    /**
     * User Object containing username.
     */
    static User user = null;
    
    /**
     * Reads keyboard input.
     */
    private Scanner scanner = new Scanner(System.in);
    
    /**
     * Connection Handler for out- and incoming traffic.
     */
    private Communicator communicator;
    
    /**
     * Client constructor.
     *
     * @param config client configuration
     * @throws IOException
     */
    private TalkClient(Config config) throws InterruptedException {
    	user = new User();
        user.setNameFromUserInput();
        connect(config);
    } //constructor
    
    /**
     * @throws IOException
     */
    private void init() throws IOException {
        System.out.println("End communication with line = \"exit.\"");
        this.communicator = CommunicatorFactory.getInstance().createCommunicator(socket);
    } //init
    
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
            	// Steffi: Informs other clients that someone left - only works if you send and received something (dunno why)
            	sendMessage(TalkClient.user.getName() + " has left.");
                sendExit();
                break;
            } else {
                sendMessage(userInput);
            }
        }
    } //sendUserInput
    
    /**
     * Sends the exit command.
     */
    private void sendExit() throws IOException {
        ExitCommand exitCommand = new ExitCommand(this.communicator);        
        // Steffi: Runs exitCommand directly instead of sending it through broadcast
        exitCommand.execute();
    } //sendExit

    /**
     * A method that receives a String message and writes it in sequences of bytes to the other host.
     *
     * @param message - message that should be sent to the other host.
     */
    public void sendMessage(String message) throws IOException {
        BroadcastCommand messageCommand = new BroadcastCommand("[" + TalkClient.user.getName() + "]: " + message);
        this.communicator.getSender().send(messageCommand);
    } //sendMessage
    
    /**
     * establishes the connection to server and tries to reconnect.
     * @param config includes port and ip of server
     * @throws InterruptedException wont happen
     */
    private void connect(Config config) throws InterruptedException  {
    	while(socket == null) {
        	try {
     			this.socket = new Socket(config.getRemoteHost(), config.getPort());
     		} catch (Exception e) {
				TimeUnit.SECONDS.sleep(10);
     		} //try-catch
        } //while  
    } //connect
    
    
    /**
     * Starts the client.
     *
     * @param args
     * @throws ConfigParserException
     * @throws IOException
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws ConfigParserException, IOException, InterruptedException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();
        
        TalkClient client = new TalkClient(config);
        client.init();
        client.sendUserInput();
    } //main
} //TalkClient Class

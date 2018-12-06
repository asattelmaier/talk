package com.app.talk;

import java.io.IOException;
import java.util.ArrayList;

import com.app.talk.client.command.set.MessageCommand;
import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.communication.Communicator;

/**
 * A simple talk server.
 */
public class TalkServer {
    /**
     * Server dispatcher instance.
     */
    private static Dispatcher dispatcher;
    /**
     * stores chat clients, represented by communicator objects.
     */
    private static ArrayList<Communicator> clientList = new ArrayList<Communicator>();
    
    /**
     * Server constructor.
     */
    private TalkServer(Config config) {
        this.dispatcher = new Dispatcher(config.getPort());
    }

    /**
     * Creates and starts a thread for the servers Dispatcher instance.
     */
    private void run() {
        Thread dispatcherThread = new Thread(this.dispatcher);
        dispatcherThread.start();
    }
    
    /**
     * adds a chat client to the list of clients.
     * @param client communicator object representing the specific chat client.
     */
    synchronized public static void addClient(Communicator client) {
    	TalkServer.clientList.add(client);
    }
    
    /**
     * removes a specific chat client from the list of clients.
     * @param client communicator object representing the specific chat client.
     */
    synchronized public static void removeClient(Communicator client) {
    	boolean removed = TalkServer.clientList.remove(client);
    	if (removed) {
			client.close();
    	}
    	if (TalkServer.clientList.size() == 0) {
    		System.out.println("No more clients available - shutting down server.");
    		dispatcher.close();
    	}
    }
    
    /**
     * set the client list
     * @param clientlist the list to set
     * 
     */
    synchronized public static void setClientList(ArrayList<Communicator> clientList) {
    	TalkServer.clientList = clientList;
    }
    
    /**
     * returns the client list of the server.
     * 
     */
    synchronized public static ArrayList<Communicator> getClients() {
    	return TalkServer.clientList;
    }
    
    /**
     * sends a received message to all known chat clients.
     * @param message textual message to be sent.
     * @throws IOException 
     */
    synchronized public static void broadcast(String message) {
    	int counter = 0;
    	System.out.println("Message: \"" + message + "\" received.");
    	for (Communicator communicator : clientList) {
    		try {
				communicator.getSender().send(new MessageCommand(message));
				System.out.println(" -> redirect to client " + counter++);    			
			} catch (Exception e) {
				e.printStackTrace();
			} //try-catch
		} //for
    } //broadcast

    /**
     * Starts the server.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    public static void main(String[] args) throws ConfigParserException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();

        TalkServer talkServer = new TalkServer(config);
        talkServer.run();
    } //main
} //TalkServer Class

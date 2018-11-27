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
    private Dispatcher dispatcher;
    
    private static ArrayList<Communicator> clientList = new ArrayList<Communicator>(); //TODO: make Thread safe, visiblity
    
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
     * @param client 
     */
    synchronized public static void addClient(Communicator client) { //TODO: synchronized may not be thread safe
    	TalkServer.clientList.add(client);
    }
    
    /**
     * @param client
     */
    synchronized public static void removeClient(Communicator client) { //TODO: synchronized may not be thread safe
    	TalkServer.clientList.remove(client);
    }
    
    /**
     * @param message
     * @throws IOException 
     */
    synchronized public static void broadcast(String message) { //TODO: synchronized may not be thread safe
    	int counter = 0;
    	System.out.println("Message: \"" + message + "\" received.");
    	for (Communicator communicator : clientList) {
    		try {
				communicator.getSender().send(new MessageCommand(message));
				System.out.println(" -> redirect to client " + counter++);
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
    }

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
    }
}

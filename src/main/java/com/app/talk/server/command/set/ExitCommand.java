package com.app.talk.server.command.set;

import java.io.IOException;

import com.app.talk.command.RemoteCommand;
import com.app.talk.communication.Communicator;

/**
 * Exit command.
 */
public class ExitCommand implements RemoteCommand {
    private static final long serialVersionUID = 7412994438667771531L;
    
    private Communicator client;
    
    /**
     * A command that removes the client from the clientlist.
     *
     * @param message the message to send
     */
    public ExitCommand(Communicator client){
    	this.client = client;
    }
    
    /**
     * Executes the ExitCommand.
     */
    @Override
    public void execute() {
    	
        System.out.println("You left the server.");
        try {
			client.getSender().closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
} //ExitCommand Class

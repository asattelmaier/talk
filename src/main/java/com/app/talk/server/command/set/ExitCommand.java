package com.app.talk.server.command.set;

import com.app.talk.Dispatcher;
import com.app.talk.command.RemoteCommand;
import com.app.talk.communication.Communicator;

/**
 * Exit command.
 */
public class ExitCommand implements RemoteCommand {
    private static final long serialVersionUID = 7412994438667771531L;
    
    /**
     * Executes the ExitCommand.
     */
    @Override
    public void execute(Communicator communicator) {    	
        Dispatcher.removeClient(communicator);
    }
} //ExitCommand Class

package com.app.talk.client.command.set;

import com.app.talk.command.RemoteCommand;

public interface RemoteCommandClient extends RemoteCommand{
	
    /**
     * Executes the command.
     */
    void execute();
}

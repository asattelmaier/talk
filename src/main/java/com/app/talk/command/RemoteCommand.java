package com.app.talk.command;

import java.io.Serializable;

/**
 * Command pattern implementation.
 */
public interface RemoteCommand extends Serializable {
    long serialVersionUID = 8056449433508589194L;

    /**
     * Executes the command.
     */
    void execute();
}

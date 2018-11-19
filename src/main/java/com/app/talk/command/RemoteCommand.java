package com.app.talk.command;

import java.io.Serializable;

/**
 * Command pattern implementation.
 */
public abstract class RemoteCommand implements Serializable {
    private static final long serialVersionUID = 8056449433508589194L;

    /**
     * Executes the command.
     */
    public abstract void execute();
}

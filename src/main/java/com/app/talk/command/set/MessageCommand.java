package com.app.talk.command.set;

import java.io.Serializable;

/**
 * Represents a command that will write a message to standard output.
 */
public class MessageCommand extends RemoteCommand implements Serializable {
    private static final long serialVersionUID = -6069480497833755234L;
    private String message;

    /**
     * A command that prints a message.
     *
     * @param message the message to send
     */
    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
}

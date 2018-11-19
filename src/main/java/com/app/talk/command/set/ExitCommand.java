package com.app.talk.command.set;

import com.app.talk.command.RemoteCommand;

import java.io.Serializable;

import static com.app.talk.common.SystemExitCode.NORMAL;

/**
 * Exit command.
 */
public class ExitCommand extends RemoteCommand implements Serializable{
    private static final long serialVersionUID = -5750452032948075603L;

    @Override
    public void execute() {
        System.out.println("User left.");
        System.exit(NORMAL.ordinal());
    }
}

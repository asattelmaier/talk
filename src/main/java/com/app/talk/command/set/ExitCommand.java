package com.app.talk.command.set;

import com.app.talk.command.RemoteCommand;

import java.io.Serializable;

import static com.app.talk.common.SystemExitCode.NORMAL;

/**
 * Exit command.
 */
public class ExitCommand extends RemoteCommand {
    private static final long serialVersionUID = 7412994438667771531L;

    @Override
    public void execute() {
        System.out.println("User left.");
        System.exit(NORMAL.ordinal());
    }
}

package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;
import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;

/**
 * A driver for a simple sender of network traffic.
 */
public class Talk {
    private Config config;
    private User user;

    /**
     * A sender of information over the network.
     */
    private Talk(Config config, User user) {
        this.config = config;
        this.user = user;
        CommunicatorFactory.getInstance().createCommunicator(config, user);
    }



    /**
     * A simple talk/chat application.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    public static void main(String[] args) throws ConfigParserException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();

        User user = new User();
        user.setNameFromUserInput();

        Talk talk = new Talk(config, user);
    }
}

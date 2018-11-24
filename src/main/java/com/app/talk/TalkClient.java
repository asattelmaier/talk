package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;
import com.app.talk.common.User;
import com.app.talk.communication.CommunicatorFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * A client to connect to a TalkServer.
 */
public class TalkClient {
    /**
     * Configuration object.
     */
    private Config config;
    /**
     * Client socket.
     */
    private Socket socket;

    /**
     * Client constructor.
     * @param config
     * @throws IOException
     */
    private TalkClient(Config config) throws IOException {
        this.config = config;
        this.socket = new Socket(config.getRemoteHost(), config.getTalkPort());
        System.out.println("Trying to connect to remote " + socket.getInetAddress() + ":" + socket.getPort());
        CommunicatorFactory.getInstance().createCommunicator(socket);
    }

    /**
     * Starts the client.
     * @param args
     * @throws ConfigParserException
     * @throws IOException
     */
    public static void main(String[] args) throws ConfigParserException, IOException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();

        User user = new User();
        user.setNameFromUserInput();
        TalkClient client = new TalkClient(config);
    }
}

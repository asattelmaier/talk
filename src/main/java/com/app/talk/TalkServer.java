package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;

/**
 * A simple talk server.
 */
public class TalkServer {
    /**
     * Configuration object.
     */
    private Config config;
    /**
     * Server dispatcher instance.
     */
    private Dispatcher dispatcher;

    /**
     * Server constructor.
     */
    private TalkServer(Config config) {
        this.config = config;
        this.dispatcher = new Dispatcher(config.getTalkPort());
        this.run();
    }

    /**
     * Creates and starts a thread for the servers Dispatcher instance.
     */
    private void run() {
        Thread dispatcherThread = new Thread(this.dispatcher);
        dispatcherThread.start();
    }

    /**
     * Starts the server.
     *
     * @param args - arguments transferred from the operating system
     *             args[0]: the port to listen to (default: 2048)
     *             args[1]: the port to talk to (default: 2049)
     *             args[2]: remoteHost of the machine to talk to (default: localhost)
     */
    public static void main(String[] args) throws ConfigParserException {
        ConfigParser configParser = new ConfigParser(args);
        Config config = configParser.getConfig();

        TalkServer talkServer = new TalkServer(config);
    }
}

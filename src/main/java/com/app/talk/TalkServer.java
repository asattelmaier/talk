package com.app.talk;

import com.app.talk.common.Config;
import com.app.talk.common.ConfigParser;
import com.app.talk.common.ConfigParserException;

public class TalkServer {
    private Dispatcher dispatcher;

    private TalkServer(Config config) {
        dispatcher = new Dispatcher(config.getPort());
    }

    private void run() {
        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();
    }

    public static void main(String[] args) {
        Config config = ConfigParser.makeConfig(args);

        TalkServer talkServer = new TalkServer(config);
        talkServer.run();
    }
}

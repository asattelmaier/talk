package com.app.talk.communicator;

import com.app.talk.communication.Communicator;
import com.app.talk.communication.CommunicatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class CommunicatorFactoryTest {
    @Test
    @DisplayName("Create Server Communicator")
    void createServerCommunicator() {
        CommunicatorFactory communicatorFactory = new CommunicatorFactory();

        Communicator communicator = communicatorFactory.createCommunicator(null, true);

        assertNotNull(communicator.context);
    }

    @Test
    @DisplayName("Create Client Communicator")
    void createClientCommunicator() {
        CommunicatorFactory communicatorFactory = new CommunicatorFactory();

        Communicator communicator = communicatorFactory.createCommunicator(null, false);

        assertNull(communicator.context);
    }
}

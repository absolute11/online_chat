package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ServerTest {

    @Test
    void testServerStart() {
        Server server = new Server();
        assertDoesNotThrow(server::start);
    }

    @Test
    void testServerStop() {
        Server server = new Server();
        server.stopServer();

    }

    @Test
    void testBroadcastMessage() {
        Server server = new Server();
        assertDoesNotThrow(() -> server.broadcastMessage("Test Message"));
    }

    @Test
    void testSaveMessageToFile() {
        Server server = new Server();
        assertDoesNotThrow(() -> server.saveMessageToFile("Test Message"));
    }

    @Test
    void testUpdateLastActivityTime() {
        Server server = new Server();
        assertDoesNotThrow(server::updateLastActivityTime);
    }
}
package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTest {

    @Test
    void testLoadConfig() {
        Config config = new Config();
        assertEquals(8080, config.getPort());
        assertEquals("localhost", config.getLocalHost());
    }
}
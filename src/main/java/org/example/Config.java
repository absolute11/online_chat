package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.nio.file.*;

public class Config {
    private static final String CONFIG_FILE = "config1.txt";
    private static int port;
    private static String localhost;

    public Config() {
        loadConfig();
    }

    public int getPort() {
        return port;
    }

    public String getLocalHost() {
        return localhost;
    }

    private void loadConfig() {
        Path configPath = Paths.get(CONFIG_FILE);

        if (!Files.isReadable(configPath)) {
            System.err.println("Error: Unable to read the config file. Check file permissions.");
            return;
        }

        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            properties.load(reader);
            String portValue = properties.getProperty("port");
            if (portValue != null && !portValue.isEmpty()) {
                port = Integer.parseInt(portValue.trim());
            }
            localhost = properties.getProperty("localhost");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading configuration");
            e.printStackTrace();
        }
    }
}
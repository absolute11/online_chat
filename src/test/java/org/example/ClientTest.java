package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientTest {

    @Test
    void testClientStart() {
        Client client = new Client();
        // Mocking System.in and System.out
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestUser\nHello\n/exit\n".getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        assertDoesNotThrow(client::start);

        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    void testSaveMessageToFile() {
        // Arrange
        Client client = new Client();
        String message = "Test message";

        try (StringWriter stringWriter = new StringWriter();
             PrintWriter fileWriter = new PrintWriter(stringWriter)) {
            PowerMockito.whenNew(PrintWriter.class)
                    .withParameterTypes(FileWriter.class)
                    .withArguments(any(FileWriter.class))
                    .thenReturn(fileWriter);

            // Act
            Whitebox.invokeMethod(client, "saveMessageToFile", message);

            // Assert
            assertEquals(message + System.lineSeparator(), stringWriter.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFormatMessage() {
        Client client = new Client();
        String clientName = "TestUser";
        String message = "Test message";

        String formattedMessage = client.formatMessage(clientName, message);

        // Assert that the formatted message has the correct format
        assertTrue(formattedMessage.matches("\\[\\d{2}:\\d{2}:\\d{2}\\] TestUser: Test message"));
    }
}



package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
public class Client {
    private static final String EXIT_COMMAND = "/exit";

    public static void main(String[] args) {
        Client chatClient = new Client();
        chatClient.start();
    }

    public void start() {
        Config config = new Config();
        String serverAddress = config.getLocalHost();
        int serverPort = config.getPort();

        try (Socket socket = new Socket(serverAddress, serverPort);
             Scanner scanner = new Scanner(System.in);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();

            // Отправляем имя на сервер
            writer.println(userName);

            // Запускаем поток для чтения сообщений от сервера
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        if (serverMessage.equalsIgnoreCase(EXIT_COMMAND)) {
                            System.out.println("Disconnected from the server.");
                            break;
                        }

                        System.out.println(serverMessage);
                        saveMessageToFile(serverMessage); // Сохраняем сообщение в файл
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Читаем сообщения с консоли и отправляем на сервер
            String message;
            while (true) {
                message = scanner.nextLine();
                if (message.equalsIgnoreCase(EXIT_COMMAND)) {
                    // Отправляем команду exit на сервер и затем закрываем соединение
                    writer.println(EXIT_COMMAND);
                    break;
                }

                // Отправляем сообщение на сервер
                writer.println(message);

                // Форматируем и сохраняем сообщение в файл
                String formattedMessage = formatMessage(userName, message);
                saveMessageToFile(formattedMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMessageToFile(String message) {
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter("client_log.txt", true))) {
            fileWriter.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String formatMessage(String clientName, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        return "[" + timestamp + "] " + clientName + ": " + message;
    }
}
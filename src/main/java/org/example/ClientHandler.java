package org.example;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private final List<ClientHandler> clients;
    private final Server server;
    private String clientName;


    public ClientHandler(Socket clientSocket, Server server, List<ClientHandler> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clients = clients;
        this.server = server;
    }


    @Override
    public void run() {
        try {
            String entername = "Enter your name: ";
            server.broadcastMessage(entername);
            this.clientName = reader.readLine();
            System.out.println(clientName + " connected.");

            // Отправляем старые сообщения клиенту
            sendOldMessages();

            // Отправляем сообщение о новом участнике всем клиентам
            String joinMessage = formatMessage(clientName, "joined the chat");
            server.broadcastMessage(joinMessage);

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("/exit")) {
                    break;
                }

                String formattedMessage = formatMessage(clientName, clientMessage);
                System.out.println(formattedMessage);

                // Отправляем сообщение всем клиентам
                server.broadcastMessage(formattedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void sendOldMessages() throws IOException {
        // Читаем из файла и отправляем старые сообщения
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream("file.log")))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                sendMessage(line);
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    String formatMessage(String clientName, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        return "[" + timestamp + "] " + clientName + ": " + message;
    }

    void closeConnection() {
        try {
            clients.remove(this); // Удаляем текущего клиента из списка
            clientSocket.close();
            String leaveMessage = formatMessage(clientName, "left the chat");
            server.broadcastMessage(leaveMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

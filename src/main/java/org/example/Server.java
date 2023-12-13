package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Server {
    private List<ClientHandler> clients = new ArrayList<>();
    private static final int TIMEOUT_MILLISECONDS = 60000; // 1 минута
    private long lastActivityTime;
    private volatile boolean isServerRunning = true;

    public void start() {
        Config config = new Config();
        int port = config.getPort();

        // Очистка файла file.log перед запуском сервера
        clearLogFile();


        ExecutorService executorService = Executors.newFixedThreadPool(10);


        new Thread(() -> checkActivityPeriodically(executorService)).start();


        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server is running on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New connection from " + clientSocket.getInetAddress().getHostAddress());


                    ClientHandler clientHandler = new ClientHandler(clientSocket, this, clients);
                    clients.add(clientHandler);
                    executorService.execute(clientHandler);

                    // Обновляем время последней активности
                    lastActivityTime = System.currentTimeMillis();
                    if (System.in.available() > 0) {

                        stopServer();
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {

                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopServer() {
        isServerRunning = false;
    }

    private void clearLogFile() {
        try (PrintWriter fileWriter = new PrintWriter("file.log")) {
            // Очищаем содержимое файла
            fileWriter.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }


        saveMessageToFile(message);
    }

    public void saveMessageToFile(String message) {
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter("file.log", true))) {
            fileWriter.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLastActivityTime() {
        lastActivityTime = System.currentTimeMillis();
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    private void checkActivityPeriodically(ExecutorService executorService) {
        while (isServerRunning) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastActivityTime > TIMEOUT_MILLISECONDS) {
                // Если прошло больше TIMEOUT_MILLISECONDS миллисекунд без активности, устанавливаем флаг в false
                isServerRunning = false;
                System.out.println("No active clients for a while. Shutting down the server.");
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
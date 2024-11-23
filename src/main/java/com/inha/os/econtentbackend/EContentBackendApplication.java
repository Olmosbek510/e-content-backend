package com.inha.os.econtentbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class EContentBackendApplication {
    public static void main(String[] args) throws Exception {
        startSocketServer();
        SpringApplication.run(EContentBackendApplication.class, args);
    }

    private static void startSocketServer() {
        int port = 9090;
        ExecutorService executorService = Executors.newCachedThreadPool();

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Socket server is running on port " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected!");
                    executorService.submit(() -> handleClient(clientSocket));
                }
            } catch (IOException e) {
                System.err.println("Error starting server: " + e.getMessage());
            }
        }).start();
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String clientData;
            while ((clientData = in.readLine()) != null) {
                System.out.println("Received from client: " + clientData);

                // Process the data (convert to uppercase)
                String responseData = "Processed: " + clientData.toUpperCase();

                // Send response to the client
                out.write(responseData);
                out.newLine();
                out.flush();

                System.out.println("Response sent to client: " + responseData);
            }

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
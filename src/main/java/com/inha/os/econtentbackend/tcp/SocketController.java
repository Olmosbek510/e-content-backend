package com.inha.os.econtentbackend.tcp;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.inha.os.econtentbackend.dispatcher.CentralizedDispatcher;
import com.inha.os.econtentbackend.dto.request.RequestDto;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketController implements CommandLineRunner {
    private final CentralizedDispatcher centralizedDispatcher;

    @Value("${backend.socket.port}")
    private int socketPort; // Port on which this backend listens for socket connections

    private final Gson gson; // JSON parser

    @Override
    public void run(String... args) {
        try (ServerSocket serverSocket = new ServerSocket(socketPort, 50, InetAddress.getByName("0.0.0.0"))) {
            log.info("Backend is listening on port {}", socketPort);

            // Continuously accept new client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Client connected: {}", clientSocket.getRemoteSocketAddress());

                // Handle each connection in a separate thread
                new Thread(() -> handleClientConnection(clientSocket)).start();
            }
        } catch (IOException e) {
            log.error("Error starting server", e);
        }
    }

    private void handleClientConnection(Socket clientSocket) {
        try (
                // Use try-with-resources to ensure streams are closed properly
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String rawMessage;

            // Read line by line: each line should contain a valid JSON object
            while ((rawMessage = reader.readLine()) != null) {
                // Log received JSON
                log.info("Received JSON payload:\n{}", rawMessage);

                // Skip empty lines (in case of stray newlines)
                if (rawMessage.trim().isEmpty()) {
                    continue;
                }

                try {
                    // Parse the JSON payload into a RequestDto
                    RequestDto requestDto = gson.fromJson(rawMessage, RequestDto.class);

                    // Process the request using the dispatcher
                    String response = centralizedDispatcher.dispatch(
                            requestDto.getEntity(),
                            requestDto.getAction(),
                            requestDto.getData(),
                            requestDto.getToken()
                    );

                    // Send the response back to the client
                    log.info("Sending response:\n{}", response);
                    sendResponse(writer, response);

                } catch (JsonSyntaxException e) {
                    // Handle malformed JSON
                    log.error("Invalid JSON format", e);
                    String errorResponse = ExceptionUtils.respondWithError(
                            ResponseStatus.ERROR,
                            "Error: Invalid JSON format"
                    );
                    sendResponse(writer, errorResponse);

                } catch (Exception e) {
                    // Handle any other exceptions in your dispatcher
                    log.error("Error processing request", e);
                    String errorResponse = ExceptionUtils.respondWithError(
                            ResponseStatus.ERROR,
                            "Error: Processing failed"
                    );
                    sendResponse(writer, errorResponse);
                }
            }

        } catch (IOException e) {
            log.error("Error handling client connection", e);
        } finally {
            // Close the socket when done
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.error("Error closing client socket", e);
            }
            log.info("Client disconnected: {}", clientSocket.getRemoteSocketAddress());
        }
    }

    /**
     * Helper method to send a JSON response back to the client.
     * Expects the response to be a valid JSON string.
     */
    private void sendResponse(BufferedWriter writer, String response) throws IOException {
        writer.write(response);
        writer.newLine(); // Ensure newline termination for the client to recognize message boundaries
        writer.flush(); // Force data to be written immediately
    }
}
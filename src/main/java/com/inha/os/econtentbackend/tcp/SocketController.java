package com.inha.os.econtentbackend.tcp;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dispatcher.CentralizedDispatcher;
import com.inha.os.econtentbackend.dto.request.RequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketController implements CommandLineRunner {
    private final CentralizedDispatcher centralizedDispatcher;
    @Value("${backend.socket.port}")
    private int socketPort;
    private final Gson gson;

    @Override
    public void run(String... args) {
        try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
            System.out.println("Backend is listening on port " + socketPort);
            while (true) {
                Socket bridgeSocket = serverSocket.accept();
                System.out.println("Bridge connected!");

                new Thread(() -> handleBridgeConnection(bridgeSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBridgeConnection(Socket bridgeSocket) {
        try (InputStream input = bridgeSocket.getInputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bridgeSocket.getOutputStream()))) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            StringBuilder receivedData = new StringBuilder();

            log.info("Waiting to receive data...");

            // Read data from the socket input stream
            while ((bytesRead = input.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, bytesRead);
                receivedData.append(chunk);

                // Stop reading when <<EOF>> is found
                if (receivedData.toString().contains("<<EOF>>")) {
                    break;
                }
            }

            // Clean up the received message by removing the <<EOF>> marker
            String rawMessage = receivedData.toString();
            String cleanedMessage = cleanMessage(rawMessage);
            log.info("Complete JSON payload received:\n{}", cleanedMessage);

            // Deserialize and process the cleaned JSON
            try {
                RequestDto requestDTO = gson.fromJson(cleanedMessage, RequestDto.class);

                // Dispatch the request to your centralized handler
                String response = centralizedDispatcher.dispatch(
                        requestDTO.getEntity(),
                        requestDTO.getAction(),
                        requestDTO.getData(),
                        requestDTO.getToken()
                );

                // Send the response back to the bridge
                log.info("Sending response:\n{}", response);
                writer.write(response);
                writer.newLine();
                writer.flush();
            } catch (Exception e) {
                log.error("Error deserializing JSON or processing the request", e);
                writer.write("Error: Invalid JSON or processing error");
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            log.error("Error handling bridge connection", e);
        }
    }

    private String cleanMessage(String rawMessage) {
        // Remove the <<EOF>> marker
        String withoutEOF = rawMessage.replace("<<EOF>>", "").trim();

        // Use a dynamic approach to identify and validate JSON structure
        StringBuilder jsonPayload = new StringBuilder();
        boolean isInJson = false;

        for (String line : withoutEOF.split("\n")) {
            line = line.trim();

            // Start capturing JSON when we encounter an opening brace
            if (line.startsWith("{")) {
                isInJson = true;
            }

            if (isInJson) {
                jsonPayload.append(line).append("\n");
            }

            // Stop capturing JSON when we encounter a closing brace
            if (line.endsWith("}")) {
                isInJson = false; // Ensures we capture only one JSON block
            }
        }

        return jsonPayload.toString().trim();
    }

}

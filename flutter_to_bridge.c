#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define FLUTTER_PORT 7000   // Port on which this program listens for Flutter connections
#define BRIDGE_HOST "127.0.0.1"
#define BRIDGE_PORT 9000
#define BUFFER_SIZE 4096

int main() {
    int flutter_server_socket, flutter_client_socket;
    struct sockaddr_in flutter_server_addr, flutter_client_addr;
    socklen_t flutter_client_addr_len = sizeof(flutter_client_addr);

    // Create a server socket to accept connections from Flutter
    flutter_server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (flutter_server_socket == -1) {
        perror("Flutter server socket creation failed");
        exit(EXIT_FAILURE);
    }

    flutter_server_addr.sin_family = AF_INET;
    flutter_server_addr.sin_addr.s_addr = INADDR_ANY;
    flutter_server_addr.sin_port = htons(FLUTTER_PORT);

    if (bind(flutter_server_socket, (struct sockaddr *)&flutter_server_addr, sizeof(flutter_server_addr)) < 0) {
        perror("Bind failed for Flutter server");
        close(flutter_server_socket);
        exit(EXIT_FAILURE);
    }

    if (listen(flutter_server_socket, 5) < 0) {
        perror("Listen failed for Flutter server");
        close(flutter_server_socket);
        exit(EXIT_FAILURE);
    }

    printf("Listening for Flutter on port %d...\n", FLUTTER_PORT);

    // Accept a single Flutter client connection (you can enhance this for multiple clients if needed)
    flutter_client_socket = accept(flutter_server_socket, (struct sockaddr *)&flutter_client_addr, &flutter_client_addr_len);
    if (flutter_client_socket < 0) {
        perror("Accept failed for Flutter client");
        close(flutter_server_socket);
        exit(EXIT_FAILURE);
    }

    printf("Flutter client connected!\n");

    // Connect to the socket bridge
    int bridge_socket;
    struct sockaddr_in bridge_addr;

    bridge_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (bridge_socket == -1) {
        perror("Socket creation failed for bridge");
        close(flutter_client_socket);
        close(flutter_server_socket);
        exit(EXIT_FAILURE);
    }

    bridge_addr.sin_family = AF_INET;
    bridge_addr.sin_port = htons(BRIDGE_PORT);
    bridge_addr.sin_addr.s_addr = inet_addr(BRIDGE_HOST);

    if (connect(bridge_socket, (struct sockaddr *)&bridge_addr, sizeof(bridge_addr)) < 0) {
        perror("Connection to bridge failed");
        close(flutter_client_socket);
        close(flutter_server_socket);
        close(bridge_socket);
        exit(EXIT_FAILURE);
    }

    printf("Connected to the socket bridge.\n");

    char flutter_buffer[BUFFER_SIZE];
    char bridge_buffer[BUFFER_SIZE];
    int bytes_received;

    // Communication loop
    // 1. Receive JSON from Flutter
    // 2. Send JSON to bridge
    // 3. Receive response from bridge
    // 4. Send response back to Flutter

    while (1) {
        // Receive JSON request from Flutter
        bytes_received = recv(flutter_client_socket, flutter_buffer, BUFFER_SIZE, 0);
        if (bytes_received <= 0) {
            // Flutter closed connection
            break;
        }

        flutter_buffer[bytes_received] = '\0';
        printf("Received from Flutter: %s\n", flutter_buffer);

        // Forward the received JSON to the bridge
        if (send(bridge_socket, flutter_buffer, bytes_received, 0) < 0) {
            perror("Failed to send data to bridge");
            break;
        }

        // Now receive the response from the bridge
        bytes_received = recv(bridge_socket, bridge_buffer, BUFFER_SIZE, 0);
        if (bytes_received <= 0) {
            // Bridge closed connection or error
            perror("Failed to receive data from bridge");
            break;
        }

        bridge_buffer[bytes_received] = '\0';
        printf("Received from Bridge: %s\n", bridge_buffer);

        // Send the bridge response back to Flutter
        if (send(flutter_client_socket, bridge_buffer, bytes_received, 0) < 0) {
            perror("Failed to send data back to Flutter");
            break;
        }

        // The loop continues, allowing Flutter to send multiple requests and receive responses
    }

    close(bridge_socket);
    close(flutter_client_socket);
    close(flutter_server_socket);
    return 0;
}

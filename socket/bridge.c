#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

// Define constants for ports and buffer size
#define CLIENT_PORT 9000          // Port where Flutter connects
#define BACKEND_IP "192.168.1.122" // IP address of the backend (remote)
#define BACKEND_PORT 9090         // Port of the backend
#define BUFFER_SIZE 4096          // Buffer size for messages

int main() {
    int server_socket, client_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_addr_len = sizeof(client_addr);

    // Step 1: Create a server socket to listen for connections from Flutter
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    // Configure server socket address
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY; // Accept connections from any address
    server_addr.sin_port = htons(CLIENT_PORT);

    // Bind the server socket to the specified port
    if (bind(server_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    // Start listening for incoming connections
    if (listen(server_socket, 5) < 0) {
        perror("Listen failed");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    printf("Listening for Flutter app on port %d...\n", CLIENT_PORT);

    // Main loop to accept connections from Flutter
    while ((client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_addr_len)) >= 0) {
        printf("Received connection from Flutter app!\n");

        // Step 2: Create a socket to connect to the backend
        int backend_socket;
        struct sockaddr_in backend_addr;

        backend_socket = socket(AF_INET, SOCK_STREAM, 0);
        if (backend_socket == -1) {
            perror("Backend socket creation failed");
            close(client_socket);
            continue;
        }

        // Configure backend socket address
        backend_addr.sin_family = AF_INET;
        backend_addr.sin_port = htons(BACKEND_PORT);
        backend_addr.sin_addr.s_addr = inet_addr(BACKEND_IP); // Backend IP address

        // Connect to the backend
        if (connect(backend_socket, (struct sockaddr *)&backend_addr, sizeof(backend_addr)) < 0) {
            perror("Connection to backend failed");
            close(client_socket);
            close(backend_socket);
            continue;
        }

        printf("Connected to Backend at %s:%d\n", BACKEND_IP, BACKEND_PORT);

        char client_buffer[BUFFER_SIZE];
        char backend_buffer[BUFFER_SIZE];
        int bytes_received;

        // Step 3: Communication loop between Flutter and Backend
        while ((bytes_received = recv(client_socket, client_buffer, BUFFER_SIZE, 0)) > 0) {
            client_buffer[bytes_received] = '\0'; // Null-terminate the received message
            printf("Received from Flutter app: %s\n", client_buffer);

            // Forward the message to the backend
            if (send(backend_socket, client_buffer, bytes_received, 0) < 0) {
                perror("Failed to send data to backend");
                break;
            } else {
                printf("Message forwarded to backend.\n");
            }

            // Receive the response from the backend
            bytes_received = recv(backend_socket, backend_buffer, BUFFER_SIZE, 0);
            if (bytes_received <= 0) {
                perror("Failed to receive response from backend");
                break;
            }
            backend_buffer[bytes_received] = '\0'; // Null-terminate the response
            printf("Received from backend: %s\n", backend_buffer);

            // Send the backend's response back to Flutter
            if (send(client_socket, backend_buffer, bytes_received, 0) < 0) {
                perror("Failed to send response to Flutter app");
                break;
            } else {
                printf("Response sent back to Flutter app.\n");
            }
        }

        // Clean up sockets
        close(client_socket);
        close(backend_socket);
        printf("Client connection closed.\n");
    }

    // Close the server socket
    close(server_socket);
    printf("Server shut down.\n");

    return 0;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define CLIENT_PORT 9000   // Port for Flutter client to connect
#define BACKEND_PORT 9090  // Port to connect to Spring Boot backend
#define BUFFER_SIZE 4096

// Connect to the backend
int connect_to_backend() {
    int backend_socket;
    struct sockaddr_in backend_addr;

    backend_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (backend_socket == -1) {
        perror("Backend socket creation failed");
        return -1;
    }

    backend_addr.sin_family = AF_INET;
    backend_addr.sin_port = htons(BACKEND_PORT);
    backend_addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Backend is on the same machine

    if (connect(backend_socket, (struct sockaddr *)&backend_addr, sizeof(backend_addr)) < 0) {
        perror("Connection to backend failed");
        close(backend_socket);
        return -1;
    }

    printf("Connected to Backend at port %d\n", BACKEND_PORT);
    return backend_socket;
}

// Handle client request
void handle_client_request(int client_socket, int *backend_socket) {
    char client_buffer[BUFFER_SIZE];
    char backend_buffer[BUFFER_SIZE];
    int bytes_received;

    // Read data from the Flutter client
    while ((bytes_received = recv(client_socket, client_buffer, BUFFER_SIZE - 1, 0)) > 0) {
        client_buffer[bytes_received] = '\0'; // Null-terminate the received data
        printf("\nReceived from Flutter client:\n%s\n", client_buffer);

        // Ensure the data is treated as a JSON string and add a newline
        char json_to_send[BUFFER_SIZE];
        snprintf(json_to_send, sizeof(json_to_send), "%s\n", client_buffer); // Append newline

        // Reconnect to the backend if needed
        if (*backend_socket == -1) {
            printf("Backend connection lost. Attempting to reconnect...\n");
            *backend_socket = connect_to_backend();
            if (*backend_socket == -1) {
                printf("Failed to reconnect to backend. Dropping client request.\n");
                break;
            }
        }

        // Forward the JSON string (with newline) to the backend
        if (send(*backend_socket, json_to_send, strlen(json_to_send), 0) == -1) {
            perror("Failed to send JSON to backend");
            close(*backend_socket);
            *backend_socket = -1; // Mark backend as disconnected
            break;
        } else {
            printf("JSON successfully forwarded to backend:\n%s\n", json_to_send);
        }

        // Read response from the backend
        bytes_received = recv(*backend_socket, backend_buffer, BUFFER_SIZE - 1, 0);
        if (bytes_received <= 0) {
            perror("Failed to receive response from backend");
            close(*backend_socket);
            *backend_socket = -1; // Mark backend as disconnected
            break;
        }
        backend_buffer[bytes_received] = '\0'; // Null-terminate the backend response
        printf("\nReceived from Backend:\n%s\n", backend_buffer);

        // Send the response back to the Flutter client
        if (send(client_socket, backend_buffer, bytes_received, 0) == -1) {
            perror("Failed to send response to client");
            break;
        } else {
            printf("Response successfully sent back to Flutter client.\n");
        }
    }

    close(client_socket);
    printf("Client connection closed.\n");
}

int main() {
    int server_socket, client_socket, backend_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_addr_len = sizeof(client_addr);

    // Connect to the backend at startup
    backend_socket = connect_to_backend();
    if (backend_socket == -1) {
        fprintf(stderr, "Failed to connect to backend. Exiting...\n");
        exit(EXIT_FAILURE);
    }

    // Create server socket for Flutter client connections
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Socket creation failed");
        close(backend_socket);
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(CLIENT_PORT);

    if (bind(server_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        close(server_socket);
        close(backend_socket);
        exit(EXIT_FAILURE);
    }

    if (listen(server_socket, 5) < 0) {
        perror("Listen failed");
        close(server_socket);
        close(backend_socket);
        exit(EXIT_FAILURE);
    }

    printf("Socket bridge is listening on port %d...\n", CLIENT_PORT);

    // Main loop to accept and handle client connections
    while (1) {
        client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_addr_len);
        if (client_socket < 0) {
            perror("Client accept failed");
            continue;
        }

        printf("Client connected! Handling request...\n");

        handle_client_request(client_socket, &backend_socket);
    }

    close(server_socket);
    close(backend_socket);
    return 0;
}
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define CLIENT_PORT 9000   // Port for client C to connect
#define BACKEND_PORT 9090  // Port to connect to backend
#define BUFFER_SIZE 4096

int main() {
    int server_socket, client_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_addr_len = sizeof(client_addr);

    // Create server socket for the client
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(CLIENT_PORT);

    if (bind(server_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    if (listen(server_socket, 5) < 0) {
        perror("Listen failed");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    printf("Socket bridge is listening on port %d...\n", CLIENT_PORT);

    while ((client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_addr_len)) >= 0) {
        printf("Client connected! Handling request...\n");

        // Connect to the backend
        int backend_socket;
        struct sockaddr_in backend_addr;

        backend_socket = socket(AF_INET, SOCK_STREAM, 0);
        if (backend_socket == -1) {
            perror("Backend socket creation failed");
            close(client_socket);
            continue;
        }

        backend_addr.sin_family = AF_INET;
        backend_addr.sin_port = htons(BACKEND_PORT);
        backend_addr.sin_addr.s_addr = inet_addr("127.0.0.1");

        if (connect(backend_socket, (struct sockaddr *)&backend_addr, sizeof(backend_addr)) < 0) {
            perror("Connection to backend failed");
            close(client_socket);
            close(backend_socket);
            continue;
        }

        printf("Connected to Backend\n");

        char client_buffer[BUFFER_SIZE];
        char backend_buffer[BUFFER_SIZE];
        int bytes_received;

        // Communication loop
        while ((bytes_received = recv(client_socket, client_buffer, BUFFER_SIZE, 0)) > 0) {
            client_buffer[bytes_received] = '\0';
            printf("Received from client: %s\n", client_buffer);

            // Forward to backend
            if (send(backend_socket, client_buffer, bytes_received, 0) == -1) {
                perror("Failed to send data to backend");
                break;
            } else {
                printf("Message successfully sent to backend.\n");
            }

            // Receive response from backend
            bytes_received = recv(backend_socket, backend_buffer, BUFFER_SIZE, 0);
            if (bytes_received <= 0) {
                perror("Failed to receive response from backend");
                break;
            }
            backend_buffer[bytes_received] = '\0';
            printf("Received from backend: %s\n", backend_buffer);

            // Send backend response back to client
            if (send(client_socket, backend_buffer, bytes_received, 0) == -1) {
                perror("Failed to send response to client");
                break;
            } else {
                printf("Response successfully sent to client.\n");
            }
        }

        close(client_socket);
        close(backend_socket);
        printf("Client connection closed.\n");
    }

    close(server_socket);
    return 0;
}

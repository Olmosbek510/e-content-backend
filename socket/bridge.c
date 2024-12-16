#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define CLIENT_PORT 9000   // Port for Flutter client to connect
#define BACKEND_PORT 9090  // Port for Backend connection
#define BUFFER_SIZE 4096

int main() {
    int server_socket, flutter_socket, backend_socket;
    struct sockaddr_in server_addr, flutter_addr, backend_addr;
    socklen_t addr_len = sizeof(struct sockaddr_in);

    // Create server socket for Flutter client
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY; // Allow connections from any IP
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

    printf("Bridge is listening for Flutter client on port %d...\n", CLIENT_PORT);

    // Accept Flutter connection
    flutter_socket = accept(server_socket, (struct sockaddr *)&flutter_addr, &addr_len);
    if (flutter_socket < 0) {
        perror("Failed to accept Flutter connection");
        close(server_socket);
        exit(EXIT_FAILURE);
    }
    printf("Flutter client connected.\n");

    // Connect to Backend
    backend_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (backend_socket == -1) {
        perror("Backend socket creation failed");
        close(flutter_socket);
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    backend_addr.sin_family = AF_INET;
    backend_addr.sin_port = htons(BACKEND_PORT);
    backend_addr.sin_addr.s_addr = inet_addr("BACKEND_MACHINE_IP"); // Replace with backend machine's IP

    printf("Connecting to backend on port %d...\n", BACKEND_PORT);
    if (connect(backend_socket, (struct sockaddr *)&backend_addr, sizeof(backend_addr)) < 0) {
        perror("Connection to backend failed");
        close(flutter_socket);
        close(server_socket);
        close(backend_socket);
        exit(EXIT_FAILURE);
    }
    printf("Backend connected.\n");

    char flutter_buffer[BUFFER_SIZE];
    char backend_buffer[BUFFER_SIZE];
    int bytes_received;

    // Communication loop
    while (1) {
        // Receive JSON from Flutter
        bytes_received = recv(flutter_socket, flutter_buffer, BUFFER_SIZE, 0);
        if (bytes_received <= 0) {
            printf("Flutter disconnected.\n");
            break;
        }
        flutter_buffer[bytes_received] = '\0';
        printf("Received from Flutter: %s\n", flutter_buffer);

        // Forward JSON to Backend
        if (send(backend_socket, flutter_buffer, bytes_received, 0) == -1) {
            perror("Failed to send data to backend");
            break;
        }

        // Receive JSON response from Backend
        bytes_received = recv(backend_socket, backend_buffer, BUFFER_SIZE, 0);
        if (bytes_received <= 0) {
            printf("Backend disconnected.\n");
            break;
        }
        backend_buffer[bytes_received] = '\0';
        printf("Received from Backend: %s\n", backend_buffer);

        // Send JSON response back to Flutter
        if (send(flutter_socket, backend_buffer, bytes_received, 0) == -1) {
            perror("Failed to send data to Flutter");
            break;
        }
    }

    close(flutter_socket);
    close(backend_socket);
    close(server_socket);
    return 0;
}

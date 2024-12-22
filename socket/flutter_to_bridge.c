#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define CLIENT_PORT 9000   // Port for Flutter client to connect
#define BACKEND_PORT 9090  // Port to connect to Spring Boot backend
#define BUFFER_SIZE 4096

// Global backend socket (shared among threads)
int backend_socket;

// Mutex for backend socket access
pthread_mutex_t backend_mutex = PTHREAD_MUTEX_INITIALIZER;

// Connect to the backend
int connect_to_backend() {
    int socket_fd;
    struct sockaddr_in backend_addr;

    socket_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_fd == -1) {
        perror("Backend socket creation failed");
        return -1;
    }

    backend_addr.sin_family = AF_INET;
    backend_addr.sin_port = htons(BACKEND_PORT);
    backend_addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Backend is on the same machine

    if (connect(socket_fd, (struct sockaddr *)&backend_addr, sizeof(backend_addr)) < 0) {
        perror("Connection to backend failed");
        close(socket_fd);
        return -1;
    }

    printf("Connected to Backend at port %d\n", BACKEND_PORT);
    return socket_fd;
}

// Handle client request
void *client_handler(void *arg) {
    int client_socket = *(int *)arg;
    free(arg); // Free memory allocated for client socket

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

        // Lock the backend socket for thread-safe access
        pthread_mutex_lock(&backend_mutex);

        // Forward the JSON string (with newline) to the backend
        if (send(backend_socket, json_to_send, strlen(json_to_send), 0) == -1) {
            perror("Failed to send JSON to backend");
            pthread_mutex_unlock(&backend_mutex); // Unlock before breaking
            break;
        } else {
            printf("JSON successfully forwarded to backend:\n%s\n", json_to_send);
        }

        // Read response from the backend
        bytes_received = recv(backend_socket, backend_buffer, BUFFER_SIZE - 1, 0);
        if (bytes_received <= 0) {
            perror("Failed to receive response from backend");
            pthread_mutex_unlock(&backend_mutex); // Unlock before breaking
            break;
        }
        backend_buffer[bytes_received] = '\0'; // Null-terminate the backend response
        pthread_mutex_unlock(&backend_mutex); // Unlock after backend operations

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
    pthread_exit(NULL);
}

int main() {
    int server_socket;
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
        int *client_socket = malloc(sizeof(int));
        if (!client_socket) {
            perror("Memory allocation failed");
            continue;
        }

        *client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_addr_len);
        if (*client_socket < 0) {
            perror("Client accept failed");
            free(client_socket);
            continue;
        }

        printf("Client connected! Spawning a new thread to handle request...\n");

        // Create a new thread to handle the client
        pthread_t thread_id;
        if (pthread_create(&thread_id, NULL, client_handler, client_socket) != 0) {
            perror("Thread creation failed");
            close(*client_socket);
            free(client_socket);
            continue;
        }

        pthread_detach(thread_id); // Detach the thread for automatic cleanup
    }

    close(server_socket);
    close(backend_socket);
    return 0;
}
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define BRIDGE_HOST "127.0.0.1"  // Host address for the bridge
#define BRIDGE_PORT 9000         // Port for the bridge
#define BUFFER_SIZE 4096         // Buffer size for input and response

int main() {
    int client_socket;
    struct sockaddr_in server_addr;

    // Create socket
    client_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (client_socket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    // Configure server address
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(BRIDGE_PORT);
    server_addr.sin_addr.s_addr = inet_addr(BRIDGE_HOST);

    // Connect to the socket bridge
    if (connect(client_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Connection to bridge failed");
        close(client_socket);
        exit(EXIT_FAILURE);
    }

    printf("Connected to the socket bridge.\n");

    char buffer[BUFFER_SIZE];
    char recv_buffer[BUFFER_SIZE];
    int bytes_received;

    while (1) {
        printf("Enter multi-line JSON (end input with '<<EOF>>'):\n");

        // Clear the buffer for new input
        memset(buffer, 0, BUFFER_SIZE);

        // Collect multi-line input from the user
        char line[BUFFER_SIZE];
        while (1) {
            if (fgets(line, BUFFER_SIZE, stdin) == NULL) {
                break; // End of input
            }

            // Check for end of input
            if (strcmp(line, "<<EOF>>\n") == 0) {
                break;
            }

            // Append line to the buffer
            if (strlen(buffer) + strlen(line) >= BUFFER_SIZE) {
                fprintf(stderr, "Input too large! Aborting.\n");
                close(client_socket);
                exit(EXIT_FAILURE);
            }
            strcat(buffer, line);
        }

        // Append <<EOF>> explicitly to ensure backend recognizes end of message
        strcat(buffer, "<<EOF>>\n");

        // Send the collected input to the bridge
        if (send(client_socket, buffer, strlen(buffer), 0) < 0) {
            perror("Failed to send data");
            close(client_socket);
            exit(EXIT_FAILURE);
        }

        // Wait for response from the backend
        memset(recv_buffer, 0, BUFFER_SIZE);
        bytes_received = recv(client_socket, recv_buffer, BUFFER_SIZE - 1, 0);
        if (bytes_received <= 0) {
            printf("Connection closed by the server.\n");
            break;
        }

        // Null-terminate and print the response
        recv_buffer[bytes_received] = '\0';
        printf("Response from Backend:\n%s\n", recv_buffer);
    }

    // Close the client socket (only after the user decides to quit)
    close(client_socket);
    return 0;
}

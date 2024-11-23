#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <curl/curl.h> // For HTTP requests

#define PORT 8080        // Port for the C server
#define BUFFER_SIZE 1024 // Buffer size for communication

void handle_client(int client_socket) {
    char buffer[BUFFER_SIZE];
    memset(buffer, 0, BUFFER_SIZE);

    // Receive data from the client
    ssize_t bytes_received = recv(client_socket, buffer, BUFFER_SIZE, 0);
    if (bytes_received <= 0) {
        if (bytes_received == 0) {
            printf("Client disconnected\n");
        } else {
            perror("recv failed");
        }
        close(client_socket);
        return;
    }

    printf("Received from client: %s\n", buffer);

    // Send data to Java API using libcurl
    CURL *curl = curl_easy_init();
    if (curl) {
        CURLcode res;
        char response[BUFFER_SIZE] = {0}; // Local buffer for thread safety

        curl_easy_setopt(curl, CURLOPT_URL, "http://localhost:9090/api"); // Java API endpoint
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, buffer);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, NULL);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, response);

        res = curl_easy_perform(curl);
        if (res == CURLE_OK) {
            printf("Response from Java API: %s\n", response);
            send(client_socket, response, strlen(response), 0);
        } else {
            printf("Error communicating with Java API: %s\n", curl_easy_strerror(res));
        }
        curl_easy_cleanup(curl);
    }

    close(client_socket);
}

int main() {
    int server_fd, client_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len = sizeof(client_addr);

    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd == 0) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        close(server_fd);
        exit(EXIT_FAILURE);
    }

    if (listen(server_fd, 3) < 0) {
        perror("Listen failed");
        close(server_fd);
        exit(EXIT_FAILURE);
    }

    printf("Server is listening on port %d\n", PORT);

    while ((client_socket = accept(server_fd, (struct sockaddr *)&client_addr, &addr_len)) >= 0) {
        printf("Client connected\n");
        handle_client(client_socket);
    }

    close(server_fd);
    return 0;
}
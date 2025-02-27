#include <iostream>
#include <cstring>
#include <cstdlib>
#include <winsock2.h>
#include <ws2tcpip.h>

#pragma comment(lib, "ws2_32.lib")

#define BUFFER_SIZE 1024

using namespace std;

int main() {
    WSADATA wsaData;
    int sockfd;
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t addr_len = sizeof(clientAddr);
    char buffer[BUFFER_SIZE];
    int port;

    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        cerr << "Winsock initialization failed" << endl;
        return EXIT_FAILURE;
    }

    if ((sockfd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == INVALID_SOCKET) {
        cerr << "Socket creation failed: " << WSAGetLastError() << endl;
        WSACleanup();
        return EXIT_FAILURE;
    }

    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_port = htons(0);

    if (bind(sockfd, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        cerr << "Socket binding failed: " << WSAGetLastError() << endl;
        closesocket(sockfd);
        WSACleanup();
        return EXIT_FAILURE;
    }

    socklen_t len = sizeof(serverAddr);
    if (getsockname(sockfd, (struct sockaddr*)&serverAddr, &len) == SOCKET_ERROR) {
        cerr << "Failed to get assigned port: " << WSAGetLastError() << endl;
        closesocket(sockfd);
        WSACleanup();
        return EXIT_FAILURE;
    }

    port = ntohs(serverAddr.sin_port);
    cout << "Server started on port: " << port << endl;

    while (true) {
        memset(buffer, 0, BUFFER_SIZE);

        ssize_t bytes_received = recvfrom(sockfd, buffer, BUFFER_SIZE, 0, (struct sockaddr*)&clientAddr, &addr_len);
        if (bytes_received == SOCKET_ERROR) {
            cerr << "Error receiving data: " << WSAGetLastError() << endl;
            continue;
        }

        char client_ip[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &(clientAddr.sin_addr), client_ip, INET_ADDRSTRLEN);
        cout << "Client IP: " << client_ip << ", Port: " << ntohs(clientAddr.sin_port) << endl;
        cout << "   Received from client: " << buffer << endl;

        int number = atoi(buffer);
        int result = number * 2;

        snprintf(buffer, BUFFER_SIZE, "%d", result);
        if (sendto(sockfd, buffer, strlen(buffer), 0, (struct sockaddr*)&clientAddr, addr_len) == SOCKET_ERROR) {
            cerr << "Error sending data: " << WSAGetLastError() << endl;
        } else {
            cout << "   Sent to client: " << result << endl;
        }
    }

    closesocket(sockfd);
    WSACleanup();
    return 0;
}
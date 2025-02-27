#include <iostream>
#include <cstring>
#include <cstdlib>
#include <winsock2.h>
#include <ws2tcpip.h>

#pragma comment(lib, "ws2_32.lib")

#define BUFFER_SIZE 1024

using namespace std;

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cerr << "Usage: ./client <server_IP> <port>" << endl;
        return EXIT_FAILURE;
    }

    const char* server_ip = argv[1];
    int server_port = stoi(argv[2]);

    WSADATA wsaData;
    int sockfd;
    struct sockaddr_in serverAddr;
    char buffer[BUFFER_SIZE];

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
    serverAddr.sin_port = htons(server_port);
    if (inet_pton(AF_INET, server_ip, &serverAddr.sin_addr) != 1) {
        cerr << "Invalid server IP address" << endl;
        closesocket(sockfd);
        WSACleanup();
        return EXIT_FAILURE;
    }

    for (int i = 1; i <= 5; ++i) {
        snprintf(buffer, BUFFER_SIZE, "%d", i);
        if (sendto(sockfd, buffer, strlen(buffer), 0, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
            cerr << "Error sending data: " << WSAGetLastError() << endl;
            continue;
        }

        socklen_t addr_len = sizeof(serverAddr);
        memset(buffer, 0, BUFFER_SIZE);
        ssize_t bytes_received = recvfrom(sockfd, buffer, BUFFER_SIZE, 0, (struct sockaddr*)&serverAddr, &addr_len);
        if (bytes_received == SOCKET_ERROR) {
            cerr << "Error receiving data: " << WSAGetLastError() << endl;
            continue;
        }

        cout << "Sent: " << i << ", Received: " << buffer << endl;

        Sleep(i * 1000);
    }

    closesocket(sockfd);
    WSACleanup();
    return 0;
}
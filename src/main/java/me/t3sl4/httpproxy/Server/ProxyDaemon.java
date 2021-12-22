package me.t3sl4.httpproxy.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyDaemon {

    public static void main(String[] args) {
        ServerSocket welcomeSocket = null;
        try {
            welcomeSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Socket connectionSocket = null;
            try {
                connectionSocket = welcomeSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new ServerHandler(connectionSocket);
        }
    }

}

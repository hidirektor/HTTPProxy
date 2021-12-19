package me.t3sl4.httpproxy.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class ProxyDaemon {

    public static void main(String args[]) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(8080);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new ServerHandler(connectionSocket);
        }

    }

}

package me.t3sl4.httpproxy.Server;

import me.t3sl4.httpproxy.Controllers.ProxyController;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

public class ServerHandler implements Runnable {

    Socket clientSocket;
    BufferedReader inFromClient;
    DataOutputStream outToClient;

    String host;

    String path;

    String url;

    String hd;

    String constructedHD = "";

    String reqHeaderRemainingLines;

    PrinterClass pC;

    public ServerHandler(Socket s) {
        clientSocket = s;
        pC = new PrinterClass();

        pC.add("A connection from a client is initiated...");

        try {
            inFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outToClient = new DataOutputStream(s.getOutputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            byte[] header = new byte[1024];

            int data;
            int h = 0;

            while ((data = inFromClient.read()) != -1) {
                header[h++] = (byte) data;

                if (header[h - 1] == '\n' && header[h - 2] == '\r' && header[h - 3] == '\n' && header[h - 4] == '\r') {
                    break;
                }
            }

            hd = new String(header, 0, h);

            int sp1 = hd.indexOf(' ');
            int sp2 = hd.indexOf(' ', sp1 + 1);
            int eol = hd.indexOf('\r');

            String method = hd.substring(0, sp1);

            reqHeaderRemainingLines = hd.substring(eol + 2);

            StringTokenizer st = new StringTokenizer(reqHeaderRemainingLines, "\r\n");

            url = hd.substring(sp1 + 1, sp2);

            host = null;

            while (st.hasMoreTokens()) {
                String line = st.nextToken();
                if (line.toLowerCase().startsWith("host")) {
                    host = line.substring(6);
                }
                if (line.toLowerCase().startsWith("connection")) {
                    constructedHD += "Connection: close\r\n";
                } else {
                    constructedHD += line + "\r\n";
                }
            }

            URL u = new URL(url);

            String tmpPath = u.getPath();

            String tmpHost = u.getHost();

            path = ((tmpPath == "") ? "/" : tmpPath);
            System.out.println("HTTP Method: " + method);
            if(ProxyController.filteredHosts.contains("http://" + host)) {
                System.out.println("The URL you specified is in the black list.");
                s.close();
            } else {
                if (method.equals("GET")) {
                    pC.add("Client requests...\r\nHost: " + host + "\r\nPath: " + path);

                    new Thread(this).start();
                } else {
                    // pC.add("Error for request: " + url);

                    String html = "<html>\r\n" + "<body>\r\n" + "<h1>Method Not Allowed</h1>\r\n" + "</body>\r\n"
                            + "</html>";

                    int htmlSize= html.length();

                    String response = "HTTP/1.1 405 Method Not Allowed\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + htmlSize + "\r\n" +
                            "Date: " + new Date() + "\r\n" +
                            "Connection: close\r\n\r\n" + html;

                    outToClient.writeBytes(response);

                    s.close();
                    // HTTP/1.1 405 Method Not Allowed
                    // Content-Type: text/html
                    // Content-Length: SIZE
                    // Date: new Date();
                    //
                    // <html>
                    // <body>
                    // <h1>Method Not Allowed</h1>
                    // </body>
                    // </html>
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            pC.add("\r\nInitiating the server connection");
            Socket sSocket = new Socket(host, 80);
            DataInputStream inFromServer = new DataInputStream(sSocket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(sSocket.getOutputStream());

            pC.add("\r\nSending to server...\r\n" + "GET " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");

            outToServer.writeBytes("GET " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");

            pC.add("HTTP request sent to: " + host);

            ByteArrayOutputStream bAOS = new ByteArrayOutputStream(10000);

            int a;

            byte[] buffer = new byte[1024];

            while ((a = inFromServer.read(buffer)) != -1) {
                bAOS.write(buffer, 0, a);
            }

            byte[] response = bAOS.toByteArray();

            String rawResponse = new String(response);

            String responseHeader = rawResponse.substring(0, rawResponse.indexOf("\r\n\r\n"));

            pC.add("\r\nResponse Header\r\n" + responseHeader + "\r\n\r\nGot " + response.length
                    + " bytes of raw response (with payload)...\r\n" + "Sending it back to the client...\r\n");

            outToClient.write(response);

            outToClient.close();

            sSocket.close();

            pC.add("Served http://" + host + path + "\r\nExiting ServerHelper thread...\r\n"
                    + "\r\n----------------------------------------------------" + "\r\n");

            pC.removeThread();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

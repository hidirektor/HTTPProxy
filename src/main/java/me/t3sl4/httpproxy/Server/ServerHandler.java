package me.t3sl4.httpproxy.Server;

import me.t3sl4.httpproxy.Controllers.ProxyController;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    String responseCode;
    String method;

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

            method = hd.substring(0, sp1);

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
                responseCode = "401";
                printReport();
                System.out.println("The URL you specified is in the black list.");
                s.close();
            } else {
                if (method.equals("GET") || method.equals("HEAD") || method.equals("POST") || method.equals("CONNECT")) {
                    responseCode = "200";
                    printReport();
                    pC.add("Client requests...\r\nHost: " + host + "\r\nPath: " + path);
                    new Thread(this).start();
                } else {
                    responseCode =  "405";
                    pC.add("Error for request: " + url);
                    printReport();
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


            if(method.equals("GET")) {
                pC.add("\r\nSending to server...\r\n" + "GET " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");

                outToServer.writeBytes("GET " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");
            } else if(method.equals("POST")) {
                pC.add("\r\nSending to server...\r\n" + "POST " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");

                outToServer.writeBytes("POST " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");
            } else if(method.equals("HEAD")) {
                pC.add("\r\nSending to server...\r\n" + "HEAD " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");

                outToServer.writeBytes("HEAD " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");
            } else if(method.equals("CONNECT")) {
                pC.add("\r\nSending to server...\r\n" + "CONNECT " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");

                outToServer.writeBytes("CONNECT " + path + " HTTP/1.1\r\n" + constructedHD + "\r\n");
            }

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

    private static String getIpAddress(byte[] rawBytes) {
        int i = 4;
        StringBuilder ipAddress = new StringBuilder();
        for (byte raw : rawBytes) {
            ipAddress.append(raw & 0xFF);
            if (--i > 0) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

    private void printReport() throws IOException {
        DateTimeFormatter reqDate = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime reqDateFormat = LocalDateTime.now();

        byte[] ip = new byte[0];
        InetSocketAddress socketAddress = (InetSocketAddress)clientSocket.getRemoteSocketAddress();
        InetAddress clientIP = socketAddress.getAddress();
        ip = clientIP.getAddress();
        String ipAddress = getIpAddress(ip);

        File userFile = new File(ipAddress);

        if(!userFile.exists()) {
            userFile.createNewFile();
        }

        try (FileWriter writer = new FileWriter(System.getProperty("user.home") + "\\Desktop\\" + ipAddress + ".txt");
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write("Date: " + reqDate.format(reqDateFormat));
            bw.newLine();
            bw.write("Client IP: " + ipAddress);
            bw.newLine();
            bw.write("Request Domain: " + host);
            bw.newLine();
            bw.write("Resource Path: " + path);
            bw.newLine();
            bw.write("HTTP Method: " + method);
            bw.newLine();
            bw.write("Response Status Code: " + responseCode);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

}

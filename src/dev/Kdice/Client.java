package dev.Kdice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    public static void main(String args[]) throws IOException {


        InetAddress address = InetAddress.getLocalHost();
        Socket socket = null;
        String line = null;
        BufferedReader buffReader = null;
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            socket = new Socket(address, 4445); // You can use static final constant PORT_NUM
            buffReader = new BufferedReader(new InputStreamReader(System.in));
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        //Receiving a welcome message
        System.out.println(inputStream.readLine());
        //login
        try {
            line = "LOGIN gamer"+ ThreadLocalRandom.current().nextInt(100, 900)+"\n";
            outputStream.println(line);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        String response = null;
        try {
            line = buffReader.readLine();
            while (line.compareTo("QUIT") != 0) {
                outputStream.println(line);
                outputStream.flush();
                response = inputStream.readLine();
                System.out.println(response);
                line = buffReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        } finally {
            inputStream.close();
            outputStream.close();
            buffReader.close();
            socket.close();
            System.out.println("Connection Closed");
        }
        */
    }
}




















/*
The code is based on https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
*/
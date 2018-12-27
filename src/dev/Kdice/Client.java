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
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        //Connect with server
        try {
            socket = new Socket(address, 4445);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        //Receiving a welcome message
        try {
            System.out.println(inputStream.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Login
        try {
            line = "LOGIN guest"+ ThreadLocalRandom.current().nextInt(100, 900);
            System.out.println(line);
            outputStream.println(line);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Start
        try {
            System.out.println(inputStream.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Receiving a map length for loop
        int mapLength = inputStream.read();

        //Receiving a map
        for (int i=0; i<mapLength; i++) {
            try {
                System.out.println(inputStream.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}




















/*
The code is based on https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
*/
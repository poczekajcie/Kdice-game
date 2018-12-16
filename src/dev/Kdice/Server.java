package dev.Kdice;

import java.net.*;

public class Server {
    public static void main(String[] args) {
        try
        {
            ServerSocket server = new ServerSocket(4444);
            Socket s = server.accept();

            System.out.println("Connected!");
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
}

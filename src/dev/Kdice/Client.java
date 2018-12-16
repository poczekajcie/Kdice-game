package dev.Kdice;

import java.net.*;

public class Client {
    public static void main(String[] args) {
        try
        {
            Socket s = new Socket("127.0.0.1", 4444);
            System.out.println("Client connected!");
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    /*
    public Client() {}
    public void init() {
        try
        {
            Socket s = new Socket("127.0.0.1", 4444);
            System.out.println("Client connected!");
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
    */

}

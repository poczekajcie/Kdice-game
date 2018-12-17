package dev.Kdice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String args[]){

        //Creating server
        Socket socket=null;
        ServerSocket serverSocket=null;
        try{
            serverSocket = new ServerSocket(4445);
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");
        }

        //Create game
        new Game();

        //Creating a thread for every client
        while(true){
            try{
                socket = serverSocket.accept();
                ServerThread st=new ServerThread(socket);
                st.start();
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }

    }
}














/*
The code is based on https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
*/
package dev.Kdice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static Game game = new Game();

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

        //Creating a thread for every client
        int clientsCount=0;
        while(clientsCount!=5){
            try{
                socket = serverSocket.accept();
                ServerThread st=new ServerThread(socket);
                st.start();
                clientsCount++;
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }

        //Set players
        Game.setPlayers();
        Game.playersready = true;
        for (int i = 0; i< Game.map.length; i++) {
            System.out.println("PLANSZA "+i+" "+ Game.map[i].getOwnerId()+" "+ Game.map[i].getCubes());
        }
        while(true) {

        }
    }
}














/*
The code is based on https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
*/
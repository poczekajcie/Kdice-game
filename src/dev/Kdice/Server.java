package dev.Kdice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static Game game = new Game();
    public static int idPlayerTurn = 1, bigRound = 1, smallRound = 1;
    public static boolean roundDone = false;

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
        Game.playersReady = true;
        for (int i = 0; i<Game.map.length; i++) {
            System.out.println("PLANSZA "+i+" "+Game.map[i].getOwnerId()+" "+Game.map[i].getCubes());
        }

        //Let the game begin
        int playersInGame = Game.players.length-1;

        while(bigRound!=11) {
            while(smallRound!=101) {
                for (int i=0; i<playersInGame; i++) {
                    while (!roundDone) {

                    }
                    idPlayerTurn++;
                    roundDone = false;
                }
                smallRound++;
            }
            bigRound++;
        }

    }
}














/*
The code is based on https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
*/
package dev.Kdice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static Game game = new Game();
    public static int idPlayerTurn, bigRound, smallRound;
    public static volatile boolean roundDone = false, bigRoundsDone = false, smallRoundsDone = false, allPlayersDone=false;
    public static String message = "Czekaj na swoją kolej";

    public static int getBigRound () {
        return bigRound;
    }
    public static int getSmallRound () {
        return smallRound;
    }
    public static int getIdPlayerTurn () {
        return idPlayerTurn;
    }


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

        //do 11
        for (bigRound=1; bigRound<=2; bigRound++ ) {
            //do 101
            smallRoundsDone = false;
            for (smallRound=1; smallRound<=3; smallRound++) {
                allPlayersDone = false;
                for (idPlayerTurn=1; idPlayerTurn<=playersInGame; idPlayerTurn++) {
                    while (!roundDone) {

                    }
                    roundDone = false;
                }
                System.out.println("KONIEC RUNDY "+getSmallRound());
                allPlayersDone = true;
            }
            System.out.println("KONIEC TURY "+getBigRound());
            smallRoundsDone = true;
        }
        bigRoundsDone = true;

        System.out.println("KONIEC");

    }
}














/*
The code is based on https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
*/
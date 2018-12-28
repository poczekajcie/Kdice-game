package dev.Kdice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.*;

public class ServerThread extends Thread {
    String line = null;
    String login = null;
    BufferedReader inputStream = null;
    PrintWriter outputStream = null;
    Socket socket = null;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
        } catch(IOException e){
            System.out.println("IO error in server thread");
        }

        //Send a welcome message
        try {
            outputStream.println("POLACZONO");
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Login
        try {
            line = inputStream.readLine();
            if (line.startsWith("LOGIN")) {
                String[] output = line.split(" ");
                login = output[1];
                Game.signUp(login);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Waiting for all players
        while (!Game.isAllPlayers()) {

        }

        //Sending message about start
        try {
            line = "START "+ Game.findPlayerId(login)+" 1";
            outputStream.println(line);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Sending map length
        try {
            outputStream.print(Game.map.length);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Sending map
        if (Game.playersReady) {
            for (int i = 0; i< Game.map.length; i++) {
                line = "PLANSZA "+i+" "+Game.map[i].getOwnerId()+" "+Game.map[i].getCubes();
                try {
                    outputStream.println(line);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //Let the game begin
        boolean pass = false;

        while(Server.bigRound!=11) {
            while (Server.smallRound != 101) {
                //Waiting for turn
                while (Server.idPlayerTurn != Game.findPlayerId(login)) {

                }
                // Attacking
                while (!pass) {

                    //Action will be here
                    line = Game.attack("smth");

                }

                pass = true;
                Server.roundDone = true;
            }
        }

    }
}

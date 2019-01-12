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

        while (!Server.bigRoundsDone) {
            while (!Server.smallRoundsDone) {

                //Waiting for turn
                while (Server.getIdPlayerTurn() != Game.findPlayerId(login)) {
                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    outputStream.println("TWOJ RUCH");
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Attack
                try {
                    outputStream.println("ATAK bla bla (ruch wykonuje gracz: "+login+")");
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Sleep for synchro
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Signal that our thread is ready for next round
                Server.roundDone = true;

                //Waiting for all players done their rounds
                while(!Server.allPlayersDone) {

                }

                //The end of small round
                try {
                    outputStream.println("KONIEC RUNDY");
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //The end of big round
            try {
                outputStream.println("TURA "+Server.getBigRound()+" <miejsce>");
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //The end of the game
        try {
            outputStream.println("KONIEC");
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

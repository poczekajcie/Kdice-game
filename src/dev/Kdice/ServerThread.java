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
            outputStream.println("(Server): POLACZONO");
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
            line = "(Server): START "+ Game.findPlayerId(login)+" 1";
            outputStream.println(line);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Sending map
        if (Game.playersReady) {
            writeMap();
        }

        //Let the game begin
        String oldMessage="", attackCommand="";
        for (int bigRound=1; bigRound<=2; bigRound++) {
            for (int smallRound=1; smallRound<=3; smallRound++) {

                //Waiting for turn
                oldMessage = Server.message;
                while (Server.getIdPlayerTurn() != Game.findPlayerId(login)) {
                    oldMessage = getString(oldMessage);

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Your turn
                try {
                    outputStream.println("(Server): TWOJ RUCH");
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Attack
                attackCommand = "("+login+"): "+"ATAK bla bla";
                Server.message = attackCommand;
                try {
                    outputStream.println(attackCommand);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (!attackCommand.equals("PASS")) {
                    attackCommand="PASS";
                    try {
                        outputStream.println(attackCommand);
                        outputStream.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                oldMessage = Server.message;
                while(!Server.allPlayersDone) {
                    oldMessage = getString(oldMessage);
                }

                //The end of small round
                try {
                    outputStream.println("(Server): KONIEC RUNDY");
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Wirte map with added cubes
                writeMap();

            }

            //The end of big round
            try {
                outputStream.println("(Server): TURA "+bigRound+" <miejsce>");
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Set that i'm ready for next big round
            Game.players[Game.findPlayerId(login)].setPlayerReadiness(true);

        }

        //The end of the game
        try {
            outputStream.println("(Server): KONIEC");
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void writeMap() {
        for (int i = 0; i< Game.map.length; i++) {
            line = "(Server): PLANSZA "+i+" "+Game.map[i].getOwnerId()+" "+Game.map[i].getCubes();
            try {
                outputStream.println(line);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getString(String oldMessage) {
        if (!oldMessage.equals(Server.message)) {
            try {
                outputStream.println(Server.message);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            oldMessage = Server.message;
        }
        return oldMessage;
    }
}

package dev.Kdice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Comparator;


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
        String oldMessage="";
        for (int bigRound=1; bigRound<=10; bigRound++) {
            for (int smallRound=1; smallRound<=100; smallRound++) {

                //Waiting for turn
                oldMessage = Server.message;
                while (Server.getIdPlayerTurn() != Game.findPlayerId(login)) {
                    oldMessage = getString(oldMessage);
                }

                //Can play only if it's not eliminated
                if (!Game.players[Game.findPlayerId(login)].getPlayerIsEliminated()) {

                    //Your turn
                    try {
                        outputStream.println("(Server): TWOJ RUCH");
                        outputStream.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Attack
                    String attackCommand="", attackResult="";
                    while (!attackCommand.equals("PASS")) {
                        //Check where player can attack
                        int attackerField, fieldToAttack;
                        int[] values = Game.whereCanIAttack(Game.findPlayerId(login));
                        attackerField = values[0];
                        fieldToAttack = values[1];

                        //If we don't have move then PASS
                        if (fieldToAttack == 99) {
                            attackCommand = "PASS";
                            try {
                                outputStream.println(attackCommand);
                                outputStream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            String winner;

                            attackCommand = "(" + login + "): " + "ATAK " + attackerField + " " + fieldToAttack;
                            Server.message = attackCommand;
                            try {
                                outputStream.println(attackCommand);
                                outputStream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            attackResult = Game.attack(attackCommand);

                            //Sleep for to a moment to refresh server messages
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Server.message = attackResult;
                            try {
                                outputStream.println(attackResult);
                                outputStream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (Game.isOnlyOnePlayer()) {
                                attackCommand = "PASS";
                            }

                            //If we lose then we pass
                            winner = attackResult.substring(attackResult.lastIndexOf(" ")+1);
                            if (Integer.parseInt(winner)!=Game.findPlayerId(login)) {
                                attackCommand = "PASS";
                            }
                        }
                    }
                }

                //Sleep for synchro

                try {
                    sleep(200);
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
                outputStream.println("(Server): TURA "+bigRound+" "+Game.players[Game.findPlayerId(login)].getPoints());
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Set that i'm ready for next big round
            Game.players[Game.findPlayerId(login)].setPlayerReadiness(true);
        }

        //The end of the game
        String summary[] = summary();
        String result="(Server): KONIEC";
        for (int i=0; i<summary.length; i++) {
            result = result + " " +summary[i];
        }
        try {
            outputStream.println(result);
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

    private String[] summary() {
        String[] result = new String[10];
        int[][] pointsId = new int[5][2];

        for (int i=1; i<Game.players.length; i++) {
            pointsId[i-1][0] = Game.players[i].getAllPoints();
            pointsId[i-1][1] = i;
        }

        Arrays.sort(pointsId, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o2[0], o1[0]);
            }
        });

        int k=0;
        for (int i=0; i<5; i++) {
            for (int j=0; j<2; j++) {
                if (j==1) {
                    result[k] = Game.players[pointsId[i][j]].getLogin();
                    k++;
                } else {
                    result[k] = String.valueOf(pointsId[i][j]);
                    k++;
                }
            }
        }
        return result;
    }

}

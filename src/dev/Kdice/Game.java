package dev.Kdice;

import java.util.concurrent.ThreadLocalRandom;

public class Game {
    public static Player players[] = new Player[6];
    public static Field map[] = new Field[25];
    public static int thread = 1;
    public static boolean playersready = false;


    public Game() {
        Game.init();
    }

    public static void init() {
        for (int i=0; i<players.length; i++) {
            players[i] = new Player();
        }
        for (int i=0; i<map.length; i++) {
            map[i] = new Field();
        }
    }

    public static void loging(String login) {
        int i=1;
        while (players[i].getLogin() != null) {
            i+=1;
        }
        players[i].setLogin(login);
        System.out.println(players[i].getLogin() + " logged in");
    }

    public static int findPlayerId(String login) {
        int id=0;
        for (int i=1; i<players.length; i++) {
            if (players[i].getLogin() == login) {
                id = i;
            }
        }
        return id;
    }

    public static boolean isAllPlayers() {
        boolean check = true;
        for (int i=1; i<players.length; i++) {
            if (players[i].getLogin() == null) {
                check = false;
            }
        }
        return check;
    }

    public static void setPlayers() {
        //Set players on map
        for (int i=1; i<players.length; i++) {
            int startingfield = 0;
            int nextfield = startingfield+5;
            while (map[startingfield].getOwnerId() != 99) {
                int position = ThreadLocalRandom.current().nextInt(2,6);
                switch (position) {
                    case 2:
                        startingfield = 3;
                        nextfield = startingfield+1;
                        break;
                    case 3:
                        startingfield = 11;
                        nextfield = startingfield+1;
                        break;
                    case 4:
                        startingfield = 20;
                        nextfield = startingfield+1;
                        break;
                    case 5:
                        startingfield = 19;
                        nextfield = startingfield+5;
                        break;
                }
            }

            map[startingfield].setOwnerId(i);
            map[nextfield].setOwnerId(i);
        }

        //Set cubes for players
        //Inactive player take remaining fields
        for (int i=0; i<map.length; i++) {
            if (map[i].getOwnerId() != 99) {
                map[i].setCubes(2);
            } else {
                map[i].setOwnerId(0);
                map[i].setCubes(ThreadLocalRandom.current().nextInt(1,6));
            }
        }

    }
}

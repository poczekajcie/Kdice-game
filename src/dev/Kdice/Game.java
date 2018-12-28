package dev.Kdice;

import java.util.concurrent.ThreadLocalRandom;

public class Game {
    public static Player players[] = new Player[6];
    public static Field map[] = new Field[25];
    public static boolean playersReady = false;


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

    public static void signUp(String login) {
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
            if (players[i].getLogin().equals(login)) {
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
        for (int i=1; i<players.length; i++) {
            int startingField = 0;
            int nextField = startingField+5;
            while (map[startingField].getOwnerId() != 99) {
                int position = ThreadLocalRandom.current().nextInt(2,6);
                switch (position) {
                    case 2:
                        startingField = 3;
                        nextField = startingField+1;
                        break;
                    case 3:
                        startingField = 11;
                        nextField = startingField+1;
                        break;
                    case 4:
                        startingField = 20;
                        nextField = startingField+1;
                        break;
                    case 5:
                        startingField = 19;
                        nextField = startingField+5;
                        break;
                }
            }

            map[startingField].setOwnerId(i);
            map[nextField].setOwnerId(i);
        }

        //Set cubes for players
        //Inactive player take remaining fields
        for (Field aMap : map) {
            if (aMap.getOwnerId() != 99) {
                aMap.setCubes(2);
            } else {
                aMap.setOwnerId(0);
                aMap.setCubes(ThreadLocalRandom.current().nextInt(1, 6));
            }
        }
    }

    public static String attack(String str) {
        String result = "";
        return result;
    }
}

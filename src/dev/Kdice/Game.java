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
        players[i].setEliminated(false);
        players[i].setPlayerReadiness(false);
        players[i].setPoints(0);
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

    public static boolean isPlayersReady() {
        boolean check = true;
        for (int i=1; i<players.length; i++) {
            if (!players[i].getPlayerReadiness()) {
                check = false;
            }
        }
        return check;
    }

    public static void resetPlayersReady() {
        for (int i=1; i<players.length; i++) {
            players[i].setPlayerReadiness(false);
        }
    }

    public static void addCubes() {
        int cubesToAdd, fieldOnMap;
        int[] playerFields = new int[25];
        for (int i=1; i<players.length; i++) {
            cubesToAdd = 0;
            boolean isPossibleToAdd = true;
            for (int j=0; j<map.length; j++) {
                if (map[j].getOwnerId() == i) {
                    playerFields[cubesToAdd] = j;
                    cubesToAdd++;
                }
            }

            while (cubesToAdd>0 && isPossibleToAdd) {
                for (Field aMap : map) {
                    if (aMap.getOwnerId() == i && aMap.getCubes() < 8) {
                        isPossibleToAdd = true;
                        break;
                    } else {
                        isPossibleToAdd = false;
                    }
                }
                fieldOnMap = playerFields[ThreadLocalRandom.current().nextInt(0, cubesToAdd)];
                if (map[fieldOnMap].getCubes()<8) {
                    map[fieldOnMap].setCubes(map[fieldOnMap].getCubes()+1);
                    cubesToAdd--;
                }
            }
        }
    }

    public static void resetMap() {
        for (Player player : players) {
            player.setPlayerReadiness(false);
            player.setEliminated(false);
        }
        for (int i=0; i<map.length; i++) {
            map[i] = new Field();
        }
        Game.setPlayers();
    }

    public static String attack(String str) {

        return "WYNIK costam wyniki itd";
    }
}

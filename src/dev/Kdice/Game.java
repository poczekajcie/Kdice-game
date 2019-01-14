package dev.Kdice;

import java.util.Arrays;
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
        players[i].setAllPoints(0);
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
            for (int j=0; j<cubesToAdd; j++) {
                fieldOnMap = playerFields[ThreadLocalRandom.current().nextInt(0, cubesToAdd)];
                if (map[fieldOnMap].getCubes()<8) {
                    map[fieldOnMap].setCubes(map[fieldOnMap].getCubes()+1);
                }
            }

        }
    }

    public static void resetMap() {
        for (Player player : players) {
            player.setPlayerReadiness(false);
            player.setEliminated(false);
            player.setPoints(0);
        }
        for (int i=0; i<map.length; i++) {
            map[i] = new Field();
        }
        Game.setPlayers();
    }

    public static String attack(String attackCommand) {

        String result="WYNIK ";
        int attackerField, fieldToAttack, random, attackerSum=0, toAttackSum=0;
        String[] output = attackCommand.split(" ");

        attackerField = Integer.parseInt(output[2]);
        fieldToAttack = Integer.parseInt(output[3]);
        int[] attackerRandoms = new int[map[attackerField].getCubes()];
        int[] toAttackRandoms = new int[map[fieldToAttack].getCubes()];

        result = result + map[attackerField].getOwnerId()+" "+map[attackerField].getCubes();
        for (int i=1; i<map[attackerField].getCubes(); i++) {
            random = ThreadLocalRandom.current().nextInt(1, 6);
            attackerSum = attackerSum + random;
            attackerRandoms[i-1]= random;
        }
        for (int i=0; i<attackerRandoms.length; i++) {
            result = result + " " + attackerRandoms[i];
        }

        result = result + " " +map[fieldToAttack].getOwnerId()+" "+map[fieldToAttack].getCubes();
        for (int i=1; i<map[fieldToAttack].getCubes(); i++) {
            random = ThreadLocalRandom.current().nextInt(1, 6);
            toAttackSum = toAttackSum + random;
            toAttackRandoms[i-1]= random;
        }
        for (int i=0; i<toAttackRandoms.length; i++) {
            result = result + " " + toAttackRandoms[i];
        }

        if (attackerSum > toAttackSum) {
            result = result + " " + map[attackerField].getOwnerId();
            int loserId = map[fieldToAttack].getOwnerId();
            map[fieldToAttack].setOwnerId(map[attackerField].getOwnerId());
            map[fieldToAttack].setCubes(map[attackerField].getCubes()-1);
            map[attackerField].setCubes(1);
            players[loserId].setEliminated(true);
            for (int i=0; i<map.length; i++) {
                if (map[i].getOwnerId()==loserId) {
                    players[loserId].setEliminated(false);
                }
            }

            if (players[loserId].getPlayerIsEliminated()) {
                int pointsToGive=0;
                for (int i=0; i<players.length; i++) {
                    if (players[i].getPoints()>0) {
                        pointsToGive++;
                    }
                }
                players[loserId].setPoints(5-pointsToGive);
                players[loserId].setAllPoints(players[loserId].getAllPoints()+5-pointsToGive);
                if (isOnlyOnePlayer()) {
                    players[attackerField].setPoints(1);
                }

            }

        } else {
            result = result + " " + map[fieldToAttack].getOwnerId();
            map[attackerField].setCubes(1);
        }

        return result;
    }

    public static int[] whereCanIAttack(int playerId) {

        //Count how many player has fields and write their numbers to table
        int[] playerFields = new int[25];
        int countPlayerFields=0, attackerField, fieldToAttack = 99, k, countOptions=0;
        int[] fields = new int[2];
        boolean good = false;

        for (int i=0; i<map.length; i++) {
            if (map[i].getOwnerId() == playerId) {
                playerFields[countPlayerFields] = i;
                countPlayerFields++;
            }
        }

        //Take random player field and check if neighbors can be attacked
        attackerField = playerFields[ThreadLocalRandom.current().nextInt(0, countPlayerFields)];
        while(!good) {
            switch (k=ThreadLocalRandom.current().nextInt(1, 4)) {
                case 1:
                    fieldToAttack = attackerField-1;
                    countOptions++;
                    break;
                case 2:
                    fieldToAttack = attackerField+1;
                    countOptions++;
                    break;
                case 3:
                    fieldToAttack = attackerField-5;
                    countOptions++;
                    break;
                case 4:
                    fieldToAttack = attackerField+5;
                    countOptions++;
                    break;
            }
            if (fieldToAttack>24 || fieldToAttack<0 || map[fieldToAttack].getOwnerId()==map[attackerField].getOwnerId()) {
                good = false;
            } else {
                good = true;
            }
            if (countOptions>=4) {
                //Flag that we don't have any move
                fieldToAttack=99;
                break;
            }
        }

        fields[0] = attackerField;
        fields[1] = fieldToAttack;

        return fields;
    }

    public static boolean isOnlyOnePlayer() {

        int check=0;
        for (int i=1; i<players.length; i++) {
            if (!players[i].getPlayerIsEliminated()) {
                check++;
            }
        }

        if (check>1) {
            return false;
        } else {
            return true;
        }

    }

}

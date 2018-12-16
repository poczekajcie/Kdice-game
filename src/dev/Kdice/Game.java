package dev.Kdice;

public class Game {
    public static Player players[] = new Player[6];
    public static Field map[] = new Field[24];

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
        System.out.println(players[i].getLogin());
    }
}

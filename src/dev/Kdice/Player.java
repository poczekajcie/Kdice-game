package dev.Kdice;

public class Player {
    private String login;
    private boolean ready, isEliminated;
    private int points;

    public Player() {}
    public Player(String login) { this.login = login; }

    public String getLogin() { return login; }
    public int getPoints() { return points; }
    public boolean getPlayerReadiness() { return ready; }
    public boolean getPlayerIsEliminated() { return isEliminated; }

    public void setLogin(String login) { this.login = login; }
    public void setPoints(int points) { this.points = points; }
    public void setPlayerReadiness(boolean ready) { this.ready = ready; }
    public void setEliminated(boolean isEliminated) { this.isEliminated = isEliminated; }
}

package util;

public class Score {
    private String playerName;
    private int score;

    public Score() {}

    public Score(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
    	this.score = score;
    }
}

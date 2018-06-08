package ie.dodwyer.model;

/**
 * Created by User on 2/4/2017.
 */

public class GamePlayers {


    private int gamePlayersId;
    private int gameId;
    private String playerId;
    private double scoreTotal;
    private int winner;

    public GamePlayers() {
    }

    public GamePlayers(int gamePlayersId,int gameId, String playerId,double scoreTotal, int winner) {
        this.gamePlayersId = gamePlayersId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.scoreTotal = scoreTotal;
        this.winner = winner;
    }

    public GamePlayers(int gameId, String playerId,double scoreTotal, int winner) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.scoreTotal = scoreTotal;
        this.winner = winner;
    }

    public int getGamePlayersId() {
        return gamePlayersId;
    }

    public void setGamePlayersId(int gamePlayersId) {
        this.gamePlayersId = gamePlayersId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public double getScoreTotal() {return scoreTotal;}

    public void setScoreTotal(double scoreTotal) {this.scoreTotal = scoreTotal;}

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GamePlayers{" +
                "gamePlayersId=" + gamePlayersId +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", scoreTotal=" + scoreTotal +
                ", winner=" + winner +
                '}';
    }
}

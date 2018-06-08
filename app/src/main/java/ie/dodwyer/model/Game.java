package ie.dodwyer.model;

/**
 * Created by User on 2/4/2017.
 */

public class Game {
    private int gameId;
    private String gameName;
    private String creationDate;
    private String createdBy;
    private String gameStatus;
    private double targetScore;

    public Game() {
    }

    public Game(int gameId, String gameName, String creationDate, String createdBy, String gameStatus, double targetScore) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.gameStatus = gameStatus;
        this.targetScore = targetScore;
    }

    public Game( String gameName, String creationDate, String createdBy, String gameStatus, double targetScore) {
        this.gameName = gameName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.gameStatus = gameStatus;
        this.targetScore = targetScore;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public double getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(double targetScore) {
        this.targetScore = targetScore;
    }
}

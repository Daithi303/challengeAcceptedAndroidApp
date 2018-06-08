package ie.dodwyer.model;

/**
 * Created by User on 2/4/2017.
 */

public class Player {
    private String playerId;
    private String email;
    //private String pWord;
    private String fName;
    private String lName;
    private boolean createGameIsChecked;
    public Player() {
    }

    public Player(String playerId, String email, String fName, String lName) {
        this.playerId = playerId;
        this.email = email;
       // this.pWord = pWord;
        this.fName = fName;
        this.lName = lName;
    }

    public Player(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }

    public Player(String email, String fName, String lName) {
        this.email = email;
       // this.pWord = pWord;
        this.fName = fName;
        this.lName = lName;
    }
    public String getPlayerId() {
        return playerId;
    }

    public String getEmail() {
        return email;
    }

   // public String getpWord() {
   //     return pWord;
   // }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

/*    public void setpWord(String pWord) {
        this.pWord = pWord;
    }*/

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public boolean isCreateGameIsChecked() {
        return createGameIsChecked;
    }

    public void setCreateGameIsChecked(boolean createGameIsChecked) {
        this.createGameIsChecked = createGameIsChecked;
    }


    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", email='" + email + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", createGameIsChecked=" + createGameIsChecked +
                '}';
    }
}

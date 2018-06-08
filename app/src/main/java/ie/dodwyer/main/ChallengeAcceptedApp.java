package ie.dodwyer.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import ie.dodwyer.activities.LoginCreateUserActivity;
import ie.dodwyer.activities.MyGamesActivity;
import ie.dodwyer.database.DBManager;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/20/2017.
 */

public class ChallengeAcceptedApp extends Application {
    public volatile boolean currentGameIsWon;
    public DBManager dbManager;
    public Player currentPlayer = new Player();
    public Game currentGame = new Game();
    public ArrayList<Player> selectedPlayers;
    public static final String GAME_STATUS_IN_PROGRESS = "In Progress";
    public static final String GAME_STATUS_COMPLETE = "Completed";
    public static final String CALLER_ACTIVITY = "callerActivity";
    public static final String CHALLENGE_STATUS_DECLINED = "Declined";
    public static final String CHALLENGE_STATUS_ACCEPTED = "Accepted";
    public static final String CHALLENGE_STATUS_PENDING_INTERUPTED = "Pending/Interupted";
    public FragmentManager gameFragmentManager;
    public ArrayList<ArrayList<String>> contactList;
    public ArrayList<ArrayList<String>> attemptContacts;
    public ArrayList<ArrayList<String>> selectedContact;
    ArrayList<Integer> randomNumbers;
    private int numOfAttemptContacts = 3;
    public ArrayList<ArrayList<String>> splitMessage = new ArrayList<ArrayList<String>> ();
    public int messageWordCount;
    public long wordsBlankedOutCount;
    public float modifiedScoreValue;
    public float unitOfScoreReduction;
    public Set<Integer> blankedOutWordPositions;
    public Random random;
    public Activity activity;

    @Override
    public void onCreate()
    {
        super.onCreate();
        dbManager = new DBManager(this);
        selectedPlayers = new ArrayList<Player>();
        currentGameIsWon = false;
    }


    public List<Player> getPlayersNotInCurrentGame(int gameId){
        List<GamePlayers> gp = dbManager.getGamePlayersConditional("gameId ="+gameId);
        List<Player> p  = dbManager.getAllPlayers();
        ArrayList<Player> removeP = new ArrayList<Player>();
        for(Player p1:p) {
            for (GamePlayers gp1 : gp) {
            if(p1.getPlayerId().equals(gp1.getPlayerId())){
                removeP.add(p1);
            }
            }
        }
        p.removeAll(removeP);
        return p;
    }

    public boolean resetDatabase(){
        try {
            dbManager.deleteAllGamePlayers();
            dbManager.deleteAllGames();
            dbManager.deleteAllChallenges();
            dbManager.deleteAllPlayers();
            return true;
        }catch(Exception e){
            return false;
        }
    }




    public String getDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = df.format(c.getTime());
        df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate+", "+formattedTime;
    }



    public boolean doesCurrentPlayerHaveGames(){
        List<GamePlayers> gamePlayers;
        ArrayList<Game> games = new ArrayList<>();
        gamePlayers = dbManager.getGamePlayersConditional("playerId = '"+currentPlayer.getPlayerId()+"'");
        for(int i = 0;i<gamePlayers.size();i++){
            games.add(dbManager.getGamesConditional("gameId = "+gamePlayers.get(i).getGameId()).get(0));
        }
        if(!(games.size()==0)){
            return true;

        }
        return false;

    }

    public void populateAttemptContacts(){
        attemptContacts = new ArrayList<>();
        randomNumbers = new ArrayList<>();
        random = new Random();

        while(attemptContacts.size()<numOfAttemptContacts){
            int next = random.nextInt(contactList.size());
            String name = contactList.get(next).get(0);
            if(!(randomNumbers.contains(new Integer(next)))){
                randomNumbers.add(new Integer(next));
                Log.d("random","Random Int: "+next);
                attemptContacts.add(contactList.get(next));

            }
        }
        Log.d("random","/////////////////////////////////////////////////////////////////////");
    }
public void populateSelectedContact(){
    selectedContact = new ArrayList<ArrayList<String>>();
    random = new Random();
    int selected = random.nextInt(attemptContacts.size());
    selectedContact.add(attemptContacts.get(selected));
}

public String hasAnyOneWon(int gameId,double scoreTotal){
    ArrayList<GamePlayers> gp = (ArrayList<GamePlayers>)dbManager.getGamePlayersConditional("gameId = "+gameId);
    if(gp==null || gp.size()==0){
        return null;
    }
    else{
       for(GamePlayers x:gp) {
           if(x.getScoreTotal()>=scoreTotal){
               ContentValues cv = new ContentValues();
               cv.put("winner",1);
               String currentGame = String.valueOf(x.getGameId());
               String currentPlayer = x.getPlayerId();
               String[] whereClauseValues = new String[]{currentGame,currentPlayer};
               long result = dbManager.updateGamePlayers(cv,"gameId = ? AND playerId = ?",whereClauseValues);
               cv = new ContentValues();
               cv.put("gameStatus",GAME_STATUS_COMPLETE);
               dbManager.updateGame(cv,x.getGameId());
               return x.getPlayerId();
           }

       }

    }
    return "";
}

    public int getContactList(){
        contactList = new ArrayList<ArrayList<String>>();
        Boolean contactIsExclusive;
        Cursor phones;
            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {   contactIsExclusive = true;
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String id=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE){
                    Log.d("random","Phone--- ID: "+id+" name: "+name+" phoneNumber: "+phoneNumber);
                    for(ArrayList<String> x:contactList){
                        if((x.get(0).equals(name))){
                            contactIsExclusive = false;
                        }
                    }
                    if(contactIsExclusive) {
                        ArrayList<String> contact = new ArrayList<>();
                        contact.add(name);
                        contact.add(phoneNumber);
                        Log.d("random", "Name: " + name + "--- Number: " + phoneNumber);
                        contactList.add(contact);
                    }
                }

            }

            phones.close();
            if(contactList.size()<numOfAttemptContacts || contactList == null){
                return -1;
            }

        return 1;
    }

/*
    public int getContactList(){
        contactList = new ArrayList<ArrayList<String>>();
        Boolean contactIsExclusive;
        Cursor phones;
        try {
            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {   contactIsExclusive = true;
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String id=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE){
                    Log.d("random","Phone--- ID: "+id+" name: "+name+" phoneNumber: "+phoneNumber);
                    for(ArrayList<String> x:contactList){
                        if((x.get(0).equals(name))){
                            contactIsExclusive = false;
                        }
                    }
                    if(contactIsExclusive) {
                        ArrayList<String> contact = new ArrayList<>();
                        contact.add(name);
                        contact.add(phoneNumber);
                        Log.d("random", "Name: " + name + "--- Number: " + phoneNumber);
                        contactList.add(contact);
                    }
                }

            }

            phones.close();
            if(contactList.size()<numOfAttemptContacts || contactList == null){
                return -2;
            }
        }catch(SecurityException e){
            return -1;
        }
        return 1;
    }
    */

}

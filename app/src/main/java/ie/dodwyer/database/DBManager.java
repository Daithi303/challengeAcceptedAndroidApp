package ie.dodwyer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/14/2017.
 */

public class DBManager {
    private SQLiteDatabase database;
    public DBDesigner dbDesigner;

    public DBManager(Context context) {
        dbDesigner = new DBDesigner(context);
    }

    public void open() throws SQLException {
        database = dbDesigner.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public long createPlayer(Player player) {
        open();

        ContentValues values = new ContentValues();
        values.put(DBDesigner.KEY_PLAYER_ID, player.getPlayerId());
        values.put(DBDesigner.KEY_EMAIL, player.getEmail());
        values.put(DBDesigner.KEY_FNAME, player.getfName());
        values.put(DBDesigner.KEY_LNAME, player.getlName());

        long result = database.insert(DBDesigner.TABLE_PLAYER, null, values);

        return result;
    }


    public long createChallenge(Challenge challenge) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBDesigner.KEY_CREATED_BY, challenge.getCreatedBy());
        values.put(DBDesigner.KEY_SUBJECT, challenge.getSubject());
        values.put(DBDesigner.KEY_MESSAGE, challenge.getMessage());
        values.put(DBDesigner.KEY_MAX_POINT_VALUE, challenge.getMaxPointValue());
        values.put(DBDesigner.KEY_CREATION_DATE, challenge.getCreationDate());
        long challengeId = database.insert(DBDesigner.TABLE_CHALLENGE, null, values);

        return challengeId;
    }

    public void deleteAllChallenges(){
        open();
        database.execSQL("delete from "+ DBDesigner.TABLE_CHALLENGE);
    }

    public int deleteChallengesConditional(int challengeId){
        open();
        long result = database.delete(DBDesigner.TABLE_CHALLENGE, "challengeId=?", new String[]{Integer.toString(challengeId)});
        return (int)result;
    }

    public int deleteChallengesConditional(String whereAttribute, int attributeValue){
        open();
        long result = database.delete(DBDesigner.TABLE_CHALLENGE, (whereAttribute+"=?"), new String[]{Integer.toString(attributeValue)});
        return (int)result;
    }

    public long createGame(Game game) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBDesigner.KEY_GAME_NAME, game.getGameName());
        values.put(DBDesigner.KEY_CREATION_DATE, game.getCreationDate());
        values.put(DBDesigner.KEY_CREATED_BY, game.getCreatedBy());
        values.put(DBDesigner.KEY_GAME_STATUS, game.getGameStatus());
        values.put(DBDesigner.KEY_TARGET_SCORE, game.getTargetScore());

        long gameId = database.insert(DBDesigner.TABLE_GAME, null, values);

        return gameId;
    }

    public long createGamePlayers(GamePlayers gamePlayers) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBDesigner.KEY_GAME_ID, gamePlayers.getGameId());
        values.put(DBDesigner.KEY_PLAYER_ID, gamePlayers.getPlayerId());
        values.put(DBDesigner.KEY_WINNER, gamePlayers.getWinner());

        long gamePlayersId = database.insert(DBDesigner.TABLE_GAME_PLAYERS, null, values);

        return gamePlayersId;
    }


    public List<Player> getAllPlayers() {
        open();
        List<Player> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_PLAYER, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Player p = toPlayer(cursor);
            list.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    public void deleteAllPlayers(){
        open();
        database.execSQL("delete from "+ DBDesigner.TABLE_PLAYER);
    }

    public void deletePlayersConditional(String whereClause){
        open();
        database.execSQL("delete from "+ DBDesigner.TABLE_PLAYER+ " WHERE "+ whereClause);
    }

    public List<Player> getPlayersConditional(String whereClause) {
        open();
        List<Player> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_PLAYER + " WHERE " +whereClause, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Player p = toPlayer(cursor);
            list.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    private Player toPlayer(Cursor cursor) {
        Player p  = new Player();
        p.setPlayerId(cursor.getString(0));
        p.setEmail(cursor.getString(1));
        p.setfName(cursor.getString(2));
        p.setlName(cursor.getString(3));
        return p;
    }

    public List<Game> getAllGames() {
        open();
        List<Game> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_GAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game g = toGame(cursor);
            list.add(g);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<Game> getGamesConditional(String whereClause) {
        open();
        List<Game> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_GAME + " WHERE " +whereClause, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game g = toGame(cursor);
            list.add(g);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    private Game toGame(Cursor cursor) {
        Game g  = new Game();
        g.setGameId(cursor.getInt(0));
        g.setGameName(cursor.getString(1));
        g.setCreationDate(cursor.getString(2));
        g.setCreatedBy(cursor.getString(3));
        g.setGameStatus(cursor.getString(4));
        g.setTargetScore(cursor.getDouble(5));
        return g;
    }

    public int deleteGamesConditional(int gameId){
        open();
        long result = database.delete(DBDesigner.TABLE_GAME, "gameId=?", new String[]{Integer.toString(gameId)});
        return (int)result;
    }

    public int deleteGamePlayersConditional(String whereAttribute, int attributeValue){
        open();
        long result = database.delete(DBDesigner.TABLE_GAME_PLAYERS, (whereAttribute+"=?"), new String[]{Integer.toString(attributeValue)});
        return (int)result;
    }


    public List<Challenge> getAllChallenges() {
        List<Challenge> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_CHALLENGE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Challenge c = toChallenge(cursor);
            list.add(c);
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    public void deleteAllGames(){
        open();
        database.execSQL("delete from "+ DBDesigner.TABLE_GAME);
    }

    public List<Challenge> getChallengesConditional(String whereClause) {
        List<Challenge> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_CHALLENGE + " WHERE " +whereClause, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Challenge c = toChallenge(cursor);
            list.add(c);
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    public long updateGame(ContentValues values,int gameId) {
        open();
        long noRowsAffected = database.update(DBDesigner.TABLE_GAME,values,"gameId = "+gameId,null);

        return noRowsAffected;
    }

    public long updateChallenge(ContentValues values,int challengeId) {
        open();
        long noRowsAffected = database.update(DBDesigner.TABLE_CHALLENGE,values,"challengeId = "+challengeId,null);

        return noRowsAffected;
    }

    private Challenge toChallenge(Cursor cursor) {
        Challenge c = new Challenge();
        ContentValues values = new ContentValues();
        c.setChallengeId(cursor.getInt(0));
        c.setCreatedBy(cursor.getString(1));
        if(cursor.isNull(2)){
            c.setGameId(-999);
        }else{
            c.setGameId(cursor.getInt(2));
        }
        if(cursor.isNull(3)){
            c.setNomineeId(null);
        }
        else {
            c.setNomineeId(cursor.getString(3));
        }
        c.setSubject(cursor.getString(4));
        c.setMessage(cursor.getString(5));
        c.setMaxPointValue(cursor.getDouble(6));
        if(cursor.isNull(7)){
            c.setResult(null);
        }
        else {
            c.setResult(cursor.getString(7));
        }
        if(cursor.isNull(8)){
            c.setPointsAwarded(-999);
        }
        else {
            c.setPointsAwarded(cursor.getDouble(8));
        }

        if(cursor.isNull(9)){
            c.setBlankWordDecimal(-999);
        }
        else {
            c.setBlankWordDecimal(cursor.getDouble(9));
        }
        c.setCreationDate(cursor.getString(10));
        if(cursor.isNull(11)){
            c.setContactInReceiverAddressBook(null);
        }
        else {
            c.setContactInReceiverAddressBook(cursor.getString(11));
        }
        return c;
    }

    public List<GamePlayers> getAllGamePlayers() {
        open();
        List<GamePlayers> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_GAME_PLAYERS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GamePlayers g = toGamePlayers(cursor);
            list.add(g);
            Log.v("DebugGamePlayers", "Cursor Count: "+String.valueOf(cursor.getCount())+". Game.ToString: "+g);
            cursor.moveToNext();

        }
        cursor.close();
        return list;

    }

    public List<GamePlayers> getGamePlayersConditionalSortDesc(String whereClauseColumn,String[] whereClauseValues){
        open();
        List<GamePlayers> list = new ArrayList<>();
        String[]columns = {DBDesigner.KEY_GAME_PLAYERS_ID, DBDesigner.KEY_GAME_ID,DBDesigner.KEY_PLAYER_ID,DBDesigner.KEY_SCORE_TOTAL,DBDesigner.KEY_WINNER};
        Cursor cursor = database.query(DBDesigner.TABLE_GAME_PLAYERS, columns, whereClauseColumn+" = ?",whereClauseValues , null, null, DBDesigner.KEY_SCORE_TOTAL+" DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GamePlayers g = toGamePlayers(cursor);
            list.add(g);
            cursor.moveToNext();

        }
        cursor.close();
        return list;
    }


    public List<GamePlayers> getGamePlayersConditional(String whereClause) {
        open();
        List<GamePlayers> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DBDesigner.TABLE_GAME_PLAYERS + " WHERE " +whereClause, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GamePlayers g = toGamePlayers(cursor);
            list.add(g);
            cursor.moveToNext();

        }
        cursor.close();
        return list;

    }

    private GamePlayers toGamePlayers(Cursor cursor) {
        GamePlayers g = new GamePlayers();
        ContentValues values = new ContentValues();
        g.setGamePlayersId(cursor.getInt(0));
        g.setGameId(cursor.getInt(1));
        g.setPlayerId(cursor.getString(2));
        g.setScoreTotal(cursor.getDouble(3));
        g.setWinner(cursor.getInt(4));
        return g;
    }

    public long updateGamePlayers(ContentValues values,String whereClause,String[] whereClauseValues) {
        open();
        long noRowsAffected = database.update(DBDesigner.TABLE_GAME_PLAYERS,values,whereClause,whereClauseValues);

        return noRowsAffected;
    }


    public void deleteAllGamePlayers(){
        open();
        database.execSQL("delete from "+ DBDesigner.TABLE_GAME_PLAYERS);
    }

}

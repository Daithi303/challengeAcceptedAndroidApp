package ie.dodwyer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2/4/2017.
 */

public class DBDesigner extends SQLiteOpenHelper {
    SQLiteDatabase db;
    // Logcat tag
    private static final String LOG = "DBDesigner";

    // Database Version
    private static final int DATABASE_VERSION = 15;

    // Database Name
    private static final String DATABASE_NAME = "challengeAccepted";

    // Table Names
    static final String TABLE_PLAYER = "player";
    static final String TABLE_CHALLENGE = "challenge";
    static final String TABLE_GAME = "game";
    static final String TABLE_GAME_PLAYERS = "gamePlayers";

    // Common column names
    static final String KEY_CREATED_BY = "createdBy";
    static final String KEY_GAME_ID = "gameId";
    static final String KEY_PLAYER_ID = "playerId";
    static final String KEY_CHALLENGE_ID = "challengeId";
    static final String KEY_CREATION_DATE = "creationDate";
    // PLAYER Table - column names

    static final String KEY_EMAIL = "email";
    static final String KEY_PWORD = "pWord";
    static final String KEY_FNAME = "fName";
    static final String KEY_LNAME = "lName";

    // CHALLENGE Table - column names
    static final String KEY_SUBJECT = "subject";
    static final String KEY_MESSAGE = "message";
    static final String KEY_MAX_POINT_VALUE = "maxPointValue";
    static final String KEY_CONTACT_IN_RECEIVER_ADDRESS_BOOK = "contactInReceiverAddressBook";


    // GAME Table - column names
    static final String KEY_GAME_NAME = "gameName";
    static final String KEY_GAME_STATUS = "gameStatus";
    static final String KEY_TARGET_SCORE = "targetScore";

    // GAME_PLAYERS Table - column names
    static final String KEY_GAME_PLAYERS_ID = "gamePLayersId";
    static final String KEY_SCORE_TOTAL = "scoreTotal";
    static final String KEY_WINNER = "winner";

    // CHALLENGE_ATTEMPT Table - column names
    static final String KEY_CHALLENGE_ATTEMPT_ID = "challengeAttemptId";
    static final String KEY_NOMINEE_ID = "nomineeId";
    static final String KEY_NOMINATOR_ID = "nominatorId";
    static final String KEY_RESULT = "result";
    static final String KEY_POINTS_AWARDED = "pointsAwarded";
    static final String KEY_BLANK_WORD_DECIMAL = "blankWordDecimal";

    // Table Create Statements

    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE "
            + TABLE_PLAYER
            + " ("
            + KEY_PLAYER_ID + " TEXT PRIMARY KEY,"
            + KEY_EMAIL + " TEXT NOT NULL,"
            + KEY_FNAME + " TEXT NOT NULL,"
            + KEY_LNAME + " TEXT NOT NULL"
            + ")";

    private static final String CREATE_TABLE_CHALLENGE = "CREATE TABLE "
            + TABLE_CHALLENGE
            + " ("
            + KEY_CHALLENGE_ID + " INTEGER PRIMARY KEY,"
            + KEY_CREATED_BY + " TEXT NOT NULL,"
            + KEY_GAME_ID + " INTEGER,"
            + KEY_NOMINEE_ID + " TEXT,"
            + KEY_SUBJECT + " TEXT NOT NULL,"
            + KEY_MESSAGE + " TEXT NOT NULL,"
            + KEY_MAX_POINT_VALUE + " REAL NOT NULL,"
            + KEY_RESULT + " TEXT,"
            + KEY_POINTS_AWARDED + "  REAL,"
            + KEY_BLANK_WORD_DECIMAL + "  REAL,"
            + KEY_CREATION_DATE + " TEXT NOT NULL,"
            + KEY_CONTACT_IN_RECEIVER_ADDRESS_BOOK + " TEXT,"
            + "FOREIGN KEY(" + KEY_GAME_ID + ") REFERENCES " + TABLE_GAME + "(" + KEY_GAME_ID + "),"
            + "FOREIGN KEY(" + KEY_NOMINEE_ID + ") REFERENCES " + TABLE_PLAYER + "(" + KEY_PLAYER_ID + "),"
            + "FOREIGN KEY(" + KEY_CREATED_BY + ") REFERENCES " + TABLE_PLAYER + "(" + KEY_PLAYER_ID + ")"
            + ")";

    private static final String CREATE_TABLE_GAME = "CREATE TABLE "
            + TABLE_GAME
            + " ("
            + KEY_GAME_ID + " INTEGER PRIMARY KEY,"
            + KEY_GAME_NAME + " TEXT NOT NULL,"
            + KEY_CREATION_DATE + " TEXT NOT NULL,"
            + KEY_CREATED_BY + " TEXT NOT NULL,"
            + KEY_GAME_STATUS + " TEXT NOT NULL,"
            + KEY_TARGET_SCORE + " REAL NOT NULL,"
            + "FOREIGN KEY(" + KEY_CREATED_BY + ") REFERENCES " + TABLE_PLAYER + "(" + KEY_PLAYER_ID + ")"
            + ")";

    private static final String CREATE_TABLE_GAME_PLAYERS = "CREATE TABLE "
            + TABLE_GAME_PLAYERS
            + " ("
            + KEY_GAME_PLAYERS_ID + " INTEGER PRIMARY KEY,"
            + KEY_GAME_ID + " INTEGER NOT NULL,"
            + KEY_PLAYER_ID + " TEXT NOT NULL,"
            + KEY_SCORE_TOTAL + " REAL,"
            + KEY_WINNER + " INTEGER NOT NULL,"
            + "FOREIGN KEY(" + KEY_GAME_ID + ") REFERENCES " + TABLE_GAME + "(" + KEY_GAME_ID + "),"
            + "FOREIGN KEY(" + KEY_PLAYER_ID+ ") REFERENCES " + TABLE_PLAYER + "(" + KEY_PLAYER_ID + ")"
            + ")";



    public DBDesigner(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        // creating required tables
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_CHALLENGE);
        db.execSQL(CREATE_TABLE_GAME);
        db.execSQL(CREATE_TABLE_GAME_PLAYERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_PLAYERS);
        // create new tables
        onCreate(db);
    }

}
package ie.dodwyer.activities;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ie.dodwyer.R;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.GamePlayers;

public class AttemptChallengeActivity extends Base implements View.OnClickListener{
    private static String ACTION_SMS_SENT = "SMS_SENT";
    private static String ACTION_SMS_DELIVERED = "SMS_DELIVERED";
    private int currentGameId;
    private String currentPlayerId;
    private double currentGamePlayersTotalScore;
    private GamePlayers currentGamePlayer;
    private TextView attemptSmsMessageSubjectTextView;
    private TextView maxScoreValueTextView;
    private TextView contact1TextView;
    private TextView contact2TextView;
    private TextView contact3TextView;
    private TextView word;
    private int wordTextViewTextColor;
    private FlexboxLayout attemptSmsMessageMessageFlexboxLayout;
    private Button attemptChallengeButton;
    private Button declineChallengeButton;
    public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_challenge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        word = new TextView(this);
        wordTextViewTextColor= word.getCurrentTextColor();
        attemptSmsMessageSubjectTextView = (TextView) findViewById(R.id.attemptSmsMessageSubjectTextView);
        attemptSmsMessageSubjectTextView.setTextColor(wordTextViewTextColor);
        maxScoreValueTextView = (TextView) findViewById(R.id.maxScoreValueTextView);
        contact1TextView = (TextView) findViewById(R.id.contact1TextView);
        contact2TextView = (TextView) findViewById(R.id.contact2TextView);
        contact3TextView = (TextView) findViewById(R.id.contact3TextView);
        attemptSmsMessageMessageFlexboxLayout = (FlexboxLayout) findViewById(R.id.attemptSmsMessageMessageFlexboxLayout);
        attemptChallengeButton = (Button) findViewById(R.id.attemptChallengeButton);
        declineChallengeButton = (Button) findViewById(R.id.declineChallengeButton);
        activityInfo = getIntent().getExtras();
        int challengeId = activityInfo.getInt("challengeId");
        String whereClause = "challengeId = "+challengeId;
        attemptedChallenge = (app.dbManager.getChallengesConditional(whereClause)).get(0);
        attemptSmsMessageSubjectTextView.setText(attemptedChallenge.getSubject());
        maxScoreValueTextView.setText(String.valueOf(attemptedChallenge.getMaxPointValue()));
        app.modifiedScoreValue = (float)attemptedChallenge.getMaxPointValue();
        appContext = getApplicationContext();
        currentGameId = app.currentGame.getGameId();
        currentPlayerId = app.currentPlayer.getPlayerId();
        List<GamePlayers> gp = app.dbManager.getGamePlayersConditional("gameId = "+currentGameId+" AND playerId = '"+currentPlayerId+"'");
        if(gp !=null && gp.size() ==1){
            currentGamePlayer = gp.get(0);
            currentGamePlayersTotalScore = currentGamePlayer.getScoreTotal();
        }
        attemptChallengeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(app.currentGameIsWon){goToMyGames(AttemptChallengeActivity.this);}else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttemptChallengeActivity.this);
                    builder.setMessage("Are you sure you wish to accept this challenge?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new AttemptChallengeListener()
                    ).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });

        declineChallengeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(app.currentGameIsWon){goToMyGames(AttemptChallengeActivity.this);}else {
                    declineChallenge();
                }
            }
        });
    }

    public void declineChallenge(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AttemptChallengeActivity.this);
        builder.setMessage("Are you sure you wish to decline this challenge?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DeclineChallengeListener()
        ).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        }).show();

    }

    class DeclineChallengeListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            int challengeId = attemptedChallenge.getChallengeId();
            ContentValues cv = new ContentValues();
            cv.put("result",app.CHALLENGE_STATUS_DECLINED);
            long result = app.dbManager.updateChallenge(cv,challengeId);
            Bundle activityInfo = new Bundle();
            activityInfo.putString(app.CALLER_ACTIVITY, this.getClass().getSimpleName());
            activityInfo.putInt("gameId", app.currentGame.getGameId());
            activityInfo.putInt("pagePosition",1);
            Intent goToGame = new Intent(AttemptChallengeActivity.this, GameActivity.class);
            goToGame.putExtras(activityInfo);
            AttemptChallengeActivity.this.startActivity(goToGame);
        }
    }

class AttemptChallengeListener implements DialogInterface.OnClickListener{
    @Override
    public void onClick(DialogInterface dialog, int id) {
        sendSMS("",attemptedChallenge.getMessage());
        Bundle activityInfo = new Bundle();
        activityInfo.putString(app.CALLER_ACTIVITY, this.getClass().getSimpleName());
        activityInfo.putInt("gameId", app.currentGame.getGameId());
        activityInfo.putInt("pagePosition",3);
        Intent goToGame = new Intent(AttemptChallengeActivity.this, GameActivity.class);
        goToGame.putExtras(activityInfo);
        AttemptChallengeActivity.this.startActivity(goToGame);
    }


    public void sendSMS(String phoneNumber, String message){
        String thisPhoneNumber = phoneNumber;
        if(thisPhoneNumber.equals("")){
            thisPhoneNumber = app.selectedContact.get(0).get(1);
        }
        int challengeId = attemptedChallenge.getChallengeId();
        String pointsAwardedString = String.format("%.1f",app.modifiedScoreValue);
        Double pointsAwarded = Double.valueOf(pointsAwardedString);
        String contact = app.selectedContact.get(0).get(0);
        Intent sendIntent = new Intent(ACTION_SMS_SENT);
        sendIntent.putExtra("contact", contact);
        Intent deliveryIntent = new Intent(ACTION_SMS_DELIVERED);
        deliveryIntent.putExtra("challengeId", challengeId);
        deliveryIntent.putExtra("pointsAwarded", pointsAwarded);
        deliveryIntent.putExtra("contact", contact);
        deliveryIntent.putExtra("currentGameId", currentGameId);
        deliveryIntent.putExtra("currentPlayerId", currentPlayerId);
        deliveryIntent.putExtra("totalScore", currentGamePlayersTotalScore+pointsAwarded);
        PendingIntent pendingSent = PendingIntent.getBroadcast(AttemptChallengeActivity.appContext, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingDelivered = PendingIntent.getBroadcast(AttemptChallengeActivity.appContext, 0, deliveryIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(thisPhoneNumber, null, message, pendingSent, pendingDelivered);

}


}

    @Override
    public void onClick(View v) {
        TextView word = (TextView) v;
        float scoreValue = Float.valueOf(maxScoreValueTextView.getText().toString());
        app.modifiedScoreValue = app.modifiedScoreValue-app.unitOfScoreReduction;
        maxScoreValueTextView.setText(String.format("%.1f",app.modifiedScoreValue));
        word.setBackground(null);
        word.setTextColor(wordTextViewTextColor);
        word.setOnClickListener(null);
    }


    public void splitMessage(){
        String message = attemptedChallenge.getMessage();
        app.splitMessage = new ArrayList<ArrayList<String>>();
        app.messageWordCount = 0;
        String[] parts = message.split("(?<!^)\\b");
        for (String part : parts) {
            ArrayList<String> partList = new ArrayList<String>();
            partList.add(part);
            partList.add((part.matches("\\w+") ? "word" : "punctuation"));
            app.messageWordCount = app.messageWordCount + (part.matches("\\w+") ? 1 : 0);
            app.splitMessage.add(partList);
        }
    }
    public void displayMessage(){
        int wordCursor = 0;
        app.unitOfScoreReduction = 0;
        app.wordsBlankedOutCount = 0;
        app.wordsBlankedOutCount = (long)Math.floor(app.messageWordCount/2);
        app.blankedOutWordPositions = new LinkedHashSet<Integer>();
        app.random = new Random();
        app.unitOfScoreReduction = (float) ((attemptedChallenge.getMaxPointValue()/2)/app.wordsBlankedOutCount);
        while (app.blankedOutWordPositions.size() < app.wordsBlankedOutCount)
        {
            Integer next = app.random.nextInt(app.messageWordCount) + 1;
            app.blankedOutWordPositions.add(next);
        }

        for(ArrayList<String>partList:app.splitMessage){
            word = new TextView(this);
            word.setLayoutParams(new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            word.setText(partList.get(0));
            word.setTextSize(TypedValue.COMPLEX_UNIT_SP ,16);
            if(partList.get(1).equals("word")){
                wordCursor=wordCursor+1;
                for(Integer i:app.blankedOutWordPositions){
                    if(wordCursor==i.intValue()){
                        word.setBackground(ContextCompat.getDrawable(this,R.drawable.blank_word));
                        word.setOnClickListener(this);
                        word.setTextColor(ContextCompat.getColor(this, android.R.color.transparent));
                    }
                }

            }
            attemptSmsMessageMessageFlexboxLayout.addView(word);
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_challenge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.backMenuItem) {
            Bundle activityInfo = new Bundle();
            activityInfo.putString(app.CALLER_ACTIVITY, this.getClass().getSimpleName());
            activityInfo.putInt("gameId", app.currentGame.getGameId());
            activityInfo.putInt("pagePosition",3);
            Intent goToGame = new Intent(this, GameActivity.class);
            goToGame.putExtras(activityInfo);
            startActivity(goToGame);
            return true;
        }
        if (id == R.id.logOutMenuItem) {
            logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed()
    {   if(app.currentGameIsWon){goToMyGames(this);}else {
        declineChallenge();
    }
    }
    @Override
    public void onResume(){
        super.onResume();
        splitMessage();
        displayMessage();
        contact1TextView.setText(app.attemptContacts.get(0).get(0));
        contact2TextView.setText(app.attemptContacts.get(1).get(0));
        contact3TextView.setText(app.attemptContacts.get(2).get(0));
    }


}


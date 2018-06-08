package ie.dodwyer.receivers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ie.dodwyer.activities.Base;
import ie.dodwyer.activities.MyGamesActivity;
import ie.dodwyer.main.ChallengeAcceptedApp;
import ie.dodwyer.model.GamePlayers;

/**
 * Created by User on 4/17/2017.
 */

public class DeliveryBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";
    protected Base activity;
    ChallengeAcceptedApp app;
    @Override
    public void onReceive(Context context, Intent intent) {
        app = ((ChallengeAcceptedApp) context.getApplicationContext());
        String action = intent.getAction();

        if (action.equals(ACTION_SMS_DELIVERED)) {

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Bundle b = intent.getExtras();
                    if (b != null) {
                        int challengeId = b.getInt("challengeId");
                        double pointsAwarded = b.getDouble("pointsAwarded");
                        String currentGameId = String.valueOf(b.getInt("currentGameId"));
                        String currentPlayerId = b.getString("currentPlayerId");
                        String contact = b.getString("contact");
                        double totalScore = b.getDouble("totalScore");
                        ContentValues cv = new ContentValues();
                        cv.put("result",app.CHALLENGE_STATUS_ACCEPTED);
                        cv.put("pointsAwarded",pointsAwarded);
                        cv.put("contactInReceiverAddressBook",contact);
                        long result = app.dbManager.updateChallenge(cv,challengeId);
                        cv = new ContentValues();
                        cv.put("scoreTotal",totalScore);
                        String[] whereClauseValues = new String[]{currentGameId,currentPlayerId};
                        long result2 = app.dbManager.updateGamePlayers(cv,"gameId = ? AND playerId = ?",whereClauseValues);
                        String gameWinner = app.hasAnyOneWon(app.currentGame.getGameId(),app.currentGame.getTargetScore());
                        Toast.makeText(context, "The message was delivered to '"+contact+"'. Please swipe down on your accepted challenges page to refresh the list.", Toast.LENGTH_LONG).show();
                        if(gameWinner==null || gameWinner.length()==0){
                            ;
                        }
                        else{
                            app.currentGameIsWon = true;
                            if(gameWinner.equals(currentPlayerId)) {
                               Toast.makeText(context, "Congratulations, you have won the game!!",Toast.LENGTH_LONG).show();
                           }

                        }
                    }

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "The message was not delivered. The challenge cannot not be counted.",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}
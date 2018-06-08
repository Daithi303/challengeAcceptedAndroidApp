package ie.dodwyer.adapters;

/**
 * Created by User on 4/23/2017.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import ie.dodwyer.R;
import ie.dodwyer.main.ChallengeAcceptedApp;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.main.ChallengeAcceptedApp;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/24/2017.
 */

public class ScoreboardPlayerItem {
    protected Base activity;
    ChallengeAcceptedApp app;
    public View view;
    public ScoreboardPlayerItem(Context context, ViewGroup parent, GamePlayers gamePlayer)
    {
        this.activity = (Base) context;
        app = new ChallengeAcceptedApp();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.scoreboard_player_row, parent, false);
        view.setId(gamePlayer.getGameId());
        view.setTag(gamePlayer);
        updateControls(gamePlayer);
    }


    private void updateControls(GamePlayers gamePlayer) {
        String playerId = gamePlayer.getPlayerId();
        if(gamePlayer.getWinner()==1){
          TextView scoreboardPlayerStatusValueTextView =  ((TextView) view.findViewById(R.id.scoreboardPlayerStatusValueTextView));
            scoreboardPlayerStatusValueTextView.setText("Winner");
            scoreboardPlayerStatusValueTextView.setTextColor(ContextCompat.getColor(activity,R.color.colorAlternative));
        }
        ArrayList<Player> p = (ArrayList<Player>) activity.app.dbManager.getPlayersConditional("playerId = '"+playerId+"'");
        ((TextView) view.findViewById(R.id.scoreboardPlayerEmailValueTextView)).setText(p.get(0).getEmail());
        ((TextView) view.findViewById(R.id.scoreboardPointsValueTextView)).setText(String.valueOf(gamePlayer.getScoreTotal()));


    }
}
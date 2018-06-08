package ie.dodwyer.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.GameActivity;
import ie.dodwyer.adapters.PlayerFilter;
import ie.dodwyer.adapters.PlayerListAdapter;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

/**
 * Created by User on 4/25/2017.
 */

public class SearchPlayerItemFragment extends PlayerItemFragment implements TextWatcher {
    EditText addPlayerSearchEditText;
    Button addPlayerButton;
    public SearchPlayerItemFragment() {
    }

    public static SearchPlayerItemFragment newInstance() {
        SearchPlayerItemFragment fragment = new SearchPlayerItemFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPlayerSearchEditText = (EditText) activity.findViewById(R.id.addPlayerSearchEditText);
        addPlayerSearchEditText.addTextChangedListener(this);
        activity.app.selectedPlayers = new ArrayList<>();
        addPlayerButton = (Button) activity.findViewById(R.id.addPlayerButton);
        addPlayerButton.setOnClickListener(new MyOnClickListener());
        playerFilter.setPlayerFilter("addPlayer");
        playerFilter.filter("");
    }

    public class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(activity.app.selectedPlayers==null|| activity.app.selectedPlayers.size()==0){
                Toast.makeText(activity,"Please select at least one player (if any are available).",Toast.LENGTH_LONG).show();

            }
            else{
                int gameId = activity.app.currentGame.getGameId();
                String playerId = null;
                GamePlayers gamePlayers;
                for (Player player : activity.app.selectedPlayers) {
                    playerId = player.getPlayerId();
                    gamePlayers = new GamePlayers(gameId, playerId,0, 0);
                    long result = (int) activity.app.dbManager.createGamePlayers(gamePlayers);
                    if (result <= 0) {
                        Toast.makeText(activity,"The player/players could not be added to this game at this time.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(activity,"The player/players have been added to this game.",Toast.LENGTH_LONG).show();
                        Bundle activityInfo = new Bundle();
                        activityInfo.putString(activity.app.CALLER_ACTIVITY, this.getClass().getSimpleName());
                        activityInfo.putInt("gameId", activity.app.currentGame.getGameId());
                        activityInfo.putInt("pagePosition", 6);
                        Intent goToGame = new Intent(activity, GameActivity.class);
                        goToGame.putExtras(activityInfo);
                        startActivity(goToGame);
                    }
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
/*        if(count == 0){
        playerFilter.setPlayerFilter("all");
        }
        else {
            playerFilter.setPlayerFilter(null);
        }*/
        playerFilter.filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

package ie.dodwyer.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;

/**
 * Created by User on 2/26/2017.
 */

public class GameOutboxFragment extends Fragment {
    View view;
    int challengeCount;
    TextView noChallengesOutboxTextView;
    ChallengeOutboxItemFragment challengeOutboxItemFragment;
    protected Base activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
view = inflater.inflate(R.layout.activity_game_outbox,container,false);
        noChallengesOutboxTextView = (TextView) view.findViewById(R.id.noChallengesOutboxTextView);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        List<Challenge> challenges;
        ArrayList<Game> newGames = new ArrayList<>();

        String currentPlayerId = activity.app.currentPlayer.getPlayerId();
        int currentGameId = activity.app.currentGame.getGameId();
        String filter = "nomineeId IS NOT NULL AND createdBy = '"+currentPlayerId+"' AND gameId = "+currentGameId;
        challenges = activity.app.dbManager.getChallengesConditional(filter);
        challengeCount = challenges.size();

        challengeOutboxItemFragment = ChallengeOutboxItemFragment.newInstance();

        getFragmentManager().beginTransaction().add(R.id.challengeOutboxListFragment, challengeOutboxItemFragment).commit(); // add it to the current activity
        if(challengeCount > 0){
            noChallengesOutboxTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesOutboxTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }
}

package ie.dodwyer.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Game;

/**
 * Created by User on 2/26/2017.
 */

public class GameInboxFragment extends Fragment {
    View view;
    int challengeCount;
    protected Base activity;
    TextView noChallengesInboxTextView;
    ChallengeInboxItemFragment challengeInboxItemFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_game_inbox,container,false);
        noChallengesInboxTextView = (TextView) view.findViewById(R.id.noChallengesInboxTextView);
        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        List<Challenge> challenges;
        ArrayList<Game> newGames = new ArrayList<>();
        activity.getPermissionToReadUserContacts();
        activity.getPermissionToSendSMS();
        String currentPlayerId = activity.app.currentPlayer.getPlayerId();
        int currentGameId = activity.app.currentGame.getGameId();
        String filter = "nomineeId = '"+currentPlayerId+"' AND gameId = "+currentGameId+" AND result IS NULL";

        challenges = activity.app.dbManager.getChallengesConditional(filter);
        challengeCount = challenges.size();

        challengeInboxItemFragment = ChallengeInboxItemFragment.newInstance();

        getFragmentManager().beginTransaction().add(R.id.challengeInboxListFragment, challengeInboxItemFragment).commit(); // add it to the current activity

        if(challengeCount > 0){
            noChallengesInboxTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesInboxTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }

}

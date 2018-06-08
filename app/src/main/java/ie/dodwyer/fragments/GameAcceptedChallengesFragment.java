package ie.dodwyer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

/**
 * Created by User on 4/16/2017.
 */

public class GameAcceptedChallengesFragment extends Fragment {

    View view;
    int challengeCount;
    protected Base activity;
    TextView noChallengesAcceptedChallengesTextView;
    ChallengeAcceptedChallengesItemFragment challengeAcceptedChallengesItemFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_game_accepted_challenges,container,false);
        noChallengesAcceptedChallengesTextView = (TextView) view.findViewById(R.id.noChallengesAcceptedChallengesTextView);

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
        String filter = "nomineeId = '"+currentPlayerId+"' AND gameId = "+currentGameId+" AND result = '"+activity.app.CHALLENGE_STATUS_ACCEPTED+"'";

        challenges = activity.app.dbManager.getChallengesConditional(filter);
        challengeCount = challenges.size();

        challengeAcceptedChallengesItemFragment = ChallengeAcceptedChallengesItemFragment.newInstance();

        getFragmentManager().beginTransaction().add(R.id.challengeAcceptedChallengesListFragment, challengeAcceptedChallengesItemFragment).commit(); // add it to the current activity
        if(challengeCount > 0){
            noChallengesAcceptedChallengesTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesAcceptedChallengesTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }

}

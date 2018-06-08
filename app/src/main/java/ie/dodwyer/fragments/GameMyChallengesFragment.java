package ie.dodwyer.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;

public class GameMyChallengesFragment extends Fragment{
        View view;
        ListView listView;
    int challengeCount;
    TextView noChallengesTextView;
    protected Base activity;
    ChallengeMyChallengesItemFragment challengeMyChallengesItemFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
view = inflater.inflate(R.layout.activity_game_my_challenges,container,false);
        noChallengesTextView = (TextView) view.findViewById(R.id.noChallengesTextView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        challengeMyChallengesItemFragment = ChallengeMyChallengesItemFragment.newInstance();
        List challenges = activity.app.dbManager.getChallengesConditional("createdBy = '"+activity.app.currentPlayer.getPlayerId()+"' AND nomineeId IS NULL");
        challengeCount = challenges.size();
        getFragmentManager().beginTransaction().add(R.id.challengeListFragment, challengeMyChallengesItemFragment).commit(); // add it to the current activity
        if(challengeCount > 0){
            noChallengesTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }
}

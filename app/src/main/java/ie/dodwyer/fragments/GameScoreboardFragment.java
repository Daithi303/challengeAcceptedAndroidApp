package ie.dodwyer.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;

/**
 * Created by User on 2/26/2017.
 */

public class GameScoreboardFragment extends Fragment {
    View view;
    PlayerScoreboardItemFragment playerScoreboardItemFragment;
    TextView scoreboardSubHeaderLabelTextView;
    protected Base activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.activity_game_scoreboard,container,false);
        scoreboardSubHeaderLabelTextView = (TextView)view.findViewById(R.id.scoreboardSubHeaderLabelTextView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String targetScore = getResources().getString(R.string.scoreboardSubHeaderLabelTextView);
        scoreboardSubHeaderLabelTextView.setText(targetScore+" "+String.valueOf(activity.app.currentGame.getTargetScore()));
        playerScoreboardItemFragment = PlayerScoreboardItemFragment.newInstance();

        getFragmentManager().beginTransaction().add(R.id.playerScoreboardListFragment, playerScoreboardItemFragment).commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }
}

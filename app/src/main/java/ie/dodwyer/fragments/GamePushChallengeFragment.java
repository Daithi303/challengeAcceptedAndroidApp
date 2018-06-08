package ie.dodwyer.fragments;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.activities.GameActivity;
import ie.dodwyer.adapters.SpinnerChallengeAdapter;
import ie.dodwyer.adapters.SpinnerPlayerAdapter;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/26/2017.
 */

public class GamePushChallengeFragment extends Fragment implements View.OnClickListener{
    protected Base activity;

    private View view;
    private List<Challenge> challengeList,challengeListFiltered;
    private List<Player> playerList,playerListFiltered;
    private List<GamePlayers> gamePlayersList;
    private Spinner selectAChallengeSpinner;
    private Spinner selectAPlayerSpinner;
    private SpinnerChallengeAdapter spinnerChallengeAdapter;
    private SpinnerPlayerAdapter spinnerPlayerAdapter;
    private Button pushChallenge;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_game_push_challenge,container,false);
        challengeList =  activity.app.dbManager.getAllChallenges();
        challengeListFiltered = new ArrayList();
        playerListFiltered = new ArrayList<Player>();
        gamePlayersList = activity.app.dbManager.getGamePlayersConditional("gameId = "+ activity.app.currentGame.getGameId());

        for(GamePlayers gp:gamePlayersList){
            playerList = activity.app.dbManager.getPlayersConditional("playerId = '"+gp.getPlayerId()+"'");
            playerListFiltered.add(playerList.get(0));
        }


        for(int i = 0;i< challengeList.size();i++){
            if((challengeList.get(i).getCreatedBy().equals(activity.app.currentPlayer.getPlayerId()))&& (challengeList.get(i).getNomineeId()==null)){
                challengeListFiltered.add(challengeList.get(i));
            }
        }

        selectAChallengeSpinner = (Spinner) view.findViewById(R.id.selectAChallengeSpinner);
        selectAPlayerSpinner = (Spinner) view.findViewById(R.id.selectAPlayerSpinner);

        spinnerChallengeAdapter = new SpinnerChallengeAdapter(getActivity(),challengeListFiltered);
        spinnerChallengeAdapter.notifyDataSetChanged();

        spinnerPlayerAdapter = new SpinnerPlayerAdapter(getActivity(),playerListFiltered);
        spinnerPlayerAdapter.notifyDataSetChanged();

        selectAChallengeSpinner.setAdapter(spinnerChallengeAdapter);
        selectAPlayerSpinner.setAdapter(spinnerPlayerAdapter);
        if(getArguments()!=null) {
            int challengeId = getArguments().getInt("challengeId");
            for (int i = 0; i < challengeListFiltered.size(); i++){
                if(challengeListFiltered.get(i).getChallengeId()==challengeId)
                {
                    selectAChallengeSpinner.setSelection(i);
                }

            }

        }

        pushChallenge = (Button) view.findViewById(R.id.pushChallengeButton);
        pushChallenge.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }


    @Override
    public void onClick(View v) {
        if (activity.app.currentGameIsWon) {
            activity.goToMyGames(activity);
        } else {
            pushChallenge();
        }
    }

    public void pushChallenge() {

            try {
                Challenge challenge;
                Player player;
                int result;
                challenge = (Challenge) selectAChallengeSpinner.getSelectedItem();
                player = (Player) selectAPlayerSpinner.getSelectedItem();
                ContentValues cv = new ContentValues();
                cv.put("nomineeId", player.getPlayerId());
                cv.put("gameId", activity.app.currentGame.getGameId());
                result = (int) activity.app.dbManager.updateChallenge(cv, challenge.getChallengeId());
                if (result <= 0) {
                    throw new Exception("Challenge hasn't been pushed. Value of result: " + result);

                } else {
                    Bundle activityInfo = new Bundle();
                    activityInfo.putString(activity.app.CALLER_ACTIVITY, this.getClass().getSimpleName());
                    activityInfo.putInt("pagePosition", 4);
                    Intent goToGame = new Intent(getActivity(), GameActivity.class);
                    goToGame.putExtras(activityInfo);
                    startActivity(goToGame);
                }


            } catch (Exception e) {
                if (!(e.getMessage() == null)) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

    }
}

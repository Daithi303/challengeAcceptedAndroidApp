package ie.dodwyer.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.activities.GameActivity;
import ie.dodwyer.activities.MyGamesActivity;
import ie.dodwyer.activities.ScoreboardCompletedGameActivity;
import ie.dodwyer.adapters.GameFilter;
import ie.dodwyer.adapters.GameListAdapter;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;

/**
 * Created by User on 2/24/2017.
 */

public class GameItemFragment extends ListFragment{
    ListView listView;
    SwipeRefreshLayout swipeLayout;
    TextView noGamesTextView;
    Base activity;
    GameFilter gameFilter;
    public static GameListAdapter listAdapter;
    public GameItemFragment(){
    }

    public static GameItemFragment newInstance() {
        GameItemFragment fragment = new GameItemFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noGamesTextView = (TextView)getActivity().findViewById(R.id.noGamesTextView);

    }
    private void populateAdapterWithFilter(){
        ArrayList<Game> gameList = new ArrayList<Game>();
        List<GamePlayers> gamePlayers;
        gamePlayers = activity.app.dbManager.getGamePlayersConditional("playerId = '"+activity.app.currentPlayer.getPlayerId()+"'");
        for(int i = 0;i<gamePlayers.size();i++){
            gameList.add(activity.app.dbManager.getGamesConditional("gameId = "+gamePlayers.get(i).getGameId()).get(0));
        }
        listAdapter = new GameListAdapter(activity, new DeleteListener(),new NewChallengeListener(), gameList);
 }


    private void refreshAdapter(){
        ArrayList<Game> gameList = new ArrayList<Game>();
        List<GamePlayers> gamePlayers;
        gamePlayers = activity.app.dbManager.getGamePlayersConditional("playerId = '"+activity.app.currentPlayer.getPlayerId()+"'");
        for(int i = 0;i<gamePlayers.size();i++){
            gameList.add(activity.app.dbManager.getGamesConditional("gameId = "+gamePlayers.get(i).getGameId()).get(0));
        }
        listAdapter.gameList = new ArrayList<Game>();
        listAdapter.gameList = gameList;
        listAdapter.notifyDataSetChanged();
        if(gameList.size()>0){
            noGamesTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noGamesTextView.setVisibility(View.VISIBLE);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        populateAdapterWithFilter();
        setListAdapter (listAdapter);
        swipeLayout = new GameItemFragment.ListFragmentSwipeRefreshLayout(getActivity());
        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeLayout.setLayoutParams(params);

        swipeLayout.setOnRefreshListener(new GameItemFragment.GameOnRefreshListener());
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return swipeLayout;
    }

    public class GameOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refreshAdapter();
            swipeLayout.setRefreshing(false);
        }
    }

    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {

        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        @Override
        public boolean canChildScrollUp() {
            final ListView listView = getListView();
            if (listView.getVisibility() == View.VISIBLE) {
                return canListViewScrollUp(listView);
            } else {
                return false;
            }
        }

    }


    private static boolean canListViewScrollUp(ListView listView) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(listView, -1);
        } else {
            return listView.getChildCount() > 0 &&
                    (listView.getFirstVisiblePosition() > 0
                            || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(20);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l,v,position,id);
        activity.app.currentGame = (Game) v.getTag();
        Bundle activityInfo = new Bundle();
        activityInfo.putString(activity.app.CALLER_ACTIVITY, getActivity().getClass().getSimpleName());
        if(activity.app.currentGame.getGameStatus().equals(activity.app.GAME_STATUS_IN_PROGRESS)) {
            activityInfo.putInt("gameId", v.getId());
            activityInfo.putInt("pagePosition", 4);
            Intent goToGame = new Intent(getActivity(), GameActivity.class);
            goToGame.putExtras(activityInfo);
            getActivity().startActivity(goToGame);
        }else if(activity.app.currentGame.getGameStatus().equals(activity.app.GAME_STATUS_COMPLETE)){
            activityInfo.putInt("gameId", v.getId());
            Intent goToGame = new Intent(getActivity(), ScoreboardCompletedGameActivity.class);
            goToGame.putExtras(activityInfo);
            getActivity().startActivity(goToGame);
        }
    }




    public class DeleteListener implements OnClickListener {
        Game game;
        @Override
        public void onClick(View v) {
            game = new Game();
            game = (Game) v.getTag();
            String gameName = game.getGameName();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Are you sure you want to delete the game " + gameName + "?");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    int result;
                    result = activity.app.dbManager.deleteChallengesConditional("gameId",game.getGameId());
                    result = activity.app.dbManager.deleteGamePlayersConditional("gameId",game.getGameId());
                    result = activity.app.dbManager.deleteGamesConditional(game.getGameId());
                    listAdapter.gameList.remove(game);
                    listAdapter.notifyDataSetChanged();
                    Intent goToGame = new Intent(getActivity(), MyGamesActivity.class);
                    startActivity(goToGame);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public class NewChallengeListener implements OnClickListener {
        Game game;
        @Override
        public void onClick(View v) {
            game = new Game();

            game = (Game) v.getTag();
            Bundle activityInfo = new Bundle();
            activityInfo.putString(activity.app.CALLER_ACTIVITY, getActivity().getClass().getSimpleName());
            activityInfo.putInt("gameId", game.getGameId());
            activityInfo.putInt("pagePosition",1);
            Intent goToGame = new Intent(getActivity(), GameActivity.class);
            goToGame.putExtras(activityInfo);
            getActivity().startActivity(goToGame);
        }
    }
}

package ie.dodwyer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ie.dodwyer.activities.Base;
import ie.dodwyer.adapters.ScoreboardPlayerListAdapter;
import ie.dodwyer.model.GamePlayers;

/**
 * Created by User on 4/23/2017.
 */

public class PlayerScoreboardItemFragment extends ListFragment{
    protected Base activity;
    SwipeRefreshLayout swipeLayout;
    protected static ScoreboardPlayerListAdapter listAdapter;
    protected ListView listView;
    public PlayerScoreboardItemFragment(){
    }

    public static PlayerScoreboardItemFragment newInstance() {
        PlayerScoreboardItemFragment fragment = new PlayerScoreboardItemFragment();
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
        String[] gameIdValue = new String[]{String.valueOf(activity.app.currentGame.getGameId())};
        listAdapter = new ScoreboardPlayerListAdapter(activity, activity.app.dbManager.getGamePlayersConditionalSortDesc("gameId",gameIdValue));
        setListAdapter (listAdapter);
    }
    private void populateAdapterWithFilter(){
        String[] gameIdValue = new String[]{String.valueOf(activity.app.currentGame.getGameId())};
        listAdapter = new ScoreboardPlayerListAdapter(activity, activity.app.dbManager.getGamePlayersConditionalSortDesc("gameId",gameIdValue));
        setListAdapter (listAdapter);
    }


    private void refreshAdapter(){
        String[] gameIdValue = new String[]{String.valueOf(activity.app.currentGame.getGameId())};
        ArrayList<GamePlayers> gp = (ArrayList<GamePlayers>) activity.app.dbManager.getGamePlayersConditionalSortDesc("gameId",gameIdValue);
        listAdapter.gamePlayersList =gp;
        listAdapter.notifyDataSetChanged();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        populateAdapterWithFilter();
        setListAdapter (listAdapter);
        swipeLayout = new PlayerScoreboardItemFragment.ListFragmentSwipeRefreshLayout(getActivity());
        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeLayout.setLayoutParams(params);

        swipeLayout.setOnRefreshListener(new PlayerScoreboardItemFragment.ScoreboardOnRefreshListener());
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return swipeLayout;
    }

    public class ScoreboardOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
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


    public void onViewCreated(View view, Bundle savedInstanceState){
        listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(10);
    }
}

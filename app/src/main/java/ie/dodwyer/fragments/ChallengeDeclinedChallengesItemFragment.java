package ie.dodwyer.fragments;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.adapters.ChallengeFilter;
import ie.dodwyer.adapters.ChallengeListAdapter;
import ie.dodwyer.model.Challenge;

/**
 * Created by User on 4/16/2017.
 */

public class ChallengeDeclinedChallengesItemFragment extends ListFragment {
    SwipeRefreshLayout swipeLayout;
    protected Base activity;
    TextView noChallengesDeclinedChallengesTextView;
    public static ChallengeListAdapter listAdapter;
    protected ListView listView;
    protected ChallengeFilter challengeFilter;
    public ChallengeDeclinedChallengesItemFragment(){
    }

    public static ChallengeDeclinedChallengesItemFragment newInstance() {
        ChallengeDeclinedChallengesItemFragment fragment = new ChallengeDeclinedChallengesItemFragment();
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
        noChallengesDeclinedChallengesTextView = (TextView) getActivity().findViewById(R.id.noChallengesDeclinedChallengesTextView);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {

    }
    @Override
    public void onResume(){
     super.onResume();

    }

    private void populateAdapterWithFilter(){
        String currentPlayerId = activity.app.currentPlayer.getPlayerId();
        int currentGameId = activity.app.currentGame.getGameId();
        String filter = "nomineeId = '"+currentPlayerId+"' AND gameId = "+currentGameId+" AND result = '"+activity.app.CHALLENGE_STATUS_DECLINED+"'";
        List<Challenge> challengeList =   activity.app.dbManager.getChallengesConditional(filter);
        listAdapter = new ChallengeListAdapter(activity, null,null,null, challengeList, R.layout.challenge_declined_challenges_row);
        listAdapter.notifyDataSetChanged();

    }

    private void refreshAdapter(){
        String currentPlayerId = activity.app.currentPlayer.getPlayerId();
        int currentGameId = activity.app.currentGame.getGameId();
        String filter = "nomineeId = '"+currentPlayerId+"' AND gameId = "+currentGameId+" AND result = '"+activity.app.CHALLENGE_STATUS_DECLINED+"'";
        List<Challenge> challengeList =   activity.app.dbManager.getChallengesConditional(filter);
        listAdapter.challengeList = new ArrayList<Challenge>();
        listAdapter.challengeList = challengeList;
        listAdapter.notifyDataSetChanged();
        if(challengeList.size()>0){
            noChallengesDeclinedChallengesTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesDeclinedChallengesTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        populateAdapterWithFilter();
        setListAdapter (listAdapter);
        swipeLayout = new ListFragmentSwipeRefreshLayout(getActivity());
        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeLayout.setLayoutParams(params);

        swipeLayout.setOnRefreshListener(new DeclinedChallengesOnRefreshListener());
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return swipeLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(20);
    }

    public class DeclinedChallengesOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
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

}
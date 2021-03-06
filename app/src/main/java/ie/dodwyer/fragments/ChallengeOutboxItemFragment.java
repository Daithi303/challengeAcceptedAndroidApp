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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.activities.EditChallengeActivity;
import ie.dodwyer.adapters.ChallengeFilter;
import ie.dodwyer.adapters.ChallengeListAdapter;
import ie.dodwyer.model.Challenge;

/**
 * Created by User on 2/24/2017.
 */

public class ChallengeOutboxItemFragment extends ListFragment implements OnClickListener {
    protected Base activity;
    SwipeRefreshLayout swipeLayout;
    TextView noChallengesOutboxTextView;
    public static ChallengeListAdapter listAdapter;
    protected ListView listView;
    protected ChallengeFilter challengeFilter;
    public ChallengeOutboxItemFragment(){
    }

    public static ChallengeOutboxItemFragment newInstance() {
        ChallengeOutboxItemFragment fragment = new ChallengeOutboxItemFragment();
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
        noChallengesOutboxTextView = (TextView)getActivity().findViewById(R.id.noChallengesOutboxTextView);

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
        String filter = "nomineeId IS NOT NULL AND createdBy = '"+currentPlayerId+"' AND gameId = "+currentGameId;
        List<Challenge> challengeList =   activity.app.dbManager.getChallengesConditional(filter);
        listAdapter = new ChallengeListAdapter(activity, null,new EditListener(), new DeleteListener(), challengeList, R.layout.challenge_outbox_row);        listAdapter.notifyDataSetChanged();
    }


    private void refreshAdapter(){
        String currentPlayerId = activity.app.currentPlayer.getPlayerId();
        int currentGameId = activity.app.currentGame.getGameId();
        String filter = "nomineeId IS NOT NULL AND createdBy = '"+currentPlayerId+"' AND gameId = "+currentGameId;
        List<Challenge> challengeList =   activity.app.dbManager.getChallengesConditional(filter);
        listAdapter.challengeList = new ArrayList<Challenge>();
        listAdapter.challengeList = challengeList;
        listAdapter.notifyDataSetChanged();
        if(challengeList.size()>0){
            noChallengesOutboxTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesOutboxTextView.setVisibility(View.VISIBLE);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        populateAdapterWithFilter();
        setListAdapter (listAdapter);
        swipeLayout = new ChallengeOutboxItemFragment.ListFragmentSwipeRefreshLayout(getActivity());
        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeLayout.setLayoutParams(params);

        swipeLayout.setOnRefreshListener(new ChallengeOutboxItemFragment.OutboxOnRefreshListener());
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return swipeLayout;
    }

    public class OutboxOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
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
    public void onClick(View v) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(20);
    }


    public class EditListener implements OnClickListener {
        Challenge challenge;
        @Override
        public void onClick(View v) {
            challenge = new Challenge();
            challenge = (Challenge) v.getTag();
            Intent i = new Intent(getActivity(), EditChallengeActivity.class);
            i.putExtra("challengeId",challenge.getChallengeId());
            startActivity(i);
        }
    }

    public class DeleteListener implements OnClickListener {
        Challenge challenge;
        @Override
        public void onClick(View v) {
            challenge = new Challenge();
            challenge = (Challenge) v.getTag();
            String challengeSubject = challenge.getSubject();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Are you sure you want to delete the challenge " + challengeSubject + "?");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {

                    int result = activity.app.dbManager.deleteChallengesConditional(challenge.getChallengeId());
                    listAdapter.challengeList.remove(challenge); // update adapters data
                    listAdapter.notifyDataSetChanged();
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

}

package ie.dodwyer.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import ie.dodwyer.activities.AttemptChallengeActivity;
import ie.dodwyer.activities.Base;
import ie.dodwyer.activities.GameActivity;
import ie.dodwyer.adapters.ChallengeFilter;

import ie.dodwyer.adapters.ChallengeListAdapter;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Game;

/**
 * Created by User on 2/24/2017.
 */

public class ChallengeInboxItemFragment extends ListFragment implements OnClickListener {
    SwipeRefreshLayout swipeLayout;
    TextView noChallengesInboxTextView;
    protected Base activity;
    public static ChallengeListAdapter listAdapter;
    protected ListView listView;
    protected ChallengeFilter challengeFilter;
    public ChallengeInboxItemFragment(){
    }

    public static ChallengeInboxItemFragment newInstance() {
        ChallengeInboxItemFragment fragment = new ChallengeInboxItemFragment();
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
        noChallengesInboxTextView = (TextView)getActivity().findViewById(R.id.noChallengesInboxTextView);
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
        String filter = "nomineeId = '"+currentPlayerId+"' AND gameId = "+currentGameId+" AND result IS NULL";
        List<Challenge> challengeList =   activity.app.dbManager.getChallengesConditional(filter);
        listAdapter = new ChallengeListAdapter(activity, new Listener(),null,null, challengeList, R.layout.challenge_inbox_row);
        listAdapter.notifyDataSetChanged();
    }


    private void refreshAdapter(){
        String currentPlayerId = activity.app.currentPlayer.getPlayerId();
        int currentGameId = activity.app.currentGame.getGameId();
        String filter = "nomineeId = '"+currentPlayerId+"' AND gameId = "+currentGameId+" AND result IS NULL";
        List<Challenge> challengeList =   activity.app.dbManager.getChallengesConditional(filter);
        listAdapter.challengeList = new ArrayList<Challenge>();
        listAdapter.challengeList = challengeList;
        listAdapter.notifyDataSetChanged();
        if(challengeList.size()>0){
            noChallengesInboxTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noChallengesInboxTextView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {

    }




    public class Listener implements OnClickListener {
        Challenge challenge;
        @Override
        public void onClick(final View v) {
            if(activity.app.currentGameIsWon){activity.goToMyGames(activity);}else {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Please note: You can only attempt a challenge once. If you wish to proceed and decline the challenge it will be removed from your inbox.Do you wish to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle activityInfo = new Bundle();
                                activityInfo.putString(activity.app.CALLER_ACTIVITY, getActivity().getClass().getSimpleName());
                                activityInfo.putInt("gameId", activity.app.currentGame.getGameId());
                                activityInfo.putInt("pagePosition",1);
                                Intent goToGame = new Intent(getActivity(), GameActivity.class);
                                goToGame.putExtras(activityInfo);
                                getActivity().startActivity(goToGame);
                            }
                        });

                        int result = 0;
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED) {
                            builder.setMessage("This application has been denied permission to the device's contact information.Please allow access " +
                                    "by changing the device's contact permissions for this application.");
                            builder.show();
                        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                                != PackageManager.PERMISSION_GRANTED) {
                            builder.setMessage("This application has been denied permission to the device's SMS capability.Please allow access " +
                                    "by changing the device's SMS permissions for this application.");
                            builder.show();

                        } else {
                            result = activity.app.getContactList();
                        }

                        if (result == -1) {
                            builder.setMessage("There are currently not enough contacts suitable for sms messaging on this device at this time.");
                            builder.show();
                        } else if (result == 1) {
                            activity.app.populateAttemptContacts();
                            activity.app.populateSelectedContact();
                            challenge = new Challenge();
                            challenge = (Challenge) v.getTag();
                            ContentValues cv = new ContentValues();
                            cv.put("result", activity.app.CHALLENGE_STATUS_PENDING_INTERUPTED);
                            activity.app.dbManager.updateChallenge(cv, challenge.getChallengeId());
                            Bundle activityInfo = new Bundle();
                            activityInfo.putString(activity.app.CALLER_ACTIVITY, getActivity().getClass().getSimpleName());
                            activityInfo.putInt("challengeId", challenge.getChallengeId());
                            Intent goToAttemptChallenge = new Intent(getActivity(), AttemptChallengeActivity.class);
                            goToAttemptChallenge.putExtras(activityInfo);
                            getActivity().startActivity(goToAttemptChallenge);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(20);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        populateAdapterWithFilter();
        setListAdapter (listAdapter);
        swipeLayout = new ListFragmentSwipeRefreshLayout(getActivity());
        SwipeRefreshLayout.LayoutParams params = new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeLayout.setLayoutParams(params);

        swipeLayout.setOnRefreshListener(new ChallengeInboxItemFragment.InboxOnRefreshListener());
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return swipeLayout;
    }

    public class InboxOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
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

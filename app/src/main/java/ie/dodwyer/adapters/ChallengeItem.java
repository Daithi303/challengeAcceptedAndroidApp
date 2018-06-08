package ie.dodwyer.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/24/2017.
 */

public class ChallengeItem {
    protected Base activity;
    public View view;
    View.OnClickListener listener;
    View.OnClickListener editListener;
    View.OnClickListener deleteListener;

    public ChallengeItem(Context context, ViewGroup parent,View.OnClickListener listener,
                         View.OnClickListener editListener, View.OnClickListener deleteListener, Challenge challenge,int resource)
    {
        this.activity = (Base) context;
        this.listener = listener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        view.setId(challenge.getChallengeId());
        view.setTag(challenge);
        if(resource==R.layout.challenge_inbox_row){
            updateControlsInbox(challenge);
        }
        if(resource==R.layout.challenge_outbox_row){
            updateControlsOutbox(challenge);
        }

        if(resource==R.layout.challenge_my_challenges_row){
            updateControlsMyChallenges(challenge);
        }

        if(resource==R.layout.challenge_accepted_challenges_row){
            updateControlsAcceptedChallenges(challenge);
        }

        if(resource==R.layout.challenge_declined_challenges_row){
            updateControlsDeclinedChallenges(challenge);
        }
    }



    private void updateControlsInbox(Challenge challenge) {
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.challengeInboxTableLayout);
        ImageView imgAttemptChallengeButton = (ImageView) view.findViewById(R.id.challengeInboxAttemptChallengeButtonImageView);
        imgAttemptChallengeButton.setTag(challenge);
        imgAttemptChallengeButton.setOnTouchListener(new MyOnTouchListener());
        imgAttemptChallengeButton.setOnClickListener(listener);
        ((TextView) view.findViewById(R.id.challengeInboxSubjectValueTextView)).setText(challenge.getSubject());

        ((TextView) view.findViewById(R.id.challengeInboxCreatedValueTextView)).setText(challenge.getCreationDate());

        ArrayList<Player> senderList = (ArrayList<Player>) activity.app.dbManager.getPlayersConditional("playerId = '"+challenge.getCreatedBy()+"'");
        if(!(senderList==null || senderList.size()==0)) {
            String senderNameAndEmail = senderList.get(0).getfName() + " " + senderList.get(0).getlName() + " (" + senderList.get(0).getEmail() + ")";
            ((TextView) view.findViewById(R.id.challengeInboxSenderValueTextView)).setText(senderNameAndEmail);
        }

        if(!(challenge.getMaxPointValue()==-999)) {
            ((TextView) view.findViewById(R.id.challengeInboxMaxScoreValueTextView)).setText(String.valueOf(challenge.getMaxPointValue()));
        }

        if(!(challenge.getResult()==null)) {
            ((TextView) view.findViewById(R.id.challengeInboxStatusValueTextView)).setText(String.valueOf(challenge.getResult()));
        }



        /////////////////////////just trying it out - conditionally removing parts of the item////////////////////////////////
        /*
        if((challenge.getMaxPointValue()==4)) {
            TableRow tableRow = (TableRow) tableLayout.findViewById(R.id.challengeInboxStatusTableRow);
            tableLayout.removeView(tableRow);
        }*/
}


private void updateControlsAcceptedChallenges(Challenge challenge){
    ((TextView) view.findViewById(R.id.challengeAcceptedChallengesSubjectValueTextView)).setText(challenge.getSubject());

    ((TextView) view.findViewById(R.id.challengeAcceptedChallengesCreatedValueTextView)).setText(challenge.getCreationDate());

    ArrayList<Player> senderList = (ArrayList<Player>) activity.app.dbManager.getPlayersConditional("playerId = '"+challenge.getCreatedBy()+"'");

    if(!(senderList==null || senderList.size()==0)) {
        String senderNameAndEmail = senderList.get(0).getfName() + " " + senderList.get(0).getlName() + " (" + senderList.get(0).getEmail() + ")";
        ((TextView) view.findViewById(R.id.challengeAcceptedChallengesSenderValueTextView)).setText(senderNameAndEmail);
    }

    if(!(challenge.getMaxPointValue()==-999)) {
        ((TextView) view.findViewById(R.id.challengeAcceptedChallengesMaxScoreValueTextView)).setText(String.valueOf(challenge.getMaxPointValue()));
    }
Double pointsAwarded = challenge.getPointsAwarded();
    if(!(challenge.getPointsAwarded()==-999)) {
        ((TextView) view.findViewById(R.id.challengeAcceptedChallengesPointsAwardedValueTextView)).setText(String.valueOf(challenge.getPointsAwarded()));
    }

    if(!(challenge.getContactInReceiverAddressBook()==null)) {
        ((TextView) view.findViewById(R.id.challengeAcceptedChallengesContactInReceiverAddressBookValueTextView)).setText(String.valueOf(challenge.getContactInReceiverAddressBook()));
    }

    ((TextView) view.findViewById(R.id.challengeAcceptedChallengesMessageValueTextView)).setText(challenge.getMessage());
}

    private void updateControlsDeclinedChallenges(Challenge challenge){
        ((TextView) view.findViewById(R.id.challengeDeclinedChallengesSubjectValueTextView)).setText(challenge.getSubject());

        ((TextView) view.findViewById(R.id.challengeDeclinedChallengesCreatedValueTextView)).setText(challenge.getCreationDate());

        ArrayList<Player> senderList = (ArrayList<Player>) activity.app.dbManager.getPlayersConditional("playerId = '"+challenge.getCreatedBy()+"'");

        if(!(senderList==null || senderList.size()==0)) {
            String senderNameAndEmail = senderList.get(0).getfName() + " " + senderList.get(0).getlName() + " (" + senderList.get(0).getEmail() + ")";
            ((TextView) view.findViewById(R.id.challengeDeclinedChallengesSenderValueTextView)).setText(senderNameAndEmail);
        }

        if(!(challenge.getMaxPointValue()==-999)) {
            ((TextView) view.findViewById(R.id.challengeDeclinedChallengesMaxScoreValueTextView)).setText(String.valueOf(challenge.getMaxPointValue()));
        }

        ((TextView) view.findViewById(R.id.challengeDeclinedChallengesMessageValueTextView)).setText(challenge.getMessage());
    }


    private void updateControlsOutbox(Challenge challenge) {
        ((TextView) view.findViewById(R.id.challengeOutboxSubjectValueTextView)).setText(challenge.getSubject());

        ((TextView) view.findViewById(R.id.challengeOutboxCreatedValueTextView)).setText(challenge.getCreationDate());

        ArrayList<Player> receiverList = (ArrayList<Player>) activity.app.dbManager.getPlayersConditional("playerId = '"+challenge.getNomineeId()+"'");
        if(!(receiverList==null || receiverList.size()==0)) {
            String receiverNameAndEmail = receiverList.get(0).getfName() + " " + receiverList.get(0).getlName() + " (" + receiverList.get(0).getEmail() + ")";
            ((TextView) view.findViewById(R.id.challengeOutboxReceiverValueTextView)).setText(receiverNameAndEmail);
        }

        if(!(challenge.getResult()==null)) {
            ((TextView) view.findViewById(R.id.challengeOutboxStatusValueTextView)).setText(challenge.getResult());

        }

        if(!(challenge.getMaxPointValue()==-999)) {
            ((TextView) view.findViewById(R.id.challengeOutboxMaxScoreValueTextView)).setText(String.valueOf(challenge.getMaxPointValue()));
        }

        if(!(challenge.getPointsAwarded()==-999)) {
            ((TextView) view.findViewById(R.id.challengeOutboxPointsAwardedValueTextView)).setText(String.valueOf(challenge.getPointsAwarded()));
        }

        if(!(challenge.getContactInReceiverAddressBook()==null)) {
            ((TextView) view.findViewById(R.id.challengeOutboxContactInReceiverAddressBookValueTextView)).setText(String.valueOf(challenge.getContactInReceiverAddressBook()));
        }


        ((TextView) view.findViewById(R.id.challengeOutboxMessageValueTextView)).setText(challenge.getMessage());


    }

    private void updateControlsMyChallenges(Challenge challenge) {
        ImageView imgEdit = (ImageView) view.findViewById(R.id.challengeEditImageView);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.challengeDeleteImageView);
        imgEdit.setTag(challenge);
        imgDelete.setTag(challenge);
        imgEdit.setOnClickListener(editListener);
        imgDelete.setOnClickListener(deleteListener);
        imgEdit.setOnTouchListener(new MyOnTouchListener());
        imgDelete.setOnTouchListener(new MyOnTouchListener());
        ((TextView) view.findViewById(R.id.challengeSubjectValueTextView)).setText(challenge.getSubject());
        ((TextView) view.findViewById(R.id.challengeCreatedValueTextView)).setText(challenge.getCreationDate());
        ((TextView) view.findViewById(R.id.challengeMaxScoreValueTextView)).setText(String.valueOf(challenge.getMaxPointValue()));
        ((TextView) view.findViewById(R.id.challengeMessageValueTextView)).setText(challenge.getMessage());
    }



    public class MyOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().clearColorFilter();
                    view.invalidate();
                    break;
                }
            }
            return false;
        }

    }

}
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
import ie.dodwyer.database.DBManager;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/24/2017.
 */

public class GameItem{
    protected Base activity;
    DBManager dbManager;
    public View view;
    TableLayout tableLayout;
    ImageView imgDeleteNotification;
    ImageView imgNewChallengeNotification;
    TextView gameWinnerValueTextView;
    public GameItem(Context context, ViewGroup parent,
                      View.OnClickListener deleteListener,View.OnClickListener newChallengeListener, Game game)
    {
        this.activity = (Base) context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.game_row, parent, false);
        view.setTag(game);
        view.setId(game.getGameId());
        gameWinnerValueTextView = (TextView)view.findViewById(R.id.gameWinnerValueTextView);
        updateControls(game);
        imgDeleteNotification = (ImageView) view.findViewById(R.id.deleteNotification);
        imgNewChallengeNotification = (ImageView) view.findViewById(R.id.newChallengeNotification);

        tableLayout = (TableLayout) view.findViewById(R.id.gameTableLayout);
        imgDeleteNotification.setTag(game);
        imgNewChallengeNotification.setTag(game);
/*        if(game.getCreatedBy().equals(activity.app.currentPlayer.getPlayerId())){
            imgDeleteNotification.setOnClickListener(deleteListener);
            imgDeleteNotification.setVisibility(View.VISIBLE);
        }
        else{
            imgDeleteNotification.setVisibility(View.INVISIBLE);
        }*/
        String whereStr = "gameId = "+game.getGameId()+" AND nomineeId = '"+activity.app.currentPlayer.getPlayerId()+"' AND result IS NULL";
        ArrayList challengeList = (ArrayList) activity.app.dbManager.getChallengesConditional(whereStr);

        if(game.getGameStatus().equals(activity.app.GAME_STATUS_IN_PROGRESS)){
            TableRow tableRow = (TableRow) tableLayout.findViewById(R.id.gameWinnerTableRow);
            tableLayout.removeView(tableRow);
        }

        if(!(challengeList==null))
        {
            if(challengeList.size()>0  && game.getGameStatus().equals(activity.app.GAME_STATUS_IN_PROGRESS)){
                    imgNewChallengeNotification.setOnClickListener(newChallengeListener);
                    imgNewChallengeNotification.setVisibility(View.VISIBLE);
                    imgNewChallengeNotification.setOnTouchListener(new MyOnTouchListener());


            }
            else{
                imgNewChallengeNotification.setVisibility(View.INVISIBLE);
            }
        }

        if(game.getCreatedBy().equals(activity.app.currentPlayer.getPlayerId())){

                imgDeleteNotification.setOnClickListener(deleteListener);
                imgDeleteNotification.setVisibility(View.VISIBLE);
                imgDeleteNotification.setOnTouchListener(new MyOnTouchListener());

        }
        else{
            imgDeleteNotification.setVisibility(View.INVISIBLE);
        }

    }


    private void updateControls(Game game) {
        TextView gameStatusTextView = (TextView) view.findViewById(R.id.gameStatusTextView);
        ((TextView) view.findViewById(R.id.gameNameTextView)).setText(game.getGameName());
        gameStatusTextView.setText(game.getGameStatus());
        if(game.getGameStatus().equals(activity.app.GAME_STATUS_IN_PROGRESS)){
            gameStatusTextView.setTextColor(ContextCompat.getColor(activity,R.color.colorAccent));
        }else if(game.getGameStatus().equals(activity.app.GAME_STATUS_COMPLETE)){
            gameStatusTextView.setTextColor(ContextCompat.getColor(activity,R.color.colorAlternative));
        }
        ArrayList<GamePlayers> gp = (ArrayList<GamePlayers>)activity.app.dbManager.getGamePlayersConditional("gameId = "+game.getGameId()+" AND winner = 1");
        if(gp.size()==1){
            String playerId = gp.get(0).getPlayerId();
            ArrayList<Player>p = (ArrayList<Player>)activity.app.dbManager.getPlayersConditional("playerId = '"+playerId+"'");
            if(p.size()==1){
                gameWinnerValueTextView.setText(p.get(0).getEmail());
            }
        }

        ArrayList<Player> playerList = (ArrayList<Player>) activity.app.dbManager.getPlayersConditional("playerId = '"+game.getCreatedBy()+"'");
        ((TextView) view.findViewById(R.id.gameCreatedByValueTextView)).setText((CharSequence) playerList.get(0).getEmail());
        ((TextView) view.findViewById(R.id.gameTargetScoreValueTextView)).setText(String.valueOf(game.getTargetScore()));
        ((TextView) view.findViewById(R.id.gameCreatedValueTextView)).setText(game.getCreationDate());



}



    public class MyOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

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
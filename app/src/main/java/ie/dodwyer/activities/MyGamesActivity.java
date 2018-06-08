package ie.dodwyer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.fragments.GameItemFragment;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

public class MyGamesActivity extends Base implements View.OnClickListener{
int gameCount;
    TextView noGamesTextView;
    String welcome = "Welcome";
    TextView myGamesWelcomeLabelTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noGamesTextView =(TextView) findViewById(R.id.noGamesTextView);
        myGamesWelcomeLabelTextView =(TextView) findViewById(R.id.myGamesWelcomeLabelTextView);
        myGamesWelcomeLabelTextView.setText(welcome+" "+app.currentPlayer.getfName());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context thisActivity = MyGamesActivity.this;
                Intent i = new Intent(thisActivity,CreateGameActivity.class);
                thisActivity.startActivity(i);
            }
        });
    }


    public static MyGamesActivity newInstance() {
        MyGamesActivity activity = new MyGamesActivity();
        return activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<GamePlayers> gamePlayers;
        ArrayList<Game> newGames = new ArrayList<>();
        gamePlayers =app.dbManager.getGamePlayersConditional("playerId = '"+app.currentPlayer.getPlayerId()+"'");
        for(int i = 0;i<gamePlayers.size();i++){
            newGames.add(app.dbManager.getGamesConditional("gameId = "+gamePlayers.get(i).getGameId()).get(0));
        }
        gameCount = newGames.size();


        gameItemFragment = GameItemFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.gameListFragment, gameItemFragment).commit(); // add it to the current activity
        if(gameCount > 0){
            noGamesTextView.setVisibility(View.INVISIBLE);
        }
        else{
            noGamesTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_games, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logOutMenuItem) {
            logout(this);
            return true;
        }
        if (id == R.id.createGameMenuItem) {
            Context thisActivity = MyGamesActivity.this;
            Intent i = new Intent(thisActivity,CreateGameActivity.class);
            thisActivity.startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        logout(this);

    }


    @Override
    public void onClick(View v) {

    }
}

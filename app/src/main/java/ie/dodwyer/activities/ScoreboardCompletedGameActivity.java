package ie.dodwyer.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.fragments.GameScoreboardFragment;
import ie.dodwyer.model.Game;

public class ScoreboardCompletedGameActivity extends Base {
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard_completed_game);
        activityInfo = getIntent().getExtras();
        if(activityInfo.get(app.CALLER_ACTIVITY).equals(MyGamesActivity.class.getSimpleName())){
            List<Game> games = app.dbManager.getGamesConditional("gameId = "+activityInfo.get("gameId"));
            app.currentGame = games.get(0);
            this.setTitle(app.currentGame.getGameName());
        }
        fragmentManager = getFragmentManager();
        app.gameFragmentManager = fragmentManager;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.scoreboardCompletedFrame, new GameScoreboardFragment(),"scoreboard");
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {

            Intent i = new Intent(ScoreboardCompletedGameActivity.this,MyGamesActivity.class);
            startActivity(i);
            super.onBackPressed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scoreboard_completed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.backToMyGamesMenuItem) {
            Intent i = new Intent(ScoreboardCompletedGameActivity.this,MyGamesActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.logOutMenuItem) {
            logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

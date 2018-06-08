package ie.dodwyer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ie.dodwyer.R;
import ie.dodwyer.fragments.SearchPlayerItemFragment;

public class AddPlayerActivity extends Base {
    SearchPlayerItemFragment searchPlayerItemFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        this.setTitle(app.currentGame.getGameName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchPlayerItemFragment = SearchPlayerItemFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.addPlayerListFragment, searchPlayerItemFragment)
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.backMenuItem) {
            if(app.currentGameIsWon){goToMyGames(this);}else {
                Bundle activityInfo = new Bundle();
                activityInfo.putString(app.CALLER_ACTIVITY, this.getClass().getSimpleName());
                activityInfo.putInt("gameId", app.currentGame.getGameId());
                activityInfo.putInt("pagePosition", 4);
                Intent goToGame = new Intent(this, GameActivity.class);
                goToGame.putExtras(activityInfo);
                startActivity(goToGame);
                return true;
            }
        }
        if (id == R.id.logOutMenuItem) {
            logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {   if(app.currentGameIsWon){goToMyGames(this);}else {
        Bundle activityInfo = new Bundle();
        activityInfo.putString(app.CALLER_ACTIVITY, this.getClass().getSimpleName());
        activityInfo.putInt("gameId", app.currentGame.getGameId());
        activityInfo.putInt("pagePosition", 4);
        Intent goToGame = new Intent(this, GameActivity.class);
        goToGame.putExtras(activityInfo);
        startActivity(goToGame);
        super.onBackPressed();
    }
    }

}

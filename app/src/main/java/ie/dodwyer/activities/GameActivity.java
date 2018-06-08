package ie.dodwyer.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.fragments.GameAcceptedChallengesFragment;
import ie.dodwyer.fragments.GameDeclinedChallengesFragment;
import ie.dodwyer.fragments.GameInboxFragment;
import ie.dodwyer.fragments.GameMyChallengesFragment;
import ie.dodwyer.fragments.GameOutboxFragment;
import ie.dodwyer.fragments.GamePushChallengeFragment;
import ie.dodwyer.fragments.GameScoreboardFragment;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Game;

public class GameActivity extends Base
        implements NavigationView.OnNavigationItemSelectedListener {

    int pagePosition;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    int challengeCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabCreateChallenge = (FloatingActionButton) findViewById(R.id.fabCreateChallenge);
        fabCreateChallenge.setOnClickListener(new FabCreateChallengeOnClickListener());

        FloatingActionButton fabAddPLayer = (FloatingActionButton) findViewById(R.id.fabAddPLayer);
        fabAddPLayer.setOnClickListener(new FabAddPlayerOnClickListener());

        fragmentManager = getFragmentManager();
        app.gameFragmentManager = fragmentManager;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        activityInfo = getIntent().getExtras();


        if(activityInfo.get(app.CALLER_ACTIVITY).equals(MyGamesActivity.class.getSimpleName())){
            List<Game> games = app.dbManager.getGamesConditional("gameId = "+activityInfo.get("gameId"));
            app.currentGame = games.get(0);


        }

        if(activityInfo.get(app.CALLER_ACTIVITY).equals(CreateChallengeActivity.class.getSimpleName())){
            List<Game> games = app.dbManager.getGamesConditional("gameId = "+activityInfo.get("gameId"));
            app.currentGame = games.get(0);
            this.setTitle(app.currentGame.getGameName());

        }

        if(getIntent().hasExtra("fragment")){
            if(activityInfo.getString("fragment").equals("My Challenges")){
            }

        }

        this.setTitle(app.currentGame.getGameName());

        View headerView =  navigationView.getHeaderView(0);
        TextView navPlayerName = (TextView)headerView.findViewById(R.id.navDrawerPlayerNameTextView);
        TextView navPlayerEmail = (TextView)headerView.findViewById(R.id.navDrawerPlayerEmailTextView);
        navPlayerName.setText(app.currentPlayer.getfName()+" "+app.currentPlayer.getlName());
        navPlayerEmail.setText(app.currentPlayer.getEmail());
        pagePosition = (int) activityInfo.get("pagePosition");

        fragmentTransaction = fragmentManager.beginTransaction();
            switch(pagePosition) {
            case 1:
                if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else{
                fragment = new GameInboxFragment();
                fragmentTransaction.replace(R.id.gameNavigationFrame, new GameInboxFragment());
                fragmentTransaction.commit();}
                break;
            case 2:
                if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                    fragment = new GameOutboxFragment();
                    fragmentTransaction.replace(R.id.gameNavigationFrame, new GameOutboxFragment());
                    fragmentTransaction.commit();
                }
                break;
            case 3:
                if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                    fragment = new GameAcceptedChallengesFragment();
                    fragmentTransaction.replace(R.id.gameNavigationFrame, new GameAcceptedChallengesFragment());
                    fragmentTransaction.commit();
                }
                break;
            case 4:
                if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                    fragment = new GameMyChallengesFragment();
                    fragmentTransaction.replace(R.id.gameNavigationFrame, new GameMyChallengesFragment());
                    fragmentTransaction.commit();
                }
                break;
            case 5:
                if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                    fragment = new GamePushChallengeFragment();
                    Bundle fragmentArgs = new Bundle();
                    fragmentArgs.putInt("challengeId", activityInfo.getInt("challengeId"));
                    fragment.setArguments(fragmentArgs);
                    fragmentTransaction.replace(R.id.gameNavigationFrame, fragment);
                    fragmentTransaction.commit();
                }
                break;
            case 6:
                if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                    fragment = new GameScoreboardFragment();
                    fragmentTransaction.replace(R.id.gameNavigationFrame, new GameScoreboardFragment());
                    fragmentTransaction.commit();
                }
                break;
                case 7:
                    if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                        fragment = new GameDeclinedChallengesFragment();
                        fragmentTransaction.replace(R.id.gameNavigationFrame, new GameDeclinedChallengesFragment());
                        fragmentTransaction.commit();
                    }
                    break;
            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(GameActivity.this,MyGamesActivity.class);
            startActivity(i);
            super.onBackPressed();
        }

    }

    public class FabCreateChallengeOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                Intent i = new Intent(GameActivity.this, CreateChallengeActivity.class);
                startActivity(i);
            }
        }
    }

    public class FabAddPlayerOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                Intent i = new Intent(GameActivity.this, AddPlayerActivity.class);
                startActivity(i);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.createChallengeMenuItem) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                Intent i = new Intent(GameActivity.this, CreateChallengeActivity.class);
                startActivity(i);
            }
            return true;
        }

        if (id == R.id.addPlayerMenuItem) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                Intent i = new Intent(GameActivity.this, AddPlayerActivity.class);
                startActivity(i);
            }
            return true;
        }

        if (id == R.id.backToMyGamesMenuItem) {
                Intent i = new Intent(GameActivity.this, MyGamesActivity.class);
                startActivity(i);
                finish();

            return true;
        }

        if (id == R.id.logOutMenuItem) {
            logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_game_inbox) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GameInboxFragment(), "inbox").commit();
            }
        }
        if (id == R.id.nav_game_outbox) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GameOutboxFragment(), "outbox").commit();
            }
        }

        if (id == R.id.nav_game_accepted_challenges) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GameAcceptedChallengesFragment(), "acceptedChallenges").commit();
            }
        }
        if (id == R.id.nav_game_declined_challenges) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GameDeclinedChallengesFragment(), "acceptedChallenges").commit();
            }
        }
        if (id == R.id.nav_game_my_challenges) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GameMyChallengesFragment(), "myChallenges").commit();
            }
            }

        if (id == R.id.nav_game_push_challenge) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else {
                List challenges = app.dbManager.getChallengesConditional("createdBy = '" + app.currentPlayer.getPlayerId() + "' AND nomineeId IS NULL");
                challengeCount = challenges.size();
                if (challengeCount > 0) {
                    fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GamePushChallengeFragment(), "pushChallenges").commit();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("There are currently no challenges available to push.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", null).show();
                }
            }
        }

        if (id == R.id.nav_game_scoreboard) {
            if(app.currentGameIsWon){goToMyGames(GameActivity.this);}else{
            fragmentManager.beginTransaction().replace(R.id.gameNavigationFrame, new GameScoreboardFragment(),"scoreboard").commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

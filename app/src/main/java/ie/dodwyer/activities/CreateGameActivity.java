package ie.dodwyer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.fragments.PlayerItemFragment;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;
import ie.dodwyer.model.Player;

public class CreateGameActivity extends Base implements View.OnClickListener{

    private EditText gameTitleEditText;
    private EditText targetScoreEditText;
    private Button createGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        app.selectedPlayers = new ArrayList<Player>();
        gameTitleEditText = (EditText) findViewById(R.id.gameTitleEditText);
        targetScoreEditText = (EditText) findViewById(R.id.targetScoreEditText);
        createGameButton = (Button) findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerItemFragment = PlayerItemFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.playerListFragment, playerItemFragment).commit();

    }

    @Override
    public void onClick(View v) {
        createGameUponValidation();
    }

    private void createGameUponValidation() {

        try {
            int result;
            String gameTitle = gameTitleEditText.getText().toString();
            gameTitle = gameTitle.trim();
            String targetScore = targetScoreEditText.getText().toString();


            if (!(gameTitle.length() > 0)) {
                gameTitleEditText.requestFocus();
                gameTitleEditText.setError("Empty field.");
                throw new Exception();
            }

            if (!(targetScore.length() > 0)) {
                targetScoreEditText.requestFocus();
                targetScoreEditText.setError("Empty field.");
                throw new Exception();
            }


            int targetScoreInt = Integer.parseInt(targetScore);
            if(targetScoreInt <5 || targetScoreInt > 999){
                targetScoreEditText.requestFocus();
                targetScoreEditText.setError("The game's target score must be between 5 and 999.");
                throw new Exception();
            }

            if(!(app.selectedPlayers.size() > 0)){
                throw new Exception("Select at least one player.");
            }
            Boolean currentPlayerIsSelected = false;
            for(Player p:app.selectedPlayers){
                if(p.getPlayerId().equals(app.currentPlayer.getPlayerId())){
                    currentPlayerIsSelected = true;
                }
            }

            if(!currentPlayerIsSelected){
                throw new Exception("You must include yourself in the game.");
            }

            List<Game> g = (List<Game>) app.dbManager.getGamesConditional("gameName = '" + gameTitle + "'");
            if (!(g.size() == 0)) {
                gameTitleEditText.requestFocus();
                gameTitleEditText.setError("A game with this title field already exists.");
                throw new Exception();
            }

            Game game = new Game(gameTitle,app.getDate(),app.currentPlayer.getPlayerId(),app.GAME_STATUS_IN_PROGRESS,targetScoreInt);



            result = (int) app.dbManager.createGame(game);
            if(!(result > 0)){
                throw new Exception("ERROR: Your game could not be created at this time.");
            }
            Toast.makeText(this,"Your game has been created", Toast.LENGTH_LONG).show();

            addToGamePlayersDbTable(result);
            Intent i = new Intent(this,MyGamesActivity.class);
            startActivity(i);

        } catch (Exception e) {
            if(!(e.getMessage()== null)) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

}

    private Boolean addToGamePlayersDbTable(int gameId){
        int result = 0;
        String playerId = null;
        GamePlayers gamePlayers;
        Log.v("DebugSelectedPlayers","app.selectedPlayers(at time of addToGamePlayersDbTable call): "+app.selectedPlayers);
        try {
            if (app.selectedPlayers.size() == 0) {
                return false;
            }
            for (Player player : app.selectedPlayers) {
                playerId = player.getPlayerId();

                Log.v("DebugSelectedPlayers","Player.toString: "+player);
                gamePlayers = new GamePlayers(gameId, playerId,0, 0);
                result = (int) app.dbManager.createGamePlayers(gamePlayers);
                if (result <= 0) {
                    throw new Exception("The selected player/players could not be added at this time." + result);
                }
            }

            return true;
        }
            catch(Exception e){
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.backToMyGamesMenuItem) {
            Intent i = new Intent(CreateGameActivity.this,MyGamesActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.logOutMenuItem) {
            logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(CreateGameActivity.this,MyGamesActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}

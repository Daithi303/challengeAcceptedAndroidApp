package ie.dodwyer.activities;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Player;

public class CreateChallengeActivity extends Base implements View.OnClickListener{
    private EditText createSmsMessageSubjectEditText;
    private EditText createSmsMessageMessagetEditText;
    private Spinner spinner;
    private Button createChallenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.setMaxScoreValueSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.string_array_max_score_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        createSmsMessageSubjectEditText = (EditText) findViewById(R.id.createSmsMessageSubjectEditText);
        createSmsMessageMessagetEditText = (EditText) findViewById(R.id.createSmsMessageMessagetEditText);
        createSmsMessageMessagetEditText.setHorizontallyScrolling(false);
        createSmsMessageMessagetEditText.setLines(Integer.MAX_VALUE);
        createChallenge = (Button) findViewById(R.id.createChallengeButton);
        createChallenge.setOnClickListener(this);
        this.setTitle(app.currentGame.getGameName());
    }

    @Override
    public void onClick(View v) {
        if(app.currentGameIsWon){goToMyGames(this);}else {
            createChallengeUponValidation();
        }
    }

    private void createChallengeUponValidation() {
        try {
            int result;
            String subject = createSmsMessageSubjectEditText.getText().toString();
            String message = createSmsMessageMessagetEditText.getText().toString();
            Double maxPointValue = Double.valueOf(spinner.getSelectedItem().toString());
            subject = subject.trim();
            message = message.trim();

            if (!(subject.length() > 0))
            {
                createSmsMessageSubjectEditText.requestFocus();
                createSmsMessageSubjectEditText.setError("Empty field");
                throw new Exception();
            }

            if (!(message.length() > 0))
            {
                createSmsMessageMessagetEditText.requestFocus();
                createSmsMessageMessagetEditText.setError("Empty field");
                throw new Exception();
            }
            List<Challenge> l = (List<Challenge>) app.dbManager.getChallengesConditional("subject = \"" + subject + "\"");
            if (!(l.size() == 0)) {
                createSmsMessageSubjectEditText.requestFocus();
                createSmsMessageSubjectEditText.setError("A challenge with this subject field already exists.");
                throw new Exception();
            }

            Challenge c = new Challenge(app.currentPlayer.getPlayerId(), subject, message, maxPointValue, app.getDate());


            result = (int) app.dbManager.createChallenge(c);

            if (result <= 0) {
                throw new Exception("Challenge hasn't been created. Value of result: " + result);

            } else {
                Bundle activityInfo = new Bundle();
                activityInfo.putString(app.CALLER_ACTIVITY, this.getClass().getSimpleName());
                activityInfo.putInt("gameId", app.currentGame.getGameId());
                activityInfo.putInt("pagePosition",4);
                Intent goToGame = new Intent(this, GameActivity.class);
                goToGame.putExtras(activityInfo);
                startActivity(goToGame);
            }


        } catch (Exception e) {
            if(!(e.getMessage()== null)) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_challenge, menu);
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

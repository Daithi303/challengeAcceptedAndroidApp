package ie.dodwyer.activities;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Challenge;

/**
 * Created by User on 2/26/2017.
 */

public class EditChallengeActivity extends Base implements View.OnClickListener{
    private EditText editSmsMessageSubjectEditText;
    private EditText editSmsMessageMessagetEditText;
    private Spinner spinner;
    private Button editChallenge;
    ArrayList<Challenge> challengeList;
    Challenge challenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_challenge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.editSetMaxScoreValueSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.string_array_max_score_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        editSmsMessageSubjectEditText = (EditText) findViewById(R.id.editSmsMessageSubjectEditText);
        editSmsMessageMessagetEditText = (EditText) findViewById(R.id.editSmsMessageMessagetEditText);
        editSmsMessageMessagetEditText.setHorizontallyScrolling(false);
        editSmsMessageMessagetEditText.setLines(Integer.MAX_VALUE);
        editChallenge = (Button) findViewById(R.id.editChallengeButton);
        editChallenge.setOnClickListener(this);
        this.setTitle(app.currentGame.getGameName());
        if(getIntent().hasExtra("challengeId")){
            Bundle activityInfo = getIntent().getExtras();
            challengeList = new ArrayList<Challenge>();
            challenge = new Challenge();
            challengeList = (ArrayList<Challenge>) app.dbManager.getChallengesConditional("challengeId = "+activityInfo.getInt("challengeId"));
            challenge = challengeList.get(0);
            editSmsMessageSubjectEditText.setText(challenge.getSubject());
            editSmsMessageMessagetEditText.setText(challenge.getMessage());
            String maxPointValueStr = String.valueOf((int)challenge.getMaxPointValue());
            String maxPointValueFromArrayStr;
            String[] maxScoreArray = getResources().getStringArray(R.array.string_array_max_score_values);
            for(int i = 0;i<maxScoreArray.length;i++){
                maxPointValueFromArrayStr = maxScoreArray[i];
                if(maxPointValueFromArrayStr.equals(maxPointValueStr)){;
                    spinner.setSelection(i);
                }
            }

        }
    }

    @Override
    public void onClick(View v) {

        if(app.currentGameIsWon){goToMyGames(this);}else {
            editChallengeUponValidation();
        }
    }

    private void editChallengeUponValidation() {
        try {
            int result;
            int challengeId = challenge.getChallengeId();
            String subject = editSmsMessageSubjectEditText.getText().toString();
            String message = editSmsMessageMessagetEditText.getText().toString();
            Double maxPointValue = Double.valueOf(spinner.getSelectedItem().toString());
            subject = subject.trim();
            message = message.trim();

            if (!(subject.length() > 0))
            {
                editSmsMessageSubjectEditText.requestFocus();
                editSmsMessageSubjectEditText.setError("Empty field");
                throw new Exception();
            }

            if (!(message.length() > 0))
            {
                editSmsMessageMessagetEditText.requestFocus();
                editSmsMessageMessagetEditText.setError("Empty field");
                throw new Exception();
            }
            String whereClause = "subject = \"" + subject + "\" AND challengeId != "+challengeId;
            List<Challenge> l = (List<Challenge>) app.dbManager.getChallengesConditional(whereClause);
            if (!(l.size() == 0)) {
                editSmsMessageSubjectEditText.requestFocus();
                editSmsMessageSubjectEditText.setError("Another challenge with this subject field already exists.");
                throw new Exception();
            }

            ContentValues cv = new ContentValues();
            cv.put("subject",subject);
            cv.put("message",message);
            cv.put("maxPointValue",maxPointValue);
            result = (int) app.dbManager.updateChallenge(cv,challengeId);
            if (result <= 0) {
                throw new Exception("Challenge hasn't been edited. Value of result: " + result);

            } else {
                Bundle activityInfo = new Bundle();
                activityInfo.putString(app.CALLER_ACTIVITY,this.getClass().getSimpleName());
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
        getMenuInflater().inflate(R.menu.edit_challenge, menu);
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

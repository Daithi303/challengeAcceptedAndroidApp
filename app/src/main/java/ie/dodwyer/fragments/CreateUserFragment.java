package ie.dodwyer.fragments;

/**
 * Created by User on 2/12/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.GameActivity;
import ie.dodwyer.activities.LoginCreateUserActivity;
import ie.dodwyer.activities.MyGamesActivity;
import ie.dodwyer.main.ChallengeAcceptedApp;
import ie.dodwyer.model.Player;

public class CreateUserFragment extends Fragment implements View.OnClickListener{
    public LoginCreateUserActivity loginCreateUserActivity;
    //FirebaseAuth auth;
    int result;
    DatabaseReference mDatabase;
    public String playerId;
    private EditText emailEditTextCreateUser;
    private EditText firstNameEditTextCreateUser;
    private EditText surnameEditTextCreateUser;
    private EditText passwordEditTextCreateUser;
    private EditText confirmPasswordEditTextCreateUser;
    private Button createUserButton;
    private ProgressDialog createUserDialog;
    //public ProgressBar createUserProgressBar;
    public String fName, surname, email, pass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.loginCreateUserActivity = (LoginCreateUserActivity) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_create_user, container, false);
        emailEditTextCreateUser = (EditText) rootView.findViewById(R.id.emailEditTextCreateUser);
        firstNameEditTextCreateUser = (EditText) rootView.findViewById(R.id.firstNameEditTextCreateUser);
        surnameEditTextCreateUser = (EditText) rootView.findViewById(R.id.surnameEditTextCreateUser);
        passwordEditTextCreateUser = (EditText) rootView.findViewById(R.id.passwordEditTextCreateUser);
        confirmPasswordEditTextCreateUser = (EditText) rootView.findViewById(R.id.confirmPasswordEditTextCreateUser);
        createUserButton = (Button) rootView.findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        //createUserProgressBar.setVisibility(View.VISIBLE);
        createPlayerUponValidation();

    }

    private void createPlayerUponValidation() {
        try {

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            email = emailEditTextCreateUser.getText().toString();
                email = email.trim().toLowerCase();
            pass = passwordEditTextCreateUser.getText().toString().trim();
            String confirmPass = confirmPasswordEditTextCreateUser.getText().toString().trim();
            fName = firstNameEditTextCreateUser.getText().toString().trim();
            surname = surnameEditTextCreateUser.getText().toString().trim();
            if (!(email.length() > 0) ||
                    !((firstNameEditTextCreateUser.getText().toString()).length() > 0) ||
                    !((surnameEditTextCreateUser.getText().toString()).length() > 0) ||
                    !(pass.length() > 0) ||
                    !(confirmPass.length() > 0)
                    ) {
                throw new Exception("There is an empty field remaining.");
            }
            if (!(email.matches(emailPattern))) {
                throw new Exception("Invalid email address");
            }
            if (!(pass.equals(confirmPass))) {
                throw new Exception("Password field is not the same as Confirm Password field.");
            }

            List<Player> l = (List<Player>) loginCreateUserActivity.app.dbManager.getPlayersConditional("email = \"" + email + "\"");
            if (!(l.size() == 0)) {
                throw new Exception("A player with that email already exists within local sqlite database.");
            }
            createUserDialog = new ProgressDialog(loginCreateUserActivity);
            createUserDialog.setMessage("Creating player, please wait...");
            createUserDialog.setCancelable(false);
            createUserDialog.show();
            Task x = loginCreateUserActivity.auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(loginCreateUserActivity, new MyAuthOnCompleteListener());
        } catch (Exception e) {

            if(!(e.getMessage()== null)) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        passwordEditTextCreateUser.setText("");
        confirmPasswordEditTextCreateUser.setText("");
    }


/*public class MySetPlayerOnCompleteListener implements OnCompleteListener{

    @Override
    public void onComplete(@NonNull Task task) {
        if(task.isSuccessful()){
            Player p = new Player();
            p.setEmail(loginCreateUserActivity.app.currentUser.getEmail());
            p.setfName(fName);
            p.setlName(surname);
            p.setPlayerId(loginCreateUserActivity.app.currentUser.getUid());
            loginCreateUserActivity.app.currentPlayer = p;
*//*            Intent i = new Intent(loginCreateUserActivity,GameActivity.class);
            startActivity(i);*//*
            Toast.makeText(loginCreateUserActivity,"Your player has been created.",Toast.LENGTH_LONG).show();

        }
        else{
            loginCreateUserActivity.app.currentUser.delete().addOnCompleteListener(new MyDeletePlayerOnCompleteListener());
        }

    }

}

    public class MyDeletePlayerOnCompleteListener extends AppCompatActivity implements OnCompleteListener{
        @Override
        public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {

                Toast.makeText(loginCreateUserActivity,"An Error has occured in creating your player.",Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(loginCreateUserActivity,"An Error has occured in creating your player. Please contact our support team for details.",Toast.LENGTH_LONG).show();
            }

        }
    }*/

public class MyAuthOnCompleteListener extends AppCompatActivity implements OnCompleteListener{
    @Override
    public void onComplete(@NonNull Task task) {
        if (createUserDialog.isShowing()) {
            createUserDialog.dismiss();
        }

        if (!task.isSuccessful()) {
            if (task.getException() instanceof FirebaseException) {
                if(task.getException().getMessage().contains("WEAK_PASSWORD")){
                    passwordEditTextCreateUser.requestFocus();
                    passwordEditTextCreateUser.setError("Your password is too weak. Please select another.");

                }

            }

            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                emailEditTextCreateUser.requestFocus();
                emailEditTextCreateUser.setError("A player with that email already exists within firebase.");
            }
            if(task.getException() instanceof FirebaseNetworkException){
                Toast.makeText(loginCreateUserActivity,"There has been a network error. Please try again later.", Toast.LENGTH_LONG).show();

            }

        } else {
            playerId = loginCreateUserActivity.auth.getCurrentUser().getUid();
            Player p = new Player(playerId,email,fName, surname);
            List<Player> l = (List<Player>) loginCreateUserActivity.app.dbManager.getPlayersConditional("playerId = '" + playerId + "'");
            if (!(l.size() == 0)) {
                loginCreateUserActivity.app.dbManager.deletePlayersConditional("playerId = '" + playerId + "'");
            }
            result = (int) loginCreateUserActivity.app.dbManager.createPlayer(p);

            if (result <= 0) {
                //createUserProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(loginCreateUserActivity,"Firebase authentication registration has been successful but your player hasn't been added to the local database. Please conttact our support team.", Toast.LENGTH_LONG).show();
            } else {
                l = (List<Player>) loginCreateUserActivity.app.dbManager.getPlayersConditional("email = '" + email + "'");
                if (!(l.size() == 0)) {

                    Intent i = new Intent(loginCreateUserActivity, MyGamesActivity.class);
                 loginCreateUserActivity.app.currentPlayer = l.get(0);
                    loginCreateUserActivity.startActivity (i);
                    loginCreateUserActivity.finish();
                    Toast.makeText(loginCreateUserActivity, "Your player has been created.", Toast.LENGTH_SHORT).show();
                }

            }
            /*
            Toast.makeText(loginCreateUserActivity, "The Firebase authentication process succeeded.",Toast.LENGTH_SHORT).show();
            loginCreateUserActivity.app.currentUser = auth.getCurrentUser();
            playerId = loginCreateUserActivity.app.currentUser.getUid();
            Player p = new Player(fName,surname);
            mDatabase.child(playerId).setValue(p).addOnCompleteListener(new MySetPlayerOnCompleteListener());
*/
        }
    }
}

}
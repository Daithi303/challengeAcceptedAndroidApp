package ie.dodwyer.fragments;

/**
 * Created by User on 2/12/2017.
 */

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
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.LoginCreateUserActivity;
import ie.dodwyer.activities.MyGamesActivity;
import ie.dodwyer.model.Player;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private LoginCreateUserActivity loginCreateUserActivity;
    EditText emailEditTextLogin;
    EditText passwordEditTextLogin;
    private Button loginButton;
    //public ProgressBar loginProgressBar;
    private ProgressDialog loggingInDialog;
    String email;
    String pass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_login, container, false);
        emailEditTextLogin = (EditText) rootView.findViewById(R.id.emailEditTextLogin);
        passwordEditTextLogin = (EditText) rootView.findViewById(R.id.passwordEditTextLogin);
        emailEditTextLogin.setText("");
        passwordEditTextLogin.setText("");
        //loginProgressBar = (ProgressBar)rootView.findViewById(R.id.loginProgressBar);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        //loginProgressBar.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (loginCreateUserActivity.auth.getCurrentUser() != null) {
            String email = loginCreateUserActivity.auth.getCurrentUser().getEmail();
            List<Player> l = (List<Player>)loginCreateUserActivity.app.dbManager.getPlayersConditional("email = \"" + email + "\"");
            if(l.size()==1) {
                loginCreateUserActivity.app.currentPlayer = l.get(0);
                startActivity(new Intent(loginCreateUserActivity, MyGamesActivity.class));
                loginCreateUserActivity.finish();
            }
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.loginCreateUserActivity = (LoginCreateUserActivity) context;
    }

    @Override
    public void onClick(View v) {
        //loginProgressBar.setVisibility(View.VISIBLE);
        loginPlayerUponValidation();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loginPlayerUponValidation(){
        try{
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        email = emailEditTextLogin.getText().toString();
            email = email.trim().toLowerCase();
        pass = passwordEditTextLogin.getText().toString();
        if(!(email.length() > 0)) {
            emailEditTextLogin.requestFocus();
            emailEditTextLogin.setError("Empty field");
            throw new Exception();
        }
            if(!(pass.length() > 0)){
                passwordEditTextLogin.requestFocus();
                passwordEditTextLogin.setError("Empty field");
                throw new Exception();
            }
        if(!(email.matches(emailPattern)))
        {
            emailEditTextLogin.requestFocus();
            emailEditTextLogin.setError("Invalid email address");
            throw new Exception();
        }
        List<Player> l = (List<Player>)loginCreateUserActivity.app.dbManager.getPlayersConditional("email = \"" + email + "\"");
        if(!(l.size()==1)){
            emailEditTextLogin.requestFocus();
            emailEditTextLogin.setError("A player with that email does not exist in the local sqlite database.");
            throw new Exception();
        }
            loggingInDialog = new ProgressDialog(loginCreateUserActivity);
            loggingInDialog.setMessage("Logging in, please wait...");
            loggingInDialog.setCancelable(false);
            loggingInDialog.show();
            loginCreateUserActivity.auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new MyAuthOnCompleteListener());
    }catch(Exception e){
            //loginProgressBar.setVisibility(View.INVISIBLE);
            if(!(e.getMessage()== null)) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public class MyAuthOnCompleteListener extends AppCompatActivity implements OnCompleteListener {
        Context context;


        @Override
        public void onComplete(@NonNull Task task) {
            if (loggingInDialog.isShowing()) {
                loggingInDialog.dismiss();
            }
            if(!task.isSuccessful()) {
                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                    emailEditTextLogin.setText("");
                    passwordEditTextLogin.setText("");
                    Toast.makeText(loginCreateUserActivity,"Your firebase credentials are invalid. Plaese re-enter.", Toast.LENGTH_LONG).show();
                }

                if(task.getException() instanceof FirebaseNetworkException){
                    Toast.makeText(loginCreateUserActivity,"There has been a network error. Please try again later.", Toast.LENGTH_LONG).show();
                }

            }else {
                List<Player> l = (List<Player>)loginCreateUserActivity.app.dbManager.getPlayersConditional("email = \"" + email + "\"");
                Intent i = new Intent(loginCreateUserActivity, MyGamesActivity.class);
                loginCreateUserActivity.app.currentPlayer = l.get(0);
                loginCreateUserActivity.startActivity (i);
                loginCreateUserActivity.finish();
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



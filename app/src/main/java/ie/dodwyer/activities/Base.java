package ie.dodwyer.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.dodwyer.fragments.GameItemFragment;
import ie.dodwyer.fragments.PlayerItemFragment;
import ie.dodwyer.main.ChallengeAcceptedApp;
import ie.dodwyer.model.Challenge;

/**
 * Created by User on 2/24/2017.
 */

public class Base extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 2;
    public ChallengeAcceptedApp app;
    public Activity thisActivity;
    public FirebaseAuth auth;
    public ProgressDialog loggingOutdDialog;
    public Challenge attemptedChallenge;
    public PlayerItemFragment playerItemFragment;
    public GameItemFragment gameItemFragment;
    protected Bundle activityInfo;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (ChallengeAcceptedApp) getApplication();
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(this);
    }



   /* private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        OnCompleteListener listener;
        String email;
        String pass;
        String message;

        public BackgroundTask(Context context,String email,String pass,String message,OnCompleteListener listener) {
            dialog = new ProgressDialog(context);
            this.listener = listener;
            this.email = email;
            this.pass = pass;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(message);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
*/

    public void goToMyGames(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("This game has been won. You will now be re-directed to the 'My Games' page.");
        builder.setNeutralButton("Close", new MyOnClickListener());
        Dialog dialog = builder.create();
        /** this required special permission but u can use aplication context */
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        /** show dialog */
        dialog.show();

    }
    public class MyOnClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent i = new Intent(Base.this, MyGamesActivity.class);
            startActivity(i);
            app.currentGameIsWon = false;

        }
    }


    public void logout(Activity activity){
        thisActivity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                loggingOutdDialog = new ProgressDialog(thisActivity);
                loggingOutdDialog.setMessage("Logging out, please wait...");
                loggingOutdDialog.show();
                auth.signOut();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void resetDatabase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to reset the local database? NOTE: Firebase authentication users will also need to be deleted.");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                if(app.resetDatabase()){
                    Toast.makeText(Base.this,"All games, players and challenges have been removed from the database.",Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }


    public void getPermissionToSendSMS(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},
                        SEND_SMS_PERMISSIONS_REQUEST);
            }
        }

    }

    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }else {

                // Fire off an async request to actually get the permission
                // This will show the standard permission request dialog UI
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_PERMISSIONS_REQUEST);


            }
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == SEND_SMS_PERMISSIONS_REQUEST){
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Send SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Send SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        if(auth.getCurrentUser()==null){
            if(loggingOutdDialog !=null && loggingOutdDialog.isShowing()){
                loggingOutdDialog.dismiss();
                Intent i = new Intent(thisActivity,LoginCreateUserActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                thisActivity.startActivity(i);
            }
        }
    }
}

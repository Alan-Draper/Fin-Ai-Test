package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //placeholder for sign in
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public void showDatabaseTest() {
        Intent intent = new Intent(getApplicationContext(), Database_test.class);
        startActivity(intent);
    }




    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (auth.getCurrentUser() != null) {
            // already signed in
        } else {
            // not signed in
        }
        // Choose an arbitrary request code value
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo).build(),RC_SIGN_IN);


    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mRootRef.child("users").child(userId).setValue(user);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                this.writeNewUser(auth.getUid(), auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getEmail());
                showDatabaseTest();
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(MainActivity.this, R.string.sign_in_cancelled,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(MainActivity.this, R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this,R.string.unknown_error,Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
}
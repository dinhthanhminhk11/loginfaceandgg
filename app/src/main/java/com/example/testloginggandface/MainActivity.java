package com.example.testloginggandface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sigin;
    private Button siginOut;
    private Button siginFace;
    private static final String EMAIL = "email";
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextView status;
    private Button face;
    private TextView statusFace;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();


        sigin = (Button) findViewById(R.id.sigin);
        siginOut = (Button) findViewById(R.id.siginOut);
        siginFace = (Button) findViewById(R.id.siginFace);

        status = (TextView) findViewById(R.id.status);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        face = (Button) findViewById(R.id.face);
        statusFace = (TextView) findViewById(R.id.statusFace);


        sigin.setOnClickListener(this);
        siginOut.setOnClickListener(this);
        siginFace.setOnClickListener(this);
        face.setOnClickListener(this);


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                statusFace.setText("Login thanfh coong");
                Toast.makeText(MainActivity.this, loginResult.getAccessToken() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                statusFace.setText("Login thatas baij");
                Log.e("Minh_Log", exception.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            status.setText(" aa " + account.getDisplayName());

            sigin.setVisibility(View.VISIBLE);
            siginFace.setVisibility(View.GONE);
        } else {
            status.setText("R.string.signed_out");

            sigin.setVisibility(View.VISIBLE);
            siginFace.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sigin:
                signIn();
                break;
            case R.id.siginOut:
                signOut();
                break;
            case R.id.siginFace:
                revokeAccess();
                break;
            case R.id.face:
                loginFaceBook();
                break;
        }
    }

    private void loginFaceBook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

}
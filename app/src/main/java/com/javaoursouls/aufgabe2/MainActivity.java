package com.javaoursouls.aufgabe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText
        ETemail,
        ETpassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.login_tv_createaccount);
        register.setOnClickListener(this);

        this.ETemail = (EditText) findViewById(R.id.login_input_email);
        this.ETpassword = (EditText) findViewById(R.id.login_input_password);
        this.loginButton = (Button) findViewById(R.id.login_btn_login);
        this.progressBar = (ProgressBar) findViewById(R.id.login_progressbar);
        signInButton = findViewById(R.id.sign_in_button);

        loginButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //account = GoogleSignIn.getLastSignedInAccount(this);
        // update ui
    }

    private void login(){
        final String email = this.ETemail.getText().toString().trim();
        final String password = this.ETpassword.getText().toString().trim();

        if(email.isEmpty()){
            this.ETemail.setError("Email is required");
            this.ETemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.ETemail.setError("Invalid E-Mail");
            this.ETemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            this.ETpassword.setError("password is required");
            this.ETpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // redirect to home page
                    startActivity(new Intent(MainActivity.this, HomePage.class));
                }else {
                    Toast.makeText(MainActivity.this, "email or password incorrect", Toast.LENGTH_LONG);
                }
            }
        });

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("my", account.toString()); 
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(MainActivity.this, HomePage.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_tv_createaccount :
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.login_btn_login :
                login();
                break;
            case R.id.sign_in_button :
                signIn();
        }
    }
}
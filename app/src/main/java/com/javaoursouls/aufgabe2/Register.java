package com.javaoursouls.aufgabe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText
        ETfirstName,
        ETlastName,
        ETemail,
        ETaccountID,
        ETpassword;
    private ProgressBar progressBar;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        this.mAuth = FirebaseAuth.getInstance();

        // initialize widgets
        this.ETfirstName = (EditText) findViewById(R.id.register_input_firstname);
        this.ETlastName = (EditText) findViewById(R.id.register_input_lastname);
        this.ETemail = (EditText) findViewById(R.id.register_input_email);
        this.ETaccountID = (EditText) findViewById(R.id.register_input_accountid);
        this.ETpassword = (EditText) findViewById(R.id.register_input_password);

        this.progressBar = (ProgressBar) findViewById(R.id.register_progressbar);
        this.registerButton = (Button) findViewById(R.id.register_btn_register);

        this.registerButton.setOnClickListener(this);

    }

    private void register(){

        // get user input
        final String firstName = this.ETfirstName.getText().toString().trim();
        final String lastName = this.ETlastName.getText().toString().trim();
        final String email = this.ETemail.getText().toString().trim();
        final String accountID = this.ETaccountID.getText().toString().trim();
        final String password = this.ETpassword.getText().toString().trim();

        // validate user input
        if(firstName.isEmpty()){
            this.ETfirstName.setError("First Name is required");
            this.ETfirstName.requestFocus();
            return;
        }


        if(lastName.isEmpty()){
            this.ETlastName.setError("Last Name is required");
            this.ETlastName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            this.ETemail.setError("E-Mail is required");
            this.ETemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.ETemail.setError("E-Mail is invalid");
            this.ETemail.requestFocus();
            return;
        }

        if(accountID.isEmpty()){
            this.ETaccountID.setError("Account ID is required");
            this.ETaccountID.requestFocus();
            return;
        }

        if(password.isEmpty()){
            this.ETpassword.setError("Password is required");
            this.ETpassword.requestFocus();
            return;
        }

        if(password.length() < 8 ){
            this.ETpassword.setError("Password must be longer than 8 characters");
            this.ETpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(firstName, lastName, email, accountID, password);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    // redirect to Login
                                                    startActivity(new Intent(Register.this, MainActivity.class));
                                                }else {
                                                    Toast.makeText(Register.this, "faild to register. Try again", Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.register_btn_register:
                register();
                break;
        }
    }
}
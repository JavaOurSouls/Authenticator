package com.javaoursouls.aufgabe2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        logoutButton = (Button) findViewById(R.id.homepage_btn_logout);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homepage_btn_logout :
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePage.this, MainActivity.class));
        }
    }
}
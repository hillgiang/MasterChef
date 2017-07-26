package com.project.cyim.masterchef;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void member(View view){
        session = new SessionManagement(getApplicationContext());
        if (session.isLoggedIn())
            startActivity(new Intent(this, UsersInfo.class));
        else
            startActivity(new Intent(this, Login.class));
    }

    public void recipes(View view){
        startActivity(new Intent(this, GetAllRecipes.class));
    }
}

package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hillary on 8/9/2017.
 */

public class Begin extends AppCompatActivity {
    private Handler mHandler = new Handler();
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin);
        getSupportActionBar().hide();

        mHandler.postDelayed(mLaunchTask, 3000);
    }

    private Runnable mLaunchTask = new Runnable() {
        public void run() {
            session = new SessionManagement(getApplicationContext());
            if (session.isLoggedIn()) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        }
    };
}

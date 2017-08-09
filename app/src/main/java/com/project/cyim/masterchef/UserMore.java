package com.project.cyim.masterchef;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Hillary on 8/6/2017.
 */

public class UserMore extends AppCompatActivity {
    String[] moreArray = new String[3];
    private ListView listView;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_more);
        moreArray[0] = getString(R.string.language_text);
        moreArray[1] = getString(R.string.aboutus_text);
        moreArray[2] = getString(R.string.logout_text);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManagement(getApplicationContext());

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.usermore_list, moreArray);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                if (item.equals(getString(R.string.language_text))) {

                } else if (item.equals(getString(R.string.aboutus_text))) {

                } else if (item.equals(getString(R.string.logout_text)))
                    session.logoutUser();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

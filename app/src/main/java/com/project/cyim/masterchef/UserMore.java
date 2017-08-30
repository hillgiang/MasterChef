package com.project.cyim.masterchef;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hillary on 8/6/2017.
 */

public class UserMore extends AppCompatActivity {
    ArrayList<String> moreArray = new ArrayList<String>();
    private ListView listView;
    SessionManagement session;
    AlertDialog levelDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_more);
        //moreArray[0] = getString(R.string.language_text);
        //moreArray[1] = getString(R.string.aboutus_text);
        //moreArray[2] = getString(R.string.logout_text);

        moreArray.add(getString(R.string.language_text));
        moreArray.add(getString(R.string.aboutus_text));
        moreArray.add(getString(R.string.logout_text));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.option));
        session = new SessionManagement(getApplicationContext());

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.usermore_list, moreArray);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                if (item.equals(getString(R.string.language_text)))
                    showRadioButtonDialog();
                else if (item.equals(getString(R.string.aboutus_text))) {

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

    private void showRadioButtonDialog() {

        // custom dialog
        final CharSequence[] items = {getString(R.string.chinese), getString(R.string.english)};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_language));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Configuration config = new Configuration();
                Locale locale;
                switch(item)
                {
                    case 0:
                        locale = new Locale("zh-rTW");
                        config.locale =locale;
                        break;
                    case 1:
                        config.locale = Locale.ENGLISH;
                        break;
                }
                getResources().updateConfiguration(config, null);
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }
}

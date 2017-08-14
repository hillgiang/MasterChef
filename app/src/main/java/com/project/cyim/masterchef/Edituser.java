package com.project.cyim.masterchef;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by user on 2017/8/11.
 */

public class Edituser extends Activity {
    private EditText nameField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituser);

        nameField = (EditText)findViewById(R.id.name);
    }
    public void EditUser(View view){
        String fullname = nameField.getText().toString();

       // new EditUsersInfo(this, "info").execute(fullname);
    }
}

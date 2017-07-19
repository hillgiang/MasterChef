package com.project.cyim.masterchef;

/**
 * Created by Hillary on 7/19/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends Activity{
    private EditText usernameField,passwordField, nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
        nameField = (EditText)findViewById(R.id.name);
    }

    public void register(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String fullname = nameField.getText().toString();

        new LoginActivity(this).execute(username,password,fullname);
    }

    public void toLogin(View view){
        startActivity(new Intent(this, Login.class));
    }
}

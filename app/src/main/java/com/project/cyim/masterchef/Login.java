package com.project.cyim.masterchef;

/**
 * Created by Hillary on 7/19/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity{
    private EditText usernameField,passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.email);
        passwordField = (EditText)findViewById(R.id.password);
    }

    public void loginPost(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        new LoginActivity(this).execute(username,password);
    }

    public void toRegister(View view){
        startActivity(new Intent(this, Register.class));
    }
}

package com.project.cyim.masterchef;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.HashMap;


/**
 * Created by user on 2017/8/11.
 */

public class Edituser extends Activity {
    private EditText nameField;
    private Button photo;
    private Button edit;
    SessionManagement session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituser);
        session = new SessionManagement(getApplicationContext());

        nameField = (EditText)findViewById(R.id.name);
        photo = (Button) findViewById(R.id.changephoto);
        edit = (Button) findViewById(R.id.edit);

        //button的
        //photo.setOnClickListener(new Button.OnClickListener(){
        //   public void onClick(View v){
        //      Intent intent = new Intent(getActivity(), ChangePhoto.class);
        //     startActivity(intent);
        //}
        //});

        // edit.setOnClickListener(new Button.OnClickListener(){
        //     public void onClick(View v){
        //        Intent intent = new Intent(getActivity(), Edit.class);
        //      startActivity(intent);
        // }
        //});

        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManagement.KEY_EMAIL);
        nameField.setText(email);

        new EditGetUser(this, nameField).execute(email);

    }

}

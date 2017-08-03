package com.project.cyim.masterchef;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import android.view.View;

/**
 * Created by Hillary on 7/26/2017.
 */

public class UsersInfo extends Activity {
    private TextView textview;
    private Button logout;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_info);
        session = new SessionManagement(getApplicationContext());

        textview = (TextView) findViewById(R.id.textView);
        logout = (Button) findViewById(R.id.logout);

        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManagement.KEY_EMAIL);
        textview.setText(email);
        //所以現在email變數所存的就是賬號。
        //再把賬號名稱傳給GetMemberInfo.php。
        //最後，伺服器會回傳使用者資料。
        //把那些資料在UI上顯示出來(set TextView)。


        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                session.logoutUser();
            }
        });
    }
}

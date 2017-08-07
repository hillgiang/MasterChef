package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import java.util.HashMap;
import android.content.Intent;

/**
 * Created by Hillary on 7/26/2017.
 */

public class UsersInfo extends Fragment {
    private TextView textview;
    SessionManagement session;

    public static UsersInfo newInstance() {
        UsersInfo fragment = new UsersInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.users_info, container, false);
        session = new SessionManagement(getActivity());

        textview = (TextView) v.findViewById(R.id.textView);

        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManagement.KEY_EMAIL);
        textview.setText(email);
        //所以現在email變數所存的就是賬號。
        //再把賬號名稱傳給GetMemberInfo.php。
        //最後，伺服器會回傳使用者資料。
        //把那些資料在UI上顯示出來(set TextView)。

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_more:
                startActivity(new Intent(getActivity(), UserMore.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

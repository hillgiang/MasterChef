package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by yu fen on 2017/10/14.
 */

public class UploadIntro extends Fragment {
    SessionManagement session;
    TextView username;
    EditText title, content;

    public UploadIntro() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upload_intr, container, false);
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManagement.KEY_EMAIL);
        username = (TextView)v.findViewById(R.id.username);
        title = (EditText)v.findViewById(R.id.title);
        content = (EditText)v.findViewById(R.id.content);
        username.setText(email);

        return v;
    }
}
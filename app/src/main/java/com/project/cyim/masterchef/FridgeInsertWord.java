package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2017/8/8.
 */
public class FridgeInsertWord extends Fragment {
    ArrayList<String> list = new ArrayList<String>();
    private ListView listview;
   // List <Boolean> checkedlist = new ArrayList <Boolean>();
    Button insert;
    EditText item;
    SessionManagement session;
    public FridgeInsertWord() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fridgeinsert_word, container, false);
        session = new SessionManagement(getActivity());
        item = (EditText) v.findViewById(R.id.ingredient);
        insert = (Button) v.findViewById(R.id.insert);
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);
       // new FridgeInsertWord().FridgeData(this, listview).execute("insert", email);


        return v;
    }
}

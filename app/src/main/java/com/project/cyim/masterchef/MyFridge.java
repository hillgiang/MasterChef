package com.project.cyim.masterchef;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * Created by user on 2017/8/8.
 * 我的冰箱
 */
public class MyFridge extends Fragment {
    private ListView listview;
    private Button add;
    // TextView item;
    SessionManagement session;

    public MyFridge() {
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
        View v = inflater.inflate(R.layout.my_fridge, container, false);
        //item = (TextView) v.findViewById(R.id.item);
        listview = (ListView) v.findViewById(R.id.listview);
        add = (Button) v.findViewById(R.id.add);
        session = new SessionManagement(getActivity());

        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManagement.KEY_EMAIL);
        new FridgeData(this, listview).execute(email);
        return v;
    }


    class FridgeData extends AsyncTask<String, String, String> {
        private MyFridge context;
        private ListView listview;

        public FridgeData(MyFridge context, ListView listview) {
            this.context = context;
            this.listview = listview;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String username = (String) arg0[0];
                //String password = (String) arg0[1];

                String ip = "http://140.135.113.99";
                String link = ip + "/GetFridgeDetail.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        //String item2;
        @Override
        protected void onPostExecute(String result) {
            ArrayList<String> list = new ArrayList<String>();
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
            try {
                //ArrayList<String> ingredient = new ArrayList<String>();
                JSONArray reci = new JSONArray(result);
                for (int i = 0; i < reci.length(); i++) {
                    JSONObject c = reci.getJSONObject(i);
                    String item2 = c.getString("fridge_ingredient");
                    String[] temp = null;
                    temp = item2.split(",");
                    for (int j = 0; j < temp.length; ++j) {
                        list.add(temp[j]);
                    }
                    //ingredient.add(item);
                }

            } catch (final JSONException e) {
            }
            // item.setText(item2);
            MyFridgeAdapter adapter = new MyFridgeAdapter(getActivity(), list);
            listview.setAdapter(adapter);
        }
    }
}
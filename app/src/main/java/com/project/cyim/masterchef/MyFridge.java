package com.project.cyim.masterchef;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by user on 2017/8/8.
 * 我的冰箱
 */
public class MyFridge extends Fragment {
    TextView item;
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
        item = (TextView) v.findViewById(R.id.item);
        new FridgeData(getContext()).execute();
        return v;
    }


    class FridgeData extends AsyncTask<String, String, String> {
        private Context context;
        SessionManagement session;

        public FridgeData(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                //String username = (String) arg0[0];
                //String password = (String) arg0[1];

                String ip = "http://140.135.113.99";
                String link = ip + "/loginpost.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode("", "UTF-8");

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

        @Override
        protected void onPostExecute(String result) {
            try {
                //ArrayList<String> ingredient = new ArrayList<String>();
                JSONArray reci = new JSONArray(result);
                for (int i = 0; i < reci.length(); i++) {
                    JSONObject c = reci.getJSONObject(i);

                    String item = c.getString("fridge_ingredient");
                    //ingredient.add(item);
                }
            } catch (final JSONException e) {
            }
        }
    }
}
package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Created by Hillary on 12/13/2017.
 */

public class MyVideo extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    SessionManagement session;

    public MyVideo() {
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
        View v = inflater.inflate(R.layout.my_video, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        final String name = user.get(SessionManagement.KEY_EMAIL);
        new GetMyVideo(this).execute(name);
        return v;
    }

    public class GetMyVideo extends AsyncTask<String, String, String> {
        private MyVideo context;
        String task;

        public GetMyVideo(MyVideo context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String email = (String) arg0[0];

                String ip = "http://140.135.113.99";
                String link = ip + "/GetMyVideo.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(email, "UTF-8") ;
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
            ArrayList<HashMap<String, String>> result_list = new ArrayList<HashMap<String, String>>();
            try {
                JSONArray reci = new JSONArray(result);
                for (int i = 0; i < reci.length(); i++) {
                    HashMap<String, String> item = new HashMap<>();
                    JSONObject c = reci.getJSONObject(i);

                    String url = c.getString("url");
                    String title = c.getString("title");

                    item.put("TITLE", title);
                    item.put("URL", url);
                    item.put("DURATION", c.getString("duration"));
                    item.put("THUMBNAIL", c.getString("thumbnail"));
                    result_list.add(item);
                }

            } catch (final JSONException e) {
            }
            adapter = new VideoAdapter(getActivity(), result_list);
            recyclerView.setAdapter(adapter);

        }
    }
}

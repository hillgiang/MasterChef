package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
 */
public class Follow extends Fragment {
    private ListView listview;
    SessionManagement session;

    public Follow() {
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
        View v = inflater.inflate(R.layout.follow, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        session = new SessionManagement(getActivity());


        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);
        new FollowData(this, listview).execute("get", email);
        return v;
    }
    class FollowData extends AsyncTask<String, String, String> {
        private Follow context;
        private ListView listview;
        String task;

        public FollowData(Follow context, ListView listview) {
            this.context = context;
            this.listview = listview;
        }

        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... arg0) {
            task = (String) arg0[0];
            if (task.equals("get")) {
                try {
                    String username = (String) arg0[1];
                    //String password = (String) arg0[1];

                    String ip = "http://140.135.113.99";
                    String link = ip + "/GetFollow.php";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("task", "UTF-8") + "=" +
                            URLEncoder.encode("get", "UTF-8");

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
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
            if (task.equals("get")) {
                ArrayList<HashMap<String, String>> result_list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONArray reci = new JSONArray(result);
                    for (int i = 0; i < reci.length(); i++) {
                        HashMap<String, String> item = new HashMap<>();
                        JSONObject c = reci.getJSONObject(i);

                        String follow = c.getString("username");
                        String thumbnail = c.getString("avatar");
                        String id = c.getString("user_id");

                        item.put("USER", follow);
                        item.put("FULLNAME", c.getString("fullname"));
                        item.put("THUMBNAIL", thumbnail);
                        item.put("ID", id);
                        result_list.add(item);
                    }

                } catch (final JSONException e) {
                }
                // item.setText(item2);
                UserAdapter adapter;
                adapter = new UserAdapter(getActivity(), result_list);
                listview.setAdapter(adapter);
            }

        }
    }
}

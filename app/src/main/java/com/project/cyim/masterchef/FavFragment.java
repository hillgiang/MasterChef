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
 * Created by Hillary on 8/4/2017.
 */

public class FavFragment extends Fragment{
    //list
    private ListView listview;
    SessionManagement session;


    public static FavFragment newInstance() {
        FavFragment fragment = new FavFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorites, container, false);
        //list
        listview = (ListView)v.findViewById(R.id.list);
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);
        new Favdata(FavFragment.this).execute("get",email);


        return v;
    }
    class Favdata extends AsyncTask<String, String, String> {

        private FavFragment context;


        public Favdata(FavFragment context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            String task = (String) arg0[0];
            String name = (String) arg0[1] ;


            if ( task.equals("get") ) {
                try {
                    String ip = "http://140.135.113.99";
                    String link = ip + "/MemberFav.php?username="+
                            URLEncoder.encode(name, "UTF-8");


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

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
            } // else if

            return null ;
        }

        protected void onPostExecute(String result) {
            ArrayList<HashMap<String, String>> result_list = new ArrayList<HashMap<String, String>>();
            try {
                JSONArray reci = new JSONArray(result);
                for (int i = 0; i < reci.length(); i++) {
                    HashMap<String, String> item = new HashMap<>();
                    JSONObject c = reci.getJSONObject(i);
                    String title = c.getString("recipes_name");
                    String author = c.getString("user_id");
                    String thumbnail = c.getString("thumbnail");

                    item.put("TITLE", title);
                    item.put("AUTHOR", author);
                    item.put("THUMBNAIL", thumbnail);

                    result_list.add(item);
                }

            } catch (final JSONException e) {
            }

            LazyAdapter adapter = new LazyAdapter(getActivity(), result_list);
            listview.setAdapter(adapter);
        }
    }
}

package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by yu fen on 2017/8/10.
 */

public class DiscuFrag extends Fragment{
    private ListView listview;
    Button insert;
    EditText item;
    SessionManagement session;
    String comment_str;
    DiscuAdapter adapter;

    public DiscuFrag() {
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
        View v = inflater.inflate(R.layout.frag_discu, container, false);
        listview = (ListView) v.findViewById(R.id.discuss_list);
        item = (EditText) v.findViewById(R.id.comment);
        insert = (Button) v.findViewById(R.id.send_comment);
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        final String userid = user.get(SessionManagement.KEY_ID);
        final int id = ((OnFragmentInteractionListener)getActivity()).recipe_id();
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_str = item.getText().toString();
                new DiscuData(DiscuFrag.this, listview).execute("add", id + "", userid, comment_str);
            }
        });

        new DiscuFrag.DiscuData(this, listview).execute("get", id + "");
        return v;
    }

    class DiscuData extends AsyncTask<String, String, String> {
        private DiscuFrag context;
        private ListView listview;
        String task;

        public DiscuData(DiscuFrag context, ListView listview) {
            this.context = context;
            this.listview = listview;
        }

        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... arg0) {
            task = (String) arg0[0];
            String link, data;

            if (task.equals("get")) {
                try {
                    String id = (String) arg0[1];

                    String ip = "http://140.135.113.99";
                    link = ip + "/RecipeComment.php?recipe="+
                            URLEncoder.encode(id, "UTF-8") ;
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(link);
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
            } else if (task.equals("add")) {
                try {
                    String id = (String) arg0[1];
                    String userid = (String) arg0[2];
                    String comment = (String) arg0[3];

                    String ip = "http://140.135.113.99";
                    link = ip + "/RecipeComment.php?recipe="+
                            URLEncoder.encode(id, "UTF-8") + "&user_id=" + URLEncoder.encode(userid, "UTF-8") + "&content=" + URLEncoder.encode(comment, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(link);
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

                        String username = c.getString("fullname");
                        //String time = c.getString("time");
                        String content = c.getString("content");
                        String avatar = c.getString("avatar");

                        item.put("USERNAME", username);
                        item.put("AVATAR", avatar);
                        item.put("CONTENT", content);
                        result_list.add(item);
                    }

                } catch (final JSONException e) {
                }
                // item.setText(item2);
                adapter = new DiscuAdapter(getActivity(), result_list);
                listview.setAdapter(adapter);
            } else if (task.equals("add")) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(context).attach(context).commit();
                //adapter.notifyDataSetChanged();
            }

        }
    }
}

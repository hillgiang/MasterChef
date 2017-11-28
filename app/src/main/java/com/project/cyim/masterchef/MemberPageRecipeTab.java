package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by Hillary on 11/17/2017.
 */

public class MemberPageRecipeTab extends Fragment {
    ListView recipes_list;

    public MemberPageRecipeTab() {
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
        View v = inflater.inflate(R.layout.member_page_recipes, container, false);
        recipes_list = (ListView)v.findViewById(R.id.recipe_list);
        new RecipesData(this, recipes_list).execute("get", ((User)getActivity()).username());

        return v;
    }

    class RecipesData extends AsyncTask<String, String, String> {
        private MemberPageRecipeTab context;
        private ListView listview;
        String task;

        public RecipesData(MemberPageRecipeTab context, ListView listview) {
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
                    String link = ip + "/GetRecipe.php";
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
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
            if (task.equals("get")) {
                ArrayList<HashMap<String, String>> result_list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONArray reci = new JSONArray(result);
                    for (int i = reci.length() / 2; i < reci.length(); i++) {
                        HashMap<String, String> item = new HashMap<>();
                        JSONObject c = reci.getJSONObject(i);
                        String title = c.getString("recipes_name");
                        String author = c.getString("user_id");
                        String thumbnail = c.getString("thumbnail");
                        String id = c.getString("recipes_id");

                        item.put("TITLE", title);
                        item.put("AUTHOR", author);
                        item.put("THUMBNAIL", thumbnail);
                        item.put("ID", id);
                        item.put("AVATAR", c.getString("avatar"));
                        item.put("USERNAME", c.getString("username"));
                        item.put("FAVORITE", c.getString("favorite"));
                        if (c.has("comment"))
                            item.put("COMMENT", c.getString("comment"));
                        else
                            item.put("COMMENT", "0");
                        result_list.add(item);
                    }

                } catch (final JSONException e) {
                }
                // item.setText(item2);
                LargeRecipeCardAdapter adapter;
                adapter = new LargeRecipeCardAdapter(getActivity(), result_list);
                listview.setAdapter(adapter);
            }

        }
    }
}

package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
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
 * Created by Sabrina on 2017/8/13.
 */

public class SearchFragmentInWord extends Fragment {

    SearchView searchView;
    ListView listview;
    TextView noresult;

    public SearchFragmentInWord() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.searchfragment_word, container, false);
        listview = (ListView)v.findViewById(R.id.list);
        noresult = (TextView)v.findViewById(R.id.noresult);

        searchView=(SearchView) v.findViewById(R.id.searchView);
        searchView.setQueryHint("Please enter recipe title...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
                new SearchData(SearchFragmentInWord.this).execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return v;
    }

    class SearchData extends AsyncTask<String, String, String> {

        private SearchFragmentInWord context;

        public SearchData(SearchFragmentInWord context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {
            try {
                String recipes_name = (String) arg0[0];

                String ip = "http://140.135.113.99";
                String link = ip + "/SearchRecipeByName.php";
                String data = URLEncoder.encode("recipes_name", "UTF-8") + "=" +
                        URLEncoder.encode(recipes_name, "UTF-8");

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

        protected void onPostExecute(String result) {
            if ( result.equals("none") )
                noresult.setText(R.string.noresult);
            else {
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
}


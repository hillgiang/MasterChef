package com.project.cyim.masterchef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

/**
 * Created by Hillary on 7/19/2017.
 */

public class GetRecipes extends AsyncTask<String, String, String> {
    private Context context;
    private ListView list;
    private LazyAdapter adapter;
    private Activity a;
    public ArrayList<HashMap<String, String>> recipes;
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_AUTHOR = "author";

    public GetRecipes(Context context, ListView list, Activity a) {
        this.a = a;
        this.list = list;
        this.context = context;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String ip = "http://140.135.113.99";
            String link = ip + "/GetAllRecipes.php";

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            //wr.write(data);
            //wr.flush();

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
    protected void onPostExecute(String result){
        recipes = new ArrayList<HashMap<String, String>>();
        for (String i: result.split("/")) {
            if ( !i.isEmpty() ) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_ID, i.split(",")[0]);
                map.put(KEY_TITLE, i.split(",")[4]);

                recipes.add(map);
            }
        }

        adapter=new LazyAdapter(a, recipes);
        list.setAdapter(adapter);
    }
}

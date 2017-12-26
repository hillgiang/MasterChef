package com.project.cyim.masterchef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Hillary on 7/19/2017.
 * In order to make a horizontal RecyclerView , we need create a layout and Adapter class for each row
 */

public class GetRecipes extends AsyncTask<String, String, String> {
    private Context context;
    public ArrayList<HashMap<String, String>> recipes;
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_AUTHOR = "author";
    static final String KEY_THUMBNAIL = "thumbnail";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecipeAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout;
    String SectionName, order;

    public GetRecipes(Context context, RecyclerView recyclerView, String SectionName, String order) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.SectionName = SectionName;
        this.order = order;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String ip = "http://140.135.113.99";
            String link = ip + "/GetAllRecipes.php";
            String data = URLEncoder.encode("order", "UTF-8") + "=" + URLEncoder.encode(order, "UTF-8");

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
    protected void onPostExecute(String result){
        recipes = new ArrayList<HashMap<String, String>>();
        /*
        for (String i: result.split("/")) {
            if ( !i.isEmpty() ) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_ID, i.split(",")[0]);
      //          map.put(KEY_TITLE, i.split(",")[4]);

                recipes.add(map);
            }
        }
        */
        try {
            JSONArray reci = new JSONArray(result);
            for (int i = 0; i < reci.length(); i++) {
                JSONObject c = reci.getJSONObject(i);

                String id = c.getString("recipes_id");
                String title = c.getString("recipes_name");
                String thumbnail = c.getString("thumbnail");
                if (thumbnail.equals(""))
                    thumbnail = R.drawable.foods + "";
                if (thumbnail.indexOf("http") == -1)
                    thumbnail = "http://" + thumbnail;
                HashMap<String, String> reci2 = new HashMap<>();

                // adding each child node to HashMap key => value
                reci2.put(KEY_ID, id);
                reci2.put(KEY_AUTHOR, c.getString("fullname"));
                reci2.put(KEY_TITLE, title);
                reci2.put(KEY_THUMBNAIL, thumbnail);

                // adding contact to contact list
                recipes.add(reci2);
            }

            RecyclerViewLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(RecyclerViewLayoutManager);

            RecyclerViewHorizontalAdapter = new RecipeAdapter(context, recipes);

            HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);
            recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
        } catch (final JSONException e) {
        }
    }
}

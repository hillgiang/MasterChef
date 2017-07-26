package com.project.cyim.masterchef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hillary on 7/19/2017.
 */

public class GetRecipes extends AsyncTask<String, String, String> {
    private Context context;
    private TextView recipes;

    public GetRecipes(Context context, TextView recipes) {
        this.recipes = recipes;
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
        this.recipes.setText(result);
    }
}

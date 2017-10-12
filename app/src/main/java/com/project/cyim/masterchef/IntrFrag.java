package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by yu fen on 2017/8/10.
 * Introduction
 */

public class IntrFrag extends Fragment{
    OnFragmentInteractionListener mListener;
    TextView introtext, title, author;
    ImageView thumbnail;

    public IntrFrag() {
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
        View v = inflater.inflate(R.layout.frag_intr, container, false);

        introtext = (TextView)v.findViewById(R.id.introtext);
        title = (TextView)v.findViewById(R.id.title);
        author = (TextView)v.findViewById(R.id.username);
        thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        int id = ((OnFragmentInteractionListener)getActivity()).recipe_id();
        new RecipeData(this).execute(id + "");

        return v;
    }



    // Get Recipe detail
    class RecipeData extends AsyncTask<String, String, String> {
        private IntrFrag context;

        public RecipeData(IntrFrag context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String id = (String) arg0[0];

                String ip = "http://140.135.113.99";
                String link = ip + "/GetRecipe.php";
                String data = URLEncoder.encode("id", "UTF-8") + "=" +
                        URLEncoder.encode(id, "UTF-8");

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

        //String item2;
        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray reci = new JSONArray(result);
                JSONObject c = reci.getJSONObject(0);

                introtext.setText(c.getString("content"));
                title.setText(c.getString("recipes_name"));
                author.setText(c.getString("user_id"));

                // loading album cover using Glide library
                String thumb = c.getString("thumbnail");
                if (thumb.equals(""))
                    thumb = R.drawable.foods + "";
                if (thumb.indexOf("http") == -1)
                    thumb = "http://" + thumb;
                Glide.with(getContext())
                        .load(thumb)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(thumbnail);

                //discuss.put("DISCUSS", );
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

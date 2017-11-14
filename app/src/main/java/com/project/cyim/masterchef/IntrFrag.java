package com.project.cyim.masterchef;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

/**
 * Created by yu fen on 2017/8/10.
 * Introduction
 */

public class IntrFrag extends Fragment{
    OnFragmentInteractionListener mListener;
    TextView introtext, title, author;
    ImageView thumbnail;
    ImageView like;
    SessionManagement session;

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
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        final String name = user.get(SessionManagement.KEY_EMAIL);

        introtext = (TextView)v.findViewById(R.id.introtext);
        title = (TextView)v.findViewById(R.id.title);
        author = (TextView)v.findViewById(R.id.username);
        thumbnail = (ImageView)v.findViewById(R.id.thumbnail);
        like = (ImageView)v.findViewById(R.id.likepic);

        final int id = ((OnFragmentInteractionListener)getActivity()).recipe_id();
        new RecipeData(this).execute(id + "", "null", name);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeData(IntrFrag.this).execute(id + "", "add_fav", name);
            }
        });

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
                String task = (String) arg0[1];
                String name = (String) arg0[2];

                String ip = "http://140.135.113.99";
                String link, data;
                StringBuilder sb = new StringBuilder();

                if (task.equals("add_fav")) {
                    link = ip + "/MemberFav.php?username="+
                            URLEncoder.encode(name, "UTF-8") + "&recipes_id="+URLEncoder.encode(id, "UTF-8") + "&task=add";
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.flush();
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                }
                else {
                    link = ip + "/GetRecipe.php";
                    data = URLEncoder.encode("id", "UTF-8") + "=" +
                            URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(name, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                }

                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        //String item2;
        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("done") && !result.equals("removed")) // GetRecipe
                try {
                    JSONArray reci = new JSONArray(result);
                    JSONObject c = reci.getJSONObject(0);

                    introtext.setText(c.getString("content"));
                    title.setText(c.getString("recipes_name"));
                    author.setText(c.getString("username"));
                    if (c.getString("fav").equals("true"))
                        like.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                    else
                        like.setColorFilter(ContextCompat.getColor(getContext(), R.color.input_register), android.graphics.PorterDuff.Mode.MULTIPLY);

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
            else { // AddFavRecipe
                if (result.equals("done")) {
                    Toast.makeText(getContext(), R.string.add_fav, Toast.LENGTH_SHORT).show();
                    // change to red
                    like.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else if (result.equals("removed")) {
                    Toast.makeText(getContext(), R.string.remove_fav, Toast.LENGTH_SHORT).show();
                    // change to gray
                    like.setColorFilter(ContextCompat.getColor(getContext(), R.color.input_register), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            }
        }
    }
}

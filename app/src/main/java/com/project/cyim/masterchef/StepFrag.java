package com.project.cyim.masterchef;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yu fen on 2017/8/10.
 */

public class StepFrag extends Fragment{
    ListView steps_list;

    public StepFrag() {
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
        View v = inflater.inflate(R.layout.frag_step, container, false);
        steps_list = (ListView)v.findViewById(R.id.list);

        int id = ((OnFragmentInteractionListener)getActivity()).recipe_id();
        new StepsData(this).execute(id + "");
        return v;
    }

    // Get Recipe detail
    class StepsData extends AsyncTask<String, String, String> {
        private StepFrag context;

        public StepsData(StepFrag context) {
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
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
            try {
                JSONArray reci = new JSONArray(result);
                JSONObject c = reci.getJSONObject(0);

                JSONArray steps = new JSONArray(c.getString("steps"));
                for ( int i = 0; i < steps.length(); i++ ) {
                    JSONObject stepobj = steps.getJSONObject(i);
                    HashMap<String, String> step = new HashMap<>();
                    step.put("DESCRIBE", stepobj.getString("describe"));
                    step.put("PIC", stepobj.getString("pic"));

                    list.add(step);
                }

            } catch (final JSONException e) {
                e.printStackTrace();
            }

            StepsAdapter adapter = new StepsAdapter(getContext(), list);
            steps_list.setAdapter(adapter);
        }
    }

    // Steps Adapter
    class StepsAdapter extends ArrayAdapter
    {
        Context context;
        ArrayList<HashMap<String,String>> list;

        public StepsAdapter(Context context, ArrayList<HashMap<String,String>> list)
        {
            super(context,R.layout.step_row,list);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.list = list;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.step_row, parent, false);
            HashMap<String, String> item = list.get(position);
            TextView step_num = (TextView) convertView.findViewById(R.id.step_num);
            step_num.setText("Step " + (position + 1));
            TextView describe = (TextView) convertView.findViewById(R.id.describe);
            describe.setText(item.get("DESCRIBE"));
            ImageView pic = (ImageView) convertView.findViewById(R.id.pic);
            String thumb = item.get("PIC");
            if (thumb.equals(""))
                thumb = R.drawable.foods + "";
            if (thumb.indexOf("http") == -1)
                thumb = "http://" + thumb;
            Glide.with(getContext())
                    .load(thumb)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(pic);

            return convertView;
        }
    }
}

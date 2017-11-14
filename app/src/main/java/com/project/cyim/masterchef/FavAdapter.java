package com.project.cyim.masterchef;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yu fen on 2017/11/2.
 */

public class FavAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private String username;
    //public ImageLoader imageLoader;
    //btn
    private Context context;


    public FavAdapter(Activity a, ArrayList<HashMap<String, String>> d, String name) {
        activity = a;
        data=d;
        username = name;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.fav_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // recipe title
        TextView author = (TextView)vi.findViewById(R.id.author); // author
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        ImageView cancel = (ImageView)vi.findViewById(R.id.cancelfav);

        HashMap<String, String> recipe = new HashMap<String, String>();
        recipe = data.get(position);

        // Setting all values in listview
        title.setText(recipe.get("TITLE"));
        author.setText(recipe.get("AUTHOR"));
        final String id = recipe.get("ID");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Delete(FavFragment.newInstance()).execute(id, username);
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        String thumb = recipe.get("THUMBNAIL");
        if (thumb.equals(""))
            thumb = R.drawable.foods + "";
        if (thumb.indexOf("http") == -1)
            thumb = "http://" + thumb;
        Glide.with(activity)
                .load(thumb)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumb_image);
        //imageLoader.DisplayImage(recipe.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }

    class Delete extends AsyncTask<String, String, String> {

        private FavFragment context;


        public Delete(FavFragment context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            String id = (String) arg0[0];
            String name = (String) arg0[1] ;

            try {
                String ip = "http://140.135.113.99";
                String link = ip + "/MemberFav.php?username="+
                        URLEncoder.encode(name, "UTF-8") + "&recipes_id="+URLEncoder.encode(id, "UTF-8") + "&task=delete";


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
        }
    }
}
package com.project.cyim.masterchef;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.EditText;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2017/8/21.
 */
public class EditGetUser extends AsyncTask<String, String, String> {
    private Context context;
    private EditText user;
    private ImageView avatar;

    public EditGetUser(Edituser context, EditText user, ImageView avatar) {
        this.user = user;
        this.context = context;
        this.avatar = avatar;
    }
    protected void onPreExecute(){
    }

    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];
            String ip = "http://140.135.113.99";
            String link = ip + "/GetMemberInfo.php";
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
        if (result.contains("Exception:") == true) {
            this.user.setText(result);
        } else {
            try {
                JSONArray reci = new JSONArray(result);
                for (int i = 0; i < reci.length(); i++) {
                    HashMap<String, String> item = new HashMap<>();
                    JSONObject c = reci.getJSONObject(i);
                    this.user.setText(c.getString("fullname"));
                    String thumb = c.getString("avatar");
                    if (thumb.equals(""))
                        thumb = R.drawable.member + "";
                    if (thumb.indexOf("http") == -1)
                        thumb = "http://" + thumb;
                    Glide.with(context)
                            .load(thumb)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(avatar);
                }
            } catch (final JSONException e) {}
        }
    }
}

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
import android.widget.TextView;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by user on 2017/8/10.
 */

public class GetUserInfo extends AsyncTask<String, String, String> {
    private UsersInfo context;
    private TextView userinfo;

    public GetUserInfo(UsersInfo context, TextView userinfo) {
        this.userinfo = userinfo;
        this.context = context;
    }


    protected void onPreExecute() {
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
            this.userinfo.setText(result);
        } else {
            String[] temp = null;
            temp = result.split(",");
            this.userinfo.setText("fullname : "+temp[4]+"\nfridge : "+temp[3]);

        }
    }
}

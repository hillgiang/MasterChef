package com.project.cyim.masterchef;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Hillary on 7/19/2017.
 */

public class LoginActivity extends AsyncTask<String, String, String> {
    private Context context;
    SessionManagement session;
    private String name = "";
    private String task; //任務

    //flag 0 means get and 1 means post.(By default it is get.)
    public LoginActivity(Context context, String task) {
        this.task = task;
        this.context = context;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        if ( task.equals("login") ) {
            try {
                String username = (String) arg0[0];
                String password = (String) arg0[1];

                String ip = "http://140.135.113.99";
                String link = ip + "/loginpost.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");

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

                name = username;
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        } else if (task.equals("reg")) {
            try {
                String username = (String) arg0[0];
                String password = (String) arg0[1];
                String fullname = (String) arg0[2];

                String ip = "http://140.135.113.99";
                String link = ip + "/register.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("fullname", "UTF-8") + "=" +
                        URLEncoder.encode(fullname, "UTF-8");

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
                // 把伺服器回傳的東西存進sb變數裡
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){ // result變數是從上面doInBackground函數回傳的
        session = new SessionManagement(context);
        if ( result.equals("Login") ) {
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
            session.createLoginSession(name);
            context.startActivity(new Intent(context, UsersInfo.class));
        }
        else if ( result.equals("") )
            Toast.makeText(context, "Username or Password incorrect!", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(context, "Register Successful!", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, Login.class));
        }
    }
}

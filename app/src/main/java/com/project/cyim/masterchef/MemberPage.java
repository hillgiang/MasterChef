package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hillary on 11/17/2017.
 */

public class MemberPage extends AppCompatActivity implements User {
    // tab
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManagement session;
    MemberPage.ViewPagerAdapter adapter;
    String username, user_id, my_id;
    Button followbtn;
    TextView username2, follow_count;
    ImageView avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_page);

        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);
        my_id = user.get(SessionManagement.KEY_ID);
        Log.i("aaa", my_id);

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        username2 = (TextView) findViewById(R.id.username);
        //follow_count = (TextView) findViewById(R.id.follow_count);
        followbtn = (Button) findViewById(R.id.followbtn);
        avatar = (ImageView) findViewById(R.id.thumbnail);

        // get recipe id
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                user_id = "-1";
                username = "-1";
            } else {
                user_id = extras.getString("USER_ID");
                username = extras.getString("USERNAME");
            }
        } else {
            user_id = (String)savedInstanceState.getSerializable("USER_ID");
            username = (String)savedInstanceState.getSerializable("USERNAME");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(username);
        username2.setText(username);

        new UserData(this, "get").execute(username);

        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserData(MemberPage.this, "follow").execute(email);
            }
        });
    }

    // tab
    private void setupViewPager(ViewPager viewPager) {
        adapter = new MemberPage.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MemberPageRecipeTab(), getString(R.string.recipe));
        adapter.addFragment(new MemberPageFanTab(), getString(R.string.fan));
        viewPager.setAdapter(adapter);
    }

    // tab
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class UserData extends AsyncTask<String, String, String> {
        private MemberPage context;
        String task;

        public UserData(MemberPage context, String task) {
            this.context = context;
            this.task = task;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {
            // String task = (String) arg0[0];
            String name = (String) arg0[0] ;

            if (task.equals("get")) {
                try {
                    String ip = "http://140.135.113.99";
                    String link = ip + "/GetMemberInfo.php";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("me", "UTF-8") + "=" +
                            URLEncoder.encode(my_id, "UTF-8");

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
            } else if (task.equals("follow")) {
                try {
                    String ip = "http://140.135.113.99";
                    String link = ip + "/GetFollow.php";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("task", "UTF-8") + "=" +
                            URLEncoder.encode("add", "UTF-8");
                    data += "&" + URLEncoder.encode("follow_id", "UTF-8") + "=" +
                            URLEncoder.encode(user_id, "UTF-8");

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
            return null;
        }

        protected void onPostExecute(String result) {

            if ( task.equals("get") ){
                try {
                    JSONArray reci = new JSONArray(result);
                    for (int i = 0; i < reci.length(); i++) {
                        HashMap<String, String> item = new HashMap<>();
                        JSONObject c = reci.getJSONObject(i);
                        //follow_count.setText(c.getString("follower").split(",").length + "");
                        if (c.getString("isfollow").equals("true")) {
                            followbtn.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_main));
                            followbtn.setText("-取消追蹤");
                        } else {
                            followbtn.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                            followbtn.setText("+追蹤");
                        }

                        username2.setText(c.getString("fullname"));
                        String thumb = c.getString("avatar");
                        if (thumb.equals("") || thumb.equals("null"))
                            thumb = R.drawable.foods + "";
                        if (thumb.indexOf("http") == -1)
                            thumb = "http://" + thumb;
                        Glide.with(context)
                                .load(thumb)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(avatar);

                    }

                } catch (final JSONException e) {}
            } else if ( task.equals("follow") ){
                if (result.equals("done")) {
                    Toast.makeText(context, R.string.add_follow, Toast.LENGTH_SHORT).show();
                    followbtn.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                    followbtn.setText("+追蹤");
                } else if (result.equals("removed")) {
                    Toast.makeText(context, R.string.remove_follow, Toast.LENGTH_SHORT).show();
                    followbtn.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_main));
                    followbtn.setText("-取消追蹤");
                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    public String username() {
        return username;
    }

}

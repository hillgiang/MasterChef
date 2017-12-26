package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yu fen on 2017/10/14.
 */

public class UploadRecipe extends AppCompatActivity implements UploadFragmentInteractionListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private JSONObject recipe;
    private JSONArray ingredient_item, steps;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_edit_reci);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.newrecipe));

        recipe = new JSONObject();
        ingredient_item = new JSONArray();
        steps = new JSONArray();
        try { recipe.put("INGREDIENT", ingredient_item);
              recipe.put("STEPS", steps); } catch (Exception e) {}

        viewPager = (ViewPager) findViewById(R.id.pagers);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabuped);
        tabLayout.setupWithViewPager(viewPager);

    }


    // tab
    private void setupViewPager(ViewPager viewPager) {
        UploadRecipe.ViewPagerAdapter adapter = new UploadRecipe.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UploadIntro(), getString(R.string.intro));
        adapter.addFragment(new UploadReci(), getString(R.string.ingredient));
        adapter.addFragment(new UploadSteps(), getString(R.string.step));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                super.onBackPressed();
                return true;
            //case R.id.recipe_save:
                //return true;
            case R.id.recipe_publish:
                //Uploading code
                Log.i("recipe", recipe.toString());

                try {
                    String uploadId = UUID.randomUUID().toString();

                    //Creating a multi part request
                    MultipartUploadRequest request = new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL).setUtf8Charset();
                    request.addFileToUpload(recipe.getString("THUMBNAIL"), "image"); //Adding file
                    request.addParameter("title", recipe.getString("TITLE")); //Adding text parameter to the request
                    request.addParameter("content", recipe.getString("CONTENT"));
                    request.addParameter("userid", recipe.getString("USER_ID"));
                    JSONArray stepsjson = new JSONArray(recipe.getString("STEPS"));
                    for(int i=0; i<stepsjson.length(); i++){
                        JSONObject j = stepsjson.getJSONObject(i);
                        //change path and data according to your files
                        request.addFileToUpload(j.getString("PIC"), "steps_pic[]");
                        request.addParameter("steps_describe[]", j.getString("DESCRIBE"));
                    }
                    JSONArray ingredient = new JSONArray(recipe.getString("INGREDIENT"));
                    for(int i=0; i<ingredient.length(); i++){
                        JSONObject j = ingredient.getJSONObject(i);
                        request.addParameter("ingredient_name[]", j.getString("NAME"));
                        request.addParameter("ingredient_weight[]", j.getString("WEIGHT"));
                    }
                    request.setNotificationConfig(new UploadNotificationConfig());
                    request.setMaxRetries(2);
                    request.startUpload(); //Starting the upload
                    Log.i("upload_info", recipe.toString());
                    Intent intent = new Intent(this, MainActivity.class);
                    //intent.putExtra("RECIPE_ID", id);
                    //intent.putExtra("RECIPE_TITLE", recipe.getString("TITLE"));
                    this.startActivity(intent);

                } catch (Exception exc) {
                    Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void add(String key, String value) {
        try { recipe.put(key, value); } catch (Exception e) {}
    }

    public JSONArray add_ingredient() {
        return ingredient_item;
    }

    public JSONArray add_steps() {
        return steps;
    }
}
package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yu fen on 2017/8/10.
 */

public class Food extends AppCompatActivity implements OnFragmentInteractionListener {
    // tab
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManagement session;
    private BottomBar bottomBar;
    ViewPagerAdapter adapter;
    int recipe_id;

    HashMap<String, String> intro = new HashMap<String, String>();
    HashMap<String, String> ingredient = new HashMap<String, String>();
    HashMap<String, String> steps = new HashMap<String, String>();
    HashMap<String, String> discuss = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);

        session = new SessionManagement(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.option));

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // get recipe id
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                recipe_id = -1;
            } else {
                recipe_id = extras.getInt("RECIPE_ID");
            }
        } else {
            recipe_id = Integer.parseInt((String)savedInstanceState.getSerializable("RECIPE_ID"));
        }

        /**
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.fav) {

                } else if (tabId == R.id.pic) {

                }
            }
        });
         **/
    }

    // tab
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IntrFrag(), getString(R.string.intro));
        adapter.addFragment(new ReciFrag(), getString(R.string.ingredient));
        adapter.addFragment(new StepFrag(), getString(R.string.step));
        adapter.addFragment(new DiscuFrag(), getString(R.string.discuss));
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

    @Override
    public int recipe_id() {
        return recipe_id;
    }
}
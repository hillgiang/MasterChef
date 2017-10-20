package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu fen on 2017/10/14.
 */

public class UploadRecipe extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_edit_reci);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.option));

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

}
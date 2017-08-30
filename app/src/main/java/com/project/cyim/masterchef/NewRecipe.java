package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;

import com.project.cyim.masterchef.NewRecipeFrag.NewIngredientsFrag;
import com.project.cyim.masterchef.NewRecipeFrag.NewIntroFrag;
import com.project.cyim.masterchef.NewRecipeFrag.NewStepsFrag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu fen on 2017/8/29.
 */

public class NewRecipe extends AppCompatActivity {
    // tab
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManagement session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recipe);

        session = new SessionManagement(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.option));

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // tab
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewIntroFrag(), getString(R.string.intro));
        adapter.addFragment(new NewIngredientsFrag(), getString(R.string.ingredient));
        adapter.addFragment(new NewStepsFrag(), getString(R.string.step));
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
}
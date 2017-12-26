package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import java.util.ArrayList;
import android.app.Activity;
import java.util.List;
import java.util.HashMap;
import android.content.Intent;

/**
 * Created by Hillary on 7/26/2017.
 */

public class UsersInfo extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textview, fullname;
    private ImageView avatar;
    private Button button01;
    SessionManagement session;

    public static UsersInfo newInstance() {
        UsersInfo fragment = new UsersInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.users_info, container, false);
        // tab
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        session = new SessionManagement(getActivity());

        textview = (TextView) v.findViewById(R.id.textView);
        fullname = (TextView) v.findViewById(R.id.author);
        avatar = (ImageView) v.findViewById(R.id.list_image);
        button01 = (Button) v.findViewById(R.id.edit);

        button01.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), Edituser.class);
                startActivity(intent);
            }
        });


        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManagement.KEY_EMAIL);
        textview.setText(email);

        new GetUserInfo(this, fullname, avatar).execute(email);

        //所以現在email變數所存的就是賬號。
        //再把賬號名稱傳給GetMemberInfo.php。
        //最後，伺服器會回傳使用者資料。
        //把那些資料在UI上顯示出來(set TextView)。
        return v;

    }
    public void Edit(View view){
        startActivity(new Intent(getActivity(), Edituser.class));
    }
    // tab
    private void setupViewPager(ViewPager viewPager) {
        UsersInfo.ViewPagerAdapter adapter = new UsersInfo.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyFridge(), getString(R.string.myfridge));
        adapter.addFragment(new MyRecipes(), getString(R.string.myrecipes));
        adapter.addFragment(new MyVideo(), getString(R.string.myvideo));
        adapter.addFragment(new Follow(), getString(R.string.follow));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_more:
                startActivity(new Intent(getActivity(), UserMore.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

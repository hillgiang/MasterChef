package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Hillary on 7/26/2017.
 */

public class GetAllRecipes extends Fragment {
    RecyclerView my_recycler_view;
    RecyclerView mRecyclerViewType;
   // private ArrayList<Integer> mDatas;
    View ChildView ;
    int RecyclerViewItemPosition ;

    public static GetAllRecipes newInstance() {
        GetAllRecipes fragment = new GetAllRecipes();
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
        View v = inflater.inflate(R.layout.recipes, container, false);

        ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("米飯、麵類主食");
        mDatas.add("開胃菜、沙拉");
        mDatas.add("肉類");
        mDatas.add("海鮮");
        mDatas.add("中華料理");
        mDatas.add("甜點");
        mDatas.add("鍋、湯");
        //new GetRecipes(getActivity(), recyclerView).execute();
        mRecyclerViewType = (RecyclerView) v.findViewById(R.id.my_recycler_view_type);
        mRecyclerViewType.setHasFixedSize(true);
        RecipeTypeAdapter adapter2 = new RecipeTypeAdapter(getActivity(), mDatas);
        mRecyclerViewType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewType.setAdapter(adapter2);

        ArrayList<String> cata = new ArrayList<>();
        cata.add("熱門");
        cata.add("最新");

        //RecyclerView to show the list in vertical order
        my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        RecipeCataAdapter adapter = new RecipeCataAdapter(getActivity(), cata);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);


        // Adding on item click listener to RecyclerView.
        my_recycler_view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_write:
                startActivity(new Intent(getActivity(), UploadRecipe.class));
                return true;
            //case R.id.action_cart:
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

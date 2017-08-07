package com.project.cyim.masterchef;

/**
 * Created by Hillary on 8/4/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyView> {

    private ArrayList<HashMap<String, String>> list;

    public class MyView extends RecyclerView.ViewHolder {

        public TextView title;

        public MyView(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);

        }
    }


    public RecyclerViewAdapter(ArrayList<HashMap<String, String>> horizontalList) {
        this.list = horizontalList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        HashMap<String, String> recipe = new HashMap<String, String>();
        recipe = list.get(position);
        holder.title.setText(recipe.get(GetRecipes.KEY_TITLE));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
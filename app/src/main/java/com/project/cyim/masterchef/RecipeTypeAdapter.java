package com.project.cyim.masterchef;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Hillary on 8/12/2017.
 * Adapter Class for the recyclerView in the GetAllRecipes
 */

public class RecipeTypeAdapter extends RecyclerView.Adapter<RecipeTypeAdapter.ViewHolder>
{
    private Context mContext;
    private ArrayList<String> mDatas;

    public RecipeTypeAdapter(Context mContext, ArrayList<String> datats)
    {
        this.mContext = mContext;
        mDatas = datats;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }
       Button mImg;
        // TextView mTxt;

    }
    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
    /**
     * 創建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipes_type_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (Button) view
                .findViewById(R.id.label);
        final String type = viewHolder.mImg.getText().toString();

                    viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    //FragmentManager manager = getSupportFragmentManager();
                        public void onClick(View v){
                           Intent intent = new Intent(mContext, GetType.class);
                           intent.putExtra("Type", type);
                            mContext.startActivity(intent);
                        }
                    });


        return viewHolder;
    }
    /**
     * 設置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        viewHolder.mImg.setText(mDatas.get(i));

    }
}
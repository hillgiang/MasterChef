package com.project.cyim.masterchef;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hillary on 8/12/2017.
 * Adapter Class for the recyclerView in the GetAllRecipes
 */

public class RecipeCataAdapter extends RecyclerView.Adapter<RecipeCataAdapter.ItemRowHolder> {
    private ArrayList<String> dataList;
    private Context mContext;

    public RecipeCataAdapter(Context context, ArrayList<String> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipes_card_list, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i);

        itemRowHolder.itemTitle.setText(sectionName);
        if (i == 1)
            new GetRecipes(mContext, itemRowHolder.recycler_view_list, sectionName, "hot").execute();
        else if (i == 2)
            new GetRecipes(mContext, itemRowHolder.recycler_view_list, sectionName, "lastest").execute();
        else if (i== 0)
           new GetMasterchef(mContext, itemRowHolder.recycler_view_list, sectionName, "masterchef").execute();

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected Button btnMore;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);
        }

    }
}

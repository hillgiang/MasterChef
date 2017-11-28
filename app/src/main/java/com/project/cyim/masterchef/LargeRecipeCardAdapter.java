package com.project.cyim.masterchef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hillary on 11/18/2017.
 */

public class LargeRecipeCardAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public LargeRecipeCardAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.recipe_card, null);

        TextView title = (TextView)vi.findViewById(R.id.recipes_title); // recipe title
        TextView username = (TextView)vi.findViewById(R.id.username);
        TextView favorite = (TextView)vi.findViewById(R.id.liken);
        TextView comment = (TextView)vi.findViewById(R.id.disn);
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.pic); // thumb image
        ImageView avatar = (ImageView)vi.findViewById(R.id.avatar);

        HashMap<String, String> recipe = new HashMap<String, String>();
        recipe = data.get(position);
        final int id = Integer.parseInt(recipe.get("ID"));
        final String titlestr = recipe.get("TITLE");

        // Setting all values in listview
        title.setText(recipe.get("TITLE"));
        username.setText(recipe.get("USERNAME"));
        favorite.setText(recipe.get("FAVORITE"));
        comment.setText(recipe.get("COMMENT"));
        String ava = recipe.get("AVATAR");
        String thumb = recipe.get("THUMBNAIL");
        if (thumb.equals(""))
            thumb = R.drawable.foods + "";
        if (thumb.indexOf("http") == -1)
            thumb = "http://" + thumb;
        Glide.with(activity)
                .load(thumb)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumb_image);
        Glide.with(activity)
                .load(ava)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar);
        //imageLoader.DisplayImage(recipe.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Food.class);
                intent.putExtra("RECIPE_ID", id);
                intent.putExtra("RECIPE_TITLE", titlestr);
                activity.startActivity(intent);
            }
        });

        return vi;
    }
}

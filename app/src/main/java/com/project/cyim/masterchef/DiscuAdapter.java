package com.project.cyim.masterchef;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Hillary on 7/31/2017.
 */

public class DiscuAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public DiscuAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.discuss_row, null);

        TextView username = (TextView)vi.findViewById(R.id.username); // userfullname
        ImageView avatar_image=(ImageView)vi.findViewById(R.id.avatar); // avatar image
        //TextView time = (TextView)vi.findViewById(R.id.time); // time
        ImageView more=(ImageView)vi.findViewById(R.id.more); // thumb image
        TextView content = (TextView)vi.findViewById(R.id.content); // content

        HashMap<String, String> recipe = new HashMap<String, String>();
        recipe = data.get(position);

        // Setting all values in listview

        username.setText(recipe.get("USERNAME"));
        content.setText(recipe.get("CONTENT"));
        String avatar = recipe.get("AVATAR");

        if (avatar.equals(""))
            avatar = R.drawable.foods + "";
        if (avatar.indexOf("http") == -1)
            avatar = "http://" + avatar;
        Glide.with(activity)
                .load(avatar)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar_image);
        //imageLoader.DisplayImage(recipe.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}

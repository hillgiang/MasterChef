package com.project.cyim.masterchef;

import java.util.ArrayList;
import java.util.HashMap;

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

/**
 * Created by Hillary on 7/31/2017.
 */

public class UserAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public UserAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.follow_list, null);

        TextView user = (TextView)vi.findViewById(R.id.user); // author
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        HashMap<String, String> recipe = new HashMap<String, String>();
        recipe = data.get(position);

        // Setting all values in listview
        final String username = recipe.get("USER");
        final String id = recipe.get("ID");
        user.setText(recipe.get("FULLNAME"));

        String thumb = recipe.get("THUMBNAIL");
        if (thumb.equals("") || thumb.equals("null"))
            thumb = R.drawable.foods + "";
        if (thumb.indexOf("http") == -1)
            thumb = "http://" + thumb;
        Glide.with(activity)
                .load(thumb)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumb_image);
        //imageLoader.DisplayImage(recipe.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MemberPage.class);
                intent.putExtra("USER_ID", id);
                intent.putExtra("USERNAME", username);
                activity.startActivity(intent);
            }
        });
        return vi;
    }
}

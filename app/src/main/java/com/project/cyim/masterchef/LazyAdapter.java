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

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.recipes_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // recipe title
        TextView author = (TextView)vi.findViewById(R.id.author); // author
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        HashMap<String, String> recipe = new HashMap<String, String>();
        recipe = data.get(position);
        final int id = Integer.parseInt(recipe.get("ID"));
        final String titlestr = recipe.get("TITLE");

        // Setting all values in listview
        title.setText(recipe.get("TITLE"));
        author.setText(recipe.get("USERNAME"));

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

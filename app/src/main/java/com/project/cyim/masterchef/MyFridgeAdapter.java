package com.project.cyim.masterchef;

/**
 * Created by user on 2017/8/26.
 */

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class MyFridgeAdapter extends ArrayAdapter {
    Context context;
    List<String> modelItems;



    @SuppressWarnings("unchecked")

    public MyFridgeAdapter(Context context, List<String> resource) {
        super(context, R.layout.myfridge_list, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
    }
    private CheckedTextView cb;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.myfridge_list, parent, false);

        CheckedTextView chkBshow = (CheckedTextView) convertView.findViewById(R.id.check1);
        chkBshow.setText(modelItems.get(position));

        return convertView;

    }


}
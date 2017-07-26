package com.project.cyim.masterchef;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Hillary on 7/26/2017.
 */

public class GetAllRecipes extends Activity {
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);

        textview = (TextView) findViewById(R.id.textView2);

        new GetRecipes(this, textview).execute();
    }
}

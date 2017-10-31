package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yu fen on 2017/10/15.
 */

public class UploadSteps extends Fragment{
    Button addstep;
    LinearLayout steps;
    ImageButton image_select;
    int stepno = 1;

    public UploadSteps() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.upload_step, container, false);
        addstep = (Button)v.findViewById(R.id.addstep);
        EditText content = (EditText)v.findViewById(R.id.step_content);
        content.setMovementMethod(new ScrollingMovementMethod());
        image_select = (ImageButton)v.findViewById(R.id.thumb);

        steps = (LinearLayout)v.findViewById(R.id.steps);
        addstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View nextstep = LayoutInflater.from(getActivity()).inflate(R.layout.step, null);
                TextView step = (TextView)nextstep.findViewById(R.id.stepno);
                stepno++;
                step.setText("步驟"+stepno);
                steps.addView(nextstep);
                EditText content = (EditText)nextstep.findViewById(R.id.step_content);
                content.setMovementMethod(new ScrollingMovementMethod());
            }
        });

        image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }
}
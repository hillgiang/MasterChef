package com.project.cyim.masterchef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yu fen on 2017/10/14.
 */

public class UploadReci extends Fragment{
    private Button add_ingredient, ok;
    private EditText name1, weight1;
    private TextView test;
    JSONArray allingredient;
    LinearLayout ingredient;

    public UploadReci() {
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
        View v = inflater.inflate(R.layout.upload_reci, container, false);
        allingredient = ((UploadFragmentInteractionListener)getActivity()).add_ingredient();
        add_ingredient = (Button)v.findViewById(R.id.add_ingredient);
        ingredient = (LinearLayout)v.findViewById(R.id.ingredient);
        JSONObject item = new JSONObject();
        allingredient.put(item);
        name1 = (EditText)v.findViewById(R.id.name1);
        name1.addTextChangedListener(new GenericTextWatcher(name1, item));
        weight1 = (EditText)v.findViewById(R.id.weight1);
        weight1.addTextChangedListener(new GenericTextWatcher(weight1, item));

        add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View nextstep = LayoutInflater.from(getActivity()).inflate(R.layout.upload_reci_row, null);
                final JSONObject item = new JSONObject();
                allingredient.put(item);
                ingredient.addView(nextstep);
                final EditText name = (EditText)nextstep.findViewById(R.id.name);
                final EditText weight = (EditText)nextstep.findViewById(R.id.weight);
                weight.setMovementMethod(new ScrollingMovementMethod());
                name.setMovementMethod(new ScrollingMovementMethod());
                name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(Editable editable) {
                        try { item.put("NAME", name.getText().toString()); } catch (Exception e) {}
                    }
                });
                weight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(Editable editable) {
                        try { item.put("WEIGHT", weight.getText().toString()); } catch (Exception e) {}
                    }
                });
            }
        });

        return v;
    }

    private class GenericTextWatcher implements TextWatcher{
        private View view;
        private JSONObject item;
        private GenericTextWatcher(View view, JSONObject item) {
            this.view = view;
            this.item = item;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            switch(view.getId()){
                case R.id.name1:
                    try { item.put("NAME", name1.getText().toString()); } catch (Exception e) {}
                    break;
                case R.id.weight1:
                    try { item.put("WEIGHT", weight1.getText().toString()); } catch (Exception e) {}
                    break;
            }
        }
    }
}
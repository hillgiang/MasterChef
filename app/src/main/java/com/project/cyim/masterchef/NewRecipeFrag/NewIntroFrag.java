package com.project.cyim.masterchef.NewRecipeFrag;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.cyim.masterchef.R;

/**
 * Created by Hillary on 8/30/2017.
 */

public class NewIntroFrag extends Fragment {

    public NewIntroFrag() {
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
        return inflater.inflate(R.layout.new_intro_frag, container, false);
    }

    public interface IntroFragmentInteractionListener {
        public void onFragmentInteraction(String userContent, int size, int colorValue);
    }
}
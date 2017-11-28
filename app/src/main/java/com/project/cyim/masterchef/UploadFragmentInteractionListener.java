package com.project.cyim.masterchef;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hillary on 11/14/2017.
 */

public interface UploadFragmentInteractionListener {
    public void add(String key, String value);
    public JSONArray add_ingredient();
    public JSONArray add_steps();
}

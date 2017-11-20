package com.project.cyim.masterchef;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yu fen on 2017/10/15.
 */

public class UploadSteps extends Fragment{
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    Button addstep;
    LinearLayout steps;
    ImageView image_select, showimage;
    int stepno = 1;
    JSONArray steps_arr;
    JSONObject step2;

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
        final EditText content = (EditText)v.findViewById(R.id.step_content);
        content.setMovementMethod(new ScrollingMovementMethod());
        image_select = (ImageView)v.findViewById(R.id.thumb);
        steps_arr = ((UploadFragmentInteractionListener)getActivity()).add_steps();
        final JSONObject step = new JSONObject();
        steps_arr.put(step);

        steps = (LinearLayout)v.findViewById(R.id.steps);
        addstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View nextstep = LayoutInflater.from(getActivity()).inflate(R.layout.step, null);
                TextView step = (TextView)nextstep.findViewById(R.id.stepno);
                stepno++;
                step.setText("步驟"+stepno);
                steps.addView(nextstep);
                final EditText content = (EditText)nextstep.findViewById(R.id.step_content);
                content.setMovementMethod(new ScrollingMovementMethod());
                final JSONObject stepj = new JSONObject();
                steps_arr.put(stepj);
                final ImageView thumb = (ImageView)nextstep.findViewById(R.id.thumb);
                thumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showimage = thumb;
                        step2 = stepj;
                        showFileChooser();
                    }
                });

                content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(Editable editable) {
                        try { step2.put("DESCRIBE", content.getText().toString()); } catch (Exception e) {}
                    }
                });
            }
        });

        image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimage = image_select;
                step2 = step;
                showFileChooser();
            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) { try { step.put("DESCRIBE", content.getText().toString()); } catch (Exception e) {} }
        });
        return v;
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                showimage.setImageBitmap(bitmap);
                try { step2.put("PIC", getPath(filePath)); } catch (Exception e) {}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
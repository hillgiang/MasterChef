package com.project.cyim.masterchef;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yu fen on 2017/8/10.
 */

public class DiscuFrag extends Fragment{
    // comment
    private ListView listview;
    Button insert;
    EditText item;
    SessionManagement session;
    String comment_str;
    DiscuAdapter adapter;

    // upload Image
    ImageView thumbnail;
    Button buttonChoose, buttonUpload;
    private JSONObject image;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;

    public DiscuFrag() {
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
        View v = inflater.inflate(R.layout.frag_discu, container, false);
        listview = (ListView) v.findViewById(R.id.discuss_list);
        item = (EditText) v.findViewById(R.id.comment);
        insert = (Button) v.findViewById(R.id.send_comment);
        buttonChoose = (Button) v.findViewById(R.id.choose_pic);
        buttonUpload = (Button) v.findViewById(R.id.upload_pic);
        thumbnail = (ImageView)v.findViewById(R.id.thumbnail);

        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        final String userid = user.get(SessionManagement.KEY_ID);
        final int id = ((OnFragmentInteractionListener)getActivity()).recipe_id();
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_str = item.getText().toString();
                new DiscuData(DiscuFrag.this, listview).execute("add", id + "", userid, comment_str);
            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();
            }
        });

        new DiscuFrag.DiscuData(this, listview).execute("get", id + "");

        return v;
    }

    public void uploadMultipart() {
        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                thumbnail.setImageBitmap(bitmap);
                ((UploadFragmentInteractionListener)getActivity()).add("THUMBNAIL", getPath(filePath));
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

    class DiscuData extends AsyncTask<String, String, String> {
        private DiscuFrag context;
        private ListView listview;
        String task;

        public DiscuData(DiscuFrag context, ListView listview) {
            this.context = context;
            this.listview = listview;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            task = (String) arg0[0];
            String link, data;

            if (task.equals("get")) {
                try {
                    String id = (String) arg0[1];

                    String ip = "http://140.135.113.99";
                    link = ip + "/RecipeComment.php?recipe="+
                            URLEncoder.encode(id, "UTF-8") ;
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(link);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            } else if (task.equals("add")) {
                // DELETE THIS
                try {
                    String id = (String) arg0[1];
                    String userid = (String) arg0[2];
                    String comment = (String) arg0[3];

                    String ip = "http://140.135.113.99";
                    link = ip + "/RecipeComment.php?recipe="+
                            URLEncoder.encode(id, "UTF-8") + "&user_id=" + URLEncoder.encode(userid, "UTF-8") + "&content=" + URLEncoder.encode(comment, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(link);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
            // recipe, content, user_id, thumb -> ImageUpload.java
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
            if (task.equals("get")) {
                ArrayList<HashMap<String, String>> result_list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONArray reci = new JSONArray(result);
                    for (int i = 0; i < reci.length(); i++) {
                        HashMap<String, String> item = new HashMap<>();
                        JSONObject c = reci.getJSONObject(i);

                        String username = c.getString("fullname");
                        //String time = c.getString("time");
                        String content = c.getString("content");
                        String avatar = c.getString("avatar");

                        item.put("USERNAME", username);
                        item.put("AVATAR", avatar);
                        item.put("CONTENT", content);
                        result_list.add(item);
                    }

                } catch (final JSONException e) {
                }
                // item.setText(item2);
                adapter = new DiscuAdapter(getActivity(), result_list);
                listview.setAdapter(adapter);
            } else if (task.equals("add")) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(context).attach(context).commit();
                //adapter.notifyDataSetChanged();
            }

        }
    }
}

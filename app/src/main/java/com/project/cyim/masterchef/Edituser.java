package com.project.cyim.masterchef;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by user on 2017/8/11.
 */

public class Edituser extends Activity {
    private EditText nameField;
    private Button photo, edit;
    private ImageView avatar;
    private String email;
    private boolean hasPic = false;

    SessionManagement session;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituser);
        session = new SessionManagement(getApplicationContext());

        nameField = (EditText)findViewById(R.id.name);
        photo = (Button) findViewById(R.id.changephoto);
        edit = (Button) findViewById(R.id.edit);
        avatar = (ImageView) findViewById(R.id.avatar);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();
            }
        });

        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManagement.KEY_EMAIL);

        new EditGetUser(this, nameField, avatar).execute(email);

    }

    public void uploadMultipart() {
        if (hasPic) {
            //getting the actual path of the image
            String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, Constants.AVATAR)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("username", email)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

                Toast.makeText(this, R.string.change_success, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            new EditName(this).execute();
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
            hasPic = true;
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                avatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = this.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public class EditName extends AsyncTask<String, String, String> {
        private Context context;
        public EditName(Edituser context) {
            this.context = context;
        }
        protected void onPreExecute(){
        }

        protected String doInBackground(String... arg0) {
            try {
                String ip = "http://140.135.113.99";
                String link = ip + "/EditUser.php";
                String data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("fullname", "UTF-8") + "="
                        + URLEncoder.encode(nameField.getText().toString(), "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        protected void onPostExecute(String result) {
            Toast.makeText(context, R.string.change_success, Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
    }
}

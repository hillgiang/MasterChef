package com.project.cyim.masterchef;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by user on 2017/8/8.
 */
public class FridgeInsertWord extends Fragment {
    Button insert;
    EditText item;
    String ingredient;
    SessionManagement session;
    private Spinner sp;
    ArrayAdapter<String> adapter ;

    public FridgeInsertWord() {
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
        View v = inflater.inflate(R.layout.fridgeinsert_word, container, false);
        session = new SessionManagement(getActivity());
        item = (EditText) v.findViewById(R.id.ingredient);
        insert = (Button) v.findViewById(R.id.insert);
        sp = (Spinner) v.findViewById(R.id.spn);

        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);

        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.spn_list, android.R.layout.simple_spinner_item );
        sp.setAdapter(nAdapter);


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient = item.getText().toString();
                new  InsertFridge(FridgeInsertWord.this, "insert").execute( ingredient,email);
            }
        });

        return v;
    }

        public class InsertFridge extends AsyncTask<String, String, String> {
            private FridgeInsertWord context;
            String task;

            public InsertFridge(FridgeInsertWord context, String task) {
                this.context = context;
                this.task = task;
            }

            protected void onPreExecute() {
            }

            protected String doInBackground(String... arg0) {
                if (task.equals("insert")) {
                    String ingredient = (String) arg0[0];
                    try {
                        String username = (String) arg0[1];
                        String ip = "http://140.135.113.99";
                        String link = ip + "/InsertFridge.php";
                        String data = URLEncoder.encode("username", "UTF-8") + "=" +
                                URLEncoder.encode(username, "UTF-8");
                        data += "&" + URLEncoder.encode("ingredient", "UTF-8") + "=" +
                                URLEncoder.encode(ingredient, "UTF-8");

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
                return null;
            }

            protected void onPostExecute(String result) {
                if (task.equals("insert")) {
                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), UsersInfo.class);
                    startActivity(intent);
                }

            }
        }
}

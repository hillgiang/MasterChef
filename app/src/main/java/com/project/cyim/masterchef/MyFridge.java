package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2017/8/8.
 * 我的冰箱
 */
public class MyFridge extends Fragment {
    List<String> list = new ArrayList<String>();
    private ListView listview;
    List <Boolean> checkedlist = new ArrayList <Boolean>();
    List <String> list2 = new ArrayList <String>();
    Button delete;
    Button add;
    Button search;
    MyFridgeAdapter adapter;
    boolean firsttime_del = true;
    // TextView item;
    SessionManagement session;

    public MyFridge() {
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
        View v = inflater.inflate(R.layout.my_fridge, container, false);
        //item = (TextView) v.findViewById(R.id.item);
        listview = (ListView) v.findViewById(R.id.listview);
        session = new SessionManagement(getActivity());
        add = (Button) v.findViewById(R.id.add);
        delete = (Button) v.findViewById(R.id.delete);
        search = (Button) v.findViewById(R.id.search);

        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);
        new FridgeData(this, listview).execute("get", email);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CheckedTextView chkItem = (CheckedTextView) view.findViewById(R.id.check1);
                chkItem.setChecked(!chkItem.isChecked());
                //Toast.makeText(getContext(), "您點選了 "+list.get(position)+" ", Toast.LENGTH_SHORT).show();
                checkedlist.set(position, chkItem.isChecked());
            }
        });
        
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nothing checked
                int size = -1;
                if (!firsttime_del)
                    size = checkedlist.size() - 1;
                else
                    size = checkedlist.size();
                boolean nothing = true;
                firsttime_del = false;
                for ( int i = 0; i < size; i++ )
                    if (checkedlist.get(i))
                        nothing = false;

                if (!nothing) {
                    String checkeditems = "";
                    for (int i = 0; i < size; i++)
                        if (!checkedlist.get(i)) {
                            checkeditems += list.get(i) + ","; //沒有要刪除
                            list2.add(list.get(i));
                        }
                    new FridgeData(MyFridge.this, listview).execute("delete", checkeditems, email);
                    //Toast.makeText(getContext(), checkeditems, Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkeditems = "";
                for ( int i = 0; i < checkedlist.size(); i++ )
                    if (checkedlist.get(i))
                        checkeditems += list.get(i) + ","; //要搜尋的

                new FridgeData(MyFridge.this, listview).execute("search", checkeditems);
                //Toast.makeText(getContext(), checkeditems, Toast.LENGTH_SHORT).show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            //FragmentManager manager = getSupportFragmentManager();
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), FridgeInsert.class);
                startActivity(intent);
            }
        });

        return v;
    }


    class FridgeData extends AsyncTask<String, String, String> {
        private MyFridge context;
        private ListView listview;
        String task;

        public FridgeData(MyFridge context, ListView listview) {
            this.context = context;
            this.listview = listview;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            task = (String) arg0[0];
            if (task.equals("get")) {
                try {
                    String username = (String) arg0[1];
                    //String password = (String) arg0[1];

                    String ip = "http://140.135.113.99";
                    String link = ip + "/GetFridgeDetail.php";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(username, "UTF-8");

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

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            } else if (task.equals("delete")) {
                String checkeditems = (String) arg0[1];
                try {
                    String username = (String) arg0[2];
                    String ip = "http://140.135.113.99";
                    String link = ip + "/DeleteFridgeItem.php";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" +
                            URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("checkeditems", "UTF-8") + "=" +
                            URLEncoder.encode(checkeditems, "UTF-8");

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

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }else if (task.equals("search")) {
                String checkeditems = (String) arg0[1];
                try {
                    return checkeditems;
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
            if (task.equals("get")) {
                try {
                    //ArrayList<String> ingredient = new ArrayList<String>();
                    JSONArray reci = new JSONArray(result);
                    for (int i = 0; i < reci.length(); i++) {
                        JSONObject c = reci.getJSONObject(i);
                        String item2 = c.getString("fridge_ingredient");
                        String[] temp = null;
                        temp = item2.split(",");
                        for (int j = 0; j < temp.length; ++j) {
                            list.add(temp[j]);
                            checkedlist.add(false);
                        }
                        //ingredient.add(item);
                    }

                } catch (final JSONException e) {
                }
                // item.setText(item2);
                adapter = new MyFridgeAdapter(getActivity(), list);
                listview.setAdapter(adapter);
            } else if (task.equals("delete")) {
                adapter.clear();
                adapter.addAll(list2);
                adapter.notifyDataSetChanged();
                list = list2;
            }else if (task.equals("search")) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
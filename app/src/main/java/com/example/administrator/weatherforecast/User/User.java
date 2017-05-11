package com.example.administrator.weatherforecast.User;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.administrator.weatherforecast.MainActivity;
import com.example.administrator.weatherforecast.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/8.
 */

public class User extends ListActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private String username="";
    private String password="";
    private EditText userEdit;
    private EditText passEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.d).setOnClickListener(this);
        userEdit=(EditText) findViewById(R.id.user_name);
        passEdit=(EditText) findViewById(R.id.user_pass);
//        findViewById(R.id.get2).setOnClickListener(this);
//        findViewById(R.id.get3).setOnClickListener(this);
//        findViewById(R.id.insert).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.d:
                username=userEdit.getText().toString();
                password=passEdit.getText().toString();

                break;
        }


    }



    private ArrayList<HashMap<String, Object>> jsonParseString(String jsonString){

        ArrayList<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();



        try {

            JSONObject jsonObject=new JSONObject(jsonString);
            JSONObject dataObject=jsonObject.getJSONObject("data");
            int totalNum=dataObject.getInt("totalnum");
            Log.d(TAG, "totalNum:"+totalNum);
            if(totalNum>0){

                JSONArray userArray=dataObject.getJSONArray("_id");
                Log.d(TAG, "totalNum:"+userArray.length());
                for(int i=0;i<userArray.length();i++){

                    JSONObject userObject=(JSONObject) userArray.opt(i);
                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("_id",userObject.getInt("_id"));
                    hashMap.put("name",userObject.getString("username"));
                    hashMap.put("password",userObject.getString("userpassword"));
                    list.add(hashMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }




    private void fetchJsonObjectByGet(){
        String url="http://10.0.2.2:8080/JsonServer/user?method=3&username="+username+"&password="+password;

        JsonObjectRequest request=
                new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, "correct resp: "+jsonObject.toString());
//                        setListAdapter(new SimpleAdapter(User.this,parseJsonObject(jsonObject),R.layout.list_item,
//                                new String[]{"id","name","password"},
//                                new int[]{R.id.user_id,R.id.user_name,R.id.user_password}));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "volley error: "+volleyError.toString());
                    }
                });
        request.setTag("myGet");
        MyApplication.getRequestQueue().add(request);

    }

    private ArrayList<HashMap<String, Object>> parseJsonObject(JSONObject jsonObject){

        ArrayList<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();



        try {


            JSONObject dataObject=jsonObject.getJSONObject("data");
            int totalNum=dataObject.getInt("totalNum");
            Log.d(TAG, "totalNum:"+totalNum);
            if(totalNum>0){

                JSONArray userArray=dataObject.getJSONArray("info");
                Log.d(TAG, "totalNum:"+userArray.length());
                for(int i=0;i<userArray.length();i++){


                    JSONObject userObject=(JSONObject) userArray.opt(i);
                    HashMap<String, Object> hashMap=new HashMap<>();

                    hashMap.put("user_id",userObject.getInt("user_id"));
                    hashMap.put("user_name",userObject.getString("user_name"));
                    hashMap.put("user_password",userObject.getString("user_password"));


                    list.add(hashMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


}

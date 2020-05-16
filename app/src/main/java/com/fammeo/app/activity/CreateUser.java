package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.adapter.UserSuggestion;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.util.CustomAuthRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.fammeo.app.constants.Constants.METHOD_SEARCH_CREATE_USER;
import static com.fammeo.app.constants.Constants.METHOD_SEARCH_GET_USER;
import static com.fammeo.app.constants.Constants.METHOD_SEARCH_NEW_CREATE_USER;

public class CreateUser extends AppCompatActivity {

    private static final String TAG = CreateUser.class.getSimpleName();
    public CustomAuthRequest request;
    private Context mContext;
    ArrayList<String> suglist = new ArrayList<String>();
    RecyclerView recycler_view;
    boolean flags = true;
    String getName,getEmail,getUrl,FN,LN;
    EditText edt_search;
    Button btn_createNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Log.e("TEST","Create User :"+ App.getInstance().getId());
        recycler_view = findViewById(R.id.recycler_view);
        edt_search = findViewById(R.id.edt_search);
        btn_createNext = findViewById(R.id.btn_createNext);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);

        Log.e("TEST","Last Name :"+App.getInstance().getFacebookId());
        //Log.e("TEST","Last Name :"+App.getInstance().getFullname());

        Intent intent = getIntent();
        getName = intent.getStringExtra("N");
        getEmail = intent.getStringExtra("E");
        getUrl = intent.getStringExtra("url");
        FN = intent.getStringExtra("FN");
        LN = intent.getStringExtra("LN");
        Log.e("TEST","Name :"+getName);
        Log.e("TEST","Name :"+getEmail);
        Log.e("TEST","Name :"+getUrl);
        Log.e("TEST","Name :"+FN);
        Log.e("TEST","Name :"+LN);
        ((TextView) findViewById(R.id.txt_create_name)).setText(getName);
        ((TextView) findViewById(R.id.txt_create_email)).setText(getEmail);

        if(flags != false){
            ((EditText) findViewById(R.id.edt_search)).addTextChangedListener(new TextWatcher() {
                long lastChange=0;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() <= 4){
                        Toast.makeText(CreateUser.this,"UserName Length must be greater than 4 and lesser than 31 characters",Toast.LENGTH_LONG).show();
                        ((ImageButton) findViewById(R.id.bt_false)).setVisibility(View.VISIBLE);
                        ((ImageButton) findViewById(R.id.bt_true)).setVisibility(View.GONE);
                    }
                    if(s.length() >= 5){
                        final String getText = s.toString();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (System.currentTimeMillis() - lastChange >= 300) {
                                    //send request
                                    CreateUserSearch(getText);
                                }
                            }
                        }, 300);
                        lastChange = System.currentTimeMillis();
                    }
                    else if(s.length() == 0){
                        // getcompanies(false, 0, CurrentSearctText);

                    }
                }
            });
        }

        btn_createNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUname = edt_search.getText().toString();
                Log.e("TEST","Get User NAme :"+getUname);
                createUser(getUname);
            }
        });
    }

    private void createUser(final String uname) {

            //pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Call Search User");
            ((TextView) findViewById(R.id.message)).setVisibility(View.GONE);
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_SEARCH_NEW_CREATE_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {

                                String strResponse = response.toString();
                                Log.e("TEST","Create Response :"+response.toString());

                                if(strResponse != null){
                                    Log.e("TEST","Response Not null");
                                    JSONObject object = new JSONObject();
                                    try {
                                        String getMsg = object.getString("MessageType");
                                        if(getMsg.equalsIgnoreCase("success")){
                                            Log.e("TEST","You have joined Successfully");
                                        }
                                        JSONObject userObj = object.getJSONObject("obj");
                                        String GId = userObj.getString("GId");
                                        String Id = userObj.getString("Id");
                                        String FN = userObj.getString("FN");
                                        String S = userObj.getString("S");
                                        String I = userObj.getString("I");
                                        String UserName = userObj.getString("UN");

                                        Log.e("TEST","Gid :"+GId);
                                        Log.e("TEST","Id :"+Id);
                                        Log.e("TEST","FN :"+FN);
                                        Log.e("TEST","S :"+S);
                                        Log.e("TEST","I :"+I);
                                        Log.e("TEST","UserName :"+UserName);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                }else {
//                                pbHeaderProgress.setVisibility(View.VISIBLE);
                                    //SnakebarCustom.danger(mContext, View , "Unable to fetch contact: " + "No data found", 5000);
                                }
                            }

                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //pbHeaderProgress.setVisibility(View.VISIBLE);
                    //SnakebarCustom.danger(mContext, v, "Unable to fetch Companies: " + error.getMessage(), 5000);
                }
            }) {

                @Override
                protected JSONObject getParams() {
                    try {
                        JSONObject params = new JSONObject();
                        JSONObject objUser =new JSONObject();
                        objUser.put("FN",FN);
                        objUser.put("LN",LN);
                        objUser.put("S","male");
                        objUser.put("UN",uname);
                        params.put("user",objUser);

                        return params;
                    } catch (JSONException ex) {
                        DataGlobal.SaveLog(TAG, ex);
                        return null;
                    }
                }

                @Override
                protected void onCreateFinished(CustomAuthRequest request) {
                    int socketTimeout = 300000;//0 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    request.customRequest.setRetryPolicy(policy);
                    App.getInstance().addToRequestQueue(request);
                }
            };
    }

    private void CreateUserSearch(final String getText) {
            //pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Call Search User");
        ((TextView) findViewById(R.id.message)).setVisibility(View.GONE);
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_SEARCH_CREATE_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {

                                String strResponse = response.toString();
                                Log.e("TEST","Create User Search :"+response.toString());
                                if(strResponse != null){
                                    try {
                                        //contactList.clear();

                                        //pbHeaderProgress.setVisibility(View.GONE);
                                        JSONObject obj = new JSONObject(response.toString());
                                        Object intervention = obj.get("obj");
                                        JSONArray jsonArray=null;
                                        if (intervention instanceof JSONArray)
                                         jsonArray = obj.getJSONArray("obj");
                                        Log.e("TEST","Sugession jsonArray :"+jsonArray);
                                        String msg = obj.getString("Message");
                                        Log.e("TEST","MESSAGE :"+msg);

                                        Toast.makeText(CreateUser.this,msg,Toast.LENGTH_LONG).show();

                                        if(msg.equalsIgnoreCase("Username already Registered")){
                                            ((ImageButton) findViewById(R.id.bt_false)).setVisibility(View.VISIBLE);
                                            ((ImageButton) findViewById(R.id.bt_true)).setVisibility(View.GONE);
                                        }else if(msg.equalsIgnoreCase("Available")){
                                            ((ImageButton) findViewById(R.id.bt_false)).setVisibility(View.GONE);
                                            ((ImageButton) findViewById(R.id.bt_true)).setVisibility(View.VISIBLE);
                                        }

                                        if(jsonArray != null){
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                suglist.add(jsonArray.getString(i));
                                            }
                                            /*UserSuggestion adaper = new UserSuggestion(CreateUser.this,suglist);
                                            recycler_view.setAdapter(adaper);*/
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("error",e.getMessage());
                                    }
                                }else {
//                                pbHeaderProgress.setVisibility(View.VISIBLE);
                                    //SnakebarCustom.danger(mContext, View , "Unable to fetch contact: " + "No data found", 5000);
                                }
                            }
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //pbHeaderProgress.setVisibility(View.VISIBLE);
                    //SnakebarCustom.danger(mContext, v, "Unable to fetch Companies: " + error.getMessage(), 5000);
                }
            }) {

                @Override
                protected JSONObject getParams() {
                    try {
                        JSONObject params = new JSONObject();
                        Log.e("TEST","Param :"+getText);
                        params.put("text", getText.trim());
                        JSONObject filterExpression = new JSONObject();

                        return params;
                    } catch (JSONException ex) {
                        DataGlobal.SaveLog(TAG, ex);
                        return null;
                    }
                }

                @Override
                protected void onCreateFinished(CustomAuthRequest request) {
                    int socketTimeout = 300000;//0 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    request.customRequest.setRetryPolicy(policy);
                    App.getInstance().addToRequestQueue(request);
                }
            };
        }

    public void  getSugName(String sugName, boolean flag){
        Log.e("Test","Get Name :"+sugName);
        ((EditText) findViewById(R.id.edt_search)).setText(sugName);
        flags = flag;
    }

}

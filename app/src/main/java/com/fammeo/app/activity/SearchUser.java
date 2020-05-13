package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.SnakebarCustom;
import com.fammeo.app.fragment.Fammeo_Search;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.fammeo.app.constants.Constants.METHOD_SEARCH_GET_USER;

public class SearchUser extends AppCompatActivity {

    private static final String TAG = SearchUser.class.getSimpleName();
    public CustomAuthRequest request;
    private Context mContext;
    EditText edt_name;
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        edt_name = findViewById(R.id.edt_name);
        btn_next = findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateUser.class);
                intent.putExtra("N",edt_name.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void searchUser(final String getText) {

        //pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST","Call Search User");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_SEARCH_GET_USER, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {

                            String strResponse = response.toString();
                            Log.e("TEST","Fammeo Search Response :"+response.toString());
                            if(strResponse != null){
                                try {
                                    //contactList.clear();

                                    //pbHeaderProgress.setVisibility(View.GONE);
                                    JSONObject obj = new JSONObject(response.toString());
                                    JSONArray jsonArray = obj.getJSONArray("obj");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        // CompanyRelationJ cd = new CompanyRelationJ();
                                        JSONObject userDetail = jsonArray.getJSONObject(i);
                                        Log.e("TEST","GEt Company Id :"+userDetail.getString("Id"));
                                        //cd.Name = userDetail.getString("FN");
                                        //cd.UId = userDetail.getString("Id");
                                        // Log.e("TEST","Company Name :"+cd.Name);
                                        //list.add(cd);
                                    }

                                    //list.clear();
                                    //mSearchResultsAdapter.notifyDataSetChanged();
                                    //CompanyListAdapter
									/*GetCompanyAdapter adapter = new GetCompanyAdapter(CompanyFragment.this,getContext(),tempContactList);
									mSearchResultsList.setAdapter(adapter);*/

                                    //adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                    params.put("PageIndex", 1);
                    params.put("PageSize", 10);
                    params.put("text", getText.trim());
                    JSONObject filterExpression = new JSONObject();
                        /*try {
                            filterExpression.put("Status", "All");

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*/
                    //params.put("filterExpression", filterExpression);

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
}

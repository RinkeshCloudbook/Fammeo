package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.adapter.fammeoAdapter.LanguageAdapter;
import com.fammeo.app.adapter.fammeoAdapter.LanguageListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.fammeo.app.constants.Constants.METHOD_GET_PUBLIC_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_LANGUAGE_USER;

public class EditLanguage extends AppCompatActivity {
    private static final String TAG = EditLanguage.class.getSimpleName();
    EditText edt_lang;
    RecyclerView recycler_view,recycler_view_lang_dbox;
    public CustomAuthRequest request;
    Button bt_save;
    String userId;
    ArrayList<CommonModel> lanList;
    ArrayList<CommonModel> searchLang = new ArrayList<>();
    LanguageListAdapter listAdapter, listAdapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_language);

        userId = App.getInstance().getUserId();

        edt_lang = findViewById(R.id.edt_lang);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view_lang_dbox = findViewById(R.id.recycler_view_lang_dbox);
        bt_save = findViewById(R.id.bt_save);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler_view_lang_dbox.setLayoutManager(gridLayoutManager);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(EditLanguage.this, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(recyce);

        lanList  = (ArrayList<CommonModel>) getIntent().getSerializableExtra("list");
        /*Log.e("TEST","List Lenth :"+langList.size());
        for (int i = 0; i < langList.size(); i++) {

        }*/
        listAdapter1 = new LanguageListAdapter(EditLanguage.this, lanList, true);
        recycler_view_lang_dbox.setAdapter(listAdapter1);

        edt_lang.addTextChangedListener(new TextWatcher() {
            long lastChange = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    final String lagText = s.toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seachLanguage(lagText);
                            recycler_view.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    lastChange = System.currentTimeMillis();
                    Log.e("TEST","Search Lenth :"+s.length());
                } else if (s.length() == 0) {
                    recycler_view.setVisibility(View.GONE);
                }
            }
        });

        ((Button) findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSaveLanguage(lanList);
            }
        });
        ((ImageButton) findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSaveLanguage(final ArrayList<CommonModel> allLangList) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_LANGUAGE_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Save Language Response :" + response.toString());
                                if (strResponse != null) {
                                    lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")) ;
                                        toastIconSuccess("Language");
                                        Intent intent = new Intent(getApplicationContext(),SettingEdit.class);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
//                                pbHeaderProgress.setVisibility(View.VISIBLE);
                                //SnakebarCustom.danger(mContext, View , "Unable to fetch contact: " + "No data found", 5000);
                            }
                        }

                    }, new Response.ErrorListener() {
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
                        JSONArray jsonArray = new JSONArray();

                       /* for (int i = 0; i < getemailList.size(); i++) {
                            JSONObject email2Object=new JSONObject();

                            String tmpId = "null";
                            if (getemailList.get(i).recordId != null)
                                tmpId = getemailList.get(i).recordId;

                            email2Object.put("E", getemailList.get(i).emailAddress);
                            email2Object.put("Id",getemailList.get(i).recordId);
                            email2Object.put("T", getemailList.get(i).emailType);
                            jsonArray.put(email2Object);
                        }
                        */
                        //allLangList.add(lanList);

                        for (int i = 0; i < allLangList.size(); i++) {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", null);
                            jsonObject.put("N", allLangList.get(i).lName);
                            jsonArray.put(jsonObject);
                        }
                        params.put("languages", jsonArray);
                        params.put("UserId", userId);

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

    private void seachLanguage(final String lagText) {
        // pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Search User Text :" + lagText);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_PUBLIC_LANGUAGE_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Search Response :" + response.toString());
                            if (strResponse != null) {
                                //lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        bt_save.setVisibility(View.VISIBLE);
                                        // bt_true.setVisibility(View.VISIBLE);
                                    }
//                                        JSONObject obj = object.getJSONObject("obj");
                                    JSONArray arr = object.getJSONArray("obj");
                                    for (int i = 0; i < arr.length(); i++) {
                                        CommonModel cm = new CommonModel();
                                        JSONObject ob = arr.getJSONObject(i);
                                        cm.lId = ob.getString("PId");
                                        cm.lName = ob.getString("N");
                                        cm.languageId = ob.getString("Id");

                                        searchLang.add(cm);
                                    }
                                    LanguageAdapter adaper = new LanguageAdapter(EditLanguage.this, searchLang);
                                    recycler_view.setAdapter(adaper);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
//                                pbHeaderProgress.setVisibility(View.VISIBLE);
                            //SnakebarCustom.danger(mContext, View , "Unable to fetch contact: " + "No data found", 5000);
                        }
                    }

                }, new Response.ErrorListener() {
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
                    params.put("text", lagText);
                    params.put("UserId", userId);

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

    public void getLanName(String lan) {
        CommonModel cm = new CommonModel();
        cm.lName = lan;
        lanList.add(cm);

        //listAdapter1.notifyDataSetChanged();
        recycler_view.setVisibility(View.GONE);
        edt_lang.setText("");
    }
    public void enableSaveBottn(boolean flage) {
        if (flage == true) {
            bt_save.setVisibility(View.VISIBLE);
        }
    }
    private void toastIconSuccess(String msgText) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);

            ((TextView) custom_view.findViewById(R.id.message)).setText("Language(s) Saved Successfully!");

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }
}

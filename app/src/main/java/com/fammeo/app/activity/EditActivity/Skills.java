package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.fammeo.app.adapter.HobbyAdapter;
import com.fammeo.app.adapter.fammeoAdapter.SkillAdapter;
import com.fammeo.app.adapter.fammeoAdapter.SkillsListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_HOBBY_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SEARCH_HOBBIES_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SKILL_SAVE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SKILL_USER;

public class Skills extends AppCompatActivity {
    private static final String TAG = Skills.class.getSimpleName();
    public CustomAuthRequest request;
    EditText edt_skills;
    RecyclerView recycler_view_skills,recycler_view_skills_dbox;
    ArrayList<CommonModel> skillLang = new ArrayList<>();
    ArrayList<CommonModel> skillList = new ArrayList<>();
    AppCompatButton bt_save_skill;
    String userId;
    SkillsListAdapter lisyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        userId = App.getInstance().getUserId();
        edt_skills = findViewById(R.id.edt_skills);
        recycler_view_skills = findViewById(R.id.recycler_view_skills);
        recycler_view_skills_dbox = findViewById(R.id.recycler_view_skills_dbox);
        bt_save_skill = findViewById(R.id.bt_save_skill);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler_view_skills_dbox.setLayoutManager(gridLayoutManager);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(Skills.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_skills.setLayoutManager(recyce);

        skillList = (ArrayList<CommonModel>) getIntent().getSerializableExtra("Slist");

        lisyAdapter = new SkillsListAdapter(Skills.this, skillList);
        recycler_view_skills_dbox.setAdapter(lisyAdapter);

        edt_skills.addTextChangedListener(new TextWatcher() {
            long lastChange = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() >= 3) {
                    ((ImageButton) findViewById(R.id.btn_add)).setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seachSkills(s.toString());
                            recycler_view_skills.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    lastChange = System.currentTimeMillis();
                } else if (s.length() == 0) {
                    recycler_view_skills.setVisibility(View.GONE);
                }
            }
        });

        bt_save_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSaveSkills(skillList);
            }
        });
        ((ImageButton) findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((ImageButton) findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getText =edt_skills.getText().toString();
                CommonModel cm = new CommonModel();
                cm.lName = getText;

                skillList.add(cm);
                lisyAdapter.notifyDataSetChanged();
            }
        });
    }

    private void seachSkills(final String lagText) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST", "Search User Text :" + lagText);
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SKILL_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Search Skills Response :" + response.toString());
                                if (strResponse != null) {
                                    //lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")) {
                                            bt_save_skill.setVisibility(View.VISIBLE);
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

                                            skillLang.add(cm);
                                        }
                                        SkillAdapter adapter = new SkillAdapter(Skills.this, skillLang);
                                        recycler_view_skills.setAdapter(adapter);

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

    private void getSaveSkills(final ArrayList<CommonModel> hobbyList) {
        // pbHeaderProgress.setVisibility(View.VISIBLE);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SKILL_SAVE_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Save Language Response :" + response.toString());
                            if (strResponse != null) {
                                //lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) ;
                                    toastIconSuccess();
                                    Intent intent = new Intent(getApplicationContext(), SettingEdit.class);
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

                    for (int i = 0; i < hobbyList.size(); i++) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", null);
                        jsonObject.put("N", hobbyList.get(i).lName);
                        jsonArray.put(jsonObject);
                    }
                    params.put("skills", jsonArray);
                    params.put("UserId", userId);
                Log.e("TEST","Skills Param :"+params);
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

        skillList.add(cm);

        //listAdapter1.notifyDataSetChanged();
        recycler_view_skills.setVisibility(View.GONE);
        edt_skills.setText("");
    }

    private void toastIconSuccess() {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);

        ((TextView) custom_view.findViewById(R.id.message)).setText("Skills(s) Saved Successfully!");

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }
}

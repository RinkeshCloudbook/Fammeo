package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.R;
import com.fammeo.app.adapter.fammeoAdapter.EmailListAdapter;
import com.fammeo.app.adapter.fammeoAdapter.LanguageAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.AlertDailogBox;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.model.EmailModel;
import com.fammeo.app.model.EmailType;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.view.siv.CircularImageView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_GET_PUBLIC_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVEMAIL_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_USERDATA_USER;

public class SettingEdit extends AppCompatActivity {
    private static final String TAG = SettingEdit.class.getSimpleName();
    SharedPreferences sp;
    public CustomAuthRequest request;
    private String userId;
    ArrayList<CommonModel> lanList = new ArrayList<>();
    RecyclerView recycler_view,recycler_view_email;
    Button bt_save;
    ImageButton bt_true;
    EditText edt_lang,edt_emailtwo;
    List<String> allLangList = new ArrayList<>();
    ChipGroup tag_group;
    LinearLayout pbHeaderProgress;
    String pe,peType,valid_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);

        recycler_view_email = findViewById(R.id.recycler_view_email);
        sp = getSharedPreferences("uId", MODE_PRIVATE);
        userId = sp.getString("u","");
        String un = sp.getString("un","");
        Log.e("TEST","SP Get User Id :"+userId);
        if(userId.equals("") || userId.length() > 0){
            userId = App.getInstance().getUserId();
            Log.e("TEST","App User Id :"+userId);
        }

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
        recycler_view_email.setLayoutManager(recyce);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((ImageButton) findViewById(R.id.imgbt_lang)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.e("TEST","Click Language Edit");
                AlertDailogBox dailogBox = new AlertDailogBox(getApplicationContext(),"Edit Language");
                dailogBox.show();*/
                showCustomDialog();
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((ImageButton) findViewById(R.id.imgbtn_email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailCustomDialog();
            }
        });
        getUserData();
    }

    private void showEmailCustomDialog() {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.email_dialog_event);
            dialog.setCancelable(true);

            final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            final EditText edt_email = dialog.findViewById(R.id.edt_email);
            final EditText edt_emailType = dialog.findViewById(R.id.edt_emailType);
            edt_emailtwo = dialog.findViewById(R.id.edt_emailtwo);
            final EditText edt_emailTypetwo = dialog.findViewById(R.id.edt_emailTypetwo);
           bt_save = dialog.findViewById(R.id.bt_save);

            edt_email.setText(pe);
            edt_emailType.setText(peType);
            edt_email.setFocusable(false);
            edt_email.setClickable(false);

            edt_emailtwo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Is_Valid_Email(edt_emailtwo);
                }
            });
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            ((TextView) dialog.findViewById(R.id.txt_addNew)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayout) dialog.findViewById(R.id.lin_emailBox)).setVisibility(View.VISIBLE);
                }
            });
            ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

           bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edt_emailtwo.getText().toString().isEmpty()) {
                        edt_emailtwo.requestFocus();
                        edt_emailtwo.setError("Enter Email!");
                    }else {
                        if (edt_emailtwo.getText().toString().trim().matches(emailPattern)) {
                            addEmail(edt_email.getText(),edt_emailType.getText(),edt_emailtwo.getText(),edt_emailTypetwo.getText());
                            dialog.dismiss();
                        } else {
                            edt_emailtwo.requestFocus();
                            edt_emailtwo.setError("Invalid email address!");
                        }
                    }
                }
            });
            edt_emailTypetwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEmailTupe(v);
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
    }

    private void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
            bt_save.setVisibility(View.VISIBLE);
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    private void addEmail(final Editable EdtOne, final Editable EdtTypeOne, final Editable EdtTwo, final Editable EdtTypeTwo) {
           // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Save Email Data");
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVEMAIL_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST","Save Email Response :"+response.toString());
                                if(strResponse != null) {
                                    pbHeaderProgress.setVisibility(View.GONE);
                                    lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if(msgType.equalsIgnoreCase("success"));
                                        toastIconSuccess("email");
                                        JSONObject obj = object.getJSONObject("obj");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }else {
                                //  pbHeaderProgress.setVisibility(View.VISIBLE);
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
                        JSONArray jsonArray=new JSONArray();

                        JSONObject email1Object=new JSONObject();
                        email1Object.put("T",EdtTypeOne);
                        email1Object.put("E",EdtOne);

                        JSONObject email2Object=new JSONObject();
                        email2Object.put("T",EdtTypeTwo);
                        email2Object.put("E",EdtTwo);

                        jsonArray.put(email1Object);
                        jsonArray.put(email2Object);
                        params.put("emails",jsonArray);
                        params.put("UserId",userId);

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

    private void showEmailTupe(final View v) {
        final String[] emailType = new String[]{
            "Mobile","Office","Home","Main","Work Fax","Home Fax","other"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(emailType, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(emailType[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.laguage_dialog_event);
        dialog.setCancelable(true);
        recycler_view = dialog.findViewById(R.id.recycler_view);
        bt_save = dialog.findViewById(R.id.bt_save);
        bt_true = dialog.findViewById(R.id.bt_true);
        edt_lang = dialog.findViewById(R.id.edt_lang);
        tag_group = dialog.findViewById(R.id.tag_group);


        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        edt_lang.addTextChangedListener(new TextWatcher() {
            long lastChange=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 3){
                    final String lagText = s.toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seachLanguage(lagText);
                            recycler_view.setVisibility(View.VISIBLE);
                        }
                    },300);
                    lastChange = System.currentTimeMillis();
                }else if(s.length() == 0){
                    recycler_view.setVisibility(View.GONE);
                }
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","List :"+allLangList);
                //aaa
                for (int i = 0; i < allLangList.size(); i++) {
                    Log.e("TEST","SAVE :"+allLangList.get(i).toString());
                }

                getSaveLanguage(allLangList);
                dialog.dismiss();
            }
        });
        bt_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Add Language");
                String getText = edt_lang.getText().toString();
                if(getText.length() > 0){
                    List<String> items = new ArrayList<>();
                    items.add(getText);
                    Log.e("TEST","Get Tag Name :"+edt_lang);
                    setTag(items);
                    edt_lang.setText("");
                }else {
                    edt_lang.setFocusable(true);
                }


                //getLanName();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void getSaveLanguage(final List<String> allLangList) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);

            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_LANGUAGE_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST","Save Language Response :"+response.toString());
                                if(strResponse != null) {
                                    lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if(msgType.equalsIgnoreCase("success"));
                                        toastIconSuccess("Language");

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
                        JSONArray jsonArray=new JSONArray();
                        for (int i = 0; i < allLangList.size(); i++) {
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("id",null);
                            jsonObject.put("N",allLangList.get(i));
                            jsonArray.put(jsonObject);
                        }
                        params.put("languages",jsonArray);
                        params.put("UserId",userId);

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

    private void toastIconSuccess(String msgText) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);
        if(msgText.equalsIgnoreCase("Language")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Language(s) Saved Successfully!");
        }else if(msgText.equalsIgnoreCase("email")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Email(s) Saved Successfully!");
        }

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

    private void setTag(final List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            final String tagName = items.get(i);
            final Chip chip = new Chip(SettingEdit.this);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
            chip.setCloseIconResource(R.drawable.ic_close);
            chip.setCloseIconEnabled(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.remove(tagName);
                    tag_group.removeView(chip);
                }
            });
            Log.e("TEST","Get Lang Name :"+tag_group);
            allLangList.add(tagName);
            tag_group.addView(chip);
        }
    }

    private void seachLanguage(final String lagText) {
           // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Search User Text :"+lagText);
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_PUBLIC_LANGUAGE_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST","Search Response :"+response.toString());
                                if(strResponse != null) {
                                    lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if(msgType.equalsIgnoreCase("success"))
                                        {
                                            bt_save.setVisibility(View.VISIBLE);
                                            bt_true.setVisibility(View.VISIBLE);
                                        }
//                                        JSONObject obj = object.getJSONObject("obj");
                                        JSONArray arr = object.getJSONArray("obj");
                                        for (int i = 0; i < arr.length(); i++) {
                                            CommonModel cm= new CommonModel();
                                            JSONObject ob = arr.getJSONObject(i);
                                            cm.lId =ob.getString("PId");
                                            cm.lName = ob.getString("N");

                                            lanList.add(cm);
                                        }
                                        LanguageAdapter adaper = new LanguageAdapter(SettingEdit.this,lanList);
                                        recycler_view.setAdapter(adaper);

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
                        params.put("text",lagText);
                        params.put("UserId",userId);

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

    private void getUserData() {
         pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST","Get User Data");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_USERDATA_USER, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST","Get User Data Response :"+response.toString());
                            if(strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {

                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if(msgType.equalsIgnoreCase("success"));
                                    JSONObject obj = object.getJSONObject("obj");

                                    JSONArray arr = obj.getJSONArray("Ls");
                                    String userId = obj.getString("Id");
                                    String userURl = obj.getString("I");
                                    String un = obj.getString("UN");
                                    String fullName = obj.getString("FN")+" "+obj.getString("LN");

                                    ((TextView) findViewById(R.id.txt_name)).setText(fullName);
                                    ((TextView) findViewById(R.id.txt_uname)).setText("@"+un);
                                    Log.e("TEST","Image URL :"+App.getInstance().GetUrl());
                                    if(userURl != null){
                                        Glide.with(getApplicationContext()).load(DataText.GetImagePath(App.getInstance().GetUrl()))
                                                .thumbnail(0.5f)
                                                .transition(withCrossFade())
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(((CircularImageView) findViewById(R.id.search_image)));
                                    }else {
                                        String firstLater = fullName.substring(0,1).toUpperCase();
                                        Log.e("TEST","Get Image Url NULL:");
                                        ((CircularImageView) findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
                                        ((CircularImageView) findViewById(R.id.search_image)).setColorFilter(null);
                                        ((TextView) findViewById(R.id.search_image_text)).setText(firstLater);
                                    }

                                    ArrayList<EmailModel> emailList = new ArrayList<>();
                                    JSONArray arrEs = obj.getJSONArray("Es");
                                    for (int i = 0; i < arrEs.length(); i++) {
                                        JSONObject objEs = arrEs.getJSONObject(i);
                                        EmailModel em = new EmailModel();
                                        pe = objEs.getString("E");
                                        peType = objEs.getString("T");
                                        em.emailAddress = pe;
                                        em.emailType = peType;
                                        Log.e("TEST","Type :"+peType);
                                        Log.e("TEST","Email :"+pe);
                                        emailList.add(em);
                                    }
                                    EmailListAdapter emailadapter = new EmailListAdapter(getApplicationContext(),emailList);
                                    recycler_view_email.setAdapter(emailadapter);

                                    LinearLayout lLayout = (LinearLayout) findViewById(R.id.rel_lang);
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject arrObj = arr.getJSONObject(i);
                                        CommonModel cm = new CommonModel();
                                        cm.lId = arrObj.getString("Id");
                                        cm.lName = arrObj.getString("N");
                                        Log.e("TEST","Get user Name :"+cm.lName);
                                        Log.e("TEST","Get user Id :"+cm.lId);
                                        String getname = cm.lName;
                                        TextView rowTextView = new TextView(SettingEdit.this);
                                        rowTextView.setText(getname+i);
                                        rowTextView.setBackground(getApplicationContext().getDrawable(R.drawable.shape_rounded_orange));
                                        rowTextView.setPadding(10,10,10,10);

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(10,0,0,0);
                                        rowTextView.setLayoutParams(params);

                                        rowTextView.setTextColor(Color.parseColor("#f2f4f7"));
                                        lLayout.addView(rowTextView);
                                    }

                                    //rowTextView.setText(getname);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }else {
                             //  pbHeaderProgress.setVisibility(View.VISIBLE);
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
                    JSONArray jsonArray=new JSONArray();
                    for (int i = 0; i < 6; i++) {
                        //JSONObject jsonObject=new JSONObject();
                        //jsonObject.put("id",null);
                        //jsonObject.put("N",allLangList.get(i));
                        jsonArray.put("email");
                        jsonArray.put("phone");
                        jsonArray.put("address");
                        jsonArray.put("language");
                        jsonArray.put("hobby");
                        jsonArray.put("skill");
                        jsonArray.put("blob");
                    }
                    Log.e("TEST","Json Array :"+jsonArray);
                    params.put("scopes",jsonArray);
                    params.put("UserId",userId);

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

    public void getLanName(String lan){
        Log.e("TEST","Language Name :"+lan);
        List<String> items = new ArrayList<>();
        items.add(lan);
        setTag(items);
        recycler_view.setVisibility(View.GONE);
        edt_lang.setText("");
    }
}

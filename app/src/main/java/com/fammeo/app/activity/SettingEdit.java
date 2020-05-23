package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fammeo.app.adapter.AddressAdapter;
import com.fammeo.app.adapter.fammeoAdapter.AddressDailogeAdapter;
import com.fammeo.app.adapter.fammeoAdapter.CityAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailDailogeAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailListAdapter;
import com.fammeo.app.adapter.fammeoAdapter.LanguageAdapter;
import com.fammeo.app.adapter.fammeoAdapter.LanguageListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.model.EmailModel;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.view.siv.CircularImageView;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_GET_PUBLIC_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVEMAIL_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_ADDRESS_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SEARCHCITY_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_USERDATA_USER;

public class SettingEdit extends AppCompatActivity {
    private static final String TAG = SettingEdit.class.getSimpleName();
    SharedPreferences sp;
    public CustomAuthRequest request;
    private String userId;
    ArrayList<CommonModel> lanList = new ArrayList<>();
    ArrayList<CommonModel> mAddressList = new ArrayList<>();
    List<String> mCityList = new ArrayList<>();
    ArrayList<EmailModel> emailList;
    RecyclerView recycler_view,recycler_view_email,recycler_view_lang,
            recycler_add_type,recycler_view_lang_dbox,recycler_view_address,recycler_address_list;
    private Button bt_save,bt_save_address;
    ImageButton bt_true;
    EditText edt_lang,edt_city;
    List<String> allLangList = new ArrayList<String>();
    ChipGroup tag_group;
    LinearLayout pbHeaderProgress;
    String pe,peType,cityName,cityState,cityCountry;
    JSONArray arr;
    AddressDailogeAdapter addsDaiAdapter;
    AddressAdapter addsAdapter;
    LanguageListAdapter listAdapter,listAdapter1;
    ArrayList<String> lanListName = new ArrayList<>();
    List<CommonModel> profileLangList;
    private EmailListAdapter emailadapter = null;
    private EmailDailogeAdapter emailAdapterDialogList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);

        recycler_view_email = findViewById(R.id.recycler_view_email);
        recycler_view_address = findViewById(R.id.recycler_view_address);
        recycler_view_lang = findViewById(R.id.recycler_view_lang);
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
        RecyclerView.LayoutManager addsrecy = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
        recycler_view_address.setLayoutManager(addsrecy);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("View Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((ImageButton) findViewById(R.id.imgbt_lang)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
        ((ImageButton) findViewById(R.id.img_edt_address)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressCustomDialog();
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

    private void addressCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address_dialog_event);
        dialog.setCancelable(true);

        final EditText edt_addressType = dialog.findViewById(R.id.edt_addressType);
        final EditText edt_address = dialog.findViewById(R.id.edt_address);
        edt_city = dialog.findViewById(R.id.edt_city);
        bt_save_address = dialog.findViewById(R.id.bt_save_address);
        recycler_add_type = dialog.findViewById(R.id.recycler_add_type);
        recycler_address_list = dialog.findViewById(R.id.recycler_address_list);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
        recycler_address_list.setLayoutManager(recyce);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        edt_addressType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdrressType(v);
            }
        });

        //addsAdapter = new AddressAdapter(SettingEdit.this,cityList);
        //recycler_view_address.setAdapter(addsAdapter);

        //  OLD CODE
        addsDaiAdapter = new AddressDailogeAdapter(SettingEdit.this, mAddressList);
        recycler_address_list.setAdapter(addsDaiAdapter);

        /*edt_city.addTextChangedListener(new TextWatcher() {
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
                    final String cName = s.toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seachCity(cName);
                            recycler_add_type.setVisibility(View.VISIBLE);
                        }
                    },300);
                    lastChange = System.currentTimeMillis();
                }else if(s.length() == 0){
                    recycler_view.setVisibility(View.GONE);
                }
            }
        });*/

        ((TextView) dialog.findViewById(R.id.txt_addNew)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonModel cm = new CommonModel();

                cm.recordId = null;
                cm.cType = "Office";
                cm.cAddress = "";
                cm.cN = "";
                cm.cState = "";
                cm.cCountry = "";
                mAddressList.add(cm);
                addsDaiAdapter.notifyItemInserted(emailList.size()-1);
              // ((LinearLayout) dialog.findViewById(R.id.lin_emailBox)).setVisibility(View.VISIBLE);
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save_address)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saveAddress(cityName,cityCountry,edt_address.getText().toString(),cityState,edt_addressType.getText());
                saveAddress(mAddressList);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void saveAddress(final ArrayList<CommonModel> cityList) {
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ADDRESS_USER, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST","Save Address Response :"+response.toString());
                            if(strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if(msgType.equalsIgnoreCase("success"));
                                    toastIconSuccess("address");
                                    // getUserData();
                                    //  JSONObject obj = object.getJSONObject("obj");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("TEST","Get JSONException :"+e);
                                }
                            }
                        }else {

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


                    for (int i = 0; i < cityList.size(); i++) {
                        JSONObject addObj=new JSONObject();

                        String tmpId = "null";
                        if (cityList.get(i).recordId != null)
                            tmpId = cityList.get(i).recordId;

                            addObj.put("C", cityList.get(i).cN);
                            addObj.put("CR",cityList.get(i).cCountry);
                            addObj.put("Id", cityList.get(i).recordId);
                            addObj.put("L1", cityList.get(i).cAddress);
                            addObj.put("S", cityList.get(i).cState);
                            addObj.put("T", cityList.get(i).cType);
                            jsonArray.put(addObj);

                            params.put("addresses",jsonArray);
                            params.put("UserId",userId);
                    }
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

    private void saveAddresss(final String cityName, final String cityCountry, final String address, final String cityState, final Editable addType) {

            Log.e("TEST","Save Address :");
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ADDRESS_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST","Save Address Response :"+response.toString());
                                if(strResponse != null) {
                                    pbHeaderProgress.setVisibility(View.GONE);
                                    lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if(msgType.equalsIgnoreCase("success"));
                                        toastIconSuccess("address");
                                        // getUserData();
                                      //  JSONObject obj = object.getJSONObject("obj");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("TEST","Get JSONException :"+e);
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

                    JSONObject addObj=new JSONObject();

                    addObj.put("C", cityName);
                    addObj.put("CR",cityCountry);
                    addObj.put("Id", null);
                    addObj.put("L1", address);
                    addObj.put("S", cityState);
                    addObj.put("T", addType);
                    jsonArray.put(addObj);

                    params.put("addresses",jsonArray);
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

    private void seachCity(final String cName) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Search City :"+cName);
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SEARCHCITY_USER, null,0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST","Search City Response :"+response.toString());

                                RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
                                recycler_add_type.setLayoutManager(recyce);

                                if(strResponse != null) {
                                    //lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if(msgType.equalsIgnoreCase("success"))
                                        {
                                            bt_save_address.setVisibility(View.VISIBLE);
                                        }
//                                        JSONObject obj = object.getJSONObject("obj");
                                        JSONArray addOBJ = object.getJSONArray("obj");
                                        for (int i = 0; i < addOBJ.length(); i++) {
                                            CommonModel cm= new CommonModel();
                                            JSONObject ob = addOBJ.getJSONObject(i);
                                            cm.cN = ob.getString("N");
                                            cm.cState = ob.getString("S");
                                            cm.cCountry = ob.getString("CR");
                                            cm.cSC = ob.getString("SC");
                                            cm.cCRC = ob.getString("CRC");

                                            String name = cm.cN+", "+cm.cState + ", "+cm.cCountry;
                                            mCityList.add(name);

                                            mAddressList.add(cm);
                                        }

                                        //addsDaiAdapter = new AddressDailogeAdapter(SettingEdit.this, mAddressList);
                                        //recycler_address_list.setAdapter(addsDaiAdapter);

                                        //CityAdapter cityadaper = new CityAdapter(SettingEdit.this, mAddressList);
                                      //  recycler_add_type.setAdapter(cityadaper);

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
                        params.put("city",cName);
                        params.put("country","IN");

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

    public void showAdrressType(final View v) {
        final String[] addType = new String[]{
                "Home","Work","Other"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(addType, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(addType[i]);
               // emailList.get(i).emailType = emailType[i];
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showEmailCustomDialog() {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.email_dialog_event);
            dialog.setCancelable(true);

            recycler_view_email = dialog.findViewById(R.id.recycler_view_email);
            bt_save = dialog.findViewById(R.id.bt_save);

           RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
           recycler_view_email.setLayoutManager(recyce);

            emailAdapterDialogList = new EmailDailogeAdapter(SettingEdit.this,emailList);
            recycler_view_email.setAdapter(emailAdapterDialogList);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            ((TextView) dialog.findViewById(R.id.txt_addNew)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmailModel em = new EmailModel();

                    em.recordId = null;
                    em.emailAddress = "";
                    em.emailType = "Office";
                    emailList.add(em);
                    emailAdapterDialogList.notifyItemInserted(emailList.size()-1);
                    // ((LinearLayout) dialog.findViewById(R.id.lin_emailBox)).setVisibility(View.VISIBLE);
                }
            });
            ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    for (int i = 0; i < emailList.size(); i++) {
                       // Log.e("TEST", "Email = "+emailList.get(i).emailAddress + " | "+emailList.get(i).emailType);
                    }
                }
            });

           bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            addEmail(emailList);
                            dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
    }

    public void enableSaveBottn(boolean flage){
        if(flage == true){
            bt_save.setVisibility(View.VISIBLE);
        }
    }

    private void addEmail(final ArrayList<EmailModel> getemailList) {
           // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Save Email Data :");
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
                                       // getUserData();
                                        emailadapter.notifyDataSetChanged();
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

                      /*JSONObject email1Object=new JSONObject();
                        email1Object.put("T",EdtTypeOne);
                        email1Object.put("E",EdtOne);

                        JSONObject email2Object=new JSONObject();
                        email2Object.put("T",EdtTypeTwo);
                        email2Object.put("E",EdtTwo);*/

                        for (int i = 0; i < getemailList.size(); i++) {
                            JSONObject email2Object=new JSONObject();

                            String tmpId = "null";
                            if (getemailList.get(i).recordId != null)
                                tmpId = getemailList.get(i).recordId;

                            email2Object.put("E", getemailList.get(i).emailAddress);
                            email2Object.put("Id",getemailList.get(i).recordId);
                            email2Object.put("T", getemailList.get(i).emailType);
                            jsonArray.put(email2Object);
                        }

                        //jsonArray.put(email2Object);
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

    public void showEmailTupe(final View v, final int position) {
        final String[] emailType = new String[]{
            "Mobile","Office","Home","Main","Work Fax","Home Fax","other"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(emailType, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(emailType[i]);
                emailList.get(position).emailType = emailType[i];
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

        recycler_view_lang_dbox = dialog.findViewById(R.id.recycler_view_lang_dbox);
        bt_save = dialog.findViewById(R.id.bt_save);
        bt_true = dialog.findViewById(R.id.bt_true);
        edt_lang = dialog.findViewById(R.id.edt_lang);
        tag_group = dialog.findViewById(R.id.tag_group);
        LinearLayout linDailogLag =  dialog.findViewById(R.id.dailog_rel_lang);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        recycler_view_lang_dbox.setLayoutManager(gridLayoutManager);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        List<String> lang = new ArrayList<>();
        listAdapter1 = new LanguageListAdapter(SettingEdit.this,profileLangList,true);
        recycler_view_lang_dbox.setAdapter(listAdapter1);
       /* for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject arrObj = arr.getJSONObject(i);
                CommonModel cm = new CommonModel();
                cm.lId = arrObj.getString("Id");
                cm.lName = arrObj.getString("N");

                String getname = cm.lName;

                lang.add(getname);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setTag(lang);*/



        /*for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject arrObj = arr.getJSONObject(i);

                CommonModel cm = new CommonModel();
                cm.lId = arrObj.getString("Id");
                cm.lName = arrObj.getString("N");

                String getname = cm.lName;
                TextView rowTextView = new TextView(SettingEdit.this);
                rowTextView.setText(getname);

                allLangList.add(getname);
                rowTextView.setBackground(getApplicationContext().getDrawable(R.drawable.shape_rounded_orange));
                rowTextView.setPadding(25,15,25,15);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,0,0,0);
                rowTextView.setLayoutParams(params);

                rowTextView.setTextColor(Color.parseColor("#f2f4f7"));
                linDailogLag.addView(rowTextView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

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
                getSaveLanguage(profileLangList);
                dialog.dismiss();
            }
        });
        bt_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getText = edt_lang.getText().toString();
                if(getText.length() > 0){
                    List<String> items = new ArrayList<>();
                    items.add(getText);
                    //setTag(items);
                    edt_lang.setText("");
                }else {
                    edt_lang.setFocusable(true);
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void getSaveLanguage(final List<CommonModel> allLangList) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST","Language allLangList :"+allLangList.toString());
            Log.e("TEST","Language lanList :"+lanList.toString());
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

                                        getUserData();

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

                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put("id",null);
                            jsonObject.put("N",allLangList.get(i).lName);
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
        }else if(msgText.equalsIgnoreCase("address")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Address(s) Saved Successfully!");
        }

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

   /* private void setTag(final List<String> items) {
        Log.e("TEST","Get Name :"+items.toString());
        items.clear();
        for (int i = 0; i < items.size(); i++) {
            Log.e("TEST","Item Size :"+items.size());
            final String tagName = items.get(i);
            final Chip chip = new Chip(SettingEdit.this);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
           // chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setPadding(25,15,25,15);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(SettingEdit.this, R.color.appColore)));
            chip.setTextColor(Color.parseColor("#fafbfc"));
            //chip.setBackground(getApplicationContext().getDrawable(R.drawable.shape_rounded_orange));
            chip.setText(tagName);
            chip.setCloseIconResource(R.drawable.ic_close);
            chip.setCloseIconEnabled(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.remove(tagName);
                    tag_group.removeView(chip);
                    bt_save.setVisibility(View.VISIBLE);
                }
            });
            //allLangList.addAll(items);
            Log.e("TEST","Get Lang Name :"+tag_group);
            profileLangList.add(tagName);
            tag_group.addView(chip);
        }
    }*/

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
                                           // bt_true.setVisibility(View.VISIBLE);
                                        }
//                                        JSONObject obj = object.getJSONObject("obj");
                                        arr = object.getJSONArray("obj");
                                        for (int i = 0; i < arr.length(); i++) {
                                            CommonModel cm= new CommonModel();
                                            JSONObject ob = arr.getJSONObject(i);
                                            cm.lId =ob.getString("PId");
                                            cm.lName = ob.getString("N");
                                            cm.languageId = ob.getString("Id");

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
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                                recycler_view_lang.setLayoutManager(gridLayoutManager);
                                lanList.clear();
                                try {

                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                if(msgType.equalsIgnoreCase("success"));

                                        JSONObject obj = object.getJSONObject("obj");

                                        arr = obj.getJSONArray("Ls");
                                        String userId = obj.getString("Id");
                                        String userURl = obj.getString("I");
                                        String un = obj.getString("UN");
                                        String fullName = obj.getString("FN") + " " + obj.getString("LN");
                                        Log.e("TEST", "Full Name :" + fullName);
                                        ((TextView) findViewById(R.id.txt_name)).setText(fullName);
                                        ((TextView) findViewById(R.id.txt_uname)).setText("@" + un);
                                        Log.e("TEST", "Image URL :" + App.getInstance().GetUrl());
                                        if (userURl != null) {
                                            Glide.with(getApplicationContext()).load(DataText.GetImagePath(userURl))
                                                    .thumbnail(0.5f)
                                                    .transition(withCrossFade())
                                                    .apply(RequestOptions.circleCropTransform())
                                                    .into(((CircularImageView) findViewById(R.id.search_image)));
                                        } else {
                                            String firstLater = fullName.substring(0, 1).toUpperCase();
                                            Log.e("TEST", "Get Image Url NULL:");
                                            ((CircularImageView) findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
                                            ((CircularImageView) findViewById(R.id.search_image)).setColorFilter(null);
                                            ((TextView) findViewById(R.id.search_image_text)).setText(firstLater);
                                        }

                                        emailList = new ArrayList<>();
                                        JSONArray arrEs = obj.getJSONArray("Es");
                                        for (int i = 0; i < arrEs.length(); i++) {
                                            JSONObject objEs = arrEs.getJSONObject(i);
                                            EmailModel em = new EmailModel();
                                            pe = objEs.getString("E");
                                            peType = objEs.getString("T");
                                            String id = objEs.getString("Id");
                                            em.emailAddress = pe;
                                            em.emailType = peType;
                                            em.recordId = id;
                                            emailList.add(em);
                                        }
                                        emailadapter = new EmailListAdapter(getApplicationContext(), emailList);
                                        recycler_view_email.setAdapter(emailadapter);

                                        JSONArray arrAdds = obj.getJSONArray("Adds");
                                    for (int j = 0; j < arrAdds.length(); j++) {
                                        JSONObject addsObj = arrAdds.getJSONObject(j);
                                        String fullAddress = addsObj.getString("C")+addsObj.getString("S")+addsObj.getString("CR");
                                        CommonModel am= new CommonModel();

                                        am.cType = addsObj.getString("T");
                                        am.cAddress = addsObj.getString("L1");
                                        am.cN = addsObj.getString("C");
                                        am.cState = addsObj.getString("S");
                                        am.cCountry = addsObj.getString("CR");
                                        am.fullAddress = fullAddress;

                                        mAddressList.add(am);
                                    }
                                    addsAdapter = new AddressAdapter(SettingEdit.this, mAddressList);
                                    recycler_view_address.setAdapter(addsAdapter);


                                        profileLangList = new ArrayList<>();
                                        // LinearLayout lLayout = (LinearLayout) findViewById(R.id.rel_lang);
                                        // lLayout.removeAllViews();
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject arrObj = arr.getJSONObject(i);
                                            CommonModel cm = new CommonModel();
                                            cm.lId = arrObj.getString("Id");
                                            cm.lName = arrObj.getString("N");
                                            profileLangList.add(cm);
                                            String getname = cm.lName;
                                            // TextView rowTextView = new TextView(SettingEdit.this);
                                            //rowTextView.setText(getname);
                                            //  rowTextView.setBackground(getApplicationContext().getDrawable(R.drawable.shape_rounded_orange));
                                            //  rowTextView.setPadding(25,15,25,15);

                                            //   LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            //  params.setMargins(10,0,0,0);
                                            //   rowTextView.setLayoutParams(params);
//
                                            // rowTextView.setTextColor(Color.parseColor("#f2f4f7"));
                                            // lLayout.addView(rowTextView);
                                        }

                                        listAdapter = new LanguageListAdapter(SettingEdit.this, profileLangList, false);
                                        recycler_view_lang.setAdapter(listAdapter);

                                        //rowTextView.setText(getname);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.VISIBLE);
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
            CommonModel cm = new CommonModel();
            cm.lName = lan;
            profileLangList.add(cm);

        listAdapter1.notifyDataSetChanged();
        recycler_view.setVisibility(View.GONE);
        edt_lang.setText("");
    }

    public void setCityName(String cn, String cs, String cc, String city){
        cityName = cn;
        cityState = cs;
        cityCountry = cc;
        edt_city.setText(city);
        recycler_add_type.setVisibility(View.GONE);
    }
}

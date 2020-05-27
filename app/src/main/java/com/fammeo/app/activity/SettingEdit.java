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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import com.fammeo.app.activity.EditActivity.AboutDetails;
import com.fammeo.app.activity.EditActivity.EditAddress;
import com.fammeo.app.activity.EditActivity.EditHobbies;
import com.fammeo.app.activity.EditActivity.EditLanguage;
import com.fammeo.app.activity.EditActivity.EditPhone;
import com.fammeo.app.adapter.AddressAdapter;
import com.fammeo.app.adapter.HobbyAdapter;
import com.fammeo.app.adapter.LanuageSettingAdapter;
import com.fammeo.app.adapter.fammeoAdapter.AddressDailogeAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailDailogeAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailListAdapter;
import com.fammeo.app.adapter.fammeoAdapter.HobbyAdapterSetting;
import com.fammeo.app.adapter.fammeoAdapter.LanguageAdapter;
import com.fammeo.app.adapter.fammeoAdapter.LanguageListAdapter;
import com.fammeo.app.adapter.fammeoAdapter.PhoneAdapter;
import com.fammeo.app.adapter.fammeoAdapter.PhoneAdapterDialogList;
import com.fammeo.app.app.App;
import com.fammeo.app.common.CommomInterface;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_GET_PUBLIC_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVEMAIL_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_ABOUT_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_ADDRESS_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_LANGUAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_PHONE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SEARCHCITY_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_USERDATA_USER;

public class SettingEdit extends AppCompatActivity{
    private static final String TAG = SettingEdit.class.getSimpleName();
    SharedPreferences sp;
    public CustomAuthRequest request;
    private String userId;
    ArrayList<CommonModel> lanList = new ArrayList<>();
    ArrayList<CommonModel> mAddressList = new ArrayList<>();
    List<String> mCityList = new ArrayList<>();
    ArrayList<EmailModel> emailList;
    RecyclerView recycler_view, recycler_view_email, recycler_view_lang,
            recycler_add_type, recycler_view_lang_dbox, recycler_view_address, recycler_address_list, recycler_view_phone, recycler_view_dailouge_phone,recycler_view_hb;
    private Button bt_save, bt_save_address;
    ImageButton bt_true;
    private EditText edt_lang, edt_city, edt_cCode, edt_phone, edt_type;
    ChipGroup tag_group;
    LinearLayout pbHeaderProgress;
    String pe, peType;
    JSONArray arr;
    AddressDailogeAdapter addsDaiAdapter;
    AddressAdapter addsAdapter;
    LanguageListAdapter listAdapter, listAdapter1;
    PhoneAdapter phoneAdapter;
    ArrayList<CommonModel> phoneList = new ArrayList<>();
    List<CommonModel> profileLangList;
    List<CommonModel> hobbyList = new ArrayList<>();
    private EmailListAdapter emailadapter = null;
    private EmailDailogeAdapter emailAdapterDialogList = null;
    PhoneAdapterDialogList phoneAdapterDailouge;
    TextView txt_title,txt_dec,txt_link;
    int phoneLenght;
    String email_id,getLink,getDec,getTitle;
    boolean flage = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);

        recycler_view_email = findViewById(R.id.recycler_view_email);
        recycler_view_address = findViewById(R.id.recycler_view_address);
        recycler_view_lang = findViewById(R.id.recycler_view_lang);
        recycler_view_phone = findViewById(R.id.recycler_view_phone);
        txt_link = findViewById(R.id.txt_link);
        txt_title = findViewById(R.id.txt_title);
        txt_dec = findViewById(R.id.txt_dec);
        recycler_view_hb = findViewById(R.id.recycler_view_hb);

        Bundle getbundle = getIntent().getExtras();
        if(getbundle != null){
            getUserData();
        }

        sp = getSharedPreferences("uId", MODE_PRIVATE);
        userId = sp.getString("u", "");
        String un = sp.getString("un", "");

        if (userId.equals("") || userId.length() > 0) {
            userId = App.getInstance().getUserId();
        }

        setRecleyViewManager(recycler_view_email);
        setRecleyViewManager(recycler_view_address);
        setRecleyViewManager(recycler_view_phone);
        setRecleyViewManager(recycler_view_hb);
        /*RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_email.setLayoutManager(recyce);
        RecyclerView.LayoutManager addsrecy = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_address.setLayoutManager(addsrecy);
        RecyclerView.LayoutManager phonerecy = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_phone.setLayoutManager(phonerecy);*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler_view_hb.setLayoutManager(gridLayoutManager);


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
                //addressCustomDialog();
                /*Intent intent = new Intent(getApplicationContext(), EditAddress.class);
                startActivity(intent);*/
                addsAdapter.getShowImage(flage);
                if(flage == true){
                    flage = false;
                }else if(flage == false){
                    flage = true;
                }

            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((ImageButton) findViewById(R.id.imgbtn_email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showEmailCustomDialog();
                /*Intent intent = new Intent(getApplicationContext(),EditEmail.class);
                startActivity(intent);*/

                emailadapter.getShowImage(flage);
                if(flage == true){
                    flage = false;
                }else if(flage == false){
                    flage = true;
                }
            }
        });
        ((ImageButton) findViewById(R.id.img_btn_phone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showPhoneCustomDialog(phoneLenght);
                phoneAdapter.getShowImage(flage);
                if(flage == true){
                    flage = false;
                }else if(flage == false){
                    flage = true;
                }
            }
        });

        ((ImageButton) findViewById(R.id.img_btn_about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAboutCustomDialog();
                Intent intent = new Intent(getApplicationContext(), AboutDetails.class);

                intent.putExtra("T",getTitle);
                intent.putExtra("D",getDec);
                intent.putExtra("L",getLink);
                startActivity(intent);
            }
        });
        ((ImageButton) findViewById(R.id.imgbt_lang)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAboutCustomDialog();
                ArrayList<String> LangList = new ArrayList<String>();
                Intent intent = new Intent(getApplicationContext(), EditLanguage.class);
                intent.putExtra("list", (Serializable) profileLangList);
                startActivity(intent);
            }
        });

        ((ImageButton) findViewById(R.id.img_edit_hobbies)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Hlist :"+hobbyList.size());
                Intent intent = new Intent(getApplicationContext(), EditHobbies.class);
                intent.putExtra("Hlist", (Serializable) hobbyList);
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.txt_addNewAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditAddress.class);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.txt_addNewEmail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditEmail.class);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.txt_addNewPhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPhone.class);
                startActivity(intent);
            }
        });

        getUserData();
    }

    private void showAboutCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_dialog_event);
        dialog.setCancelable(true);

        final EditText edt_fname = dialog.findViewById(R.id.edt_fname);
        final EditText edt_lname = dialog.findViewById(R.id.edt_lname);
        final EditText edt_gender = dialog.findViewById(R.id.edt_gender);
        final EditText edt_title = dialog.findViewById(R.id.edt_title);
        final EditText edt_description = dialog.findViewById(R.id.edt_description);
        final EditText edt_youtube = dialog.findViewById(R.id.edt_youtube);
        Button bt_save_about = dialog.findViewById(R.id.bt_save_about);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);
        edt_fname.setText(App.getInstance().getFirstName());
        edt_lname.setText(App.getInstance().getLastName());
        if(getTitle.length()> 0)
        edt_title.setText(getTitle);
        if(getDec.length()> 0)
        edt_description.setText(getDec);
        if(getLink.length()> 0)
        edt_youtube.setText(getLink);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        bt_save_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAbouts(edt_fname.getText(), edt_lname.getText(), edt_gender.getText(), edt_title.getText(), edt_description.getText(), edt_youtube.getText());
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
private void setRecleyViewManager(RecyclerView recycler_view){
    RecyclerView.LayoutManager addsrecy = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
    recycler_view.setLayoutManager(addsrecy);
}
    private void saveAbouts(final Editable fname, final Editable lname, final Editable gender, final Editable title, final Editable dec, final Editable youlink) {

        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ABOUT_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Save About Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")){
                                        toastIconSuccess("about");
                                        getUserData();
                                    }

                                    // getUserData();
                                    //  JSONObject obj = object.getJSONObject("obj");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("TEST", "Get JSONException :" + e);
                                }
                            }
                        } else {

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
                JSONObject params = new JSONObject();
                try {
                    JSONObject antobj = new JSONObject();

                    antobj.put("FN", fname);
                    antobj.put("LN", lname);
                    antobj.put("S", gender);
                    antobj.put("Id", userId);

                    JSONArray array = new JSONArray();
                    JSONObject obj = new JSONObject();
                    JSONObject obj1 = new JSONObject();
                    JSONObject obj2 = new JSONObject();

                        obj.put("OT", "BioTitle");
                        obj.put("OV", title);

                        obj1.put("OT", "BioDescription");
                        obj1.put("OV", dec);

                        obj2.put("OT", "AboutVideo");
                        obj2.put("OV", youlink);

                    array.put(obj);
                    array.put(obj1);
                    array.put(obj2);
                    antobj.put("Bls", array);
                    params.put("user",antobj);
                    Log.e("TEST","About Param :"+params);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
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

    public void updateList(ArrayList<CommonModel> updatedAddressList) {
//        mAddressList.clear();
        mAddressList = updatedAddressList;

    }

    public void updatephonelist(ArrayList<CommonModel> updatedPhoneList) {
        Log.e("TEST", "Update Phone List");
//        mAddressList.clear();
        phoneList = updatedPhoneList;

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

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
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

        //  OLD CODE
        addsDaiAdapter = new AddressDailogeAdapter(SettingEdit.this, mAddressList);
        recycler_address_list.setAdapter(addsDaiAdapter);


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
                addsDaiAdapter.notifyItemInserted(emailList.size());
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
                //String addstype = addressCustomDialog().tempphoneList.get(1).phcType;


                saveAddress(mAddressList);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void saveAddress(final ArrayList<CommonModel> cityList) {
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ADDRESS_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Save Address Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) ;
                                    toastIconSuccess("address");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            phoneList.clear();
                                            getUserData();
                                            addsAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("TEST", "Get JSONException :" + e);
                                }
                            }
                        } else {

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


                    for (int i = 0; i < cityList.size(); i++) {
                        JSONObject addObj = new JSONObject();

                        String tmpId = "null";
                        if (cityList.get(i).recordId != null)
                            tmpId = cityList.get(i).recordId;

                        addObj.put("C", cityList.get(i).cN);
                        addObj.put("CR", cityList.get(i).cCountry);
                        addObj.put("Id", cityList.get(i).recordId);
                        addObj.put("L1", cityList.get(i).cAddress);
                        addObj.put("S", cityList.get(i).cState);
                        addObj.put("T", cityList.get(i).cType);
                        jsonArray.put(addObj);

                        params.put("addresses", jsonArray);
                        params.put("UserId", userId);
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

        Log.e("TEST", "Save Address :");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ADDRESS_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Save Address Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) ;
                                    toastIconSuccess("address");


                                    //  JSONObject obj = object.getJSONObject("obj");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("TEST", "Get JSONException :" + e);
                                }
                            }
                        } else {
                            //  pbHeaderProgress.setVisibility(View.VISIBLE);
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

                    JSONObject addObj = new JSONObject();

                    addObj.put("C", cityName);
                    addObj.put("CR", cityCountry);
                    addObj.put("Id", null);
                    addObj.put("L1", address);
                    addObj.put("S", cityState);
                    addObj.put("T", addType);
                    jsonArray.put(addObj);

                    params.put("addresses", jsonArray);
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

    private void seachCity(final String cName) {
        // pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Search City :" + cName);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SEARCHCITY_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Search City Response :" + response.toString());

                            RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
                            recycler_add_type.setLayoutManager(recyce);

                            if (strResponse != null) {
                                //lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        bt_save_address.setVisibility(View.VISIBLE);
                                    }
//                                        JSONObject obj = object.getJSONObject("obj");
                                    JSONArray addOBJ = object.getJSONArray("obj");
                                    for (int i = 0; i < addOBJ.length(); i++) {
                                        CommonModel cm = new CommonModel();
                                        JSONObject ob = addOBJ.getJSONObject(i);
                                        cm.cN = ob.getString("N");
                                        cm.cState = ob.getString("S");
                                        cm.cCountry = ob.getString("CR");
                                        cm.cSC = ob.getString("SC");
                                        cm.cCRC = ob.getString("CRC");

                                        String name = cm.cN + ", " + cm.cState + ", " + cm.cCountry;
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
                    params.put("city", cName);
                    params.put("country", "IN");

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
                "Home", "Work", "Other"
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

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_email.setLayoutManager(recyce);

        emailAdapterDialogList = new EmailDailogeAdapter(SettingEdit.this, emailList);
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
                emailAdapterDialogList.notifyItemInserted(emailList.size() - 1);
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

    private void showPhoneCustomDialog(final int phoneLenght) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.phone_dialog_event);
        dialog.setCancelable(true);

        edt_cCode = dialog.findViewById(R.id.edt_cCode);
        edt_phone = dialog.findViewById(R.id.edt_phone);
        edt_type = dialog.findViewById(R.id.edt_type);
        recycler_view_dailouge_phone = dialog.findViewById(R.id.recycler_view_dailouge_phone);

        edt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhoneType(v);
            }
        });

        if (phoneLenght == 0) {
            ((LinearLayout) dialog.findViewById(R.id.lin_phone)).setVisibility(View.VISIBLE);
        } else {
            ((LinearLayout) dialog.findViewById(R.id.lin_rv_phone)).setVisibility(View.VISIBLE);
            ((LinearLayout) dialog.findViewById(R.id.lin_phone)).setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
        recycler_view_dailouge_phone.setLayoutManager(recyce);

        phoneAdapterDailouge = new PhoneAdapterDialogList(SettingEdit.this, phoneList);
        recycler_view_dailouge_phone.setAdapter(phoneAdapterDailouge);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_phone_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.txt_phone_addNew)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonModel em = new CommonModel();
                em.recordId = null;
                em.phcCode = "91";
                em.phcType = "Office";
                em.phNumber = "";
                phoneList.add(em);
                phoneAdapterDailouge.notifyItemInserted(phoneList.size() - 1);
            }
        });

        ((Button) dialog.findViewById(R.id.bt_phone_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneno = phoneAdapterDailouge.tempphoneList.get(1).phNumber;
                String maintype = phoneAdapterDailouge.tempphoneList.get(1).phcType;
                Log.e("Test", "MAIN LIST" + phoneno + "/n"+ maintype);
                //addEmail(emailList);
               /*CommonModel cm = new CommonModel();
                cm.phcCode = edt_cCode.getText().toString();
                cm.phcType = edt_type.getText().toString();
                cm.phNumber = edt_phone.getText().toString();
                phoneList.add(cm);*/

                    CommonModel cm = new CommonModel();
                    cm.phNumber = phoneno;
                    cm.phcType = maintype;
                    phoneList.add(cm);
                    Log.e("TEST", "Phone number From adapter :" + cm.phNumber);

                savePhoneNumber(phoneList);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void savePhoneNumber(final ArrayList<CommonModel> phoneList) {
        // pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Save Phone Data :");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_PHONE_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Save Phone Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("phone");
                                        phoneAdapterDailouge.notifyDataSetChanged();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getUserData();
                                            }
                                        },1000);
                                    }

                                    // getUserData();
                                    phoneAdapter.notifyDataSetChanged();
                                    // JSONObject obj = object.getJSONObject("obj");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        } else {
                            //  pbHeaderProgress.setVisibility(View.VISIBLE);
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

                    for (int i = 0; i < phoneList.size(); i++) {
                        JSONObject phoneObject = new JSONObject();

                        String tmpId = null;
                        if (phoneList.get(i).recordId != null)
                            tmpId = phoneList.get(i).recordId;

                        phoneObject.put("T", phoneList.get(i).phcType);
                        phoneObject.put("Id", tmpId);
                        phoneObject.put("Ph", phoneList.get(i).phNumber);
                        phoneObject.put("CC", 91);
                        jsonArray.put(phoneObject);
                    }

                    params.put("phones", jsonArray);
                    params.put("UserId", userId);
                    Log.e("TEST", "Phone Param :" + params);
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

    public String ShowPhoneType(final View v) {
        final String[] emailType = new String[]{
                "Mobile", "Office", "Home", "Main", "Work Fax", "Home Fax", "other"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(emailType, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                ((EditText) v).setText(emailType[i]);
                email_id = emailType[i];
                dialogInterface.dismiss();
            }
        });
        builder.show();
        return email_id;
    }

    public void enableSaveBottn(boolean flage) {
        if (flage == true) {
            bt_save.setVisibility(View.VISIBLE);
        }
    }

    private void addEmail(final ArrayList<EmailModel> getemailList) {
        // pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Save Email Data :");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVEMAIL_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Save Email Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) ;
                                    toastIconSuccess("email");
                                    // getUserData();
                                    emailadapter.notifyDataSetChanged();
                                    JSONObject obj = object.getJSONObject("obj");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        } else {
                            //  pbHeaderProgress.setVisibility(View.VISIBLE);
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

                      /*JSONObject email1Object=new JSONObject();
                        email1Object.put("T",EdtTypeOne);
                        email1Object.put("E",EdtOne);

                        JSONObject email2Object=new JSONObject();
                        email2Object.put("T",EdtTypeTwo);
                        email2Object.put("E",EdtTwo);*/

                    for (int i = 0; i < getemailList.size(); i++) {
                        JSONObject email2Object = new JSONObject();

                        String tmpId = "null";
                        if (getemailList.get(i).recordId != null)
                            tmpId = getemailList.get(i).recordId;

                        email2Object.put("E", getemailList.get(i).emailAddress);
                        email2Object.put("Id", getemailList.get(i).recordId);
                        email2Object.put("T", getemailList.get(i).emailType);
                        jsonArray.put(email2Object);
                    }

                    //jsonArray.put(email2Object);
                    params.put("emails", jsonArray);
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

    public void showEmailTupe(final View v, final int position) {
        final String[] emailType = new String[]{
                "Mobile", "Office", "Home", "Main", "Work Fax", "Home Fax", "other"
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
        LinearLayout linDailogLag = dialog.findViewById(R.id.dailog_rel_lang);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(SettingEdit.this, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(recyce);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler_view_lang_dbox.setLayoutManager(gridLayoutManager);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        List<String> lang = new ArrayList<>();
        /*listAdapter1 = new LanguageListAdapter(SettingEdit.this, profileLangList, true);
        recycler_view_lang_dbox.setAdapter(listAdapter1);*/
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
                } else if (s.length() == 0) {
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
                if (getText.length() > 0) {
                    List<String> items = new ArrayList<>();
                    items.add(getText);
                    //setTag(items);
                    edt_lang.setText("");
                } else {
                    edt_lang.setFocusable(true);
                }

            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void getSaveLanguage(final List<CommonModel> allLangList) {
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

                                    getUserData();

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

    private void toastIconSuccess(String msgText) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);
        if (msgText.equalsIgnoreCase("Language")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Language(s) Saved Successfully!");
        } else if (msgText.equalsIgnoreCase("email")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Email(s) Saved Successfully!");
        } else if (msgText.equalsIgnoreCase("address")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Address(s) Saved Successfully!");
        } else if (msgText.equalsIgnoreCase("phone")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Phone(s) Saved Successfully!");
        }else if (msgText.equalsIgnoreCase("about")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("About(s) Saved Successfully!");
        }

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
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
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        bt_save.setVisibility(View.VISIBLE);
                                        // bt_true.setVisibility(View.VISIBLE);
                                    }
//                                        JSONObject obj = object.getJSONObject("obj");
                                    arr = object.getJSONArray("obj");
                                    for (int i = 0; i < arr.length(); i++) {
                                        CommonModel cm = new CommonModel();
                                        JSONObject ob = arr.getJSONObject(i);
                                        cm.lId = ob.getString("PId");
                                        cm.lName = ob.getString("N");
                                        cm.languageId = ob.getString("Id");

                                        lanList.add(cm);
                                    }
                                    /*LanguageAdapter adaper = new LanguageAdapter(SettingEdit.this, lanList);
                                    recycler_view.setAdapter(adaper);*/

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

    private void getUserData() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Get User Data");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_USERDATA_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Get User Data Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                recycler_view_lang.setLayoutManager(gridLayoutManager);
                                lanList.clear();
                                try {

                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) ;

                                    JSONObject obj = object.getJSONObject("obj");

                                    arr = obj.getJSONArray("Ls");
                                    String userId = obj.getString("Id");
                                    String userURl = obj.getString("I");
                                    String un = obj.getString("UN");
                                    String fullName = obj.getString("FN") + " " + obj.getString("LN");
                                    ((TextView) findViewById(R.id.txt_name)).setText(fullName);
                                    ((TextView) findViewById(R.id.txt_uname)).setText("@" + un);
                                    if (userURl != null) {
                                        Glide.with(getApplicationContext()).load(DataText.GetImagePath(userURl))
                                                .thumbnail(0.5f)
                                                .transition(withCrossFade())
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(((CircularImageView) findViewById(R.id.search_image)));
                                    } else {
                                        String firstLater = fullName.substring(0, 1).toUpperCase();
                                        ((CircularImageView) findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
                                        ((CircularImageView) findViewById(R.id.search_image)).setColorFilter(null);
                                        ((TextView) findViewById(R.id.search_image_text)).setText(firstLater);
                                    }

                                    //emailList.clear();
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

                                    mAddressList.clear();
                                    JSONArray arrAdds = obj.getJSONArray("Adds");
                                    for (int j = 0; j < arrAdds.length(); j++) {
                                        JSONObject addsObj = arrAdds.getJSONObject(j);
                                        String fullAddress = addsObj.getString("C") + addsObj.getString("S") + addsObj.getString("CR");
                                        CommonModel am = new CommonModel();

                                        am.cType = addsObj.getString("T");
                                        am.cAddress = addsObj.getString("L1");
                                        am.cN = addsObj.getString("C");
                                        am.cState = addsObj.getString("S");
                                        am.cCountry = addsObj.getString("CR");
                                        am.addsId = addsObj.getString("Id");
                                        am.fullAddress = fullAddress;

                                        mAddressList.add(am);
                                    }
                                    addsAdapter = new AddressAdapter(SettingEdit.this, mAddressList);
                                    recycler_view_address.setAdapter(addsAdapter);

                                    phoneList.clear();
                                    JSONArray phoneAdds = obj.getJSONArray("PHs");
                                    phoneLenght = phoneAdds.length();
                                    for (int k = 0; k < phoneAdds.length(); k++) {
                                        JSONObject phoneObj = phoneAdds.getJSONObject(k);
                                        CommonModel am = new CommonModel();
                                        am.phNumber = phoneObj.getString("Ph");
                                        am.phcType = phoneObj.getString("T");
                                        am.phcCode = phoneObj.getString("CC");
                                        am.phId = phoneObj.getString("Id");
                                        phoneList.add(am);
                                    }
                                    phoneAdapter = new PhoneAdapter(SettingEdit.this, phoneList);
                                    recycler_view_phone.setAdapter(phoneAdapter);

                                    JSONArray arrAbout = obj.getJSONArray("Bls");

                                    JSONObject objLin = arrAbout.getJSONObject(0);
                                    getLink = objLin.getString("OV");
                                    txt_link.setText(getLink);
                                    JSONObject objDec = arrAbout.getJSONObject(1);
                                    getDec = objDec.getString("OV");
                                    txt_dec.setText(getDec);
                                    JSONObject objAbout = arrAbout.getJSONObject(2);
                                    getTitle = objAbout.getString("OV");
                                    txt_title.setText(getTitle);

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
                                    }

                                    LanuageSettingAdapter listAdapter = new LanuageSettingAdapter(SettingEdit.this, profileLangList);
                                    recycler_view_lang.setAdapter(listAdapter);
                                    //rowTextView.setText(getname);
                                    JSONArray arrHobbies = obj.getJSONArray("Hs");
                                    Log.e("TEST","Hobby :"+arrHobbies.length());
                                    for (int k = 0; k < arrHobbies.length(); k++) {
                                        JSONObject arrObj = arrHobbies.getJSONObject(k);
                                        CommonModel cm = new CommonModel();
                                        cm.lId = arrObj.getString("Id");
                                        cm.lName = arrObj.getString("N");
                                        Log.e("TEST","Hobby :"+cm.lName);
                                        hobbyList.add(cm);
                                    }

                                    HobbyAdapterSetting adpeter = new HobbyAdapterSetting(SettingEdit.this, hobbyList);
                                    recycler_view_hb.setAdapter(adpeter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }, new Response.ErrorListener() {
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
                    JSONArray jsonArray = new JSONArray();
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
                    Log.e("TEST", "Json Array :" + jsonArray);
                    params.put("scopes", jsonArray);
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
        profileLangList.add(cm);

        listAdapter1.notifyDataSetChanged();
        recycler_view.setVisibility(View.GONE);
        edt_lang.setText("");
    }

    public void setCityName(String cityName) {
       /* cityName = cn;
        cityState = cs;
        cityCountry = cc;*/
        edt_city.setText(cityName);
        recycler_add_type.setVisibility(View.GONE);
    }


}

package com.fammeo.app.fragment.FammeoFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
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
import com.fammeo.app.activity.EditEmail;
import com.fammeo.app.adapter.AddressAdapter;
import com.fammeo.app.adapter.LanuageSettingAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailDailogeAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailListAdapter;
import com.fammeo.app.adapter.fammeoAdapter.HobbyAdapterSetting;
import com.fammeo.app.adapter.fammeoAdapter.PhoneAdapter;
import com.fammeo.app.adapter.fammeoAdapter.SkillAdapterSetting;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.model.EmailModel;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.view.siv.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_GET_DELETE_ADDRESS_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_DELETE_Email_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_DELETE_PHONE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_USERDATA_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private static final String TAG = VewProfileFragment.class.getSimpleName();
    String email_id,getLink,getDec,getTitle,mode;
    LinearLayout pbHeaderProgress;
    public CustomAuthRequest request;
    RecyclerView recycler_view_lang,recycler_view_email,recycler_view_address,recycler_view_phone,recycler_view_hb
            ,recycler_view_sk;
    ArrayList<CommonModel> lanList = new ArrayList<>();
    ArrayList<CommonModel> mAddressList = new ArrayList<>();
    ArrayList<CommonModel> phoneList = new ArrayList<>();
    List<CommonModel> hobbyList = new ArrayList<>();
    List<CommonModel> skillList = new ArrayList<>();
    ArrayList<CommonModel> getFieldList = new ArrayList<>();
    List<CommonModel> profileLangList;
    ArrayList<EmailModel> emailList = new ArrayList<>();
    View mView;
    String pe, peType,userId;
    TextView txt_title,txt_dec,txt_link;
    int phoneLenght;
    JSONArray arr, arrSetting;
    private Button bt_save, bt_save_address;
    private EmailListAdapter emailadapter = null;
    private EmailDailogeAdapter emailAdapterDialogList = null;
    SharedPreferences sp;
    AddressAdapter addsAdapter;
    PhoneAdapter phoneAdapter;
    ImageView bg_image;
    AlertDialog dialog,modedialog;
    ProgressBar pr_imageLoder;
    public final String APP_TAG = "MyCustomAppContect";
    File photoFile;
    public String photoFileName = "photo.jpg";


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_about, container, false);
        pbHeaderProgress = mView.findViewById(R.id.pbHeaderProgress);
        recycler_view_email = mView.findViewById(R.id.recycler_view_email);

        setRecleyViewManager(recycler_view_email);
        sp = getActivity().getSharedPreferences("uId", MODE_PRIVATE);
        userId = sp.getString("u", "");
        String un = sp.getString("un", "");

        if (userId.equals("") || userId.length() > 0) {
            userId = App.getInstance().getUserId();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAboutData();
            }
        },300);

        ((ImageView) mView.findViewById(R.id.img_addEmail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditEmail.class);
                startActivity(intent);
            }
        });
        return mView;
    }

    private void getAboutData() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST", "Get User Data");
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_USERDATA_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Get User Data Response :" + response.toString());
                                if (strResponse != null) {
                                    pbHeaderProgress.setVisibility(View.GONE);

                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")) ;

                                        JSONObject obj = object.getJSONObject("obj");

                                        JSONArray arrEs = obj.getJSONArray("Es");
                                        if(arrEs.length() == 0){
                                            //((TextView) mView.findViewById(R.id.txt_emailContent)).setVisibility(View.VISIBLE);
                                          //  ((TextView) mView.findViewById(R.id.txt_emailContent)).setText("No data found");
                                        }
                                        emailList.clear();
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
                                        emailadapter = new EmailListAdapter(AboutFragment.this,getContext(), emailList);
                                        recycler_view_email.setAdapter(emailadapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("TEST","Get User Exception :"+e);
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

                        //JSONObject jsonObject=new JSONObject();
                        //jsonObject.put("id",null);
                        //jsonObject.put("N",allLangList.get(i));
                        jsonArray.put("email");
                        jsonArray.put("field");
                        jsonArray.put("phone");
                        jsonArray.put("address");
                        jsonArray.put("date");
                        jsonArray.put("setting");
                        jsonArray.put("language");
                        jsonArray.put("hobby");
                        jsonArray.put("skill");
                        jsonArray.put("blob");

                        params.put("scopes", jsonArray);
                        params.put("UserId", userId);

                        Log.e("TEST","Get User Param :"+params);
                        return params;
                    } catch (JSONException ex) {
                        Log.e("TEST","Get User Exception :"+ex);
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



    public void deleteEmail(final String emailId){
        Log.e("TEST","Get Delete Id From Email :"+emailId);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_DELETE_Email_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Delete Response :" + response.toString());
                            if (strResponse != null) {
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("addsdlete");
                                        getAboutData();
                                    }
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
//
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
                    params.put("Id", emailId);
                    params.put("UserId", userId);
                    Log.e("TEST","Delete Address Param :"+params);
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
        Toast toast = new Toast(getActivity());
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
        }else if (msgText.equalsIgnoreCase("addsdlete")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Delete Successfully!");
        }

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

    public void deleteAddress(final String addsId){
        Log.e("TEST","Get Delete Id From Address :"+addsId);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_DELETE_ADDRESS_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Delete Response :" + response.toString());
                            if (strResponse != null) {
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("addsdlete");
                                        getAboutData();
                                    }
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
//
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
                    params.put("Id", addsId);
                    params.put("UserId", userId);
                    Log.e("TEST","Delete Address Param :"+params);
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

    public void deletePhone(final String addsId){
        Log.e("TEST","Get Phone Id From :"+addsId);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_DELETE_PHONE_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Delete Phone Response :" + response.toString());
                            if (strResponse != null) {
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("addsdlete");
                                        getAboutData();
                                    }
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
//
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
                    params.put("Id", addsId);
                    params.put("UserId", userId);
                    Log.e("TEST","Delete Phone Param :"+params);
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

    private void settingDilougeBox() {
        //arrSetting
        Log.e("TEST","Setting Request :"+arrSetting.length());

        final Dialog settindialog = new Dialog(getActivity());
        settindialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        settindialog.setContentView(R.layout.custom_setting_alert_dailogbox);
        settindialog.setCancelable(true);

        String getShowFrnd = ((TextView) settindialog.findViewById(R.id.txt_user_ShowFriends1)).getText().toString();
        String getshowFollowers = ((TextView) settindialog.findViewById(R.id.txt_user_ShowFollowers1)).getText().toString();
        String getAllowRequest = ((TextView) settindialog.findViewById(R.id.txt_user_AllowFriendRequests1)).getText().toString();
        String getNewMessage = ((TextView) settindialog.findViewById(R.id.txt_user_Notify_NewMessage1)).getText().toString();
        String getNewConnection = ((TextView) settindialog.findViewById(R.id.txt_user_Notify_NewConnection1)).getText().toString();
        String getEmailOff = ((TextView) settindialog.findViewById(R.id.txt_user_EmailOff1)).getText().toString();
        Log.e("TEST","Get Value :"+getShowFrnd);

        for (int i = 0; i < arrSetting.length(); i++) {
            try {
                JSONObject objSetting = arrSetting.getJSONObject(i);
                String settingN = objSetting.getString("N");

                if(settingN.equalsIgnoreCase(getShowFrnd)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFriends)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFriends)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getshowFollowers)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFollowers)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFollowers)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getAllowRequest)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_AllowFriendRequests)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_AllowFriendRequests)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getNewMessage)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewMessage)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewMessage)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getNewConnection)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewConnection)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewConnection)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getEmailOff)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_EmailOff)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_EmailOff)).setChecked(false);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        //modedialog.setView(lp);
        settindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        settindialog.show();
        settindialog.getWindow().setAttributes(lp);
        dialog.dismiss();
    }



    private void setRecleyViewManager(RecyclerView recycler_view){
        RecyclerView.LayoutManager addsrecy = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(addsrecy);
    }
}

package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.model.EmailModel;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.util.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.http.Url;

import static com.fammeo.app.constants.Constants.METHOD_GET_SAVEMAIL_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SOCIAL_LINK_SAVE_USER;

public class SocialLink extends AppCompatActivity {
    private static final String TAG = SocialLink.class.getSimpleName();
    CustomAuthRequest request;
    String userId;
    EditText edt_linkIn,edt_facebook,edt_twiter,edt_instagram;
    List<CommonModel> socLink;
    ArrayList<CommonModel> getField = new ArrayList<>();
    boolean lflage,fflage,tflage,iflage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_link);
        userId = App.getInstance().getUserId();

        edt_linkIn = findViewById(R.id.edt_linkIn);
        edt_facebook = findViewById(R.id.edt_facebook);
        edt_twiter = findViewById(R.id.edt_twiter);
        edt_instagram = findViewById(R.id.edt_instagram);

        getField = (ArrayList<CommonModel>) getIntent().getSerializableExtra("field");
        for (int i = 0; i < getField.size(); i++) {
            if(getField.get(i).soc_N.equalsIgnoreCase("facebook")) {
                edt_facebook.setText(getField.get(i).soc_V);
            }else
            if(getField.get(i).soc_N.equalsIgnoreCase("twitter")) {
                edt_twiter.setText(getField.get(i).soc_V);
            }else
            if(getField.get(i).soc_N.equalsIgnoreCase("linkedin")) {
                edt_linkIn.setText(getField.get(i).soc_V);
            }else
            if(getField.get(i).soc_N.equalsIgnoreCase("instagram")) {
                edt_instagram.setText(getField.get(i).soc_V);
            }
        }

        ((AppCompatButton) findViewById(R.id.bt_save_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               socLink = new ArrayList<>();

                String getLinkdin = edt_linkIn.getText().toString();
                String getFacebook = edt_facebook.getText().toString();
                String getTwitter = edt_twiter.getText().toString();
                String getInstagram = edt_instagram.getText().toString();

                if(getLinkdin.contains("linkedin.com") || getLinkdin.equals("")){
                        CommonModel cm = new CommonModel();
                        cm.soc_N = "linkedin";
                        cm.soc_V = edt_linkIn.getText().toString();
                        socLink.add(cm);
                        lflage  = true;
                }else {
                    edt_linkIn.requestFocus();
                    toastIconAlert("linkdin");
                    lflage  = false;
                }

                if(getFacebook.contains("facebook.com") || getFacebook.equals("")){
                    CommonModel cm = new CommonModel();
                    cm.soc_N = "facebook";
                    cm.soc_V = edt_facebook.getText().toString();
                    socLink.add(cm);
                    fflage  = true;
                }else {
                    edt_facebook.requestFocus();
                    toastIconAlert("facebook");
                    fflage  = false;
                }

                if(getTwitter.contains("twitter.com") || getTwitter.equals("")){
                    CommonModel cm = new CommonModel();
                    cm.soc_N = "twitter";
                    cm.soc_V = edt_twiter.getText().toString();
                    socLink.add(cm);
                    tflage = true;
                }else {
                    edt_twiter.requestFocus();
                    toastIconAlert("twitter");
                    tflage  = false;
                }

                if(getInstagram.contains("instagram.com") || getInstagram.equals("")){
                    CommonModel cm = new CommonModel();
                    cm.soc_N = "instagram";
                    cm.soc_V = edt_instagram.getText().toString();
                    socLink.add(cm);
                    iflage = true;
                }else {
                    edt_instagram.requestFocus();
                    toastIconAlert("instagram");
                    iflage  = false;
                }

                if(lflage == true && iflage == true && tflage == true && fflage == true){
                    socialLinkSave(socLink);
                }

            }
        });
        ((ImageButton) findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void socialLinkSave(final List<CommonModel> socLink) {
        // pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Save Social link :");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SOCIAL_LINK_SAVE_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Link Response :" + response.toString());
                            if (strResponse != null) {
                                //pbHeaderProgress.setVisibility(View.GONE);
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
                    for (int i = 0; i < socLink.size(); i++) {
                        JSONObject fielsObj = new JSONObject();

                        fielsObj.put("N", socLink.get(i).soc_N);
                        fielsObj.put("V", socLink.get(i).soc_V);

                        jsonArray.put(fielsObj);
                    }

                    //jsonArray.put(email2Object);
                    params.put("fields", jsonArray);
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

    private void toastIconSuccess() {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);

            ((TextView) custom_view.findViewById(R.id.message)).setText("Field(s) Saved Successfully!");

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

    private void toastIconAlert(String atertText) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText("Please check url");
        /*if(atertText.equalsIgnoreCase("linkdin")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Please check linkedin url");
        }else if(atertText.equalsIgnoreCase("facebook")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Please check facebook url");
        }else if(atertText.equalsIgnoreCase("twitter")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Please check twitter url");
        }else  if(atertText.equalsIgnoreCase("instagram")){
            ((TextView) custom_view.findViewById(R.id.message)).setText("Please check instagram url");
        }*/
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_500));

        toast.setView(custom_view);
        toast.show();
    }

    public void getCodeList(){
        List<String> cc = new ArrayList<>();

    }
}

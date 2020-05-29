package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
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
import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.app.App;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_ABOUT_USER;

public class AboutDetails extends AppCompatActivity {

    EditText edt_fname,edt_lname,edt_gender,edt_title,edt_description,edt_youtube;
    ImageButton bt_close;
    AppCompatButton bt_save_about;
    String title, dec, link,userId;
    public CustomAuthRequest request;
    LinearLayout pbHeaderProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);

            userId = App.getInstance().getUserId();

        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_gender = findViewById(R.id.edt_gender);
        edt_title = findViewById(R.id.edt_title);
        edt_description = findViewById(R.id.edt_description);
        edt_youtube = findViewById(R.id.edt_youtube);
        bt_save_about = findViewById(R.id.bt_save_about);
        bt_close = findViewById(R.id.bt_close);

        edt_fname.setText(App.getInstance().getFirstName());
        edt_lname.setText(App.getInstance().getLastName());

        Bundle abtBundle = getIntent().getExtras();
        if(abtBundle != null){
            title = abtBundle.getString("T");
            dec = abtBundle.getString("D");
            link = abtBundle.getString("L");
           edt_title.setText(title);
           edt_description.setText(dec);
           edt_youtube.setText(link);
        }

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_save_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edt_title.getText().toString();
                dec = edt_description.getText().toString();
                link = edt_youtube.getText().toString();
                String fname = edt_fname.getText().toString();
                String lName = edt_lname.getText().toString();
                String gender = edt_gender.getText().toString();
                saveAbouts(title,dec,link,fname,lName,gender);
            }
        });

    }

    private void saveAbouts(final String title, final String dec, final String link, final String fname, final String lName, final String gender) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ABOUT_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Save About Response :" + response.toString());
                                if (strResponse != null) {
                                    pbHeaderProgress.setVisibility(View.GONE);
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")){
                                            toastIconSuccess("about");
                                            Intent intent = new Intent(getApplicationContext(), SettingEdit.class);
                                            startActivity(intent);
                                        }
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
                        antobj.put("LN", lName);
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
                        obj2.put("OV", link);

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
    private void toastIconSuccess(String msgText) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);

            ((TextView) custom_view.findViewById(R.id.message)).setText("About(s) Saved Successfully!");

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

}

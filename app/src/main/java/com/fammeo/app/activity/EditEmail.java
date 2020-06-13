package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.fammeo.app.activity.EditActivity.EditAddress;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.EmailModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.fammeo.app.constants.Constants.METHOD_GET_SAVEMAIL_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_SINGLE_EMAIL_USER;

public class EditEmail extends AppCompatActivity {

    private static final String TAG = EditEmail.class.getSimpleName();
    EditText edt_emailtwo,edt_emailTypetwo;
    public CustomAuthRequest request;
    String eId,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        userId = App.getInstance().getUserId();
        edt_emailtwo = findViewById(R.id.edt_emailtwo);
        edt_emailTypetwo = findViewById(R.id.edt_emailTypetwo);
        Bundle emailBundle = getIntent().getExtras();
        if(emailBundle != null){
            String ET = emailBundle.getString("ET");
            String EA = emailBundle.getString("EA");
            eId = emailBundle.getString("eId");
            edt_emailtwo.setText(EA);
            edt_emailTypetwo.setText(ET);
        }
        edt_emailTypetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailType(v);
            }
        });
        ((ImageButton) findViewById(R.id.bt_close_Email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((Button) findViewById(R.id.bt_saveEmail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String eType = edt_emailTypetwo.getText().toString();
                String eEmail = edt_emailtwo.getText().toString();
                if(eEmail.matches(emailPattern) && eEmail.length() > 0){
                    addEmail(eType,eEmail,eId);
                }else {
                    edt_emailtwo.requestFocus();
                    edt_emailtwo.setError("Invalid Email");

                }


            }
        });

    }

    private void addEmail(final String eType, final String eEmail, final String eId) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST", "Save Email Data :");
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_SINGLE_EMAIL_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Save Email Response :" + response.toString());
                                if (strResponse != null) {
                                    //pbHeaderProgress.setVisibility(View.GONE);
                                    //lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")){
                                            toastIconSuccess("email");
                                            Intent intent = new Intent(getApplicationContext(), VewProfileFragment.class);
                                            startActivity(intent);
                                        }
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

                            JSONObject email2Object = new JSONObject();

                            email2Object.put("E", eEmail);
                            email2Object.put("Id", eId);
                            email2Object.put("T", eType);

                        //jsonArray.put(email2Object);
                        params.put("email", email2Object);
                        params.put("UserId", userId);
                        Log.e("TEST","Save Email Param :"+params);
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

    public void showEmailType(final View v) {
        final String[] emailType = new String[]{
                "Mobile", "Office", "Home", "Main", "Work Fax", "Home Fax", "other"
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
}

package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_PHONE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_SINGLE_PHONE_USER;

public class EditPhone extends AppCompatActivity {
    private static final String TAG = EditPhone.class.getSimpleName();
    public CustomAuthRequest request;
    String PT,PN,PC,PId,userId;
    EditText edt_phone_code,edt_phone,edt_phone_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        edt_phone_code = findViewById(R.id.edt_phone_code);
        edt_phone = findViewById(R.id.edt_phone);
        edt_phone_type = findViewById(R.id.edt_phone_type);

        Bundle phoneBudle = getIntent().getExtras();
        userId = App.getInstance().getUserId();
        if(phoneBudle != null){
            PT = phoneBudle.getString("PT");
            PN = phoneBudle.getString("PN");
            PC = phoneBudle.getString("PC");
            PId = phoneBudle.getString("PId");
            Log.e("TEST","PID :"+PId);
            edt_phone.setText(PN);
            edt_phone_type.setText(PT);
            edt_phone_code.setText(PC);
        }
        edt_phone_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailType(v);
            }
        });
        ((AppCompatButton) findViewById(R.id.bt_phone_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PT = edt_phone_type.getText().toString();
                PN = edt_phone.getText().toString();
                PC = edt_phone_code.getText().toString();
                if(PN.length() == 10){
                    savePhoneNumber(PT,PN,PC,PId);
                }else {
                    edt_phone.setError("Invalid Number!");
                    edt_phone.requestFocus();
                }

            }
        });
        ((ImageButton) findViewById(R.id.bt_phone_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }

    private void savePhoneNumber(final String pt, final String pn, String pc, final String pid) {
            // pbHeaderProgress.setVisibility(View.VISIBLE);
            Log.e("TEST", "Save Phone Data :");
            // list.clear();
            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_SINGLE_PHONE_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Save Phone Response :" + response.toString());
                                if (strResponse != null) {
                                    //pbHeaderProgress.setVisibility(View.GONE);
                                    //lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")) {
                                            toastIconSuccess("phone");
                                            Intent intent = new Intent(getApplicationContext(), SettingEdit.class);
                                            startActivity(intent);
                                            finish();
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
                            JSONObject phoneObject = new JSONObject();

                            phoneObject.put("T", pt);
                            phoneObject.put("Id", pid);
                            phoneObject.put("Ph", pn);
                            phoneObject.put("CC", 91);

                        params.put("phone", phoneObject);
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
                //emailList.get(position).emailType = emailType[i];
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}

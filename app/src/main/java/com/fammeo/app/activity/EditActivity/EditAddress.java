package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.fammeo.app.adapter.fammeoAdapter.CityAdapter;
import com.fammeo.app.adapter.fammeoAdapter.CityListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.CommomInterface;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.PassDataInterface;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.fammeo.app.constants.Constants.METHOD_GET_SAVE_ADDRESS_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_SEARCHCITY_USER;

public class EditAddress extends AppCompatActivity {
    private static final String TAG = EditAddress.class.getSimpleName();
    EditText edt_addressType, edt_address, edt_city;
    RecyclerView recycler_add_type;
    public CustomAuthRequest request;
    List<CommonModel> mCityList = new ArrayList<>();
    String cName,state,country,userId,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        edt_addressType = findViewById(R.id.edt_addressType);
        edt_address = findViewById(R.id.edt_address);
        edt_city = findViewById(R.id.edt_city);
        recycler_add_type = findViewById(R.id.recycler_add_type);

        userId = App.getInstance().getUserId();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String aType = extras.getString("T");
            String aAdd = extras.getString("A");
            id = extras.getString("ID");
            Log.e("TEST","Address Id :"+id);
            cName = extras.getString("C");
            state = extras.getString("S");
            country = extras.getString("Con");

            String aCity = extras.getString("C") + ", " + extras.getString("S") + ", " + extras.getString("Con");

            edt_addressType.setText(aType);
            edt_address.setText(aAdd);
            edt_city.setText(aCity);
        }

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(EditAddress.this, LinearLayoutManager.VERTICAL, false);
        recycler_add_type.setLayoutManager(recyce);

        edt_addressType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAdrressType(v);
            }
        });

        edt_city.addTextChangedListener(new TextWatcher() {
            long lastChange = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.length() >= 3) {
                    recycler_add_type.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seachCity(s.toString());
                        }
                    }, 300);
                    lastChange = System.currentTimeMillis();
                }
            }
        });

           ((AppCompatButton) findViewById(R.id.bt_save_address)).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   saveAddress(edt_addressType.getText(),edt_address.getText(),cName,state,country,id);
               }
           });

        ((ImageButton) findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveAddress(final Editable atype, final Editable getAddress, final String cName, final String state, final String country, final String aId) {

            request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_SAVE_ADDRESS_USER, null, 0,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (App.getInstance().authorizeSimple(response)) {
                                String strResponse = response.toString();
                                Log.e("TEST", "Save Address Response :" + response.toString());
                                if (strResponse != null) {
                                    //pbHeaderProgress.setVisibility(View.GONE);
                                    //lanList.clear();
                                    try {
                                        JSONObject object = new JSONObject(strResponse);
                                        String msgType = object.getString("MessageType");
                                        if (msgType.equalsIgnoreCase("success")) ;
                                        toastIconSuccess("address");
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getApplicationContext(),SettingEdit.class);
                                                intent.putExtra("R",01);
                                                startActivity(intent);
                                                /*phoneList.clear();
                                                getUserData();
                                                addsAdapter.notifyDataSetChanged();*/
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
                        JSONObject addObj = new JSONObject();

                        addObj.put("C", cName);
                        addObj.put("CR", country);
                        addObj.put("Id", null);
                        addObj.put("L1", getAddress);
                        addObj.put("S", state);
                        addObj.put("T", atype);
                        addObj.put("Id", aId);

                        params.put("address", addObj);
                        params.put("UserId", userId);

                        Log.e("TEST","Address Param :"+params);
                        return params;
                 }
                 catch (JSONException ex) {
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
                            if (strResponse != null) {
                                mCityList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        // bt_save_address.setVisibility(View.VISIBLE);
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

                                        //String name = cm.cN + ", " + cm.cState + ", " + cm.cCountry;
                                        mCityList.add(cm);
                                    }
                                    CityListAdapter adapter = new CityListAdapter(EditAddress.this, mCityList);
                                    recycler_add_type.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
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

    public void cityName(String cN, String cState, String cCountry, String fullCname){
        edt_city.setText(fullCname);
        cName = cN;
        state = cState;
        country = cCountry;
        recycler_add_type.setVisibility(View.GONE);
    }
}

package com.fammeo.app.adapter.fammeoAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.app.App;
import com.fammeo.app.common.PassDataInterface;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.fammeo.app.constants.Constants.METHOD_GET_SEARCHCITY_USER;

public class AddressDailogeAdapter extends RecyclerView.Adapter<AddressDailogeAdapter.ViewHolder> implements PassDataInterface {
    SettingEdit contex;
    ArrayList<CommonModel> mAddressList;
    private ArrayList<CommonModel> mCityList = new ArrayList<>();
    String getName;
    int cityPos=0;

    public CustomAuthRequest request;
    public AddressDailogeAdapter(SettingEdit settingEdit, ArrayList<CommonModel> mAddressList) {
        this.contex = settingEdit;
        this.mAddressList = mAddressList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_dialog_list_item,parent,false);
        return new AddressDailogeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        Log.e("TEST","Get CName :"+getName);
           holder.edt_city.setText(getName);

        String city = "";
         if (mAddressList.get(i).cN.trim().length() == 0 || mAddressList.get(i).cState.trim().length() == 0 ||
                mAddressList.get(i).cCountry.trim().length() == 0)
            city = "";

        else
            city = mAddressList.get(i).cN+", "+ mAddressList.get(i).cState+", "+ mAddressList.get(i).cCountry;
        Log.e("TEST","Dailog Address :"+city);
        holder.edt_addressType.setText(mAddressList.get(i).cType);
        holder.edt_address.setText(mAddressList.get(i).cAddress);
        holder.edt_city.setText(city);
        holder.edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0)
                    mAddressList.get(i).cAddress=holder.edt_address.getText().toString();
            }
        });
holder.edt_city.addTextChangedListener(new TextWatcher() {
    long lastChange=0;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.e("TEST","City Name :"+s.toString());
        cityPos=i;
        if(s.length() >= 3){
            final String cName = s.toString();
            seachCity(cName,holder.recycler_addd_ail_type);
            lastChange = System.currentTimeMillis();
        }else if(s.length() == 0){
            //holder.recycler_addd_ail_type.setVisibility(View.GONE);
        }
    }
});

        holder.edt_addressType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contex.showAdrressType(v);
                mAddressList.get(i).cType=holder.edt_addressType.getText().toString();
            }
        });
        }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mAddressList.size();
    }


    @Override
    public void userData(String fullName, String FN, String LN, String email, String imgUrl) {

    }

    @Override
    public void CityData(final CommonModel cityName) {
        Log.e("TEST","Adapter City Name :"+cityName);
//                getName = cityName;
        cityName.cType=mAddressList.get(cityPos).cType;
        cityName.cAddress=mAddressList.get(cityPos).cAddress;
//        CommonModel cm = new CommonModel();
//
//        cm.recordId = null;
//        cm.cType = "Office";
//        cm.cAddress = "";
//        cm.cN = "";
//        cm.cState = "";
//        cm.cCountry = "";
        mAddressList.remove(cityPos);
//        cm.cN=cityName;
//        cm.cState="change";
        mAddressList.add(cityPos,cityName);
//        mAddressList.get(i).cN+", "+ mAddressList.get(i).cState+", "+ mAddressList.get(i).cCountry
//                mAddressList.get(cityPos).cN=cityName;
        contex.updateList(mAddressList);
                notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText edt_addressType,edt_address,edt_city;
        RecyclerView recycler_addd_ail_type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            edt_addressType = itemView.findViewById(R.id.edt_addressType);
            edt_address = itemView.findViewById(R.id.edt_address);
            edt_city = itemView.findViewById(R.id.edt_city);
            recycler_addd_ail_type = itemView.findViewById(R.id.recycler_addd_ail_type);
                        RecyclerView.LayoutManager recyce = new LinearLayoutManager(contex, LinearLayoutManager.VERTICAL,false);
            recycler_addd_ail_type.setLayoutManager(recyce);
        }
    }

    private void seachCity(final String cName, final RecyclerView recycler_addd_ail_type) {
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

                            if(strResponse != null) {
                                mCityList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if(msgType.equalsIgnoreCase("success"))
                                    {
                                     //   bt_save_address.setVisibility(View.VISIBLE);
                                    }
//                                        JSONObject obj = object.getJSONObject("obj");
                                    JSONArray addOBJ = object.getJSONArray("obj");
                                    for (int i = 0; i < addOBJ.length(); i++) {
                                        CommonModel cm= new CommonModel();
                                        JSONObject ob = addOBJ.getJSONObject(i);
                                        cm.cN = ob.getString("N");
                                        cm.cState = ob.getString("S");
                                        cm.cCountry = ob.getString("CR");

//                                        cm.cType=

                                        //cm.cSC = ob.getString("SC");
                                        //cm.cCRC = ob.getString("CRC");
//                                        String tmpCity = cm.cN+", "+cm.cState+", "+cm.cCountry;

                                        mCityList.add(cm);
                                    }
                                    recycler_addd_ail_type.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            contex.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CityAdapter cityadaper = new CityAdapter(contex, mCityList,AddressDailogeAdapter.this);
                                                    recycler_addd_ail_type.setVisibility(View.VISIBLE);
                                                    recycler_addd_ail_type.setAdapter(cityadaper);
                                                    //cityadaper.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            //pbHeaderProgress.setVisibility(View.VISIBLE);
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
                    //DataGlobal.SaveLog(TAG, ex);
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
}

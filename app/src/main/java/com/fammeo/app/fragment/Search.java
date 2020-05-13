package com.fammeo.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.activity.MainActivity;
import com.fammeo.app.adapter.fammeoAdapter.SearchListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.PassDataInterface;
import com.fammeo.app.common.SnakebarCustom;
import com.fammeo.app.model.SearchUserModel;
import com.fammeo.app.util.CustomAuthRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.fammeo.app.constants.Constants.METHOD_SEARCH_GET_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment implements PassDataInterface{
    PassDataInterface passDataInterface;
    private static String TAG = Search.class.getSimpleName();
    public CustomAuthRequest request;
    RecyclerView recycler_view;
    ArrayList<SearchUserModel> searchlist = new ArrayList<SearchUserModel>();
    private Toolbar mToolbar;
    boolean flage = false;
    public Search() {
        // Required empty public constructor
    }

public void setListener(PassDataInterface passDataInterface){
        this.passDataInterface=passDataInterface;
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        recycler_view = view.findViewById(R.id.recycler_view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        getActivity().setTitle("Create & Claim Profile");


        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);
        String email = App.getInstance().setEmailName();
        //((LinearLayout) view.findViewById(R.id.lin_bg_image)).setBackgroundResource(R.drawable.search_bg);
        ((TextView) view.findViewById(R.id.txt_email)).setText(email);
        ((TextView) view.findViewById(R.id.txt_name)).setText(App.getInstance().getFullname());
        getSearchUser("");
        ((ImageButton) view.findViewById(R.id.btn_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Click NEXT");
                String getName = ((EditText) view.findViewById(R.id.edt_name)).getText().toString();
                getSearchUser(getName);
                if(flage == true){
                    ((CardView) view.findViewById(R.id.card_data)).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.txt_name)).setText(getName);
                }
            }
        });

        String firstLater = App.getInstance().getFullname().substring(0,1).toUpperCase();

        ((ImageView) view.findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
        ((ImageView) view.findViewById(R.id.search_image)).setColorFilter(null);
        ((TextView) view.findViewById(R.id.search_image_text)).setText(firstLater);

        ((ImageView) view.findViewById(R.id.btn_create)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = ((TextView) view.findViewById(R.id.txt_name)).getText().toString();
                String mail =  App.getInstance().setEmailName();
                String FN = App.getInstance().getFirstName();
                String LN = App.getInstance().getLastName();

               Log.e("TEST","UName :"+uname);
               Log.e("TEST","FN :"+FN);
               Log.e("TEST","LN :"+LN);
               Log.e("TEST","Email :"+mail);

                passDataInterface.userData(uname,FN,LN,mail,"");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // getSearchUser("");
    }

    private void getSearchUser(final String getText) {

        Log.e("TEST","Call Search User :"+getText);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_SEARCH_GET_USER, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {

                            String strResponse = response.toString();
                            Log.e("TEST","Fammeo Search Response :"+response.toString());
                            searchlist.clear();
                            if(strResponse != null){
                                try {
                                    JSONObject obj = new JSONObject(response.toString());
                                    String msg = obj.getString("Message");
                                    Log.e("TEST","Message :"+msg);
                                    if(msg.equalsIgnoreCase("User already Exists")){

                                    }
                                    JSONArray jsonArray = obj.getJSONArray("obj");
//
                                    if(obj.getJSONArray("obj").length() == 0){
                                        Log.e("TEST","Json Null");
                                        flage = true;
                                    }else {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            // CompanyRelationJ cd = new CompanyRelationJ();
                                            JSONObject userDetail = jsonArray.getJSONObject(i);
                                            SearchUserModel SM = new SearchUserModel();

                                            SM.FN = userDetail.getString("FN");
                                            SM.LN = userDetail.getString("LN");
                                            SM.url = userDetail.getString("I");
                                            SM.Email = userDetail.getString("PE");

                                            searchlist.add(SM);
                                        }
                                    }

                                    SearchListAdapter adapter = new SearchListAdapter(Search.this,getContext(),searchlist,Search.this);
                                    recycler_view.setAdapter(adapter);

                                 } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
//                                pbHeaderProgress.setVisibility(View.VISIBLE);
                                //SnakebarCustom.danger(mContext, View , "Unable to fetch contact: " + "No data found", 5000);
                            }
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
                    Log.e("TEST","Param :"+getText);
                    params.put("PageIndex", 1);
                    params.put("PageSize", 10);
                    params.put("text", getText.trim());
                    JSONObject filterExpression = new JSONObject();
                        /*try {
                            filterExpression.put("Status", "All");

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*/
                    //params.put("filterExpression", filterExpression);

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


    @Override
    public void userData(String fullName, String FN, String LN, String email, String imgUrl) {
        passDataInterface.userData(fullName,FN,LN,email,imgUrl);
    }


}

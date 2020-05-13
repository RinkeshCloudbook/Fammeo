package com.fammeo.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.R;
import com.fammeo.app.activity.CreateUser;
import com.fammeo.app.activity.MainActivity;
import com.fammeo.app.adapter.UserSuggestion;
import com.fammeo.app.animation.ViewAnimation;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_SEARCH_CREATE_USER;
import static com.fammeo.app.constants.Constants.METHOD_SEARCH_NEW_CREATE_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreate extends Fragment {

    private static final String TAG = FragmentCreate.class.getSimpleName();
    public CustomAuthRequest request;
    private Context mContext;
    ArrayList<String> suglist = new ArrayList<String>();
    RecyclerView recycler_view;
    boolean flags = true;
    String getFullName,getEmail,getUrl,getFN,getLN;
    EditText edt_search;
    Button btn_createNext;
    View mView;
    ImageButton bt_toggle_text;
    private View lyt_expand_text;

    public FragmentCreate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create, container, false);
           // Inflate the layout for this fragment
        mView = view;
        recycler_view = view.findViewById(R.id.recycler_view);
        edt_search = view.findViewById(R.id.edt_search);
        btn_createNext = view.findViewById(R.id.btn_createNext);
        bt_toggle_text = view.findViewById(R.id.bt_toggle_text);
        lyt_expand_text = (View) view.findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);


        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionText(bt_toggle_text);
            }
        });

        if(flags != false){
            ((EditText) view.findViewById(R.id.edt_search)).addTextChangedListener(new TextWatcher() {
                long lastChange=0;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() <= 4){
                        Toast.makeText(getActivity(),"UserName Length must be greater than 4 and lesser than 31 characters",Toast.LENGTH_LONG).show();
                        ((ImageButton) mView.findViewById(R.id.bt_false)).setVisibility(View.VISIBLE);
                        ((ImageButton) mView.findViewById(R.id.bt_true)).setVisibility(View.GONE);
                    }
                    if(s.length() >= 5){
                        final String getText = s.toString();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (System.currentTimeMillis() - lastChange >= 300) {
                                    //send request
                                    CreateUserSearch(getText);
                                }
                            }
                        }, 300);
                        lastChange = System.currentTimeMillis();
                    }
                    else if(s.length() == 0){
                        // getcompanies(false, 0, CurrentSearctText);

                    }
                }
            });
        }

        btn_createNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUname = edt_search.getText().toString();
                Log.e("TEST","Get User NAme :"+getUname);
                createUser(getUname);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    private boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    private void createUser(final String uname) {

        //pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST","User Creat");
        ((TextView) mView.findViewById(R.id.message)).setVisibility(View.GONE);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_SEARCH_NEW_CREATE_USER, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {

                            String strResponse = response.toString();
                            Log.e("TEST","Create Response :"+response.toString());

                            if(strResponse != null){
                                Log.e("TEST","Response Not null");
                                JSONObject object = new JSONObject();
                                try {
                                    String getMsg = object.getString("MessageType");
                                    if(getMsg.equalsIgnoreCase("success")){
                                        Log.e("TEST","You have joined Successfully");
                                    }
                                    JSONObject userObj = object.getJSONObject("obj");
                                    String GId = userObj.getString("GId");
                                    String Id = userObj.getString("Id");
                                    String FN = userObj.getString("FN");
                                    String userName = userObj.getString("UN");
                                    String S = userObj.getString("S");
                                    String I = userObj.getString("I");
                                    String UserName = userObj.getString("UN");

                                    Log.e("TEST","Gid :"+GId);
                                    Log.e("TEST","Id :"+Id);
                                    Log.e("TEST","FN :"+FN);
                                    Log.e("TEST","UN :"+userName);
                                    Log.e("TEST","S :"+S);
                                    Log.e("TEST","I :"+I);
                                    Log.e("TEST","UserName :"+UserName);

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
                    JSONObject objUser =new JSONObject();
                    objUser.put("FN",getFN);
                    objUser.put("LN",getLN);
                    objUser.put("S","male");
                    objUser.put("UN",uname);
                    params.put("user",objUser);

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

    private void CreateUserSearch(final String getText) {
        //pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST","Call Search User");
        ((TextView) mView.findViewById(R.id.message)).setVisibility(View.GONE);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_SEARCH_CREATE_USER, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {

                            String strResponse = response.toString();
                            Log.e("TEST","Create User Search :"+response.toString());
                            if(strResponse != null){
                                try {
                                    //contactList.clear();

                                    //pbHeaderProgress.setVisibility(View.GONE);
                                    JSONObject obj = new JSONObject(response.toString());
                                    Object intervention = obj.get("obj");
                                    JSONArray jsonArray=null;
                                    if (intervention instanceof JSONArray)
                                        jsonArray = obj.getJSONArray("obj");
                                    Log.e("TEST","Sugession jsonArray :"+jsonArray);
                                    String msg = obj.getString("Message");
                                    Log.e("TEST","MESSAGE :"+msg);

                                    Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

                                    if(msg.equalsIgnoreCase("Username already Registered")){
                                        ((ImageButton) mView.findViewById(R.id.bt_false)).setVisibility(View.VISIBLE);
                                        ((ImageButton) mView.findViewById(R.id.bt_true)).setVisibility(View.GONE);
                                    }else if(msg.equalsIgnoreCase("Available")){
                                        ((ImageButton) mView.findViewById(R.id.bt_false)).setVisibility(View.GONE);
                                        ((ImageButton) mView.findViewById(R.id.bt_true)).setVisibility(View.VISIBLE);
                                    }

                                    if(jsonArray != null){
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            suglist.add(jsonArray.getString(i));
                                        }
                                        UserSuggestion adaper = new UserSuggestion(getActivity(),suglist);
                                        recycler_view.setAdapter(adaper);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("error",e.getMessage());
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
                    params.put("text", getText.trim());
                    JSONObject filterExpression = new JSONObject();

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

    public void  getSugName(String sugName, boolean flag){
        ((EditText) mView.findViewById(R.id.edt_search)).setText(sugName);
        flags = flag;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("email", "0");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("email", "1");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("email", "2");
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible) {
            Log.e("email", "Reload fragment");
            Bundle bundle=getArguments();

            String email=bundle.getString("email");
            String fullName=bundle.getString("fullName");
            String FN=bundle.getString("FN");
            String LN=bundle.getString("LN");
            String imgUrl=bundle.getString("imgUrl");
            if (email!=null)
                Log.e("Bundle email",email);
                ////Log.e("Full",fullName);
                /*Log.e("FN",FN);
                Log.e("LN",LN);
                Log.e("url",imgUrl);*/
        }
    }
  // public void userData(String fullName, String FN, String LN, final String email, String imgUrl) {
    public void userData(Bundle userData) {


        if (userData == null)
            return;

        getFullName = userData.getString("fullName");
        getFN = userData.getString("FN");
        getLN = userData.getString("LN");
        getUrl = userData.getString("imgUrl");
        getEmail = userData.getString("email");

        Log.e("TEST","Get Full Name :"+getFullName);
        Log.e("TEST","Get Full FN :"+App.getInstance().getFirstName());
        Log.e("TEST","Get Full LN :"+App.getInstance().getLastName());
        Log.e("TEST","Get Url  :"+getUrl);
        Log.e("TEST","Get Email  :"+getEmail);

        ((TextView) mView.findViewById(R.id.txt_create_name)).setText(getFullName);
        ((TextView) mView.findViewById(R.id.txt_create_email)).setText(getEmail);
        if(getUrl != null){
            Glide.with(getActivity()).load(DataText.GetImagePath(getUrl))
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((ImageView) mView.findViewById(R.id.create_image)));
        }else {
            String firstLater =getFN.substring(0,1).toUpperCase();
            ((ImageView) mView.findViewById(R.id.create_image)).setImageResource(R.drawable.bg_search_circle);
            ((ImageView) mView.findViewById(R.id.create_image)).setColorFilter(null);
            ((TextView) mView.findViewById(R.id.create_image_text)).setText(firstLater);
        }


        /*getEmail = email;
        getFullName = fullName;
        getFN = FN;
        getLN = LN;
        getUrl = imgUrl;

        Log.e(" Create Tag email",email);
        Log.e("Create FullName",fullName);
        Log.e("Create FN",FN);
        Log.e("Create LN",LN);
        Log.e("Create imgUrl",imgUrl);*/
    }
}

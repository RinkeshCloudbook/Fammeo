package com.fammeo.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.activity.CreateUser;
import com.fammeo.app.activity.SearchUser;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.SnakebarCustom;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.fammeo.app.constants.Constants.METHOD_SEARCH_GET_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fammeo_Search extends Fragment {



    public Fammeo_Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fammeo__search, container, false);

        Log.e("TEST","User Id Check :"+App.getInstance().getId());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(App.getInstance().getId().length() > 0){
                    Log.e("TEST","Create User");
                    Intent intent = new Intent(getActivity(), CreateUser.class);
                    startActivity(intent);
                }else {
                    Log.e("TEST","Search");
                    Intent intent = new Intent(getActivity(), SearchUser.class);
                    startActivity(intent);
                }
            }
        },1000);

        return view;
    }


}

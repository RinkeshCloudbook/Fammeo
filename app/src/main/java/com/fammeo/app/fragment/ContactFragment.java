package com.fammeo.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.fammeo.app.R;
import com.fammeo.app.adapter.ContactAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.SnakebarCustom;
import com.fammeo.app.common.SweetAlertCustom;
import com.fammeo.app.model.CompanyRelationJ;
import com.fammeo.app.model.ContactDetails;
import com.fammeo.app.util.CustomAuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.fammeo.app.constants.Constants.METHOD_COMPANY_CONTACT_PROFILE;

public class ContactFragment extends Fragment {

    private static final String TAG = CompanyFragment.class.getSimpleName();

    LinearLayout search_edit_frame;
    TextView mMessage;
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recycler_view;
    private Activity CurrentActivity;
    ArrayList<CompanyRelationJ> list;
    List<ContactDetails> contactList = new ArrayList<>();
    private Paint p = new Paint();
    public View mView;
    private AppCompatActivity activity;
    public CustomAuthRequest request;
    private ProgressBar pbHeaderProgress;

    public ContactFragment() {
        // Required empty public constructor
    }
    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    public View GetView() {
        return mView;
    }
    public  long[] Filter_CRIds ;
    public boolean IsFiltered()
    {
        if(Filter_CRIds != null && Filter_CRIds.length > 0)
        {
            return  true;
        }
        return  false;
    }
    public void SetFilter(long[] CRIds) {
        Filter_CRIds = CRIds;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        mView = rootView;
        //mListener.OnCompleteFragment(mView);
        CurrentActivity = this.getActivity();
        mContext = CurrentActivity.getApplicationContext();
        activity = ((AppCompatActivity) getActivity());
        mMessage = (TextView) rootView.findViewById(R.id.message);
        recycler_view = rootView.findViewById(R.id.recycler_view);
        pbHeaderProgress = rootView.findViewById(R.id.pbHeaderProgress);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);

        /* RecyclerView.LayoutManager recyce = new GridLayoutManager(getActivity(),2);
        recycler_view.setLayoutManager(recyce);*/

        //showMessage(getText(R.string.label_empty_list).toString());
        initpDialog();

        getContactDetails();

        return rootView;
    }

    private void getContactDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        request = new CustomAuthRequest(Request.Method.POST, METHOD_COMPANY_CONTACT_PROFILE, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //showMessage(getText(R.string.label_empty_list).toString());
                        Log.w(TAG, response.toString() );
                        pbHeaderProgress.setVisibility(View.VISIBLE);

                       String strResponse = response.toString();
                        if(strResponse != null){
                            try {
                                contactList.clear();
                                pbHeaderProgress.setVisibility(View.GONE);
                                JSONObject obj = new JSONObject(response.toString());
                                JSONArray jsonArray = obj.getJSONArray("obj");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ContactDetails cd = new ContactDetails();
                                    JSONObject userDetail = jsonArray.getJSONObject(i);
                                    cd.FN = userDetail.getString("FN");
                                    cd.MN = userDetail.getString("MN");
                                    cd.LN = userDetail.getString("LN");
                                    cd.imageUrl = userDetail.getString("I");
                                    cd.contectId = Long.parseLong(userDetail.getString("Id"));

                                    JSONArray emailArr = userDetail.getJSONArray("Es");
                                    for (int j = 0; j < emailArr.length(); j++) {
                                        JSONObject eObj = emailArr.getJSONObject(j);
                                        cd.email = eObj.getString("E");
                                        }
                                    JSONArray phArray = userDetail.getJSONArray("PHs");
                                    for (int k = 0; k < phArray.length(); k++) {
                                        JSONObject phObj = phArray.getJSONObject(k);
                                        cd.Ph = phObj.getString("Ph");
                                    }

                                    contactList.add(cd);
                                }
                                ContactAdapter adapter = new ContactAdapter(ContactFragment.this,getContext(),contactList);
                                recycler_view.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("TEST","GET Exception :"+e);
                            }
                        }else {
                            pbHeaderProgress.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.VISIBLE);
                SnakebarCustom.danger(mContext, mView, "Unable to fetch Companies: " + error.getMessage(), 5000);
                swipeRefreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    //params.put("PageIndex", offset);
                    //params.put("PageSize", 20);
                    //params.put("filter", CurrentSearctText);
                    JSONObject filterExpression = new JSONObject();
                    try {
                        filterExpression.put("Status","All");
                        if(Filter_CRIds != null)
                        {
                            JSONArray CRIds = new JSONArray();
                            for (Long CRId: Filter_CRIds) {
                                CRIds.put(CRId);
                            }
                            if(CRIds.length() > 0)
                            {
                                filterExpression.put("CRIds",CRIds);
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    params.put("filterExpression", filterExpression);
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
    public void onResume() {
        super.onResume();
        getContactDetails();
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
    }
    private SweetAlertCustom swc;
    protected void initpDialog() {
        if (swc == null)
            swc = new SweetAlertCustom(mContext);
        swc.CreatingLoadingDialog("Loading");
    }

}

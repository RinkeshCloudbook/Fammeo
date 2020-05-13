package com.fammeo.app.util;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fammeo.app.app.App;
import com.fammeo.app.app.ECBAuthToken;
import com.fammeo.app.common.DataGlobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class CustomAuthRequest {
    private static String TAG = "CustomAuthRequest";
    private Listener<JSONObject> listener;
    private ErrorListener errorlistener;
    private JSONObject params;
    private  Map<String, String> headers;
    public CustomRequest customRequest;
    private boolean isDestroyed;
    public void destroy()
    {
        this.isDestroyed = true;
        //Log.w(TAG, "destroy: " );
        if(this.customRequest != null){
            this.customRequest.destroy();
        }
        this.listener = null;
        this.errorlistener = null;
    }

    public void setCustomRequest(CustomRequest req)
    {
        //Log.w(TAG, "setCustomRequest: " );
        this.customRequest = req;
    }
    public CustomAuthRequest getCurrentObj()
    {
        return  this;
    }

    public CustomAuthRequest(final int method, final String url, JSONObject params,final long ACId, final Listener<JSONObject> reponseListener,
                             final ErrorListener errorListener) {
        this.listener = reponseListener;
        this.errorlistener = errorListener;
        this.isDestroyed = false;
        if (params == null)
            params = this.getParams();
        final JSONObject RParams = params;
        App app = App.getInstance();
        if (app.isConnected() && app.getId() != null) {
            if (app.IsLoggedIn()) {
                Random r =new Random();
                final String RandomNumber = String.valueOf( r.nextLong());
                Log.i(TAG, RandomNumber );
                Log.i(TAG,  url);

                long ParseACId = ACId;
                if(!(ACId > 0)) ParseACId = App.getInstance().getCurrentACId();
                Log.i(TAG, Long.toString(ParseACId));
                // Log.e("TEST","Current Acid :"+ParseACId);
                final String parsedurl = App.getInstance().GetCustonDomain(ParseACId,url);
                Log.i(TAG,  parsedurl);

                final JSONObject finalparams = app.GetCommonGSON("");

                if(!isDestroyed) {
                    if(app.isTokenExpired())
                    {
                        Log.i(TAG, RandomNumber +":"+"TokenExpired");
                        ECBAuthToken.GetECBToken("", new ECBAuthToken.OnECBAuthTokenListner() {
                            @Override
                            public void onECBAuthToken(String Token, JSONObject ResultJ) {
                                if (!isDestroyed) {
                                    if (Token != null && Token.length() > 0) {
                                        CustomAuthRequest _this = getCurrentObj();
                                        headers = _this.getHeaders(Token, ACId);
                                        try {
                                            //Log.i("mitul", url);
                                            if (RParams != null) {
                                                //Log.i("mitul", RParams.toString());
                                                Iterator i2 = RParams.keys();
                                                String tmp_key;
                                                while (i2.hasNext()) {
                                                    tmp_key = (String) i2.next();
                                                    finalparams.put(tmp_key, RParams.get(tmp_key));
                                                }
                                            }
                                            Log.i(TAG, RandomNumber +":"+finalparams.toString());
                                            _this.setCustomRequest(new CustomRequest(method, parsedurl, finalparams, headers, reponseListener, errorListener));
                                            _this.onCreateFinished(_this);
                                        } catch (JSONException ex) {
                                            DataGlobal.SaveLog(CustomAuthRequest.class.getSimpleName(), ex);
                                            if(errorListener != null) {
                                                VolleyError error = new VolleyError("Authentication Failed");
                                                errorListener.onErrorResponse(error);
                                            }
                                        }
                                    } else {
                                        if(errorListener != null) {
                                            VolleyError error = new VolleyError("Authentication Failed");
                                            errorListener.onErrorResponse(error);
                                        }
                                    }
                                }
                            }
                        });
                    }
                   else
                    {
                       // Log.i(TAG, RandomNumber +":"+"no-TokenExpired");
                        try {
                            String  Token = App.getInstance().getAccessToken();
                            Log.e("TEST","Get TOKEN :"+Token);
                            headers = this.getHeaders(Token,ACId);
                            //Log.i("mitul", url);
                            if (RParams != null) {
                                //Log.i("mitul", RParams.toString());
                                Iterator i2 = RParams.keys();
                                String tmp_key;
                                while (i2.hasNext()) {
                                    tmp_key = (String) i2.next();
                                    finalparams.put(tmp_key, RParams.get(tmp_key));
                                }
                            }
                            Log.i(TAG,RandomNumber +":"+ finalparams.toString());
                            this.customRequest = new CustomRequest(method, parsedurl, finalparams, headers, reponseListener, errorListener);
                            this.onCreateFinished(this);
                        } catch (JSONException ex) {
                            DataGlobal.SaveLog(CustomAuthRequest.class.getSimpleName(), ex);
                            if(errorListener != null){
                                VolleyError error = new VolleyError("Authentication Failed");
                                errorListener.onErrorResponse(error);
                            }
                        }
                    }
                }
            }
            else {
                if(errorListener != null) {
                    VolleyError error = new VolleyError("Authentication Failed");
                    errorListener.onErrorResponse(error);
                }
            }
        } else {
            if(errorListener != null) {
                VolleyError error = new VolleyError("Please check your Internet connection");
                errorListener.onErrorResponse(error);
            }
        }
    }

    protected void onCreateFinished(CustomAuthRequest request) {
        App.getInstance().addToRequestQueue(request);
    }

    public CustomAuthRequest(String url, JSONObject params, long ACId, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        this(Request.Method.POST, url, params,ACId, reponseListener, errorListener);
    }


    protected JSONObject getParams() {
        return null;
    }

    protected Map<String, String> getHeaders(String Token,long ACId) {
        Log.e("TEST","Token :"+Token);
        try {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json; charset=UTF-8");
            headers.put("Authorization", "Bearer "+ Token);
            if(!(ACId > 0)) ACId = App.getInstance().getCurrentACId();
            headers.put("acid", Long.toString(ACId));
            headers.put("did", App.getInstance().getDeviceId());
            Log.w(TAG, "getHeaders: "+headers );
            return headers;
        } catch (Exception ex) {
            DataGlobal.SaveLog(TAG, ex);
            return null;
        }
    }

}
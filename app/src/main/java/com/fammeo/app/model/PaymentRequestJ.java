package com.fammeo.app.model;

import com.fammeo.app.app.App;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.List;

public class PaymentRequestJ extends CommonJsonClass{
     public long Id;
    public String Note;
    public String M;

    public  DateTime CD;
    public  boolean IP;
    public  double A;

    public  AccountJ AccObj;
    public  UserSmallJ user;
    public  List<PaymentRequestObjectJ> ROjs;
    public  List<Long> Ps;

    public static PaymentRequestJ getJSON(String  json) {
       return App.getInstance().getGSON().fromJson(json, PaymentRequestJ.class);
    }

    public static List<PaymentRequestJ> getJSONList(String json) {
        Type listType = new TypeToken<List<PaymentRequestJ>>(){}.getType();
        return App.getInstance().getGSON().fromJson(json,listType);
    }





}

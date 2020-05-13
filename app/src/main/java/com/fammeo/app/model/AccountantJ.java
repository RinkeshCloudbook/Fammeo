package com.fammeo.app.model;

import com.fammeo.app.app.App;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AccountantJ extends CommonJsonClass{
    public  long AccountantId;
    public  String Name;
    public   String Image;
    public   String Role;
    public  UserSmallJ user;

    public static AccountantJ getJSON(String  json) {
       return App.getInstance().getGSON().fromJson(json, AccountantJ.class);
    }

    public static List<AccountantJ> getJSONList(String json) {
        Type listType = new TypeToken<List<AccountantJ>>(){}.getType();
        return App.getInstance().getGSON().fromJson(json,listType);
    }


}

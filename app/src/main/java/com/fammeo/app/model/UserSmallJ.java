package com.fammeo.app.model;

import com.fammeo.app.app.App;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserSmallJ extends CommonJsonClass{
    public String Id ;
    public long ACId;
    public String FN  = "Temp";
    public String MN ;
    public String LN ;
    public String Email ;
    public String Note;
    public String S;
    public String T;
    public String I;
    public String PE;
    public String PPh;
    public String PPhC;
    public String RN;
    public long DACId;

    public static UserSmallJ getJSON(String  json) {
       return App.getInstance().getGSON().fromJson(json, UserSmallJ.class);
    }

    public static List<UserSmallJ> getJSONList(String json) {
        Type listType = new TypeToken<List<UserSmallJ>>(){}.getType();
        return App.getInstance().getGSON().fromJson(json,listType);
    }
}

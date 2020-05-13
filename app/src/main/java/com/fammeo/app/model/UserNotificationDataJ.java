package com.fammeo.app.model;

import com.fammeo.app.app.App;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserNotificationDataJ extends CommonJsonClass{
    public long ObjId;
    public  String N;
    public  String ObjIdStr;
    public  String ObjV;

    public static UserNotificationDataJ getJSON(String  json) {
       return App.getInstance().getGSON().fromJson(json, UserNotificationDataJ.class);
    }

    public static List<UserNotificationDataJ> getJSONList(String json) {
        Type listType = new TypeToken<List<UserNotificationDataJ>>(){}.getType();
        return App.getInstance().getGSON().fromJson(json,listType);
    }

    @Override
    public  String toString() {
        return  App.getInstance().getGSON().toJson(this );
    }


}

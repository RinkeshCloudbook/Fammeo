package com.fammeo.app.model;

import com.fammeo.app.app.App;

public class MenuJ extends CommonJsonClass{
    public int PC ;
    public int NC;

    public MenuJ()
    {
        this.PC = 0;
        this.NC = 0;
    }
    public static MenuJ getJSON(String  json) {
       return App.getInstance().getGSON().fromJson(json, MenuJ.class);
    }



}

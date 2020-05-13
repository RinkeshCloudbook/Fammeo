package com.fammeo.app.common;

import static com.fammeo.app.constants.Constants.*;

/**
 * Created by Mitul on 01-05-2017.
 */

public class DataText {
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public  static String  GetImagePath(String path)
    {
        if(path != null)
        {
                if (path.indexOf("~/glogo/") == 0) { return GooglePath + path.replace("~/", ""); }
                else if (path.indexOf("http") != -1) { return path; }
                else {
                    return WEB_SITE + path.replace("~", "");
                }
        }
        return  "";
    }






}

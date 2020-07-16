package com.jahezly.tags;

import android.os.Environment;

public class Tags {

    //https://documenter.getpostman.com/view/11255769/T1DiFfCb?fbclid=IwAR2D4mP_JQesr0tOQHAO9U2K7DPk2u0ZelTyP2kPp1V5uDgpLa7TuOf3Qws&version=latest
    public static String base_url = " http://gahzly.creativeshare.sa/";
    public static final String IMAGE_URL= base_url+"storage/";
    public static final String session_login = "login";
    public static final String session_logout = "logout";
    public static final String audio_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Nadres";
    public static final String not_tag = "jahizly_not_tag";
    public static final int not_id = 3253;

}

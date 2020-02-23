package com.chops.android_chatting;

import android.graphics.Bitmap;

public class Data_Friends {

    public Bitmap mbitFriendProfile;
    public String mstrFriendEmail;
    //test
    public Data_Friends()
    {

    }
    public Data_Friends(Bitmap p_bitProfile, String p_strEmail)
    {
        this.mbitFriendProfile = p_bitProfile;
        this.mstrFriendEmail = p_strEmail;
    }
}

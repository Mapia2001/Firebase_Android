package com.coin.coinflipt.Component;

import android.net.Uri;

public class Friend {
    public String user_name;
    public String user_email;
    public String user_phonenumber;
    public Uri user_photo;
    public Friend(){
    }

    public Friend(String name, String email, String phonenumber, Uri photo){
        this.user_name = name;
        this.user_email = email;
        this.user_phonenumber = phonenumber;
        this.user_photo = photo;
    }

    public String getUser_email() {
        return user_email;
    }


    public String getUser_name() {
        return user_name;
    }

    public String getUser_phonenumber() {
        return user_phonenumber;
    }

    public Uri getUser_photo() {
        return user_photo;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_phonenumber(String user_phonenumber) {
        this.user_phonenumber = user_phonenumber;
    }

    public void setUser_photo(Uri user_photo) {
        this.user_photo = user_photo;
    }
}

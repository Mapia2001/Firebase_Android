package com.coin.coinflipt.Component;

import android.net.Uri;

public class Mail {

    public String invite_email;
    public String invite_date;
    public String invite_coin;
    public String invite_status;
    public Mail(){

    }

    public Mail(String invite_email, String invite_date, String invite_coin, String invite_status) {
        this.invite_email = invite_email;
        this.invite_date = invite_date;
        this.invite_coin = invite_coin;
        this.invite_status = invite_status;
    }


    public String getInvite_email() {
        return invite_email;
    }

    public String getInvite_date() {
        return invite_date;
    }

    public String getInvite_coin() {
        return invite_coin;
    }

    public String getInvite_status() {return invite_status;}

    public void setInvite_email(String invite_email) {
        this.invite_email = invite_email;
    }

    public void setInvite_date(String invite_date) {
        this.invite_date = invite_date;
    }

    public void setInvite_coin(String invite_coin) {
        this.invite_coin = invite_coin;
    }

    public void setInvite_status(String invite_status) { this.invite_status = invite_status; }
}

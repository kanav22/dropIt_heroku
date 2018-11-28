package com.example.kanav.Heroku;

import java.util.ArrayList;

public class user
{
    String userName;
    String dp_url;
    ArrayList<String> pics_urls;

    public user(String userName, String dp_url, ArrayList<String> pics_urls) {
        this.userName = userName;
        this.dp_url = dp_url;
        this.pics_urls = pics_urls;
    }

    public String getUserName() {
        return userName;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDp_url() {
        return dp_url;
    }

    public void setDp_url(String dp_url) {
        this.dp_url = dp_url;
    }

    public ArrayList<String> getPics_urls() {
        return pics_urls;
    }

    public void setPics_urls(ArrayList<String> pics_urls) {
        this.pics_urls = pics_urls;
    }


}

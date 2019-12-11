package com.example.twitterretrofit;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("user")
    private userDetails user;

    @SerializedName("text")
    private String text;

    public userDetails getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}

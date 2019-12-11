package com.example.twitterretrofit;

public class ApiClass {
    public static TwitterApi twitterApi;
    public  static OAuthToken oAuthToken;

    public TwitterApi getTwitterApi() {
        return twitterApi;
    }

    public OAuthToken getoAuthToken() {
        return oAuthToken;
    }
}

package com.example.twitterretrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitterApi {

    String BASE= "https://api.twitter.com/";

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<OAuthToken> postCredentials(@Field("grant_type") String grantType);

    @GET("1.1/users/show.json")
    Call<userDetails> getuserDetails(@Query("screen_name") String name);


    @GET("1.1/statuses/user_timeline.json")
    Call<List<Post>> getPosts(
            @Query("count") final int count,
            @Query("screen_name") final String screenName
    );

}

package com.example.twitterretrofit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
                                            //get it from https://developer.twitter.com/en/apps/
    private String credentials = Credentials.basic("YOUR_API_KEY", "YOUR_API_SECRET_KEY");


    ApiClass apiClass;
    Button mRequestToken,timeline;
    Button mRequestUserDetails;
    TextView mUrl;
    TextView mName;
    EditText mUsername;
    TextView mDescription;
    TextView mLocation;
    ImageView mProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mUsername = (EditText)findViewById(R.id.username_edittext);

        mName = (TextView) findViewById(R.id.name_textview);

        mUrl = (TextView) findViewById(R.id.url_textview);

        mLocation = (TextView) findViewById(R.id.location_textview);

        mDescription = (TextView) findViewById(R.id.description_textview);

        mRequestUserDetails = (Button) findViewById(R.id.request_user_details_button);

        mRequestToken = (Button) findViewById(R.id.request_token_button);
        timeline = (Button)findViewById(R.id.timeline_btn);
        mProfilePic = (ImageView) findViewById(R.id.extra);

        createTwitterApi();
    }

    private void createTwitterApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request originalRequest= chain.request();
                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        apiClass.oAuthToken!=null ? apiClass.oAuthToken.getAuthorization() : credentials);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TwitterApi.BASE)
                .client(okHttpClient)
                .addConverterFactory( GsonConverterFactory.create(gson) )
                .build();

        apiClass.twitterApi = retrofit.create(TwitterApi.class);

    }


    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.request_token_button:
                 postCredentials();
                 break;
            case R.id.request_user_details_button:
                if( mUsername.getText().toString().isEmpty() )
                {
                    Toast.makeText(this, "Please provide a username", Toast.LENGTH_LONG).show();
                }
                else
                getUserDetails();
                break;
        }

    }

    private void getUserDetails() {
        Call<userDetails> call = apiClass.twitterApi.getuserDetails(mUsername.getText().toString());

        call.enqueue(new Callback<userDetails>() {
            @Override
            public void onResponse(Call<userDetails> call, retrofit2.Response<userDetails> response) {
                if(response.isSuccessful())
                {
                    userDetails User= response.body();
                    mName.setText(User.getName()==null ? "No Value" : User.getName());
                    mUrl.setText(User.getUrl() ==null ? "No Value" : User.getUrl());
                    mLocation.setText(User.getLocation()==null ? "No Value" : User.getLocation());
                    mDescription.setText(User.getDescription() ==null ? "No Value" : User.getDescription());
                    Picasso.get().load(User.getprofile_image_url_https()).into(mProfilePic);

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Failure while requesting token", Toast.LENGTH_LONG).show();
                    Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<userDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void postCredentials() {
        Call<OAuthToken> tokenCall = apiClass.twitterApi.postCredentials("client_credentials");
        tokenCall.enqueue(new Callback<OAuthToken>() {
            @Override
            public void onResponse(Call<OAuthToken> call, retrofit2.Response<OAuthToken> response) {
                if(response.isSuccessful())
                {
                    mRequestToken.setEnabled(false);
                    mRequestUserDetails.setEnabled(true);
                    mUsername.setEnabled(true);
                    mName.setEnabled(true);
                    timeline.setEnabled(true);
                    apiClass.oAuthToken = response.body();
                    //mExtra.append( oAuthToken.getAuthorization() +" ");
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Failure while requesting token", Toast.LENGTH_LONG).show();
                    Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<OAuthToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
        //Toast.makeText(MainActivity.this, "Hello There", Toast.LENGTH_LONG).show();
    }

    public void TimeLine(View view) {
        Intent intent = new Intent(MainActivity.this, TImeline.class);
        if(mUsername.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please provide a username", Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra("key",mUsername.getText().toString());
        startActivity(intent);
    }
}









/*
* Dumping area
* //        Call<List<Post>> call= apiClass.twitterApi.getPosts(apiClass.oAuthToken.getAccessToken());
//        final String[] val = new String[1];
//        call.enqueue(new Callback<List<Post>>() {
//            String content;
//            @Override
//            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {
//                if(response.isSuccessful())
//                {
//                    int cnt=10;
//                    List<Post> posts = response.body();
//                    for (Post post : posts) {
//                        content += post.getUser().getName() + "\n";
//                        content += post.getText() + "\n\n";
//                        //timeline.append(content);
//                        cnt--;
//                        if(cnt==0)break;
//                    }
//                }
//                val[0] =content;
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Failure while requesting token", Toast.LENGTH_LONG).show();
//                //timeline.setText("Code: "+response.code()+"\n\n");
//            }
//
//        });
//        intent.putExtra("content", val[0]);
//
//
////         call.enqueue(new Callback<List<Post>>() {
////            @Override
////            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
////
////                if(response.isSuccessful())
////                {
////
////
////                }
////                else
////                {
////
////                }
////            }
////
////             @Override
////             public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {
////
////             }
////
////             @Override
////            public void onFailure(Call<List<Post>> call, Throwable t) {
////                //timeline.setText("Error: " + t.getMessage());
////            }
////        });

* */
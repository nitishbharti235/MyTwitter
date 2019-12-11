package com.example.twitterretrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TImeline extends AppCompatActivity {
    private ListView ttimeline;
    //private EditText mcount;
    ApiClass apiClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String content= extras.getString("key");

        ttimeline=(ListView)findViewById(R.id.list_timeline);

        //mcount=(EditText)findViewById(R.id.count);
        //ttimeline.setText("WHY?");

         int count=30;//no of tweets to show

//        if(mcount.getText().toString().isEmpty() == false)
//        {
//            count= Integer.valueOf(mcount.getText().toString());
//        }

        //post == tweets
         Call<List<Post>> call= ApiClass.twitterApi.getPosts(count,content);
         String [] tweets= new String[count];

         for(int i=0;i<count;i++)
             tweets[i]="no content, scroll to update";
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful())
                {
                    //int cnt=count;
                    int i=0;
                    List<Post> posts = response.body();
                    for (Post post : posts) {
                        String content = "";
                        //content += post.getUser().getName() + "\n";
                        content += post.getText() ;
                        tweets[i++]=content;
                        //ttimeline.append(content);

                    }
                }
                else
                {
                    Toast.makeText(TImeline.this, "Failure while requesting token", Toast.LENGTH_LONG).show();
                    //ttimeline.append("\n"+ String.valueOf(response.code()) );
                    //timeline.setText("Code: "+response.code()+"\n\n");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                //ttimeline.setText("Error: " + t.getMessage());
                Toast.makeText(TImeline.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listview,tweets);

        //ListView listView = (ListView) findViewById(R.id.list_timeline);
        ttimeline.setAdapter(adapter);

    }

}







/*
* Dumping area
* //        Call<userDetails> U = apiClass.twitterApi.getuserDetails("nitishbh0641");
//        U.enqueue(new Callback<userDetails>() {
//            @Override
//            public void onResponse(Call<userDetails> call, Response<userDetails> response) {
//                if(response.isSuccessful())
//                {
//                    //Log.d("ResponseA", "Code: " + String.valueOf(response.code()) + "Message: " + apiClass.getoAuthToken().getAuthorization());
//                    Log.d("ResponseA", "Code: " + String.valueOf(response.code()) + "Message: " );
//                    userDetails Us = response.body();
//                    ttimeline.append(Us.getName()+"\n"+Us.getId()+"\n\n");
//                }
//                else
//                {
//                        int x= response.code();
//                        //timeline.setText("Error: " + String.valueOf(x));
//                    //ttimeline.setText("HERE YOU ARE");
//                    //Log.d("Response", "Code: " + String.valueOf(response.code()) + "Message: " + apiClass.getoAuthToken().getAuthorization());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<userDetails> call, Throwable t) {
//
//            }
//        });

* */
package com.example.mostafa.fakkarny;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mostafa on 11/22/2016.
 */

public class ShowActivity extends AppCompatActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.show_container ,new ShowFragment()).commit();
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}

package com.example.mostafa.fakkarny;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mostafa on 11/21/2016.
 */

public class SearchActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.search_container ,new SearchFragment()).commit();
        }
    }
}

package com.example.mostafa.fakkarny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mostafa on 11/19/2016.
 */

public class AddActivity extends AppCompatActivity {
    private final String ACTIVITY_TYPE = "NameActivity";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(savedInstanceState == null){
            Intent intent = getIntent();
            String message = intent.getStringExtra(ACTIVITY_TYPE);

            Bundle tempData = new Bundle();
            tempData.putString(ACTIVITY_TYPE, message);
            AddFragment fragment = new AddFragment();
            fragment.setArguments(tempData);
            getSupportFragmentManager().beginTransaction().add(R.id.add_container , fragment).commit();
        }
    }
}

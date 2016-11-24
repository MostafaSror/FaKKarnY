package com.example.mostafa.fakkarny;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private boolean mTwoPane;
    private final String ACTIVITY_TYPE = "NameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container , new MainFragment()).commit();
        }

        if (findViewById(R.id.right_container) != null) {

            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.right_container, new ShowFragment()).commit();

            }
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }
    }
    @Override
    public void onItemSelected(String buttonPressed) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            switch(buttonPressed) {
                case "ADD":
                    Bundle tempData = new Bundle();
                    tempData.putString(ACTIVITY_TYPE, "twopane");
                    AddFragment fragment = new AddFragment();
                    fragment.setArguments(tempData);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.right_container , fragment).commit();
                    break;
                case "SHOW":
                    tempData = new Bundle();
                    tempData.putString(ACTIVITY_TYPE, "twopane");
                    ShowFragment fragment1 = new ShowFragment();
                    fragment1.setArguments(tempData);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.right_container , fragment1).commit();
                    break;
                case "SEARCH":
                    tempData = new Bundle();
                    tempData.putString(ACTIVITY_TYPE, "twopane");
                    SearchFragment fragment2 = new SearchFragment();
                    fragment2.setArguments(tempData);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.right_container , fragment2).commit();
                    break;
                default:
                    Log.e("MainActi onItemSelected","ERROR");
            }
        } else {
            switch(buttonPressed) {
                case "ADD":
                    Intent intent = new Intent(this,AddActivity.class).putExtra(ACTIVITY_TYPE, "onepane");
                    startActivity(intent);
                    break;
                case "SHOW":
                    intent = new Intent(this, ShowActivity.class).putExtra(ACTIVITY_TYPE, "onepane");
                    startActivity(intent);
                    break;
                case "SEARCH":
                    intent = new Intent(this , SearchActivity.class).putExtra(ACTIVITY_TYPE, "onepane");
                    startActivity(intent);
            }
        }
    }

}

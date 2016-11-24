package com.example.mostafa.fakkarny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mostafa.fakkarny.data.FakkarnyContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mostafa on 11/21/2016.
 */

public class SearchFragment extends Fragment {

    private static final String SPINNERS_SHARED_PREFS_NAME = "MY_SPINNERS_SHARED_PREF"; //Shared preferences for spinner view
    private static final String RADIO_SHARED_PREFs_NAME ="MY_RADIO_SHARED_PREF" ;//Shared preferences for radio group
    private static final String SEARCH_URI_SHARED_PREFs_NAME ="MY_SEARCH_SHARED_PREF" ;//Shared preferences for search uri

    SharedPreferences sharedprefs;

    List<String> spinnerArray =  new ArrayList<String>();
    List<String> radioArray =  new ArrayList<String>();

    Uri uri; //search values are passed to main activity using URI, then from main activity passed to provider
    String spinnerFirstItem = "Select a type" ;
    String categoryFirstItem = "Select a category" ;

    EditText searchName;
    Spinner searchSpinner;
    Spinner searchRadio;
    Button searchButton;

    public SearchFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        spinnerArray = getArray(SPINNERS_SHARED_PREFS_NAME);
        radioArray = getArray(RADIO_SHARED_PREFs_NAME);
        sharedprefs = getActivity().getSharedPreferences(SEARCH_URI_SHARED_PREFs_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstancestate){
        View rootView = inflater.inflate(R.layout.fragment_search , container , false);
        searchName = (EditText) rootView.findViewById(R.id.search_name);
        searchRadio= (Spinner) rootView.findViewById(R.id.search_radio);
        searchSpinner = (Spinner) rootView.findViewById(R.id.search_spinner);
        searchButton = (Button) rootView.findViewById(R.id.done_button);

        ArrayAdapter<String> radioAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, radioArray);
        radioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchRadio.setAdapter(radioAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter);



        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!searchName.getText().toString().equals("")){
                    // name  "name constant"   2
                    uri = FakkarnyContract.itemsEntry.CONTENT_URI.buildUpon()
                            .appendPath(searchName.getText().toString()).appendPath("name").build();

                    if(searchRadio != null && searchRadio.getSelectedItem() != categoryFirstItem){
                        //name  "name constant"  category  3
                        uri = uri.buildUpon().appendPath(searchRadio.getSelectedItem().toString()).build();

                        if(searchSpinner != null && searchSpinner.getSelectedItem() != spinnerFirstItem){
                            //name  "name constant"  category  type  4
                            uri = uri.buildUpon().appendPath(searchSpinner.getSelectedItem().toString()).build();

                        }
                    }else if(searchSpinner != null && searchSpinner.getSelectedItem() !=spinnerFirstItem){
                        // name  "name constant"  type  "type constant"  4
                        uri = uri.buildUpon().appendPath(searchSpinner.getSelectedItem().toString()).appendPath("type").build();
                    }

                }else if(searchRadio != null && searchRadio.getSelectedItem() != categoryFirstItem){
                    //category  "category constant"
                    uri = FakkarnyContract.itemsEntry.CONTENT_URI.buildUpon()
                            .appendPath(searchRadio.getSelectedItem().toString()).appendPath("category").build();

                    if(searchSpinner != null && searchSpinner.getSelectedItem() != spinnerFirstItem){
                        //category  "category constant"  type
                        uri = uri.buildUpon().appendPath(searchSpinner.getSelectedItem().toString()).build();
                    }
                }else if(searchSpinner != null && searchSpinner.getSelectedItem() != spinnerFirstItem){
                    uri = FakkarnyContract.itemsEntry.CONTENT_URI.buildUpon()
                            .appendPath(searchSpinner.getSelectedItem().toString()).appendPath("type").build();
                }else{
                    Toast.makeText(getContext(),"please add search keywords",Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedprefs.edit();
                editor.putString("searchUri",uri.toString() );
                editor.commit();

                Intent intent = new Intent (getActivity() , ShowActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    //to save data from shared prefs, used for adding gategories and types dynamically
    public boolean saveArray(String filename , List<String> mvalues) {
        SharedPreferences sp = getActivity().getSharedPreferences(filename, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(mvalues);
        mEdit1.putStringSet("list", set);
        return mEdit1.commit();
    }
    //to get data from shared prefs, used for adding gategories and types dynamically
    public ArrayList<String> getArray( String filename) {
        SharedPreferences sp = getActivity().getSharedPreferences(filename, Activity.MODE_PRIVATE);
        //NOTE: if shared preference is null, the method return empty Hashset and not null
        Set<String> set = sp.getStringSet("list", new HashSet<String>());

        return new ArrayList<String>(set);
    }

}

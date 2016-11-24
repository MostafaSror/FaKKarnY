package com.example.mostafa.fakkarny;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;

import com.example.mostafa.fakkarny.data.FakkarnyContract;

/**
 * Created by Mostafa on 11/19/2016.
 */

public class MainFragment extends Fragment {

    Button add_button;
    Button show_button;
    Button search_button;

    public MainFragment(){}

    public interface Callback {

        public void onItemSelected(String dateUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_main , container , false );

        add_button = (Button) rootView.findViewById(R.id.addEntityButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((Callback) getActivity()).onItemSelected("ADD");

            }
        });

        show_button = (Button) rootView.findViewById(R.id.mainShowButton);
        show_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((Callback) getActivity()).onItemSelected("SHOW");

            }
        });

        search_button = (Button) rootView.findViewById(R.id.mainSearchButton);
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((Callback) getActivity()).onItemSelected("SEARCH");

            }
        });

        return rootView;
    }
}

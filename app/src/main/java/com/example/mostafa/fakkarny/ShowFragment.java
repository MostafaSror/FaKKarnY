package com.example.mostafa.fakkarny;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.mostafa.fakkarny.data.FakkarnyContract;

/**
 * Created by Mostafa on 11/22/2016.
 */

public class ShowFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String SEARCH_URI_SHARED_PREFs_NAME ="MY_SEARCH_SHARED_PREF" ;//Shared preferences for search uri
    SharedPreferences sharedpreferences;

    GridView gridView;
    ImageAdapter gridAdapter;

    private static final int ITEMS_LOADER = 0;

    public ShowFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(SEARCH_URI_SHARED_PREFs_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_show , container , false );

        gridAdapter = new ImageAdapter(getActivity(), null, 0);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    Intent intent = new Intent(getActivity(), DetailsActivity.class)
                            .setData(FakkarnyContract.itemsEntry
                                    .buildItemNameUri(cursor.getString( cursor.getColumnIndex(
                                            FakkarnyContract.itemsEntry.COLUMN_NAME))));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ITEMS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri itemsUri;
        if(sharedpreferences.contains("searchUri")){
            itemsUri = Uri.parse( sharedpreferences.getString("searchUri" , null) );
            sharedpreferences.edit().clear().apply();
        }else
            itemsUri = FakkarnyContract.itemsEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                itemsUri,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        gridAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        gridAdapter.swapCursor(null);
    }
}

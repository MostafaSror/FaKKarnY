package com.example.mostafa.fakkarny;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.fakkarny.data.FakkarnyContract;

import java.io.ByteArrayInputStream;

/**
 * Created by Mostafa on 11/22/2016.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public DetailFragment(){}

    TextView nameTextView;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DATA_COLUMNS = {
            FakkarnyContract.itemsEntry.TABLE_NAME + "." + FakkarnyContract.itemsEntry._ID,
            FakkarnyContract.itemsEntry.COLUMN_NAME,
            FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE,
            FakkarnyContract.itemsEntry.COLUMN_DATE_STORED,
            FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY,
            FakkarnyContract.itemsEntry.COLUMN_ITEM_PHOTO,
            FakkarnyContract.itemsEntry.COLUMN_PLACE_DETAILS,
            FakkarnyContract.itemsEntry.COLUMN_LATITUDE,
            FakkarnyContract.itemsEntry.COLUMN_LONGITUDE


    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_ID = 0;
    private static final int COL_NAME = 1;
    private static final int COL_TYPE = 2;
    private static final int COL_DATE = 3;
    private static final int COL_CATEGORY = 4;
    private static final int COL_PHOTO = 5;
    private static final int COL_DETAILS = 6;
    private static final int COL_LATITUDE = 7;
    private static final int COL_LONGITUDE = 8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ImageButton deleteButton = (ImageButton) rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Uri uri = FakkarnyContract.itemsEntry.buildItemNameUri(nameTextView.getText().toString());
                getContext().getContentResolver().delete(uri,null,null);
                Toast.makeText(getContext(),"Deleted",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                DATA_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }

        ImageView itemPhoto = (ImageView)getView().findViewById(R.id.image_of_item);
        nameTextView = (TextView)getView().findViewById(R.id.name_text_view);
        TextView typeTextView = (TextView)getView().findViewById(R.id.type_text_view);
        TextView dateTextView = (TextView)getView().findViewById(R.id.date_text_view);
        TextView placeTextView = (TextView)getView().findViewById(R.id.place_text_view);
        TextView detailsTextView = (TextView)getView().findViewById(R.id.details_text_view);
        TextView longitudeTextView = (TextView)getView().findViewById(R.id.longitude_text_view);
        TextView latitudeTextView = (TextView)getView().findViewById(R.id.latitude_text_view);

        nameTextView.setText(data.getString(COL_NAME));
        typeTextView.setText(data.getString(COL_TYPE));
        dateTextView.setText(data.getString(COL_DATE));
        placeTextView.setText(data.getString(COL_CATEGORY));
        detailsTextView.setText(data.getString(COL_DETAILS));
        latitudeTextView.setText(data.getString(COL_LATITUDE));
        longitudeTextView.setText(data.getString(COL_LONGITUDE));

        byte[] outImage = data.getBlob(data.getColumnIndex(FakkarnyContract.itemsEntry.COLUMN_ITEM_PHOTO));
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        itemPhoto.setImageBitmap(theImage);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }


}

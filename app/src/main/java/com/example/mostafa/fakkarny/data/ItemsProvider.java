package com.example.mostafa.fakkarny.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Mostafa on 11/20/2016.
 */

public class ItemsProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ItemsDBHelper itemsOpenHelper ;

    static final int ITEMS = 100;
    static final int SINGLE_ITEM = 104;
    static final int ONE_SEARCH = 101;
    static final int TWO_SEARCH = 102;
    static final int THREE_SEARCH = 103;

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FakkarnyContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FakkarnyContract.PATH_ITEMS_TABLE , ITEMS);
        matcher.addURI(authority, FakkarnyContract.PATH_ITEMS_TABLE + "/*" , SINGLE_ITEM);
        matcher.addURI(authority, FakkarnyContract.PATH_ITEMS_TABLE + "/*/*" ,ONE_SEARCH );
        matcher.addURI(authority, FakkarnyContract.PATH_ITEMS_TABLE + "/*/*/*",TWO_SEARCH);
        matcher.addURI(authority, FakkarnyContract.PATH_ITEMS_TABLE + "/*/*/*/*", THREE_SEARCH);
        return matcher;
    }


    @Override
    public boolean onCreate(){
        itemsOpenHelper = new ItemsDBHelper(getContext());
        return true ;
    }

    @Override
    public String getType(Uri uri){

        final int match = sUriMatcher.match(uri);

        switch (match){
            case ITEMS:
                return FakkarnyContract.itemsEntry.CONTENT_TYPE;
            case SINGLE_ITEM:
                return FakkarnyContract.itemsEntry.CONTENT_ITEM_TYPE;
            case ONE_SEARCH:
                return FakkarnyContract.itemsEntry.CONTENT_TYPE;
            case TWO_SEARCH:
                return FakkarnyContract.itemsEntry.CONTENT_TYPE;
            case THREE_SEARCH:
                return FakkarnyContract.itemsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){

        final SQLiteDatabase db = itemsOpenHelper.getReadableDatabase();
        Cursor retCursor;
        String temp;

        switch (sUriMatcher.match(uri)){
            case ITEMS:
                retCursor = db.query(
                        FakkarnyContract.itemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SINGLE_ITEM:
                selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                        "." + FakkarnyContract.itemsEntry.COLUMN_NAME + " = ? ";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                retCursor = db.query(
                        FakkarnyContract.itemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case ONE_SEARCH:
                temp = uri.getPathSegments().get(2);
                switch(temp) {
                    case "name": //name  "name constant"
                        selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                                "." + FakkarnyContract.itemsEntry.COLUMN_NAME + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1)};
                        break;

                    case "category" : //category  "category constant"
                        selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                                "." + FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1)};
                        break;
                    case "type": //type  "type constant"
                        selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                                "." + FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1)};
                        break;}
                retCursor = db.query(
                        FakkarnyContract.itemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case TWO_SEARCH: //name  "name constant"  category   OR   category  "category constant"  type
                temp = uri.getPathSegments().get(2);
                switch(temp) {
                    case "name":  //name  "name constant"  category
                        selection = FakkarnyContract.itemsEntry.TABLE_NAME+
                                "." + FakkarnyContract.itemsEntry.COLUMN_NAME + " = ? AND " +
                                FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1) , uri.getPathSegments().get(3) };
                        break;

                    case "category" :  //category  "category constant"  type
                        selection = FakkarnyContract.itemsEntry.TABLE_NAME+
                                "." + FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY + " = ? AND " +
                                FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1) , uri.getPathSegments().get(3)};
                        break;}

                retCursor = db.query(
                        FakkarnyContract.itemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case THREE_SEARCH:  //name  "name constant"  type  "type constant"   OR   name  "name constant"  category type
                temp = uri.getPathSegments().get(4);
                switch (temp) {
                    case "type":

                        selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                                "." + FakkarnyContract.itemsEntry.COLUMN_NAME + " = ? AND " +
                                FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1), uri.getPathSegments().get(3)};
                        break;

                    default:
                        selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                                "." + FakkarnyContract.itemsEntry.COLUMN_NAME + " = ? AND " +
                                FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY + " = ? AND " +
                                FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE + " = ? ";
                        selectionArgs = new String[]{uri.getPathSegments().get(1), uri.getPathSegments().get(3)
                                , uri.getPathSegments().get(4)};

                }
                retCursor = db.query(
                        FakkarnyContract.itemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){

        final SQLiteDatabase db = itemsOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case ITEMS: {
                long _id = db.insert(FakkarnyContract.itemsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FakkarnyContract.itemsEntry.buildItemUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = itemsOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowDeleted;
        switch (match) {
            case SINGLE_ITEM:
                selection = FakkarnyContract.itemsEntry.TABLE_NAME +
                        "." + FakkarnyContract.itemsEntry.COLUMN_NAME + " = ? ";
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                rowDeleted = db.delete(
                        FakkarnyContract.itemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }



}

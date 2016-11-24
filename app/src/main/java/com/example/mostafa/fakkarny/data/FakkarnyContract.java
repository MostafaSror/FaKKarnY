package com.example.mostafa.fakkarny.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mostafa on 11/20/2016.
 */

public class FakkarnyContract {

    // The "Content authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.example.mostafa.fakkarny";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths
    public static final String PATH_ITEMS_TABLE = "items";

    //Inner class that defines the contents of the popular movies table
    public static final class itemsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS_TABLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS_TABLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS_TABLE;

        public static final String TABLE_NAME = "items";

        public static final String COLUMN_ITEM_TYPE = "type";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ITEM_PHOTO = "photo";
        public static final String COLUMN_DATE_STORED = "date";
        public static final String COLUMN_PLACE_CATEGORY = "place_category";
        public static final String COLUMN_PLACE_DETAILS = "place_details";
        public static final String COLUMN_LATITUDE = "place_latitude";
        public static final String COLUMN_LONGITUDE = "place_longitude";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildItemNameUri(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();

        }

    }


}
package com.example.mostafa.fakkarny.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mostafa.fakkarny.data.FakkarnyContract;

/**
 * Created by Mostafa on 11/20/2016.
 */

public class ItemsDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "items.db";

    public ItemsDBHelper( Context context){
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + FakkarnyContract.itemsEntry.TABLE_NAME + " (" +
                FakkarnyContract.itemsEntry._ID + " INTEGER PRIMARY KEY," +
                FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE + " TEXT NOT NULL, " +
                FakkarnyContract.itemsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FakkarnyContract.itemsEntry.COLUMN_ITEM_PHOTO + " BLOB NOT NULL, " +
                FakkarnyContract.itemsEntry.COLUMN_DATE_STORED + " TEXT NOT NULL, " +
                FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY + " TEXT NOT NULL, " +
                FakkarnyContract.itemsEntry.COLUMN_PLACE_DETAILS + " TEXT, " +
                FakkarnyContract.itemsEntry.COLUMN_LATITUDE + " DOUBLE, " +
                FakkarnyContract.itemsEntry.COLUMN_LONGITUDE + " DOUBLE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FakkarnyContract.itemsEntry.TABLE_NAME);
    }
}

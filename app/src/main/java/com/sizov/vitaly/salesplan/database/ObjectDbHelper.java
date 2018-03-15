package com.sizov.vitaly.salesplan.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ObjectDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "objectsDb";
    public static final String TABLE_OBJECTS = "objects";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SALESPLAN = "salesplan";
    public static final String KEY_CURRENTSALES = "currentsales";
    public static final String KEY_TOTALCURRENTSALES = "totalcurrentsales";
    public static final String KEY_PERCENTPROGRESS = "percentprogress";


    public ObjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "
                + TABLE_OBJECTS + "("
                + KEY_ID + " integer primary key,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_SALESPLAN + " TEXT,"
                + KEY_CURRENTSALES + " TEXT,"
                + KEY_TOTALCURRENTSALES + " TEXT,"
                + KEY_PERCENTPROGRESS + " TEXT"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_OBJECTS);
        onCreate(sqLiteDatabase);
    }

    public Cursor getAll() {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_OBJECTS, null);
    }
}

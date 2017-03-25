package com.example.lukaszjarka.cardatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MotoDatabaseOpenHelper extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "moto.db";

    private static String SQL_CREATE_TABLE = "CREATE TABLE " + CarsTableContract.TABLE_NAME + " ("
            + CarsTableContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CarsTableContract.COLUMN_MAKE + " TEXT, "
            + CarsTableContract.COLUMN_MODEL + " TEXT, "
            + CarsTableContract.COLUMN_IMAGE + " TEXT, "
            + CarsTableContract.COLUMN_YEAR + " INT)";

    private static String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + CarsTableContract.TABLE_NAME;

    public MotoDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(SQL_DROP_TABLE);
            onCreate(db);
        }
    }
}

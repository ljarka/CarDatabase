package com.example.lukaszjarka.cardatabase;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BetterMotoContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.lukaszjarka.cardatabase";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private MotoDatabaseOpenHelper openHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int CAR_MULTIPLE_ITEM = 1;
    public static final int CAR_SINGLE_ITEM = 2;

    static {
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME, CAR_MULTIPLE_ITEM);
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME + "/#", CAR_SINGLE_ITEM);
    }


    @Override
    public boolean onCreate() {
        openHelper = new MotoDatabaseOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case CAR_SINGLE_ITEM: {
                cursor = readableDatabase.query(CarsTableContract.TABLE_NAME,
                        projection,
                        CarsTableContract._ID + " = ? ",
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);

                break;
            }
            case CAR_MULTIPLE_ITEM: {
                cursor = readableDatabase.query(CarsTableContract.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            }

        }


        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = null;

        switch (uriMatcher.match(uri)) {
            case CAR_SINGLE_ITEM: {
                type = "vnd.android.cursor.item/vnd.com.example.lukaszjarka.cardatabase.cars";
                break;
            }
            case CAR_MULTIPLE_ITEM: {
                type = "vnd.android.cursor.dir/vnd.com.example.lukaszjarka.cardatabase.cars";
                break;
            }

        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case CAR_MULTIPLE_ITEM: {
                id = openHelper.getWritableDatabase()
                        .insert(CarsTableContract.TABLE_NAME, null, values);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedItems = 0;
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case CAR_SINGLE_ITEM: {
                deletedItems = writableDatabase.delete(CarsTableContract.TABLE_NAME,
                        CarsTableContract._ID + " = ?",
                        new String[]{uri.getLastPathSegment()});
                break;
            }
            case CAR_MULTIPLE_ITEM: {
                deletedItems = writableDatabase.delete(CarsTableContract.TABLE_NAME,
                        selection, selectionArgs);
                break;
            }

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updatedItems = 0;
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CAR_SINGLE_ITEM: {
                updatedItems = writableDatabase.update(CarsTableContract.TABLE_NAME,
                        values, CarsTableContract._ID + " = ?",
                        new String[]{uri.getLastPathSegment()});
                break;
            }
            case CAR_MULTIPLE_ITEM: {
                updatedItems = writableDatabase.update(CarsTableContract.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedItems;
    }
}

package com.example.lukaszjarka.cardatabase;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MotoContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.lukaszjarka.cardatabase";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int SINGLE_ITEM = 2;
    public static final int MULTIPLE_ITEMS = 1;

    static {
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME, MULTIPLE_ITEMS);
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME + "/#", SINGLE_ITEM);
    }

    private MotoDatabaseOpenHelper openHelper;

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
        if (uriMatcher.match(uri) == SINGLE_ITEM) {
            cursor = readableDatabase.query(CarsTableContract.TABLE_NAME, projection, CarsTableContract._ID + "= ?", new String[]{uri.getLastPathSegment()}, null, null, null);
        } else if (uriMatcher.match(uri) == MULTIPLE_ITEMS) {
            cursor = readableDatabase.query(CarsTableContract.TABLE_NAME, projection, selection, null, null, null, sortOrder);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        int deletedItems = 0;
        if (uriMatcher.match(uri) == SINGLE_ITEM) {
            deletedItems = writableDatabase.delete(CarsTableContract.TABLE_NAME,
                    CarsTableContract._ID + " = ?", new String[]{uri.getLastPathSegment()});
        } else if (uriMatcher.match(uri) == MULTIPLE_ITEMS) {
            deletedItems = writableDatabase.delete(CarsTableContract.TABLE_NAME,
                    selection, selectionArgs);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

package com.example.contentproviderloader;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PeopleContentProvider extends ContentProvider {

    public static final String AUTHRITY = "com.dream.center.provider";
    private static final int PEOPLE_TABLE_CODE = 0;

    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHRITY + "/notifyList");
    private Uri notifuuri;

    private Context context;

    private SQLiteDatabase sqLiteDatabase;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHRITY,"people",PEOPLE_TABLE_CODE);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        sqLiteDatabase = new DBOpenHelper(context).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = URI_MATCHER.match(uri);
        Cursor cursor;
        switch (match){
            case PEOPLE_TABLE_CODE:
                cursor = sqLiteDatabase.query(DBOpenHelper.TABLE_PEOPEL_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                notifuuri = uri;
                cursor.setNotificationUri(context.getContentResolver(),NOTIFY_URI);
                break;
            default:
                throw new IllegalArgumentException("uri = " + uri);
        }
        return cursor;
    }   

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URI_MATCHER.match(uri);
        long row;

        switch (uriType) {
            case PEOPLE_TABLE_CODE:
                row = sqLiteDatabase.insert(DBOpenHelper.TABLE_PEOPEL_NAME,null, values);
                break;
            default:
                throw new IllegalArgumentException("UnSupport Uri : " + uri);
        }

        if(row > -1) {
            Log.e("xxx", "insert: to notify");

            Uri newUri = ContentUris.withAppendedId(uri, row);
            context.getContentResolver().notifyChange(NOTIFY_URI,null);
            return newUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

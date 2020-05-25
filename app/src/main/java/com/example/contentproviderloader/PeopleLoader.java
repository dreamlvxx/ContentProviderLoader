package com.example.contentproviderloader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.loader.content.AsyncTaskLoader;

public class PeopleLoader extends AsyncTaskLoader<Cursor> {
    ForceLoadContentObserver mObserver = new ForceLoadContentObserver();

    private final static Uri STUDENT_URI = Uri.parse("content://" + PeopleContentProvider.AUTHRITY + "/people");

    public PeopleLoader(@NonNull Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public Cursor loadInBackground() {
        Cursor cursor = getContext().getContentResolver().query(STUDENT_URI,null,null,null);
        //这句话必须加，才能与provider里面的notifyChange结合使用，得到内容通知，重新加载。
        cursor.registerContentObserver(mObserver);
        Log.e("xxx", "loadInBackground: execute count = " + cursor.getCount());
        return cursor;
    }

    @Override
    protected void onStartLoading() {
        Log.e("xxx", "onStartLoading: ");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

}

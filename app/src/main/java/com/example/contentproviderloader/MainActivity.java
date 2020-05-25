package com.example.contentproviderloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final static Uri STUDENT_URI = Uri.parse("content://" + PeopleContentProvider.AUTHRITY + "/people");

    private LoaderManager mLoaderManager;

    private SimpleCursorAdapter mCursorAdapter;

    private ListView mListView;
    private TextView add_data;

    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            initLoader();
        } else {
            verifyStoragePermissions(this);
        }
    }

    private void initView() {
        mListView = findViewById(R.id.revy);
        add_data = findViewById(R.id.add_data);

        mCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.item_view,
                null,
                new String[]{"name"},
                new int[]{R.id.tv_name},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(mCursorAdapter);
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertValue();
            }
        });
    }

    private void initLoader() {
        mLoaderManager = LoaderManager.getInstance(this);
        mLoaderManager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                return new PeopleLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                mCursorAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                mCursorAdapter.swapCursor(null);
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }
    }

    private int mInsertId = 10;
    private void insertValue() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "peter" + new Random().nextInt());
        getContentResolver().insert(STUDENT_URI, contentValues);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            // 如果用户赋予全选，则执行相应逻辑
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLoader();
            } else {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

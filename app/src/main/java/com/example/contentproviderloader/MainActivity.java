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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private LoaderManager loaderManager;

    private SimpleCursorAdapter cursorAdapter;

    private ListView revy;
    private TextView add_data;

    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor =getContentResolver().query(STUDENT_URI,null,null,null);
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_view,
                cursor, new String[]{"name"}, new int[]{R.id.tv_name},CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        revy = findViewById(R.id.revy);
        revy.setAdapter(cursorAdapter);
        add_data = findViewById(R.id.add_data);
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertValue();
            }
        });
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            initLoader();
        }else{
            verifyStoragePermissions(this);
        }
    }

    private void initLoader() {
        Log.e("xxx", "initLoader: excute" );
        loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                return new PeopleLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                Log.e("xxx", "onLoadFinished: execute");
                cursorAdapter.swapCursor(data);
                cursorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                Log.e("xxx", "onLoaderReset: execute");
                cursorAdapter.swapCursor(null);
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }
    }

    private int mInsertId = 10;

    private void insertValue() {
        Log.e("xxx", "insertValue: ");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","peter" + new Random().nextInt());
        getContentResolver().insert(STUDENT_URI,contentValues);
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

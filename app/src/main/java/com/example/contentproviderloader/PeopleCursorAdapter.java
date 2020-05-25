package com.example.contentproviderloader;


import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PeopleCursorAdapter extends RecyclerView.Adapter<PeopleCursorAdapter.AlbumViewHolder> {

    private Cursor mCursor;

    public PeopleCursorAdapter(Cursor cursor) {
        this.mCursor = cursor;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,null);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {

        if(mCursor != null && mCursor.moveToNext()){
            String albumCoverPath = mCursor.getString(mCursor.getColumnIndex("name"));
            holder.tvAlbumName.setText(albumCoverPath);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAlbumName;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            tvAlbumName = itemView.findViewById(R.id.tv_name);
        }
    }
}

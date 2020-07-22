package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;

public class PlayListEditAdapter extends RecyclerView.Adapter<PlayListEditAdapter.PLEViewHolder> {

    private LayoutInflater mInflater;

    public PlayListEditAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PLEViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.playlistedit_item, parent, false);
        return new PLEViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PLEViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class PLEViewHolder extends RecyclerView.ViewHolder {
        public PLEViewHolder(View view) {
            super(view);
        }
    }
}

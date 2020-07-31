package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.Mark;

import java.util.ArrayList;
import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.MViewHolder> {

    private OnItemClickListener onItemClickListener;

    private List<Mark> markList;
    private LayoutInflater mInflater;

    public MarkAdapter(Context context, OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.bookmark_main_item, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MViewHolder holder, int position) {
        if(markList != null) {
            Mark current = markList.get(position);
            holder.mid.setText(String.valueOf(current.getmid()));
            holder.mMemo.setText(current.getmMemo());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int mid = current.getmid();
                    onItemClickListener.clickItem(mid);
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int mid = current.getmid();
                    onItemClickListener.clickLongItem(view, mid);
                    return false;
                }
            });
        } else {
            holder.mid.setText("No idData");
            holder.mMemo.setText("No Data");
        }
    }

    public void setMarks(List<Mark> marks) {
        markList = marks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(markList != null)
            return markList.size();
        else return 0;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView mid;
        private TextView mMemo;

        public MViewHolder(View view) {
            super(view);
            this.view = view;
            mid = view.findViewById(R.id.mid);
            mMemo = view.findViewById(R.id.mMemo);
        }
    }
}

package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.MarkViewHolderNormal;
import com.example.dbvideomarker.adapter.viewholder.MarkViewHolderSelect;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.database.entitiy.Mark;

import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MyItemView> {

    private OnItemClickListener onItemClickListener;
    private OnItemSelectedListener onItemSelectedListener;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItemIds = new SparseBooleanArray(0);
    private RequestManager mRequestManager;
    private List<Mark> markList;
    private LayoutInflater mInflater;
    private ViewCase sel_type;

    public MarkAdapter(Context context, ViewCase sel_type, OnItemClickListener onItemClickListener, OnItemSelectedListener onItemSelectedListener, RequestManager requestManager) {
        mInflater = LayoutInflater.from(context);
        mRequestManager = requestManager;
        this.sel_type = sel_type;
        this.onItemSelectedListener = onItemSelectedListener;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public MyItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        if (sel_type == ViewCase.NORMAL) {
            View view = mInflater.from(parent.getContext()).inflate(R.layout.mark_item, parent, false);
            return new MarkViewHolderNormal(view);
        } else if (sel_type == ViewCase.SELECT) {
            View view = mInflater.from(parent.getContext()).inflate(R.layout.activity_select_mark_item, parent, false);
            return new MarkViewHolderSelect(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyItemView holder, int position) {
        if (holder instanceof MarkViewHolderNormal) {
            MarkViewHolderNormal markViewHolderNormal = (MarkViewHolderNormal) holder;
            if (markList != null) {

                Mark current = markList.get(position);
                markViewHolderNormal.mid.setText(String.valueOf(current.getmid()));
                markViewHolderNormal.mMemo.setText(current.getmMemo());
                markViewHolderNormal.mStart.setText(String.valueOf(current.getmStart()));
                markViewHolderNormal.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mid = current.getvid();
                        long mstart = current.getmStart();
                        onItemClickListener.clickMark(mid, mstart);
                    }
                });
                markViewHolderNormal.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int mid = current.getmid();
                        onItemClickListener.clickLongItem(view, mid);
                        return false;
                    }
                });
            } else {
                Log.d("MarkAdapter.class", "No Data");
            }
        } else if (holder instanceof MarkViewHolderSelect) {
            MarkViewHolderSelect markViewHolderSelect = (MarkViewHolderSelect) holder;
            if (markList != null) {
                Mark current = markList.get(position);
                markViewHolderSelect._mid.setText(String.valueOf(current.getmid()));
                markViewHolderSelect._mMemo.setText(current.getmMemo());
                markViewHolderSelect._mStart.setText(String.valueOf(current.getmStart()));

                if (mSelectedItems.get(position, false)) {
                    markViewHolderSelect._view.setBackgroundColor(Color.GRAY);
                } else {
                    markViewHolderSelect._view.setBackgroundColor(Color.WHITE);
                }
                markViewHolderSelect._view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mSelectedItems.get(position, false) == true) {
                            mSelectedItems.delete(position);
                            mSelectedItemIds.delete(current.getmid());
                            notifyItemChanged(position);
                        } else {
                            mSelectedItems.put(position, true);
                            mSelectedItemIds.put(current.getmid(), true);
                            notifyItemChanged(position);
                        }
                        Log.d("MarkAdaper.class", ""+mSelectedItemIds);

                        onItemSelectedListener.onItemSelected(view, mSelectedItemIds);
                    }
                });
            }
        }
    }

    public void setMarks(List<Mark> marks) {
        markList = marks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (markList != null)
            return markList.size();
        else return 0;
    }


}

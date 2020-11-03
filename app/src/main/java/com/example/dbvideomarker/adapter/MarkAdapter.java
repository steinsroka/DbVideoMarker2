package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.MarkViewHolderNormal;
import com.example.dbvideomarker.adapter.viewholder.MarkViewHolderSelect;
import com.example.dbvideomarker.adapter.viewholder.MarkViewHolderVertical;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.util.MediaStoreLoader;

import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MyItemView> {

    private OnMarkClickListener onMarkClickListener;
    private OnItemSelectedListener onItemSelectedListener;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItemIds = new SparseBooleanArray(0);
    private List<Mark> markList;
    private ViewCase sel_type;
    private Context context;

    public MarkAdapter(Context context, ViewCase sel_type, OnMarkClickListener onMarkClickListener, OnItemSelectedListener onItemSelectedListener) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        this.context = context;
        this.sel_type = sel_type;
        this.onItemSelectedListener = onItemSelectedListener;
        this.onMarkClickListener = onMarkClickListener;
    }

    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (sel_type == ViewCase.NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark, parent, false);
            return new MarkViewHolderNormal(view);
        } else if (sel_type == ViewCase.SELECT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark_select, parent, false);
            return new MarkViewHolderSelect(view);
        } else if (sel_type == ViewCase.VERTICAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark_vertical, parent, false);
            return new MarkViewHolderVertical(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyItemView holder, int position) {
        MediaStoreLoader loader = new MediaStoreLoader();
        if (holder instanceof MarkViewHolderNormal) {
            MarkViewHolderNormal markViewHolderNormal = (MarkViewHolderNormal) holder;
            if (markList != null) {
                Mark current = markList.get(position);
                markViewHolderNormal.mMemo.setText(current.getmMemo());
                markViewHolderNormal.mStart.setText(loader.getReadableDuration(current.getmStart()));
                //mRequestManager.asBitmap().load(Uri.fromFile(new File(current.getMpath()))).frame(current.getmStart()).into(markViewHolderNormal.mthumb);
                markViewHolderNormal.mthumb.setImageBitmap(loader.getThumbnail(current.getvid(), current.getmStart(), context));
                markViewHolderNormal.view.setOnClickListener(view -> onMarkClickListener.clickMark(current.getvid(), current.getmStart(), current.getMpath()));
                markViewHolderNormal.moreImage.setOnClickListener(view -> onMarkClickListener.clickLongMark(view, current.getmid(), current.getMpath()));
            } else {
                Log.d("MarkAdapter.class", "No Data");
            }
        } else if (holder instanceof MarkViewHolderSelect) {
            MarkViewHolderSelect markViewHolderSelect = (MarkViewHolderSelect) holder;
            if (markList != null) {
                Mark current = markList.get(position);
                markViewHolderSelect._mMemo.setText(current.getmMemo());
                markViewHolderSelect._mStart.setText(loader.getReadableDuration(current.getmStart()));
                markViewHolderSelect._mThumb.setImageBitmap(loader.getThumbnail(current.getvid(), current.getmStart(), context));
                if (mSelectedItems.get(position, false)) {
                    markViewHolderSelect._view.setBackgroundColor(Color.parseColor("#A6A6A6"));
                } else {
                    markViewHolderSelect._view.setBackgroundColor(Color.parseColor("#373737"));
                }
                markViewHolderSelect._view.setOnClickListener(view -> {
                    if (mSelectedItems.get(position, false)) {
                        mSelectedItems.delete(position);
                        mSelectedItemIds.delete(current.getmid());
                        onMarkClickListener.onMarkClickListener(current, view, 0);
                        notifyItemChanged(position);
                    } else {
                        mSelectedItems.put(position, true);
                        mSelectedItemIds.put(current.getmid(), true);
                        onMarkClickListener.onMarkClickListener(current, view, 1);
                        notifyItemChanged(position);
                    }
                    Log.d("MarkAdaper.class", "" + mSelectedItemIds);

                    onItemSelectedListener.onItemSelected(view, mSelectedItemIds);
                });
            }
        } else if (holder instanceof MarkViewHolderVertical) {
            MarkViewHolderVertical markViewHolderVertical = (MarkViewHolderVertical) holder;
            if (markList != null) {
                Mark current = markList.get(position);
                markViewHolderVertical.mMemo.setText(current.getmMemo());
                markViewHolderVertical.mStart.setText(loader.getReadableDuration(current.getmStart()));
                markViewHolderVertical.mthumb.setImageBitmap(loader.getThumbnail(current.getvid(), current.getmStart(), context));
                markViewHolderVertical.view.setOnClickListener(view -> onMarkClickListener.clickMark(current.getvid(), current.getmStart(), current.getMpath()));
                markViewHolderVertical.moreImage.setOnClickListener(view -> onMarkClickListener.clickLongMark(view, current.getmid(), current.getMpath()));
            }
        }
    }

    public void setMarks(List<Mark> marks) {
        markList = marks;
        notifyDataSetChanged();
    }

    public void removeSelection(boolean isActionModeMenuClicked) {
        mSelectedItemIds = new SparseBooleanArray(0);
        if (!isActionModeMenuClicked) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (markList != null)
            return markList.size();
        else return 0;
    }

    public int getSelectedCount() {
        return mSelectedItemIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemIds;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemIds.put(position, value);
        else
            mSelectedItemIds.delete(position);

        notifyDataSetChanged();
    }
}

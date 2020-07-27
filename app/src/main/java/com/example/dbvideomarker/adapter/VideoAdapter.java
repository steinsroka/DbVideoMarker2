package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.util.MyVideoView;
import com.example.dbvideomarker.adapter.util.VideoCase;
import com.example.dbvideomarker.adapter.viewholder.ViewHolderNormal;
import com.example.dbvideomarker.adapter.viewholder.ViewHolderSelect;
import com.example.dbvideomarker.database.entitiy.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<MyVideoView> {

    public interface OnItemClickListener {
        void clickItem(int vid);
    }

    private List<Video> videoList; //cached copy of words
    private LayoutInflater mInflater;
    private VideoCase sel_type;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private OnItemSelectedListener onItemSelectedListener;
    private OnItemClickListener onItemClickListener;

    public VideoAdapter(Context context, VideoCase sel_type, OnItemSelectedListener onItemSelectedListener, OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.sel_type = sel_type;
        this.onItemSelectedListener = onItemSelectedListener;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyVideoView onCreateViewHolder(ViewGroup parent, int viewType) {
        if(sel_type == VideoCase.NORMAL) {
            View view = mInflater.from(parent.getContext()).inflate(R.layout.home_main_item, parent, false);
            return new ViewHolderNormal(view);
        } else if (sel_type == VideoCase.SELECT){
            View view = mInflater.from(parent.getContext()).inflate(R.layout.activity_select_item, parent, false);
            return new ViewHolderSelect(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyVideoView holder, int position) {
        if(holder instanceof ViewHolderNormal){
            ViewHolderNormal viewHolderNormal = (ViewHolderNormal)holder;
            if(videoList != null) {
                Video current = videoList.get(position);
                viewHolderNormal.vId.setText(String.valueOf(current.getVid()));
                viewHolderNormal.vName.setText(current.getvName());
                viewHolderNormal.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int vid = current.getVid();
                        onItemClickListener.clickItem(vid);
                    }
                });
            } else {
                viewHolderNormal.vName.setText("No Data");
            }
        } else if(holder instanceof ViewHolderSelect) {
            //선택모드
            ViewHolderSelect viewHolderSelect = (ViewHolderSelect)holder;
            if(videoList != null) {
                Video current = videoList.get(position);
                viewHolderSelect.selectedVid.setText(String.valueOf(current.getVid()));
                viewHolderSelect.selectedVname.setText(current.getvName());

                if(mSelectedItems.get(position, false)){
                    viewHolderSelect.view.setBackgroundColor(Color.GRAY);
                } else {
                    viewHolderSelect.view.setBackgroundColor(Color.WHITE);
                }
                viewHolderSelect.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemSelectedListener.onItemSelected(view, current.getVid());
                        Log.d("test", "position = " + position + "/vid = " + current.getVid());
                        toggleItemSelected(position);
                    }
                });
            }
        }
    }

    public void setVideos(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(videoList != null)
            return videoList.size();
        else return 0;
    }

    private void toggleItemSelected(int position) {

        if (mSelectedItems.get(position, false) == true) {
            mSelectedItems.delete(position);
            notifyItemChanged(position);
        } else {
            mSelectedItems.put(position, true);
            notifyItemChanged(position);
        }
    }

    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

    public void clearSelectedItem() {
        int position;

        for (int i = 0; i < mSelectedItems.size(); i++) {
            position = mSelectedItems.keyAt(i);
            mSelectedItems.put(position, false);
            notifyItemChanged(position);
        }

        mSelectedItems.clear();
    }
}

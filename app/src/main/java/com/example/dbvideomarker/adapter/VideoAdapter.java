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
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.util.MyVideoView;
import com.example.dbvideomarker.adapter.util.VideoCase;
import com.example.dbvideomarker.adapter.viewholder.VideoViewHolderNormal;
import com.example.dbvideomarker.adapter.viewholder.VideoViewHolderSelect;
import com.example.dbvideomarker.database.entitiy.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<MyVideoView> {

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
            return new VideoViewHolderNormal(view);
        } else if (sel_type == VideoCase.SELECT){
            View view = mInflater.from(parent.getContext()).inflate(R.layout.activity_select_item, parent, false);
            return new VideoViewHolderSelect(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyVideoView holder, int position) {
        if(holder instanceof VideoViewHolderNormal){
            VideoViewHolderNormal videoViewHolderNormal = (VideoViewHolderNormal)holder;
            if(videoList != null) {
                Video current = videoList.get(position);
                videoViewHolderNormal.vId.setText(String.valueOf(current.getVid()));
                videoViewHolderNormal.vName.setText(current.getvName());
                videoViewHolderNormal.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = current.getVid();
                        onItemClickListener.clickItem(id);
                    }
                });
                videoViewHolderNormal.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = current.getVid();
                        onItemClickListener.clickLongItem(id);
                        return false;
                    }
                });
            } else {
                videoViewHolderNormal.vName.setText("No Data");
            }
        } else if(holder instanceof VideoViewHolderSelect) {
            //선택모드
            VideoViewHolderSelect videoViewHolderSelect = (VideoViewHolderSelect)holder;
            if(videoList != null) {
                Video current = videoList.get(position);
                videoViewHolderSelect.selectedVid.setText(String.valueOf(current.getVid()));
                videoViewHolderSelect.selectedVname.setText(current.getvName());

                if(mSelectedItems.get(position, false)){
                    videoViewHolderSelect.view.setBackgroundColor(Color.GRAY);
                } else {
                    videoViewHolderSelect.view.setBackgroundColor(Color.WHITE);
                }
                videoViewHolderSelect.view.setOnClickListener(new View.OnClickListener() {
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

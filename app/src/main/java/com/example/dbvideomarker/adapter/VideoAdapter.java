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
import com.example.dbvideomarker.adapter.util.MyItemView;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.ItemViewHolderNormal;
import com.example.dbvideomarker.adapter.viewholder.ItemViewHolderSelect;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.database.entitiy.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<MyItemView> {

    private List<Video> videoList; //cached copy of words
    private List<PlRelVideo> plRelVideoList;
    private LayoutInflater mInflater;
    private ViewCase sel_type;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private OnItemSelectedListener onItemSelectedListener;
    private OnItemClickListener onItemClickListener;
    private ArrayList<Integer> selectedVidList = new ArrayList<>();

    public VideoAdapter(Context context, ViewCase sel_type, OnItemSelectedListener onItemSelectedListener, OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.sel_type = sel_type;
        this.onItemSelectedListener = onItemSelectedListener;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        if(sel_type == ViewCase.NORMAL) {
            View view = mInflater.from(parent.getContext()).inflate(R.layout.home_main_item, parent, false);
            return new ItemViewHolderNormal(view);
        } else if (sel_type == ViewCase.SELECT){
            View view = mInflater.from(parent.getContext()).inflate(R.layout.activity_select_item, parent, false);
            return new ItemViewHolderSelect(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyItemView holder, int position) {
        if(holder instanceof ItemViewHolderNormal){
            ItemViewHolderNormal viewHolderNormal = (ItemViewHolderNormal)holder;
            if(videoList != null) {
                Video current = videoList.get(position);
                viewHolderNormal.vId.setText(String.valueOf(current.getVid()));
                viewHolderNormal.vName.setText(current.getvName());
                viewHolderNormal.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = current.getVid();
                        onItemClickListener.clickItem(id);
                    }
                });
                viewHolderNormal.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = current.getVid();
                        onItemClickListener.clickLongItem(v, id);
                        return false;
                    }
                });
            } else {
                viewHolderNormal.vName.setText("No Data");
            }
        } else if(holder instanceof ItemViewHolderSelect) {
            //선택모드

            ItemViewHolderSelect viewHolderSelect = (ItemViewHolderSelect)holder;
            if(videoList != null) {
                Video current = videoList.get(position);
                viewHolderSelect.selectedVid.setText(String.valueOf(current.getVid()));
                viewHolderSelect.selectedVname.setText(current.getvName());

                if(mSelectedItems.get(position, false)){
                    viewHolderSelect.view.setBackgroundColor(Color.GRAY);
                    selectedVidList.add(current.getVid());
                } else {
                    viewHolderSelect.view.setBackgroundColor(Color.WHITE);
                }
                viewHolderSelect.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemSelectedListener.onItemSelected(view, selectedVidList);
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

//    private boolean isItemSelected(int position) {
//        return mSelectedItems.get(position, false);
//    }
}

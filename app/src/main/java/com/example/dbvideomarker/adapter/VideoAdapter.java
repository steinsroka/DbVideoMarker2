package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.database.entitiy.VideoSelect;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VViewHolder>  {

    private List<VideoSelect> videoList; //cached copy of words
    private LayoutInflater mInflater;

    public VideoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.home_main_item, parent, false);
        return new VViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VViewHolder holder, int position) {
//        holder.video = videoList.get(position);
        if(videoList != null) {
            VideoSelect current = videoList.get(position);
            holder.vId.setText(String.valueOf(current.getVid()));
        } else {
            holder.vName.setText("No Data");
        }
//        holder.vTag.setText(videoList.get(position).vName);
//        holder.vDur.setText((int) videoList.get(position).vDur);

//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickListener.clickItem(holder.video);
//            }
//        });
//
//        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                onItemClickListener.clickLongItem(holder.video);
//                return false;
//            }
//        });
    }

    public void setVideos(List<VideoSelect> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(videoList != null)
            return videoList.size();
        else return 0;
    }

    public class VViewHolder extends RecyclerView.ViewHolder {
        private TextView vId;
        private TextView vName;
//        private TextView vTag;
//        private TextView vDur;

        public VViewHolder(View view) {
            super(view);
            vId  = view.findViewById(R.id.vId);
            vName = view.findViewById(R.id.vName);
//            vTag = view.findViewById(R.id.vTag);
//            vDur = view.findViewById(R.id.vDur);
        }
    }

}

package com.example.dbvideomarker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.util.Callback;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.util.MediaStoreLoader;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class PlayList_VideoAdapter extends RecyclerView.Adapter<PlayList_VideoAdapter.PLEViewHolder> implements Callback.OnItemMoveListener {

    private final OnStartDragListener onStartDragListener;
    private OnItemClickListener onItemClickListener;
    public List<PlRelVideo> plRelList;
    private LayoutInflater mInflater;
    private RequestManager mRequestManager;

    public PlayList_VideoAdapter(Context context, OnItemClickListener onItemClickListener, OnStartDragListener onStartDragListener, RequestManager requestManager) {
        mInflater = LayoutInflater.from(context);
        mRequestManager = requestManager;
        this.onItemClickListener = onItemClickListener;
        this.onStartDragListener = onStartDragListener;
    }

    public interface OnStartDragListener {
        void onStartDrag(PLEViewHolder holder);
    }

    @NonNull
    @Override
    public PLEViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_video_playlist, parent, false);
        return new PLEViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PLEViewHolder holder, int position) {
        MediaStoreLoader loader = new MediaStoreLoader();
        if (plRelList != null) {
            PlRelVideo current = plRelList.get(position);
            holder.name.setText(String.valueOf(current.getPv_vname()));
            holder.vid.setText(String.valueOf(current.getPv_vid()));
            holder.pid.setText(String.valueOf(current.getPv_pid()));
            holder.dur.setText(String.valueOf(loader.getReadableDuration(current.getPv_vdur())));
            mRequestManager.asBitmap().load(Uri.fromFile(new File(current.getPv_vpath()))).into(holder.thumb);

            holder.view.setOnLongClickListener(view -> {
                onItemClickListener.clickLongItem(view, current.getPv_vid(), current.getPv_vpath());
                return false;
            });

            holder.view.setOnClickListener(view -> onItemClickListener.clickItem(current.getPv_vid(), current.getPv_vpath()));

            //이미지 터치하면 드래그시작
            holder.imageView.setOnTouchListener((v, event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(holder);
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        if (plRelList != null)
            return plRelList.size();
        else return 0;
    }

    public void setPlRelv(List<PlRelVideo> plRel) {
        plRelList = plRel;
        notifyDataSetChanged();
    }

    //아이템이 움직이면 포지션 변경
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(plRelList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class PLEViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView name;
        private TextView vid;
        private TextView pid;
        private TextView dur;
        private ImageView imageView;
        private ImageView thumb;

        public PLEViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.plrel_name);
            vid = view.findViewById(R.id.plrel_vid);
            pid = view.findViewById(R.id.plrel_pid);
            dur = view.findViewById(R.id.plrel_dur);
            thumb = view.findViewById(R.id.plrel_thumb);
            imageView = view.findViewById(R.id.dragImage);
        }

    }
}

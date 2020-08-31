package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.database.entitiy.PlRelMark;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.listener.OnItemClickListener;

import java.util.List;

public class PlayList_MarkAdapter extends RecyclerView.Adapter<PlayList_MarkAdapter.PLMViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<PlRelMark> plRelMarkList;
    private LayoutInflater minflater;
    private RequestManager mRequestManager;

    public PlayList_MarkAdapter(Context context, OnItemClickListener onItemClickListener, RequestManager requestManager) {
        minflater = LayoutInflater.from(context);
        mRequestManager = requestManager;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PlayList_MarkAdapter.PLMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.playlistedit_mark_item,parent,false);
        return new PLMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayList_MarkAdapter.PLMViewHolder holder, int position) {
        if(plRelMarkList !=null) {
            PlRelMark current = plRelMarkList.get(position);
            holder.id.setText(String.valueOf(current.getPm_mid()));
            holder.name.setText(current.getPm_mmemo());
            holder.start.setText(String.valueOf(current.getPm_mstart()));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = current.getPm_vid();
                    long start = current.getPm_mstart();
                    onItemClickListener.clickMark(id, start);
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int mid = current.getPm_mid();
                    onItemClickListener.clickLongMark(view, mid);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(plRelMarkList!= null)
            return plRelMarkList.size();
        else return 0;
    }

    public void setPlRelm(List<PlRelMark> plRel) {
        plRelMarkList = plRel;
        notifyDataSetChanged();
    }

    public class PLMViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView thumb;
        private TextView id;
        private TextView name;
        private TextView start;
        public PLMViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            thumb = view.findViewById(R.id.plrel_mark_thumb);
            id = view.findViewById(R.id.plrel_mid);
            name = view.findViewById(R.id.plrel_mmemo);
            start = view.findViewById(R.id.plrel_mstart);
        }
    }
}

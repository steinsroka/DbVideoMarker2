package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.PlayListEditViewModel;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.util.MyItemView;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.PlayListEditViewHolderNormal;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;

import java.util.List;

public class PlayListEditAdapter extends RecyclerView.Adapter<MyItemView> {

    private OnItemClickListener onItemClickListener;
    private List<PlRelVideo> plRelList;
    private LayoutInflater mInflater;
    private ViewCase sel_type;

    public PlayListEditAdapter(Context context, OnItemClickListener onItemClickListener, ViewCase sel_type) {
        mInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
        this.sel_type = sel_type;
    }

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(sel_type == ViewCase.NORMAL) {
            View view = mInflater.inflate(R.layout.playlistedit_item, parent, false);
            return new PlayListEditViewHolderNormal(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        if(holder instanceof PlayListEditViewHolderNormal) {
            PlayListEditViewHolderNormal viewHolderNormal = (PlayListEditViewHolderNormal)holder;
            if(plRelList != null) {
                PlRelVideo current = plRelList.get(position);
                viewHolderNormal.pid.setText(String.valueOf(current.getPv_pid()));
                viewHolderNormal.vname.setText(String.valueOf(current.getPv_vname()));
                viewHolderNormal.vid.setText(String.valueOf(current.getPv_vid()));
                viewHolderNormal.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pid = current.getPv_vid();
                        onItemClickListener.clickLongItem(view, pid);
                        return false;
                    }
                });

                viewHolderNormal.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pid = current.getPv_vid();
                        onItemClickListener.clickItem(pid);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(plRelList != null)
            return plRelList.size();
        else return 0;
    }

    public void setPlRels(List<PlRelVideo> plRel) {
        plRelList = plRel;
        notifyDataSetChanged();
    }
}

package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.Video;

import java.util.List;

public class PlayListEditAdapter extends RecyclerView.Adapter<PlayListEditAdapter.PLEViewHolder> {

    private List<PlRel> plRelList;
    private LayoutInflater mInflater;

    public PlayListEditAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PLEViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.playlistedit_item, parent, false);
        return new PLEViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PLEViewHolder holder, int position) {
        if(plRelList != null) {
            PlRel current = plRelList.get(position);
            holder.plid.setText(String.valueOf(current.getPlid()));
            holder.pid.setText(String.valueOf(current.getPid()));
            holder.vid.setText(String.valueOf(current.getVid()));
        }
    }

    @Override
    public int getItemCount() {
        if(plRelList != null)
            return plRelList.size();
        else return 0;
    }

    public void setPlRels(List<PlRel> plRel) {
        plRelList = plRel;
        notifyDataSetChanged();
    }

    public class PLEViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView plid;
        private TextView pid;
        private TextView vid;

        public PLEViewHolder(View view) {
            super(view);
            this.view = view;
            plid = view.findViewById(R.id.plrel_plid);
            pid = view.findViewById(R.id.plrel_pid);
            vid = view.findViewById(R.id.plrel_vid);
        }

    }
}

package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyVideoView;

public class VideoViewHolderNormal extends MyVideoView {
    public View view;
    public TextView vId;
    public TextView vName;

    public VideoViewHolderNormal(View view) {
        super(view);
        this.view = view;
        vId = view.findViewById(R.id.vId);
        vName = view.findViewById(R.id.vName);
    }
}


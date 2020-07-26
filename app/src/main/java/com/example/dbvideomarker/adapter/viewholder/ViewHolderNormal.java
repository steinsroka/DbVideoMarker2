package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyVideoView;

public class ViewHolderNormal extends MyVideoView {
    public TextView vId;
    public TextView vName;

    public ViewHolderNormal(View view) {
        super(view);
        vId = view.findViewById(R.id.vId);
        vName = view.findViewById(R.id.vName);
    }
}


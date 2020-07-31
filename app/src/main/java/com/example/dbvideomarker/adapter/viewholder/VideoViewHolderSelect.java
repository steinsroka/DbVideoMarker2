package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.adapter.util.MyVideoView;

public class VideoViewHolderSelect extends MyVideoView {
    public View view;
    public TextView selectedVid;
    public TextView selectedVname;

    public VideoViewHolderSelect(View view) {
        super(view);
        this.view = view;
        selectedVid = view.findViewById(R.id.selectedVid);
        selectedVname = view.findViewById(R.id.selectedVname);

    }
}

package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class VideoViewHolderDrag extends MyItemView {
    public View view;
    public TextView vName;
    public TextView vDur;
    public ImageView vThumb;
    public ImageView iv_drag;

    public VideoViewHolderDrag(View view) {
        super(view);
        this.view = view;
        vName = view.findViewById(R.id.plrel_name);
        vDur = view.findViewById(R.id.plrel_dur);
        vThumb = view.findViewById(R.id.plrel_thumb);
        iv_drag = view.findViewById(R.id.dragImage);
    }
}
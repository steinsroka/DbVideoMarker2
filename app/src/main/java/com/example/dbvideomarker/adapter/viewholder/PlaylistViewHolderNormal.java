package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class PlaylistViewHolderNormal extends MyItemView {
    public View view;
    public TextView pId;
    public TextView pName;
    public TextView pvCount;
    public TextView pmCount;

    public PlaylistViewHolderNormal(View view) {
        super(view);
        this.view = view;
        pId = view.findViewById(R.id.pId);
        pName = view.findViewById(R.id.pName);
        pvCount = view.findViewById(R.id.pvcount);
        pmCount = view.findViewById(R.id.pmcount);
    }

}

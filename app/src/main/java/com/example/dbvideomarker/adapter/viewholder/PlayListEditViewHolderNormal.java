package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class PlayListEditViewHolderNormal extends MyItemView{

    public View view;
    public TextView pid;
    public TextView vname;
    public TextView vid;

    public PlayListEditViewHolderNormal(@NonNull View itemView) {
        super(itemView);
        this.view = view;
        pid = view.findViewById(R.id.plrel_plid);
        vname = view.findViewById(R.id.plrel_vname);
        vid = view.findViewById(R.id.plrel_vid);
    }
}

package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class ItemViewHolderNormal extends MyItemView {
    public View view;
    public TextView vId;
    public TextView vName;

    public ItemViewHolderNormal(View view) {
        super(view);
        this.view = view;
        vId = view.findViewById(R.id.vId);
        vName = view.findViewById(R.id.vName);
    }
}


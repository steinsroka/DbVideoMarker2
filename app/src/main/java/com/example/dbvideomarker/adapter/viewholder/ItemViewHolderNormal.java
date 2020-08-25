package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

import org.w3c.dom.Text;

public class ItemViewHolderNormal extends MyItemView {
    public View view;
    public TextView vId;
    public TextView vName;
    public TextView vDur;
    public ImageView vThumb;

    public ItemViewHolderNormal(View view) {
        super(view);
        this.view = view;
        vId = view.findViewById(R.id.vId);
        vName = view.findViewById(R.id.vName);
        vDur = view.findViewById(R.id.vDur);
        vThumb = view.findViewById(R.id.thumb);
    }
}


package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class PlaylistViewHolderBottom extends MyItemView {

    public View view;
    public TextView pname;

    public PlaylistViewHolderBottom(@NonNull View view) {
        super(view);

        this.view = view;
        pname = view.findViewById(R.id.bs_pname);
    }
}

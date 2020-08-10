package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class PlayListEditViewHolderSelect extends MyItemView {
    public View view;
    //public TextView selectedVid;
    public TextView selectedVname;

    public PlayListEditViewHolderSelect(@NonNull View itemView) {
        super(itemView);

        //selectedVid = view.findViewById(R.id.selectedVid);
        //selectedVname = view.findViewById(R.id.selectedVname);
    }
}

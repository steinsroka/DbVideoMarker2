package com.example.dbvideomarker.adapter.viewholder;

import android.view.SurfaceControl;
import android.view.View;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class ItemViewHolderSelect extends MyItemView {
    public View view;
    public TextView selectedVid;
    public TextView selectedVname;
    public TextView selectedDur;

    public ItemViewHolderSelect(View view) {
        super(view);
        this.view = view;
        selectedVid = view.findViewById(R.id.selectedVid);
        selectedVname = view.findViewById(R.id.selectedVname);
        selectedDur = view.findViewById(R.id.selectedDur);
    }
}

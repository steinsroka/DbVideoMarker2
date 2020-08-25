package com.example.dbvideomarker.adapter.viewholder;

import android.view.SurfaceControl;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class ItemViewHolderSelect extends MyItemView {
    public View view;
    public TextView selectedId;
    public TextView selectedName;
    public TextView selectedDur;
    public ImageView selectedThumb;

    public ItemViewHolderSelect(View view) {
        super(view);
        this.view = view;
        selectedId = view.findViewById(R.id.selectedId);
        selectedName = view.findViewById(R.id.selectedName);
        selectedDur = view.findViewById(R.id.selectedDur);
        selectedThumb = view.findViewById(R.id.selectedThumb);
    }
}

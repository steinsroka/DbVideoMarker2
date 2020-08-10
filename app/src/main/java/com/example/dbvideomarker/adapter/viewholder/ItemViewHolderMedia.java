package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class ItemViewHolderMedia extends MyItemView {

    public View view;
    public TextView Name;
    public TextView ContentUri;

    public ItemViewHolderMedia(@NonNull View view) {
        super(view);
        this.view = view;
        Name = view.findViewById(R.id.mediaName);
        ContentUri = view.findViewById(R.id.mediaUri);
    }
}

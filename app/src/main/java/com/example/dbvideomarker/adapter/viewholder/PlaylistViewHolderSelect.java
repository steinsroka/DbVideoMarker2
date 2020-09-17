package com.example.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;

public class PlaylistViewHolderSelect extends MyItemView {

    public View _view;
    public TextView _pId;
    public TextView _pName;

    public PlaylistViewHolderSelect(View view) {
        super(view);
        this._view = view;
        _pId = view.findViewById(R.id.pId);
        _pName = view.findViewById(R.id.pName);
    }
}

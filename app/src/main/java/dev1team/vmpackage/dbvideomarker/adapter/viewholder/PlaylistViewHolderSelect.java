package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class PlaylistViewHolderSelect extends MyItemView {

    public View _view;
    public TextView _pName;
    public TextView _pvCount;
    public TextView _pmCount;

    public PlaylistViewHolderSelect(View view) {
        super(view);
        this._view = view;
        _pName = view.findViewById(R.id.pName);
        _pvCount = view.findViewById(R.id.pvcount);
        _pmCount = view.findViewById(R.id.pmcount);
    }
}

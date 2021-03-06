package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class PlaylistViewHolderNormal extends MyItemView {
    public View view;
    public TextView pName;
    public TextView pvCount;
    public TextView pmCount;

    public PlaylistViewHolderNormal(View view) {
        super(view);
        this.view = view;
        pName = view.findViewById(R.id.pName);
        pvCount = view.findViewById(R.id.pvcount);
        pmCount = view.findViewById(R.id.pmcount);
    }

}

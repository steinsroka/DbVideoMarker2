package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class PlaylistViewHolderBottom extends MyItemView {

    public View view;
    public TextView pname;

    public PlaylistViewHolderBottom(@NonNull View view) {
        super(view);

        this.view = view;
        pname = view.findViewById(R.id.bs_pname);
    }
}

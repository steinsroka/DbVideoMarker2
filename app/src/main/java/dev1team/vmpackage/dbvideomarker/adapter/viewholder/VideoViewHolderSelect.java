package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class VideoViewHolderSelect extends MyItemView {
    public View view;
    public TextView selectedName;
    public TextView selectedDur;
    public ImageView selectedThumb;

    public VideoViewHolderSelect(View view) {
        super(view);
        this.view = view;
        selectedName = view.findViewById(R.id.selectedName);
        selectedDur = view.findViewById(R.id.selectedDur);
        selectedThumb = view.findViewById(R.id.selectedThumb);
    }
}

package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class VideoViewHolderRecent extends MyItemView {
    public View view;
    public TextView rName;
    public TextView rDur;
    public ImageView rThumb;

    public VideoViewHolderRecent(View view) {
        super(view);
        this.view = view;
        rName = view.findViewById(R.id.name_recent);
        rDur = view.findViewById(R.id.dur_recent);
        rThumb = view.findViewById(R.id.thumb_recent);
    }
}


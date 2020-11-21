package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class VideoViewHolderNormal extends MyItemView {
    public View view;
    public TextView vName, vMark;
    public TextView vDur;
    public ImageView vThumb, moreImage;

    public VideoViewHolderNormal(View view) {
        super(view);
        this.view = view;
        vName = view.findViewById(R.id.vName);
        vDur = view.findViewById(R.id.vDur);
        vThumb = view.findViewById(R.id.thumb);
        moreImage = view.findViewById(R.id.moreImage);
        vMark = view.findViewById(R.id.vMark);
    }
}


package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;

public class MarkViewHolderVertical extends MyItemView {
    public View view;
    public ImageView mthumb, moreImage;
    public TextView mMemo;
    public TextView mStart;

    public MarkViewHolderVertical(@NonNull View view) {
        super(view);
        this.view = view;
        mthumb = view.findViewById(R.id.mThumb_ver);
        moreImage = view.findViewById(R.id.moreImage);
        mMemo = view.findViewById(R.id.mMemo_ver);
        mStart = view.findViewById(R.id.mStart_ver);
    }
}

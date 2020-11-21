package dev1team.vmpackage.dbvideomarker.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;


public class MarkViewHolderNormal extends MyItemView {
    public View view;
    public ImageView mthumb, moreImage;
    public TextView mMemo;
    public TextView mStart;

    public MarkViewHolderNormal(@NonNull View view) {
        super(view);
        this.view = view;
        mthumb = view.findViewById(R.id.mthumb);
        moreImage = view.findViewById(R.id.moreImage);
        mMemo = view.findViewById(R.id.mMemo);
        mStart = view.findViewById(R.id.mstart);
    }
}

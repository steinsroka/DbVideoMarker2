package dev1team.vmpackage.dbvideomarker.listener;

import android.view.View;

import dev1team.vmpackage.dbvideomarker.database.entitiy.Video;

public interface OnItemClickListener {
    void clickItem(int id, String path);

    void clickLongItem(View v, int id, String path);

    void onClickListener(Video video, View view, int typeClick);
}


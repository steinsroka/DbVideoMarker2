package com.example.dbvideomarker.listener;

import android.view.View;

public interface OnItemClickListener {
    void clickItem(int id, String path);

    void clickLongItem(View v, int id, String path);
}


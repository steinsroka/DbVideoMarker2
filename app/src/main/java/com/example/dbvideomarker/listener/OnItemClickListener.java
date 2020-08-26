package com.example.dbvideomarker.listener;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnItemClickListener {
    void clickLongItem(View v, int id);

    void clickItem(int id);
}


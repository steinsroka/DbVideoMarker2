package com.example.dbvideomarker.adapter.listener;

import android.view.View;

import androidx.annotation.Nullable;

public interface OnItemClickListener {
    void clickLongItem(View v, int id);

    void clickItem(int id);
}


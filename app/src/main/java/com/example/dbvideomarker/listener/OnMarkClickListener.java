package com.example.dbvideomarker.listener;

import android.view.View;

import com.example.dbvideomarker.database.entitiy.Mark;

public interface OnMarkClickListener {
    void clickMark(int id, long start, String path);

    void clickLongMark(View v, int id, String path);

    void onMarkClickListener(Mark mark, View view, int typeClick);
}

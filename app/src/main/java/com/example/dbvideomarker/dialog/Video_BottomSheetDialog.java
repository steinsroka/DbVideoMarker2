package com.example.dbvideomarker.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.listener.BottomSheetListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Video_BottomSheetDialog extends BottomSheetDialogFragment implements BottomSheetListener{

    private Button add;
    private Button info;
    private Button delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_video, container, false);

        add = (Button)v.findViewById(R.id.bottom_add_to_playlist);
        info = (Button)v.findViewById(R.id.bottom_video_info);
        delete = (Button)v.findViewById(R.id.bottom_delete_video);

        return v;
    }


    @Override
    public void onClick(View view) {
    }
}

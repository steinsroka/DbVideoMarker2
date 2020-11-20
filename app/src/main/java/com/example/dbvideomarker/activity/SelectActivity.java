package com.example.dbvideomarker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.VideoViewHolderDrag;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.ui.home.HomeViewModel;
import com.example.dbvideomarker.ui.mark.MarkViewModel;
import com.example.dbvideomarker.ui.playlist.PlaylistViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class SelectActivity extends AppCompatActivity implements OnItemSelectedListener, OnMarkClickListener, OnItemClickListener, VideoAdapter.OnStartDragListener {

    private PlaylistViewModel playlistViewModel;
    public RequestManager mGlideRequestManager;

    private int VIEW_TYPE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        View markSelectView = findViewById(R.id.mark_select_view);
        View videoSelectView = findViewById(R.id.video_select_view);
        View playlistSelectView = findViewById(R.id.playlist_select_view);

        mGlideRequestManager = Glide.with(this);
        Intent getIntent = getIntent();
        int pid = Objects.requireNonNull(getIntent.getExtras()).getInt("pid");
        VIEW_TYPE = getIntent.getExtras().getInt("VIEW_TYPE");

        if (VIEW_TYPE == 2001) {
            setVideoSelectView();
            videoSelectView.setVisibility(View.VISIBLE);
            markSelectView.setVisibility(View.GONE);
            playlistSelectView.setVisibility(View.GONE);
        } else if (VIEW_TYPE == 2002) {
            setMarkSelectView();
            markSelectView.setVisibility(View.VISIBLE);
            videoSelectView.setVisibility(View.GONE);
            playlistSelectView.setVisibility(View.GONE);
        }
    }

    public void setVideoSelectView() {
        RecyclerView recyclerView = findViewById(R.id.rv_select_video);
        VideoAdapter adapter = new VideoAdapter(this, ViewCase.SELECT, this, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllVideo().observe(this, adapter::setVideos);
    }

    public void setMarkSelectView() {
        RecyclerView recyclerView = findViewById(R.id.rv_select_mark);
        MarkAdapter adapter = new MarkAdapter(this, ViewCase.SELECT, this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        MarkViewModel markViewModel = new ViewModelProvider(this).get(MarkViewModel.class);
        markViewModel.getAllMark().observe(this, adapter::setMarks);
    }

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        Button btnSelection;
        if (VIEW_TYPE == 2001) {
            ArrayList<Integer> idList = new ArrayList<>();
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                idList.add(sparseBooleanArray.keyAt(i));
                Log.d("text", "길이 : " + sparseBooleanArray);
            }

            btnSelection = findViewById(R.id.btn_add);
            btnSelection.setOnClickListener(view -> {
                Intent resultIntent = new Intent();
                resultIntent.putIntegerArrayListExtra("vidlist", idList);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        } else if (VIEW_TYPE == 2002) {
            ArrayList<Integer> idList = new ArrayList<>();
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                idList.add(sparseBooleanArray.keyAt(i));
            }

            btnSelection = findViewById(R.id.btn_add);
            btnSelection.setOnClickListener(view -> {
                Intent resultIntent = new Intent();
                resultIntent.putIntegerArrayListExtra("midlist", idList);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }
    }

    @Override
    public void clickItem(int id, String path) {
    }

    @Override
    public void clickLongItem(View v, int id, String path) {
    }

    @Override
    public void onClickListener(Video video, View view, int typeClick) {

    }

    @Override
    public void clickMark(int id, long start, String path) {
    }

    @Override
    public void clickLongMark(View v, int id, String path) {
    }

    @Override
    public void onMarkClickListener(Mark mark, View view, int typeClick) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStartDrag(VideoViewHolderDrag mHolder) {

    }
}

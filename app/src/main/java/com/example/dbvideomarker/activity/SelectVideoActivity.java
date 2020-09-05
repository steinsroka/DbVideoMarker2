package com.example.dbvideomarker.activity;


//TODO: 이거 fragment로 바꿔야됨(합칠때)

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectVideoActivity extends AppCompatActivity implements OnItemSelectedListener, OnItemClickListener {

    private HomeViewModel homeViewModel;
    private Button btnSelection;
    public RequestManager mGlideRequestManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);

        mGlideRequestManager = Glide.with(this);


        RecyclerView recyclerView = findViewById(R.id.rv_select_video);
        VideoAdapter adapter = new VideoAdapter(this, ViewCase.SELECT, this, this, mGlideRequestManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

/*
        //중복된 데이터 제외하고 가져오기
        playListEditViewModel = new ViewModelProvider(this).get(PlayListEditViewModel.class);
        playListEditViewModel.getVideoOverlap(pid).observe(this, new Observer<List<PlRelVideo>>() {
            @Override
            public void onChanged(List<PlRelVideo> plRelVideos) {
                //adapter.setVideos(plRelVideos);
            }
        });
*/

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.selectVideo().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                adapter.setVideos(videos);
            }
        });
    }

    @Override
    public void clickLongItem(View v, int id) {}

    @Override
    public void clickItem(int vid) {}

    @Override
    public void clickMark(int id, long start) {

    }

    @Override
    public void clickLongMark(View v, int id) {

    }

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        ArrayList<Integer> idList = new ArrayList<>();
        for(int i=0; i<sparseBooleanArray.size(); i++) {
            idList.add(sparseBooleanArray.keyAt(i));
            Log.d("text", "길이 : " + sparseBooleanArray);
        }

        btnSelection = (Button) findViewById(R.id.btn_add_video);
        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putIntegerArrayListExtra("vidlist", idList);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

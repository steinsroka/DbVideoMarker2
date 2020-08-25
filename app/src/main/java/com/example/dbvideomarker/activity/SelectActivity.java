package com.example.dbvideomarker.activity;


//TODO: 이거 fragment로 바꿔야됨(합칠때)

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity implements OnItemSelectedListener, OnItemClickListener {

    private PlayListEditViewModel playListEditViewModel;
    private HomeViewModel homeViewModel;
    private Button btnSelection;
    private ArrayList<Integer> selectedVidList = new ArrayList<>();
    private int pid;
    public RequestManager mGlideRequestManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mGlideRequestManager = Glide.with(this);
        Intent intent = getIntent();

        pid = intent.getIntExtra("추가할 재생목록 번호", -1);
        TextView add_pid = (TextView)findViewById(R.id.playlist_id);
        add_pid.setText(""+pid);

        RecyclerView recyclerView = findViewById(R.id.rv_select);
        VideoAdapter adapter = new VideoAdapter(this, ViewCase.SELECT, this, this, mGlideRequestManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        homeViewModel.getAllVideo().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                //Update the cached copy of the words in the adapter.
                adapter.setVideos(videos);
            }
        });


    }

    @Override
    public void clickLongItem(View v, int id) {
        //Do Nothing
    }

    @Override
    public void clickItem(int vid) {
        //Do Nothing
    }


    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        ArrayList<Integer> idList = new ArrayList<>();
        for(int i=0; i<sparseBooleanArray.size(); i++) {
            idList.add(sparseBooleanArray.keyAt(i));
            Log.d("text", "길이 : " + sparseBooleanArray);
        }

        btnSelection = (Button) findViewById(R.id.btn_add);
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

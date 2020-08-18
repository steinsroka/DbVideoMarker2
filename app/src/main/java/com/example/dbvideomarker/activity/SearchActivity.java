package com.example.dbvideomarker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.listener.OnMarkClickListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.Video;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnItemSelectedListener, OnItemClickListener, OnMarkClickListener {

    private VideoAdapter videoAdapter;
    private MarkAdapter markAdapter;
    private SearchViewModel searchViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        RecyclerView recyclerViewVideo = findViewById(R.id.rv_VideoResult);
        videoAdapter = new VideoAdapter(this, ViewCase.NORMAL,this,this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewVideo.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerViewVideo.addItemDecoration(dividerItemDecoration);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewVideo.setAdapter(videoAdapter);

        RecyclerView recyclerViewMark  = findViewById(R.id.rv_MarkResult);
        markAdapter = new MarkAdapter(this, this, this);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(recyclerViewMark.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerViewMark.addItemDecoration(dividerItemDecoration);
        recyclerViewMark.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewMark.setAdapter(markAdapter);


        Button searchTextParse = (Button) findViewById(R.id.btn_searchText);
        searchTextParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.searchText);
                if(searchText.getText().toString().trim().length() != 0) {
                    String searchableText = searchText.getText().toString().trim();
                    getSearchResult(searchableText);
                }
            }
        });
    }
    private void getSearchResult(String searchableText) {
//        searchViewModel.getSearchVideo(searchableText).observe(this, new Observer<List<Video>>() {
//            @Override
//            public void onChanged(List<Video> videos) {
//                videoAdapter.setVideos(videos);
//            }
//        });

        searchViewModel.getSearchMark(searchableText).observe(this, new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                markAdapter.setMarks(marks);
            }
        });
    }

    @Override
    public void clickLongItem(View v, int id) {

    }

    @Override
    public void clickItem(int id) {

    }


    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {

    }

    @Override
    public void clickMark(int id, long start) {

    }
}
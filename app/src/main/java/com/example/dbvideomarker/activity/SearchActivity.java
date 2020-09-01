package com.example.dbvideomarker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Mark;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel searchViewModel;
    public RequestManager mGlideRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        List<Video> videoList;
        List<Mark> markList;

        //searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        //mGlideRequestManager = Glide.with(this);

    }
   /*
    private void getSearchResult(String searchableText) {
        searchViewModel.getSearchVideo(searchableText).observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                videoAdapter.setVideos(videos);
            }
        });

        searchViewModel.getSearchMark(searchableText).observe(this, new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                markAdapter.setMarks(marks);
            }
        });
    }
*/
}
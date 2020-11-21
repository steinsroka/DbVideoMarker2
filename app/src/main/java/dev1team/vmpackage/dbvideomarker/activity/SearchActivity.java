package dev1team.vmpackage.dbvideomarker.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.MarkAdapter;
import dev1team.vmpackage.dbvideomarker.adapter.VideoAdapter;
import dev1team.vmpackage.dbvideomarker.adapter.util.ViewCase;
import dev1team.vmpackage.dbvideomarker.adapter.viewholder.VideoViewHolderDrag;
import dev1team.vmpackage.dbvideomarker.database.entitiy.Mark;
import dev1team.vmpackage.dbvideomarker.database.entitiy.Video;
import dev1team.vmpackage.dbvideomarker.listener.OnItemClickListener;
import dev1team.vmpackage.dbvideomarker.listener.OnItemSelectedListener;
import dev1team.vmpackage.dbvideomarker.listener.OnMarkClickListener;
import dev1team.vmpackage.dbvideomarker.repository.MarkRepository;
import dev1team.vmpackage.dbvideomarker.repository.VideoRepository;

public class SearchActivity extends AppCompatActivity implements OnItemClickListener, OnItemSelectedListener, OnMarkClickListener, VideoAdapter.OnStartDragListener {


    VideoAdapter videoAdapter;
    MarkAdapter markAdapter;
    VideoRepository videoRepository;
    MarkRepository markRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        videoAdapter = new VideoAdapter(this, ViewCase.NORMAL, this, this, this);
        markAdapter = new MarkAdapter(this, ViewCase.NORMAL, this, this);

        videoRepository = new VideoRepository(getApplication());
        markRepository = new MarkRepository(getApplication());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (videoRepository.getSearchVideo(editable.toString()) != null) {
                    videoRepository.getSearchVideo(editable.toString()).observe(SearchActivity.this, videos -> videoAdapter.setVideos(videos));
                }

                if (markRepository.getSearchMark(editable.toString()) != null) {
                    markRepository.getSearchMark(editable.toString()).observe(SearchActivity.this, marks -> markAdapter.setMarks(marks));
                }
            }
        });

        setMarkSearchResult();
        setVideoSearchResult();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setVideoSearchResult() {
        RecyclerView searchVideo = findViewById(R.id.search_Video_Result);

        searchVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videoRepository.getAllVideo().observe(SearchActivity.this, videos -> videoAdapter.setVideos(videos));
        searchVideo.setAdapter(videoAdapter);
    }


    public void setMarkSearchResult() {
        RecyclerView searchMark = findViewById(R.id.search_Mark_Result);

        searchMark.setLayoutManager(new GridLayoutManager(this, 2));
        markRepository.getAllMark().observe(SearchActivity.this, marks -> markAdapter.setMarks(marks));
        searchMark.setAdapter(markAdapter);
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
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {

    }

    @Override
    public void onStartDrag(VideoViewHolderDrag mHolder) {

    }
}
package com.example.dbvideomarker.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.repository.MarkRepository;
import com.example.dbvideomarker.repository.VideoRepository;

public class SearchActivity extends AppCompatActivity implements OnItemClickListener, OnItemSelectedListener, OnMarkClickListener, VideoAdapter.OnStartDragListener {

    private RequestManager _mGlideRequestManager;
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

        _mGlideRequestManager = Glide.with(this);
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

            /*
            @Override
            public void afterTextChanged(Editable s) {

                videoRepository = new VideoRepository(getApplication());
                markRepository = new MarkRepository(getApplication());
                listVideo = new ArrayList<>();
                listMark = new ArrayList<>();
                ArrayList<SearchGroupList> list = new ArrayList<>();
                list.add(null);
                list.add(null);

                if (videoRepository.searchVideo(s.toString()) != null) {
                    videoLiveData = videoRepository.searchVideo(s.toString());
                }
                if (markRepository.getSearchMark(s.toString()) != null) {
                    markLiveData = markRepository.getSearchMark(s.toString());
                }

                videoLiveData.observe(SearchActivity.this, new Observer<List<Video>>() {
                    @Override
                    public void onChanged(List<Video> videos) {
                        listVideo.addAll(videos);
                        for (int i = 0; i < listVideo.size(); i++)
                            Log.d("asd", "onChanged : " + listVideo.get(i).getVname());
                        Log.d("asd", "listVideo.size : " + String.valueOf(listVideo.size()));
                        for (int i = 0; i < listVideo.size(); i++) {
                            videoGroup.add(new SearchItemList(R.drawable.ic_baseline_search_24, listVideo.get(i).vdur, listVideo.get(i).getVname(), String.valueOf(listVideo.get(i).getContentId())));
                            Log.d("asd", "add : " + videoGroup.get(i).getvName());
                        }
                        videoGroupList.setSearchItemLists(videoGroup);
                        list.set(0, videoGroupList);
                        if (listVideo.size() == 0)
                            searchAdapter.setList(null);
                        else
                            searchAdapter.setList(list);
                        Log.d("asd", "child : " + searchAdapter.getChild(0, 0));
                        expandableListView.setAdapter(searchAdapter);
                        expandableListView.expandGroup(0);
                    }
                });

                markLiveData.observe(SearchActivity.this, new Observer<List<Mark>>() {
                    @Override
                    public void onChanged(List<Mark> marks) {
                        listMark.addAll(marks);
                        for (int i = 0; i < listMark.size(); i++)
                            markGroup.add(new SearchItemList(R.drawable.ic_baseline_search_24, String.valueOf(listMark.get(i).getmStart()), listVideo.get(i).getVname() + " / " + listMark.get(i).getmMemo(), String.valueOf(listMark.get(i).getmid())));
                        markGroupList.setSearchItemLists(markGroup);
                        list.set(1, markGroupList);
                        if (listMark.size() == 0)
                            searchAdapter.setList(null);
                        else
                            searchAdapter.setList(list);
                        expandableListView.setAdapter(searchAdapter);
                        expandableListView.expandGroup(1);
                    }
                });
            }
        });

        expandableListView.setAdapter(searchAdapter);

        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
 */

        setMarkSearchResult();
        setVideoSearchResult();
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
    public void clickMark(int id, long start, String path) {
    }

    @Override
    public void clickLongMark(View v, int id, String path) {
    }

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {

    }

    @Override
    public void onStartDrag(VideoViewHolderDrag mHolder) {

    }
}
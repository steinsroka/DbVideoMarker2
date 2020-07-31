package com.example.dbvideomarker.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.repository.MarkRepository;
import com.example.dbvideomarker.repository.VideoRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private VideoRepository videoRepository;
    private MarkRepository markRepository;


    public SearchViewModel(@NonNull Application application) {
        super(application);

        videoRepository = new VideoRepository(application);
        markRepository = new MarkRepository(application);
    }

    public LiveData<List<Video>> getSearchVideo(String vname) {
        return videoRepository.getSearchVideo(vname);
    }

    public LiveData<List<Mark>> getSearchMark(String mmemo) {
        return markRepository.getSearchMark(mmemo);
    }
}

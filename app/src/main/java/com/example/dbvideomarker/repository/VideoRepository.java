package com.example.dbvideomarker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.AppDatabase;
import com.example.dbvideomarker.database.dao.VideoDao;
import com.example.dbvideomarker.database.entitiy.Video;

import java.util.List;

public class VideoRepository {

    private VideoDao videoDao;
    private LiveData<List<Video>> allVideo;

    public VideoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        videoDao = db.videoDao();
        allVideo = videoDao.findAllVideo();
    }

    public LiveData<List<Video>> getAllVideo() { return allVideo; }

//    public void insertVideo(Video video) {
//        AppDatabase.databaseWriteExecutor.execute(() -> {
//            videoDao.insertVideo(video);
//        });
//    }
}

package com.example.dbvideomarker.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlRelMark;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.repository.PlayListEditRepository;
import com.example.dbvideomarker.repository.PlayListRepository;

import java.util.List;

public class PlayListEditViewModel extends AndroidViewModel {

    private PlayListEditRepository playListEditRepository;
    private PlayListRepository playListRepository;
    private LiveData<List<PlRelVideo>> allVideoInPlayList;

    public PlayListEditViewModel(@NonNull Application application) {
        super(application);
        playListEditRepository = new PlayListEditRepository(application);
        playListRepository = new PlayListRepository(application);
        LiveData<List<PlRel>> allPlayListRelation = playListEditRepository.getAllPlRel();
    }

//    public LiveData<List<PlRelVideo>> getVideoOverlap(int pid) {
//        return playListEditRepository.getVideoOverlap(pid);
//    }

    public LiveData<Integer> getMarkRowCount(int pid) {
        return playListEditRepository.getMarkRowCount(pid);
    }

    public LiveData<Integer> getVideoRowCount(int pid) {
        return playListEditRepository.getVideoCount(pid);
    }


    public LiveData<List<PlRelVideo>> findVideoInPlayList(int pid) {
        return playListEditRepository.findVideoInPlayList(pid);
    }

    public LiveData<List<PlRelMark>> findMarkInPlayList(int pid) {
        return playListEditRepository.findMarkInPlayList(pid);
    }



    public LiveData<PlayList> getPlayList(int pid) {
        return playListRepository.getPlayList(pid);
    }

    public void insertPlRelation(PlRel plRel) {
        playListEditRepository.insertPlRelation(plRel);
    }

    public void update(PlayList playList) {
        playListRepository.update(playList);
    }

    public void deleteVideoInPlaylist(int vid) {
        playListEditRepository.deleteVideoInPlaylist(vid);
    }

    public void deleteMarkInPlaylist(int mid) {
        playListEditRepository.deleteMarkInPlaylist(mid);
    }
}

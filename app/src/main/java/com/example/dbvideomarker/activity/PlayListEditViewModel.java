package com.example.dbvideomarker.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.repository.PlayListEditRepository;
import com.example.dbvideomarker.repository.PlayListRepository;

import java.util.List;

public class PlayListEditViewModel extends AndroidViewModel {

    private final PlayListEditRepository playListEditRepository;
    private final PlayListRepository playListRepository;

    public PlayListEditViewModel(@NonNull Application application) {
        super(application);
        playListEditRepository = new PlayListEditRepository(application);
        playListRepository = new PlayListRepository(application);
        LiveData<List<PlRel>> allPlayListRelation = playListEditRepository.getAllPlRel();
    }

    public LiveData<Integer> getMarkRowCount(int pid) {
        return playListEditRepository.getMarkRowCount(pid);
    }

    public LiveData<Integer> getVideoRowCount(int pid) {
        return playListEditRepository.getVideoCount(pid);
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

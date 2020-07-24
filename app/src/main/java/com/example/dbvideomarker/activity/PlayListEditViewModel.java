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

    private PlayListEditRepository playListEditRepository;
    private PlayListRepository playListRepository;
    private LiveData<List<PlRel>> allPlayListRelation;
    private LiveData<List<PlayList>> allPlayList;

    public PlayListEditViewModel(@NonNull Application application) {
        super(application);
        playListEditRepository = new PlayListEditRepository(application);
        playListRepository = new PlayListRepository(application);
        allPlayListRelation = playListEditRepository.getAllPlRel();
        allPlayList = playListRepository.findAllPlayList();
    }

    public LiveData<List<PlRel>> getAllPlRel() {
        return allPlayListRelation;
    }

    public void update(PlayList playList) {
        playListRepository.update(playList);
    }

    public LiveData<PlayList> getPlayList(int pid) {
        return playListRepository.getPlayList(pid);
    }
}

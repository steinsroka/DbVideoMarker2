package com.example.dbvideomarker.ui.playlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.repository.PlayListEditRepository;
import com.example.dbvideomarker.repository.PlayListRepository;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private PlayListRepository playListRepository;
    private PlayListEditRepository playListEditRepository;
    private LiveData<List<PlayList>> allPlayList;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        playListRepository = new PlayListRepository(application);
        playListEditRepository = new PlayListEditRepository(application);
        allPlayList = playListRepository.findAllPlayList();
    }

    public LiveData<List<PlayList>> findAllPlayList() {
        return allPlayList;
    }

    public void insertPlayList(PlayList playList) {
        playListRepository.insertPlayList(playList);
    }

    public void deletePlayList(int pid) {
        playListRepository.deletePlayList(pid);
    }

    public void deleteWithPlayList(int pid) {
        playListEditRepository.deleteWithPlayList(pid);
    }

    public void update(PlayList playList) {
        playListRepository.update(playList);
    }

    public void updateVideoCount(int pid, int count) {
        playListRepository.updateVideoCount(pid, count);
    }

    public void updateMarkCount(int pid, int count) {
        playListRepository.updateMarkCount(pid, count);
    }
}
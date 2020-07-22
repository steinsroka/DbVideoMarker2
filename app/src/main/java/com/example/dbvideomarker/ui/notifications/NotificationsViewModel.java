package com.example.dbvideomarker.ui.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.repository.PlayListRepository;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {

    private PlayListRepository playListRepository;
    private LiveData<List<PlayList>> allPlayList;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        playListRepository = new PlayListRepository(application);
        allPlayList = playListRepository.getAllPlayList();
    }

    public LiveData<List<PlayList>> getAllPlayList() {
        return allPlayList;
    }

    public void insertPlayList(PlayList playList) {
        playListRepository.insertPlayList(playList);
    }

    public void deletePlayList(int pid) {
        playListRepository.deletePlayList(pid);
    }

    public void update(PlayList playList) {
        playListRepository.update(playList);
    }
}
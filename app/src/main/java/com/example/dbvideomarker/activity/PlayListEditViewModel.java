package com.example.dbvideomarker.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.repository.PlayListEditRepository;

public class PlayListEditViewModel extends AndroidViewModel {

    private PlayListEditRepository playListEditRepository;
    private LiveData<List<PlayListRelation>> allPlayListRelation;

    public PlayListEditViewModel(@NonNull Application application) {
        super(application);
        playListEditRepository = new PlayListEditRepository(application);
        allPlayListRelation = playListEditRepository.getAllPlayListRelation();
    }

    public LiveData<List<PlayListRelation>> getAllPlayListRelation() {
        return allPlayListRelation;
    }
}

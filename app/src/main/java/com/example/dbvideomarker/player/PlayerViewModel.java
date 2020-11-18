package com.example.dbvideomarker.player;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.repository.MarkRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private final MarkRepository markRepository;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        markRepository = new MarkRepository(application);
    }

    public LiveData<List<Mark>> getMarkByVideoId(int id) {
        return markRepository.getMarkByVideoId(id);
    }

    public void insertMark(Mark mark) {
        markRepository.insertMark(mark);
    }
}

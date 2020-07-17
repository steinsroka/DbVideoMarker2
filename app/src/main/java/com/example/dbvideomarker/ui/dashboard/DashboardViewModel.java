package com.example.dbvideomarker.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.repository.MarkRepository;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private MarkRepository markRepository;
    private LiveData<List<Mark>> allMark;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        markRepository = new MarkRepository(application);
        allMark = markRepository.getAllMark();
    }

    public LiveData<List<Mark>> getAllMark() {
        return allMark;
    }

//    public void insertMark(Mark mark) {
//        markRepository.insertMark(mark);
//    }
}
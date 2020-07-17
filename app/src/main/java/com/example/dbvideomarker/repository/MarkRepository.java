package com.example.dbvideomarker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.AppDatabase;
import com.example.dbvideomarker.database.dao.MarkDao;
import com.example.dbvideomarker.database.entitiy.Mark;

import java.util.List;

public class MarkRepository {

    private MarkDao markDao;
    private LiveData<List<Mark>> allMark;

    public MarkRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        markDao = db.markDao();
        allMark = markDao.findAllMark();
    }

    public LiveData<List<Mark>> getAllMark() { return allMark; }

//    public void insertMark(Mark mark) {
//        AppDatabase.databaseWriteExecutor.execute(() -> {
//            markDao.insertMark(mark);
//        });
//    }
}

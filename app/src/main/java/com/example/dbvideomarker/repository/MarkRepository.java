package com.example.dbvideomarker.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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

    public void insertMark(Mark mark) {
        new AsyncTask<Mark, Void, Long>() {
            @Override
            protected Long doInBackground(Mark... marks) {
                if (markDao == null)
                    return -1L;
                return markDao.insertMark(marks[0]);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d(MarkRepository.class.getSimpleName(), "insert : " + aLong);
            }
        }.execute(mark);
    }
}

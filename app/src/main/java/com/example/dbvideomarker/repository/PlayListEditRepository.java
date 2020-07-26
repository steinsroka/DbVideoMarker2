package com.example.dbvideomarker.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.AppDatabase;
import com.example.dbvideomarker.database.dao.PlRelDao;
import com.example.dbvideomarker.database.dao.PlayListDao;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.ArrayList;
import java.util.List;

public class PlayListEditRepository {
    private static final String TAG = PlayListEditRepository.class.getSimpleName();

    private PlRelDao plRelDao;
    private LiveData<List<PlRel>> allPlRel;

    public PlayListEditRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        plRelDao = db.plRelDao();
        allPlRel = plRelDao.findAllPlayListRelation();
    }

    public LiveData<List<PlRel>> getAllPlRel() {
        return allPlRel;
    }

    public void insertPlRelation(PlRel plRel) {
        new AsyncTask<PlRel, Void, Long>() {
            @Override
            protected Long doInBackground(PlRel... plRels) {
                if(plRelDao == null)
                    return -1L;
                return plRelDao.insertPlRel(plRels[0]);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d(TAG, "insert : " + aLong);
            }
        }.execute(plRel);
    }
}

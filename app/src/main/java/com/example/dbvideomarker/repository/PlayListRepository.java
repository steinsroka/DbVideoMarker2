package com.example.dbvideomarker.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.dbvideomarker.database.AppDatabase;
import com.example.dbvideomarker.database.dao.PlayListDao;
import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

public class PlayListRepository {
    private static final String TAG = PlayListRepository.class.getSimpleName();

    private PlayListDao playListDao;
    private LiveData<List<PlayList>> allPlayList;

    public PlayListRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        playListDao = db.playListDao();
        allPlayList = playListDao.findAllPlayList();
    }

    public LiveData<List<PlayList>> getAllPlayList() {
        return allPlayList;
    }

    public void insertPlayList(PlayList playList) {
        new AsyncTask<PlayList, Void, Long>() {
            @Override
            protected Long doInBackground(PlayList... playLists) {
                if(playListDao == null)
                    return -1L;
                return playListDao.insertPlayList(playLists[0]);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d(TAG, "insert : " + aLong);
            }
        }.execute(playList);


//        AppDatabase.databaseWriteExecutor.execute(() -> {
//            playListDao.insertPlayList(playList);
//        });
    }

    public void deletePlayList(int pid) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (playListDao == null)
                    return -1;
                return playListDao.deletePlayList(integers[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "deletePlayList : " + integer);
            }
        }.execute(pid);
    }
}

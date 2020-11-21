package dev1team.vmpackage.dbvideomarker.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import dev1team.vmpackage.dbvideomarker.database.AppDatabase;
import dev1team.vmpackage.dbvideomarker.database.dao.PlayListDao;
import dev1team.vmpackage.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

@SuppressWarnings("ALL")
public class PlayListRepository {
    private static final String TAG = PlayListRepository.class.getSimpleName();

    private PlayListDao playListDao;
    private LiveData<List<PlayList>> allPlayList;

    public PlayListRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        playListDao = db.playListDao();
        allPlayList = playListDao.findAllPlayList();
    }

    public LiveData<List<PlayList>> findAllPlayList() {
        return allPlayList;
    }

    public LiveData<PlayList> getPlayList(int pid) {
        return playListDao.getPlayList(pid);
    }

    public void insertPlayList(PlayList playList) {
        new AsyncTask<PlayList, Void, Long>() {
            @Override
            protected Long doInBackground(PlayList... playLists) {
                if (playListDao == null)
                    return -1L;
                return playListDao.insertPlayList(playLists[0]);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d(TAG, "insert : " + aLong);
            }
        }.execute(playList);
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

    public void update(PlayList playList) {
        new AsyncTask<PlayList, Void, Integer>() {
            @Override
            protected Integer doInBackground(PlayList... playLists) {
                if (playListDao == null)
                    return -1;
                return playListDao.update(playLists[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "update : " + integer);
            }
        }.execute(playList);
    }

    public void updateVideoCount(int pid, int count) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (playListDao == null)
                    return -1;
                return playListDao.updateVideoCount(integers[0], count);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "update : " + integer);
            }
        }.execute(pid);
    }

    public void updateMarkCount(int pid, int count) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (playListDao == null)
                    return -1;
                return playListDao.updateMarkCount(integers[0], count);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "update : " + integer);
            }
        }.execute(pid);
    }
}

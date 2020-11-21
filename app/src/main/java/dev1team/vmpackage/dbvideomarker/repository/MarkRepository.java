package dev1team.vmpackage.dbvideomarker.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import dev1team.vmpackage.dbvideomarker.database.AppDatabase;
import dev1team.vmpackage.dbvideomarker.database.dao.MarkDao;
import dev1team.vmpackage.dbvideomarker.database.entitiy.Mark;

import java.util.List;

@SuppressWarnings("ALL")
public class MarkRepository {

    private String TAG = MarkRepository.class.getSimpleName();

    private MarkDao markDao;
    private LiveData<List<Mark>> allMark;

    public MarkRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        markDao = db.markDao();
        allMark = markDao.findAllMark();
    }

    public LiveData<List<Mark>> getAllMark(int sort) {
        return markDao.findAllMark(sort);
    }

    public LiveData<List<Mark>> getAllMark() {
        return allMark;
    }

    public LiveData<List<Mark>> getSearchMark(String mMemo) {
        return markDao.searchMark(mMemo);
    }

    public LiveData<List<Mark>> getMarkByVideoId(int id) {
        return markDao.getMarkByVideoId(id);
    }

    public LiveData<List<Mark>> getMarkByPid(int pid) {
        return markDao.getMarkByPid(pid);
    }

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

    public void deleteMark(int id) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (markDao == null)
                    return -1;
                return markDao.deleteMark(integers[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "deleteVideo : " + integer);
            }
        }.execute(id);
    }

    public void deleteVideoWithMark(int id) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (markDao == null)
                    return -1;
                return markDao.deleteVideoWithMark(integers[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "deleteVideo : " + integer);
            }
        }.execute(id);
    }

    public void updateMark(int id, String name) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (markDao == null)
                    return -1;
                return markDao.updateMark(integers[0], name);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "updateMark : " + integer);
            }
        }.execute(id);
    }
}

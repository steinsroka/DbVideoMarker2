package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

@Dao
public interface PlayListDao {

    @Query("SELECT * FROM playlist ORDER BY pname")
    LiveData<List<PlayList>> findAllPlayList();

//    @Query("SELECT * FROM playlist WHERE pid = :pid")
//    LiveData<List<PlayList>> getPlayList();

    @Insert
    Long insertPlayList(PlayList playList);

    @Query("DELETE FROM playlist WHERE pid = :pid")
    int deletePlayList(int pid);

    @Update
    int update(PlayList playList);
}

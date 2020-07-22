package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.database.entitiy.PlayListSelect;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface PlayListDao {

    @Query("SELECT pid, pname, vcount FROM playlist ORDER BY pname")
    LiveData<List<PlayListSelect>> findAllPlayList();

    @Insert
    Long insertPlayList(PlayList playList);

    @Query("DELETE FROM playlist WHERE pid = :pid")
    int deletePlayList(int pid);
}

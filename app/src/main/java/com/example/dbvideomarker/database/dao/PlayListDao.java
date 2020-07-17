package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface PlayListDao {

    @Query("SELECT * FROM playlist ORDER BY pname")
    LiveData<List<PlayList>> findAllPlayList();

    @Insert
    Long insertPlayList(PlayList playList);
}

package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface PlRelDao {

    @Query("SELECT * FROM plRel ORDER BY plid")
    LiveData<List<PlRel>> findAllPlayListRelation();

    @Insert(onConflict = IGNORE)
    void insertPlRel(PlRel plRel);

    @Update
    int updatePlRel(PlRel plRel);

    @Delete
    void deletePlRel(PlRel plRel);
}

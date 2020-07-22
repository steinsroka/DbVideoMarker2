package com.example.dbvideomarker.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.PlRel;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface PlRelDao {

    @Insert(onConflict = IGNORE)
    void insertPlRel(PlRel plRel);

    @Update
    int updatePlRel(PlRel plRel);

    @Delete
    void deletePlRel(PlRel plRel);
}

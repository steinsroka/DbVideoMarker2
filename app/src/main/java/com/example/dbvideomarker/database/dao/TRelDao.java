package com.example.dbvideomarker.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.TRel;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TRelDao {

    @Insert(onConflict = IGNORE)
    void insertTRel(TRel tRel);

    @Update
    int updateTRel(TRel tRel);

    @Delete
    void deleteTRel(TRel tRel);
}

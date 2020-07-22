package com.example.dbvideomarker.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.Tag;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TagDao {

    @Insert(onConflict = IGNORE)
    void insertTag(Tag tag);

    @Update
    int updateTag(Tag tag);

    @Delete
    void deleteTag(Tag tag);
}

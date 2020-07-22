package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.Tag;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TagDao {

    @Query("SELECT * FROM tag WHERE tid = :tid")
    LiveData<List<Tag>> findTag(int tid);

    @Insert(onConflict = IGNORE)
    void insertTag(Tag tag);

    @Update
    int updateTag(Tag tag);

    @Delete
    void deleteTag(Tag tag);
}

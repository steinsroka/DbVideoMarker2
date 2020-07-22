package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.BookmarkSelect;
import com.example.dbvideomarker.database.entitiy.Mark;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface MarkDao {
    @Query("SELECT mMemo, mStart FROM Mark ORDER BY mmemo")
    LiveData<List<BookmarkSelect>> findAllMark();

    @Query("SELECT * FROM Mark WHERE mid = :mid")
    Mark findMark(int mid);

    @Insert(onConflict = IGNORE)
    void insertMark(Mark mark);

    @Update
    int updateMark(Mark mark);

    @Delete
    void deleteMark(Mark mark);
}

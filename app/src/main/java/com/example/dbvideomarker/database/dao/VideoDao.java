package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.database.entitiy.VideoSelect;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface VideoDao {

    @Query("SELECT v.vid, v.vMime, v.vName, t.tName " +
            "FROM Video v, Tag t, TREL tr, PLREL pr " +
            "WHERE tr.vid = v.vid AND tr.tid = t.tid AND pr.vid = v.vid AND pr.pid = :pid")
    LiveData<List<VideoSelect>> findAllVideoPL(int pid);

    //@Query("SELECT vName, vTag, vDur, vUri FROM Video ORDER BY CASE WHEN :sort = 0 THEN vName END ASC, CASE WHEN :sort = 1 THEN vUri END asc")
    //@Query("SELECT vName, vTag, vDur, vUri FROM Video ORDER BY vName ASC")
    @Query("SELECT v.vid, v.vMime, v.vName, t.tName " +
            "FROM Video v, Tag t , TREL tr " +
            "WHERE tr.vid = v.vid AND tr.tid = t.tid " +
            "ORDER BY vName ASC")
    //LiveData<List<Video>> findAllVideo (boolean sort);
    LiveData<List<VideoSelect>> findAllVideo ();


    @Query("SELECT * FROM Video WHERE vid=:vid")
    Video findVideo(long vid);

    @Insert(onConflict = IGNORE)
    void insertVideo(Video video);

    @Update
    int updateVideo(Video video);

    @Delete
    void deleteVideo(Video video);


}

package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;

import java.util.List;

@Dao
public interface PlRelDao {

    @Query("SELECT * FROM plrel ORDER BY plrel_id")
    LiveData<List<PlRel>> findAllPlayListRelation();

    @Query("SELECT video.contentId as video_id, video.vname as video_name , video.vdur as video_dur , video.vpath as video_path, " +
            "plrel.plrel_pid as playlist_id FROM plrel " +
            "INNER JOIN video ON video.contentId = plrel.plrel_vid " +
            "INNER JOIN playlist ON playlist.pid = plrel.plrel_pid " +
            "WHERE plrel_pid = :pid")
    LiveData<List<PlRelVideo>> findVideoInPlayList(int pid);

    @Insert
    long insertPlRel(PlRel plRel);

    @Update
    int updatePlRel(PlRel plRel);

    @Query("DELETE FROM plrel WHERE plrel_vid = :vid")
    int deletePlRel(int vid);

    @Query("DELETE FROM plrel WHERE plrel_pid = :pid")
    int deleteWithPlayList(int pid);

    @Query("SELECT * FROM Plrel WHERE plrel_pid != :pid")
    LiveData<List<PlRel>> videoOverlapCheck(int pid);

//    @Query("SELECT video.vid as video_id, video.vname as video_name, plrel_pid as playlist_id FROM plrel " +
//            "INNER JOIN video ON video.vid = plrel.plrel_vid " +
//            "WHERE plrel_pid != :pid")
//    LiveData<List<PlRelVideo>> getVideoOverlap(int pid);
}

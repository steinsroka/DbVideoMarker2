package dev1team.vmpackage.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import dev1team.vmpackage.dbvideomarker.database.entitiy.PlRel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PlRelDao {

    @Query("SELECT * FROM plrel ORDER BY plrel_id")
    LiveData<List<PlRel>> findAllPlayListRelation();
/*
    @Query("SELECT DISTINCT video.contentId as video_id, video.vname as video_name , video.vdur as video_dur , video.vpath as video_path, " +
            "plrel.plrel_pid as playlist_id FROM plrel " +
            "INNER JOIN video ON video.contentId = plrel.plrel_vid " +
            //"INNER JOIN playlist ON playlist.pid = plrel.plrel_pid " +
            "WHERE plrel_pid = :pid")
    LiveData<List<PlRelVideo>> findVideoInPlayList(int pid);


    @Query("SELECT DISTINCT mark.mid as mark_id, mark.vid as mark_vid, mark.mMemo as mark_mmemo, mark.mStart as mark_mstart, " +
            "plrel.plrel_pid as playlist_id FROM plrel " +
            "INNER JOIN mark ON mark.mid = plrel.plrel_mid " +
            //"INNER JOIN playlist ON playlist.pid = plrel.plrel_pid " +
            "WHERE plrel_pid = :pid")
    LiveData<List<PlRelMark>> findMarkInPlayList(int pid);
*/

    @Query("SELECT COUNT(DISTINCT plrel_vid) FROM plrel WHERE plrel_pid =:pid and plrel_mid == -1 ")
    LiveData<Integer> getVideoRowCount(int pid);

    @Query("SELECT COUNT(DISTINCT plrel_mid) FROM plrel WHERE plrel_pid =:pid and plrel_mid != -1")
    LiveData<Integer> getMarkRowCount(int pid);


    @Insert(onConflict = REPLACE)
    long insertPlRel(PlRel plRel);

    @Update
    int updatePlRel(PlRel plRel);

    @Query("DELETE FROM plrel WHERE plrel_vid = :vid")
    int deleteVideoInPlaylist(int vid);

    @Query("DELETE FROM plrel WHERE plrel_mid = :mid")
    int deleteMarkInPlaylist(int mid);

    @Query("DELETE FROM plrel WHERE plrel_pid = :pid")
    int deleteWithPlayList(int pid);

    @Query("SELECT * FROM Plrel WHERE plrel_pid != :pid")
    LiveData<List<PlRel>> videoOverlapCheck(int pid);

//    @Query("SELECT video.vid as video_id, video.vname as video_name, plrel_pid as playlist_id FROM plrel " +
//            "INNER JOIN video ON video.vid = plrel.plrel_vid " +
//            "WHERE plrel_pid != :pid")
//    LiveData<List<PlRelVideo>> getVideoOverlap(int pid);

}



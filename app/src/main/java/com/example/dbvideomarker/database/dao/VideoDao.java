package com.example.dbvideomarker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.dbvideomarker.database.entitiy.Media;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.database.entitiy.Mark;
import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;



@Dao
public interface VideoDao {

/*
    @Query("SELECT v.vMime, v.vName, t.tName " +
            "FROM Video v, Tag t, TREL tr, PLREL pr " +
            "WHERE tr.vid = v.vid AND tr.tid = t.tid AND pr.vid = v.vid AND pr.pid = :pid")
    LiveData<List<VideoSelect>> findAllVideoPL(int pid);

    //@Query("SELECT vName, vTag, vDur, vUri FROM Video ORDER BY vName ASC")
    @Query("SELECT v.vMime, v.vName, t.tName " +
            "FROM Video v, Tag t , TREL tr " +
            "WHERE tr.vid = v.vid AND tr.tid = t.tid " +
            "ORDER BY vName ASC")

    LiveData<List<VideoSelect>> findAllVideo ();
*/
    @Query("SELECT Video.*, count(Mark.vid) as countMark " +
            "FROM Video LEFT OUTER JOIN Mark on Video.contentId = Mark.vid " +
            "GROUP by Video.contentId " +
            "ORDER BY CASE WHEN :sort = 0 THEN vName END ASC, " +
                     "CASE WHEN :sort = 1 THEN vAdded END ASC, " +
                     "CASE WHEN :sort = 2 THEN vAdded END DESC, " +
                     "CASE WHEN :sort = 3 THEN countMark END DESC")
    LiveData<List<Video>> findAllVideo (int sort);

    @Query("SELECT * FROM Video ORDER BY contentId ASC")
    LiveData<List<Video>> findAllVideo ();

//    @Query("SELECT * FROM Video WHERE vname LIKE + '%' + :vName + '%' ORDER BY vName")
//    LiveData<List<Video>> searchVideo(String vName);

//    @Query("SELECT * FROM Video " +
//            "WHERE contentId = (SELECT plrel_vid FROM plrel) ")
//    LiveData<List<Video>> selectVideo();

    @Query("SELECT DISTINCT Video.*, plrel_vid as joined " +
            "FROM VIDEO JOIN plrel on Video.contentId != plrel_vid ")
    LiveData<List<Video>> selectVideo();

    @Insert(onConflict = IGNORE)
    long insertVideo(Video video);

    @Update
    int updateVideo(Video video);

    @Query("DELETE FROM video WHERE contentID = :id")
    int deleteVideo(int id);
}

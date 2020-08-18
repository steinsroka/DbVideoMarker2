package com.example.dbvideomarker.database.entitiy;

import android.view.ViewDebug;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.material.circularreveal.CircularRevealHelper;

@Entity(tableName = "plrel", foreignKeys = {
        @ForeignKey(entity = Video.class, parentColumns = "contentId", childColumns = "plrel_vid"),
        @ForeignKey(entity = PlayList.class, parentColumns = "pid", childColumns = "plrel_pid")
})
public class PlRel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "plrel_id")
    public int plid;

    @ColumnInfo(name = "plrel_pid")
    public int pid;

    @ColumnInfo(name = "plrel_vid")
    public int vid;


//    public int mid;

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

//    public int getMid() {
//        return mid;
//    }
//
//    public void setMid(int mid) {
//        this.mid = mid;
//    }

}


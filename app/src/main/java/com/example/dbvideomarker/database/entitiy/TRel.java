package com.example.dbvideomarker.database.entitiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tRel")
public class TRel {

    @PrimaryKey(autoGenerate = true)
    public int trid;
    public int tid;
    public int vid;
    public int mid;

    public TRel(int trid, int tid, int vid, int mid) {
        this.trid = trid;
        this.tid = tid;
        this.vid = vid;
        this.mid = mid;
    }

    public int getTrid() {
        return trid;
    }

    public void setTrid(int trid) {
        this.trid = trid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}

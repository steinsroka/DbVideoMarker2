package com.example.dbvideomarker.database.entitiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plRel")
public class PlRel {

    @PrimaryKey(autoGenerate = true)
    public int plid;
    public int pid;
    public int vid;
    public int mid;

    public PlRel(int plid, int pid, int vid, int mid) {
        this.plid = plid;
        this.pid = pid;
        this.vid = vid;
        this.mid = mid;
    }

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

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}

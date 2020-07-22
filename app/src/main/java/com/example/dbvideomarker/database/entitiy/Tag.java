package com.example.dbvideomarker.database.entitiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    public int tid;
    public String tName;
    public int tCount;

    public Tag(int tid, String tName, int tCount) {
        this.tid = tid;
        this.tName = tName;
        this.tCount = tCount;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public int gettCount() {
        return tCount;
    }

    public void settCount(int tCount) {
        this.tCount = tCount;
    }
}

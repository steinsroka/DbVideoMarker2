package com.example.dbvideomarker.database.entitiy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "video", indices = {@Index(value = {"vname"}, unique = true)})
public class Video {

    @PrimaryKey(autoGenerate = true)
    public int vid;

    @ColumnInfo(name = "vname")
    public int vName;

//    @ColumnInfo(name = "vpath")
//    public String vPath;
//
//    @ColumnInfo(name = "vdur")
//    public long vDur;
//
//    @ColumnInfo(name = "vsize")
//    public String vSize;
//
//    @ColumnInfo(name = "vmime")
//    public String vMime;
//
//    @ColumnInfo(name = "vadded")
//    public String vAdded;
//
//    @ColumnInfo(name = "vuri")
//    public String vUri;

//    public Video(int vid, String vName, String vMime) {
//        //, String vPath, long vDur, String vSize, String vAdded, String vUri
//        this.vid = vid;
//        this.vName = vName;
////        this.vPath = vPath;
////        this.vDur = vDur;
////        this.vSize = vSize;
//        this.vMime = vMime;
////        this.vAdded = vAdded;
////        this.vUri = vUri;
//    }

    public Video() {
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getvName() {
        return vName;
    }

    public void setvName(int vName) {
        this.vName = vName;
    }

    //    public String getvPath() {
//        return vPath;
//    }
//
//    public void setvPath(String vPath) {
//        this.vPath = vPath;
//    }
//
//    public long getvDur() {
//        return vDur;
//    }
//
//    public void setvDur(long vDur) {
//        this.vDur = vDur;
//    }
//
//    public String getvSize() {
//        return vSize;
//    }
//
//    public void setvSize(String vSize) {
//        this.vSize = vSize;
//    }
//
//    public String getvMime() {
//        return vMime;
//    }
//
//    public void setvMime(String vMime) {
//        this.vMime = vMime;
//    }
//
//    public String getvAdded() {
//        return vAdded;
//    }
//
//    public void setvAdded(String vAdded) {
//        this.vAdded = vAdded;
//    }
//
//    public String getvUri() {
//        return vUri;
//    }
//
//    public void setvUri(String vUri) {
//        this.vUri = vUri;
//    }
}


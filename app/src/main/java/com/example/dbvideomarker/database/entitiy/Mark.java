package com.example.dbvideomarker.database.entitiy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mark")
public class Mark {

    @PrimaryKey(autoGenerate = true)
    public int mid;

//    @ColumnInfo(name = "vid")
//    public int vid;

    @ColumnInfo(name = "mMemo")
    public String mMemo;

    @ColumnInfo(name = "mStart")
    public String mStart;

//    @ColumnInfo(name = "mEnd")
//    public String mEnd;

//    @ColumnInfo(name = "mAdded")
//    public String mAdded;

//    @ColumnInfo(name = "mRecent")
//    public String mRecent;

//    @ColumnInfo(name = "mBookMark")
//    public String mBookMark;

//    @ColumnInfo(name = "mCount")
//    public int mCount;

    public int getmid() { return mid; }

    public void setmid(int mid) { this.mid = mid; }

//    public int getvid() { return vid; }

//    public void setvid(int vid) { this.vid = vid; }

    public String getmMemo() { return mMemo; }

    public void setmMemo(String mMemo) { this.mMemo = mMemo; }

    public String getmStart() { return mStart; }

    public void setmStart(String mStart) { this.mStart = mStart; }

//    public String getmEnd() { return mEnd; }

//    public void setmEnd(String mEnd) { this.mEnd = mEnd; }

//    public String getmAdded() { return mAdded; }

//    public void setmAdded(String mAdded) { this.mAdded = mAdded; }

//    public String getmRecent() { return mRecent; }

//    public void setmRecent(String mRecent) { this.mRecent = mRecent; }

//    public String getmBookMark() { return mBookMark; }

//    public void setmBookMark(String mBookMark) { this.mBookMark = mBookMark; }

//    public int getmCount() { return mCount; }

//    public void setmCount(int mCount) { this.mCount = mCount; }
}

package com.example.dbvideomarker.database.entitiy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.exoplayer2.video.spherical.Projection;

@Entity(tableName = "video")
public class Video {

    @PrimaryKey
    @ColumnInfo(name = "contentId")
    public int contentId;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}


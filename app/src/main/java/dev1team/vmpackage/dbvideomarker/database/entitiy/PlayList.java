package dev1team.vmpackage.dbvideomarker.database.entitiy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist")
public class PlayList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pid")
    public int pid;

    @ColumnInfo(name = "pname")
    public String pName;

    @ColumnInfo(name = "vcount")
    public int vcount;

    @ColumnInfo(name = "mcount")
    public int mcount;


//    public PlayList(int pid, String pName) {
//        this.pid = pid;
//        this.pName = pName;
//    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getVcount() {
        return vcount;
    }

    public void setVcount(int vcount) {
        this.vcount = vcount;
    }

    public int getMcount() {
        return mcount;
    }

    public void setMcount(int mcount) {
        this.mcount = mcount;
    }
}


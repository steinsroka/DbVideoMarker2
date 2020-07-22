package com.example.dbvideomarker.database.entitiy;

public class VideoSelect {
    private int vid;
    private String vDur;
    private String vName;
    private String tName;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getvDur() {
        return vDur;
    }

    public void setvDur(String vDur) {
        this.vDur = vDur;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }
}

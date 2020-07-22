package com.example.dbvideomarker.database.entitiy;

public class VideoSelect {
    private String vDur;
    private String vName;
    private String tName;

    public String getvMime() {
        return vDur;
    }

    public void setvMime(String vMime) {
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

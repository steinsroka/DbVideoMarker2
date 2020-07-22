package com.example.dbvideomarker.database.entitiy;

public class VideoSelect {
    private String vMime;
    private String vName;
    private String tName;

    public String getvMime() {
        return vMime;
    }

    public void setvMime(String vMime) {
        this.vMime = vMime;
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

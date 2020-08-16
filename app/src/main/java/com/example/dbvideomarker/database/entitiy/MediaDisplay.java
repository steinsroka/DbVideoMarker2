package com.example.dbvideomarker.database.entitiy;

public class MediaDisplay {

    private int resId;
    private String Name;
    private String Dur;
    private String ContentUri;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDur() {
        return Dur;
    }

    public void setDur(String dur) {
        Dur = dur;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getContentUri() {
        return ContentUri;
    }

    public void setContentUri(String contentUri) {
        ContentUri = contentUri;
    }
}

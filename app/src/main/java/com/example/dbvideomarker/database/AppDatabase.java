package com.example.dbvideomarker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dbvideomarker.database.dao.MarkDao;
import com.example.dbvideomarker.database.dao.PlRelDao;
import com.example.dbvideomarker.database.dao.PlayListDao;
import com.example.dbvideomarker.database.dao.VideoDao;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.database.entitiy.Video;

@Database(entities = {Video.class, PlayList.class, Mark.class, PlRel.class}, version = 255, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    //데이터베이스와 연결되는 DAO, DAO는 abstract로 getter 제공
    public abstract VideoDao videoDao();

    public abstract PlayListDao playListDao();

    public abstract MarkDao markDao();

    public abstract PlRelDao plRelDao();

    private static AppDatabase INSTANCE;


    //singleton 패턴, roomdatabase는 하나만 존재
    public synchronized static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            //synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    //데이터베이스 생성
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB")
//                            .allowMainThreadQueries()
//                            .addCallback(sAppDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            //}
        }
        return INSTANCE;
    }
}

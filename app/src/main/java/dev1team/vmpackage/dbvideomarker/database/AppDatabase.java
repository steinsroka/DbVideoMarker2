package dev1team.vmpackage.dbvideomarker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dev1team.vmpackage.dbvideomarker.database.dao.MarkDao;
import dev1team.vmpackage.dbvideomarker.database.dao.PlRelDao;
import dev1team.vmpackage.dbvideomarker.database.dao.PlayListDao;
import dev1team.vmpackage.dbvideomarker.database.dao.VideoDao;
import dev1team.vmpackage.dbvideomarker.database.entitiy.Mark;
import dev1team.vmpackage.dbvideomarker.database.entitiy.PlRel;
import dev1team.vmpackage.dbvideomarker.database.entitiy.PlayList;
import dev1team.vmpackage.dbvideomarker.database.entitiy.Video;

@Database(entities = {Video.class, PlayList.class, Mark.class, PlRel.class}, version = 255, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    //데이터베이스와 연결되는 DAO, DAO는 abstract로 getter 제공
    public abstract VideoDao videoDao();

    public abstract PlayListDao playListDao();

    public abstract MarkDao markDao();

    public abstract PlRelDao plRelDao();

    private static AppDatabase INSTANCE;


    //singleton 패턴, roomdatabase는 하나만 존재
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    //데이터베이스 생성
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB")
//                            .allowMainThreadQueries()
//                            .addCallback(sAppDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    private static RoomDatabase.Callback sAppDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                VideoDao vdao = INSTANCE.videoDao();
//                //dao.deleteAll();
//
//                Video video = new Video(1, "Video1");
//                //, "path1", 100000, "100", "mp4", "2020/02/02", "contentUri1"
//                vdao.insertVideo(video);
//
//                PlayListDao pdao = INSTANCE.playListDao();
//                PlayList playList = new PlayList(1, "PlayList1");
//                pdao.insertPlayList(playList);
//
//                MarkDao mdao = INSTANCE.markDao();
//                Mark mark = new Mark(1, "Mark1");
//                mdao.insertMark(mark);
//            });
//
//        }
//    };

}

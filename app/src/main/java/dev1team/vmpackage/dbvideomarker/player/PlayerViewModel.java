package dev1team.vmpackage.dbvideomarker.player;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dev1team.vmpackage.dbvideomarker.database.entitiy.Mark;
import dev1team.vmpackage.dbvideomarker.repository.MarkRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private MarkRepository markRepository;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        markRepository = new MarkRepository(application);
    }

    public LiveData<List<Mark>> getMarkByVideoId(int id) {
        return markRepository.getMarkByVideoId(id);
    }

    public void insertMark(Mark mark) {
        markRepository.insertMark(mark);
    }
}

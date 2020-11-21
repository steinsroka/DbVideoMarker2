package dev1team.vmpackage.dbvideomarker.ui.mark;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dev1team.vmpackage.dbvideomarker.database.entitiy.Mark;
import dev1team.vmpackage.dbvideomarker.repository.MarkRepository;
import dev1team.vmpackage.dbvideomarker.repository.PlayListEditRepository;

import java.util.List;

public class MarkViewModel extends AndroidViewModel {

    private MarkRepository markRepository;
    private PlayListEditRepository playListEditRepository;
    private LiveData<List<Mark>> allMark;

    public MarkViewModel(@NonNull Application application) {
        super(application);
        markRepository = new MarkRepository(application);
        allMark = markRepository.getAllMark();
    }

    public LiveData<List<Mark>> getAllMark(int sort) {
        return markRepository.getAllMark(sort);
    }

    public LiveData<List<Mark>> getAllMark() {
        return allMark;
    }

    public LiveData<List<Mark>> getMarkByPid(int pid) {
        return markRepository.getMarkByPid(pid);
    }

    public void deleteMark(int id) {
        markRepository.deleteMark(id);
    }

    public void updateMark(int id, String name) {
        markRepository.updateMark(id, name);
    }


}
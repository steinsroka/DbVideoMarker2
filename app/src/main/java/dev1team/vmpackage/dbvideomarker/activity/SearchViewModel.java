package dev1team.vmpackage.dbvideomarker.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dev1team.vmpackage.dbvideomarker.database.entitiy.Mark;
import dev1team.vmpackage.dbvideomarker.repository.MarkRepository;
import dev1team.vmpackage.dbvideomarker.repository.VideoRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private MarkRepository markRepository;


    public SearchViewModel(@NonNull Application application) {
        super(application);

        new VideoRepository(application);
        markRepository = new MarkRepository(application);
    }

    public LiveData<List<Mark>> getSearchMark(String mmemo) {
        return markRepository.getSearchMark(mmemo);
    }
}

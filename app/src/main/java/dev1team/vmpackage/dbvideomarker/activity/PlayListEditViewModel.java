package dev1team.vmpackage.dbvideomarker.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dev1team.vmpackage.dbvideomarker.database.entitiy.PlRel;
import dev1team.vmpackage.dbvideomarker.database.entitiy.PlayList;
import dev1team.vmpackage.dbvideomarker.repository.PlayListEditRepository;
import dev1team.vmpackage.dbvideomarker.repository.PlayListRepository;

import java.util.List;

public class PlayListEditViewModel extends AndroidViewModel {

    private PlayListEditRepository playListEditRepository;
    private PlayListRepository playListRepository;

    public PlayListEditViewModel(@NonNull Application application) {
        super(application);
        playListEditRepository = new PlayListEditRepository(application);
        playListRepository = new PlayListRepository(application);
        LiveData<List<PlRel>> allPlayListRelation = playListEditRepository.getAllPlRel();
    }

    public LiveData<Integer> getMarkRowCount(int pid) {
        return playListEditRepository.getMarkRowCount(pid);
    }

    public LiveData<Integer> getVideoRowCount(int pid) {
        return playListEditRepository.getVideoCount(pid);
    }


    public LiveData<PlayList> getPlayList(int pid) {
        return playListRepository.getPlayList(pid);
    }

    public void insertPlRelation(PlRel plRel) {
        playListEditRepository.insertPlRelation(plRel);
    }

    public void update(PlayList playList) {
        playListRepository.update(playList);
    }

    public void deleteVideoInPlaylist(int vid) {
        playListEditRepository.deleteVideoInPlaylist(vid);
    }

    public void deleteMarkInPlaylist(int mid) {
        playListEditRepository.deleteMarkInPlaylist(mid);
    }
}

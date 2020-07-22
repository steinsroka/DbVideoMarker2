package com.example.dbvideomarker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.PlayListAdapter;
import com.example.dbvideomarker.adapter.PlayListEditAdapter;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.ui.notifications.NotificationsViewModel;

import java.util.List;

public class PlayListEditActivity extends AppCompatActivity {

    private PlayListEditViewModel playListEditViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_playlistedit);

        Intent intent = getIntent();

        String pidToString = intent.getExtras().getString("재생목록 번호");
        int pid = Integer.parseInt(pidToString);
        String pname = intent.getExtras().getString("재생목록 이름");

        RecyclerView recyclerView = findViewById(R.id.rv_Playlist);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        PlayListEditAdapter adapter = new PlayListEditAdapter(this);

        // Get a new or existing ViewModel from the ViewModelProvider.
        playListEditViewModel = new ViewModelProvider(this).get(PlayListEditViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        playListEditViewModel.getAllPlayList().observe(this, new Observer<List<PlayList>>() {
            @Override
            public void onChanged(List<PlayList> playList) {
                //Update the cached copy of the words in the adapter.
                adapter.setPlayLists(playList);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }
}

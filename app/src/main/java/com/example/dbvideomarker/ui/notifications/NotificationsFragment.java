package com.example.dbvideomarker.ui.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.MainActivity;
import com.example.dbvideomarker.adapter.PlayListAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.ui.home.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_notifications, container, false);
        Context context = rv.getContext();

        RecyclerView recyclerView = rv.findViewById(R.id.rv_Playlist);
        PlayListAdapter adapter = new PlayListAdapter(context);

        // Get a new or existing ViewModel from the ViewModelProvider.
        notificationsViewModel = new ViewModelProvider(getActivity()).get(NotificationsViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        notificationsViewModel.getAllPlayList().observe(getViewLifecycleOwner(), new Observer<List<PlayList>>() {
            @Override
            public void onChanged(List<PlayList> playList) {
                //Update the cached copy of the words in the adapter.
                adapter.setPlayLists(playList);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

//        FloatingActionButton fab = (FloatingActionButton) rv.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        rv.findViewById(R.id.btn_addPlayList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = rv.findViewById(R.id.et_PlayListName);
                if (et.getText().toString().trim().length() != 0) {
                    String name = et.getText().toString().trim();
                    PlayList playList = new PlayList();
                    playList.setpName(name);

                    notificationsViewModel.insertPlayList(playList);
                }

            }
        });
        return rv;
    }
}
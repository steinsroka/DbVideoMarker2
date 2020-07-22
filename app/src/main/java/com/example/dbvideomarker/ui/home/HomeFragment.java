package com.example.dbvideomarker.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.database.AppDatabase;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.database.entitiy.VideoSelect;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Context context = v.getContext();

        RecyclerView recyclerView = v.findViewById(R.id.rv_Home);
        VideoAdapter adapter = new VideoAdapter(context);

        // Get a new or existing ViewModel from the ViewModelProvider.
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        homeViewModel.getAllVideo().observe(getActivity(), new Observer<List<VideoSelect>>() {
            @Override
            public void onChanged(List<VideoSelect> videos) {
                //Update the cached copy of the words in the adapter.
                adapter.setVideos(videos);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        return v;
    }
}
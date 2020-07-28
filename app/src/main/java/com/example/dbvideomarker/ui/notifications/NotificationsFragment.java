package com.example.dbvideomarker.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.PlayListEditActivity;
import com.example.dbvideomarker.adapter.PlayListAdapter;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

public class NotificationsFragment extends Fragment implements OnItemClickListener {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_notifications, container, false);
        Context context = rv.getContext();

        RecyclerView recyclerView = rv.findViewById(R.id.rv_Playlist);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        PlayListAdapter adapter = new PlayListAdapter(context, this);

        // Get a new or existing ViewModel from the ViewModelProvider.
        notificationsViewModel = new ViewModelProvider(getActivity()).get(NotificationsViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        notificationsViewModel.findAllPlayList().observe(getViewLifecycleOwner(), new Observer<List<PlayList>>() {
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

    @Override
    public void clickLongItem(int pid) {
        notificationsViewModel.deletePlayList(pid);
    }

    @Override
    public void clickItem(int pid) {
        Intent intent = new Intent(getContext(), PlayListEditActivity.class);
        intent.putExtra("재생목록 번호", pid);
        getContext().startActivity(intent);
    }
}
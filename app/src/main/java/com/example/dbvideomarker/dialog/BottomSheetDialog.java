package com.example.dbvideomarker.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.PlayListEditViewModel;
import com.example.dbvideomarker.adapter.PlayListAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.ui.home.HomeFragment;
import com.example.dbvideomarker.ui.playlist.PlaylistViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BottomSheetDialog extends BottomSheetDialogFragment implements OnItemClickListener, OnItemSelectedListener {

    private PlaylistViewModel playlistViewModel;
    private PlayListEditViewModel playListEditViewModel;
    private int pid;
    private TextView tv1;
    private ArrayList<Integer> idList;
    private ArrayList<Integer> pidList;
    private Button btnSelection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            idList = getArguments().getIntegerArrayList("idList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_bottom_sheet, container, false);

        Context context = v.getContext();

        tv1 = v.findViewById(R.id.textview);
        if(idList != null) {
            tv1.setText(""+idList.size());
        }

        RecyclerView recyclerView = v.findViewById(R.id.rv_playlist_bottom);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        PlayListAdapter adapter = new PlayListAdapter(context, ViewCase.SELECT, this, this);

        // Get a new or existing ViewModel from the ViewModelProvider.
        playlistViewModel = new ViewModelProvider(getActivity()).get(PlaylistViewModel.class);
        playListEditViewModel = new ViewModelProvider(getActivity()).get(PlayListEditViewModel.class);
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        playlistViewModel.findAllPlayList().observe(getViewLifecycleOwner(), new Observer<List<PlayList>>() {
            @Override
            public void onChanged(List<PlayList> playList) {
                //Update the cached copy of the words in the adapter.
                adapter.setPlayLists(playList);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        btnSelection = (Button) v.findViewById(R.id.parse_pid);
        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<pidList.size(); i++) {
                    playlistViewModel.updateVideoCount(pidList.get(i), idList.size());
                    for(int j=0; j<idList.size(); j++) {
                        PlRel plrel = new PlRel();
                        plrel.setPid((pidList.get(i)));
                        plrel.setVid(idList.get(j));
                        playListEditViewModel.insertPlRelation(plrel);
                    }

                }
                dismiss();
                // TODO: 여기서 선택모드 종료
            }
        });

        return v;
    }

    @Override
    public void clickItem(int id, String path) {}

    @Override
    public void clickLongItem(View v, int id, String path) {}

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        pidList = new ArrayList<>();
        for(int i=0; i<sparseBooleanArray.size(); i++) {
            pidList.add(sparseBooleanArray.keyAt(i));
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("BottomSheetDialog", "bottom sheet dialog dismissed");
    }
}

package com.example.dbvideomarker.ui.playlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.MainActivity;
import com.example.dbvideomarker.activity.PlayListEditActivity;
import com.example.dbvideomarker.activity.SearchActivity;
import com.example.dbvideomarker.adapter.PlayListAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.VideoViewHolderDrag;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnPlaylistClickListener;
import com.example.dbvideomarker.player.PlayerActivity;
import com.example.dbvideomarker.ui.home.HomeViewModel;

public class PlaylistFragment extends Fragment implements OnPlaylistClickListener, OnItemSelectedListener, OnItemClickListener, VideoAdapter.OnStartDragListener {

    private PlaylistViewModel playlistViewModel;
    private HomeViewModel homeViewModel;
    private View rv;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(R.layout.fragment_playlist, container, false);
        context = rv.getContext();

        rv.findViewById(R.id.btn_addPlayList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                EditText plName = new EditText(getActivity());
                mBuilder.setView(plName);
                mBuilder.setTitle("재생목록 추가");
                mBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(plName.getText().toString().trim().length() != 0) {
                            PlayList playList = new PlayList();
                            playList.setpName(plName.getText().toString());
                            playList.setVcount(0);
                            playList.setMcount(0);
                            playlistViewModel.insertPlayList(playList);
                        }
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        setPlaylistView();
        setRecentView();

        return rv;
    }

    public void setPlaylistView() {
        RecyclerView recyclerView = rv.findViewById(R.id.rv_Playlist);
        PlayListAdapter adapter = new PlayListAdapter(context, ViewCase.NORMAL, this, this);
        playlistViewModel = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
        playlistViewModel.findAllPlayList().observe(getViewLifecycleOwner(), adapter::setPlayLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void setRecentView() {
        RecyclerView recentView = rv.findViewById(R.id.rv_recentView);
        VideoAdapter recentAdapter = new VideoAdapter(context, ViewCase.RECENT, this, this, this);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.findRecentViewVideo().observe(getViewLifecycleOwner(), recentAdapter::setVideos);
        recentView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recentView.setAdapter(recentAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.select:
                Toast.makeText(getActivity(), "1111", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
//                Intent intent = new Intent(this, SettingActivity.class);
//                //액티비티 시작!
//                startActivity(intent);
                break;
            case R.id.menu_search:
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                startActivity(intentSearch);
                break;
        }
        return true;
    }


    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {

    }

    @Override
    public void clickPlaylist(int id) {
        Intent intent = new Intent(getContext(), PlayListEditActivity.class);
        intent.putExtra("재생목록 번호", id);
        requireContext().startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void clickLongPlaylist(View v, int id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_playlist, menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.modifyPlayList:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    EditText et = new EditText(getActivity());
                    builder.setView(et);
                    builder.setPositiveButton("확인", (dialogInterface, i) -> {
                        if (et.getText().toString().trim().length() != 0) {
                            PlayList playList = new PlayList();
                            playList.setpName(et.getText().toString());
                            playList.setPid(id);
                            playlistViewModel.update(playList);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                case (R.id.deletePlayList):
                    playlistViewModel.deleteWithPlayList(id);
                    playlistViewModel.deletePlayList(id);
                    break;
            }
            return false;
        });
        popupMenu.show();
    }


    //비디오 터치 처리
    @Override
    public void clickItem(int id, String path) {
        Intent playerIntent = new Intent(getContext(), PlayerActivity.class);
        playerIntent.putExtra("ContentID", id);
        playerIntent.putExtra("Path", path);
        playerIntent.putExtra("Start", 0L);
        //update
        homeViewModel.updateRecentVideo(id, System.currentTimeMillis());

        requireContext().startActivity(playerIntent);
    }

    @Override
    public void clickLongItem(View v, int id, String path) {
        //Do Nothing
    }

    @Override
    public void onStartDrag(VideoViewHolderDrag mHolder) {

    }
}
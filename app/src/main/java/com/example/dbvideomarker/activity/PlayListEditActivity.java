package com.example.dbvideomarker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.PlayList_MarkAdapter;
import com.example.dbvideomarker.adapter.PlayList_VideoAdapter;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.util.Callback;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.player.PlayerActivity;
import com.example.dbvideomarker.ui.home.HomeViewModel;
import com.example.dbvideomarker.ui.playlist.PlaylistViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlayListEditActivity extends AppCompatActivity implements OnItemClickListener, OnMarkClickListener, PlayList_VideoAdapter.OnStartDragListener {

    private PlayListEditViewModel playListEditViewModel;
    private HomeViewModel homeViewModel;
    private PlaylistViewModel playlistViewModel;
    private List<PlRelVideo> resultList = new ArrayList<>();
    public int SELECT_VIDEO_REQUEST_CODE = 1001;
    public int SELECT_MARK_REQUEST_CODE = 1002;

    private int pid;

    private ItemTouchHelper itemTouchHelper;
    private RequestManager _mGlideRequestManager;
    private FloatingActionButton fab_video;
    private FloatingActionButton fab_mark;
    private TextView vCount, mCount;
    private PlayList_VideoAdapter adapter_video;
    private PlayList_MarkAdapter adapter_mark;

    private int VIDEO_COUNT;
    private int MARK_COUNT;

    Boolean IS_OPEN = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_playlistedit);

        _mGlideRequestManager = Glide.with(this);
        Intent intent = getIntent();

        pid = intent.getIntExtra("재생목록 번호", -1);
//        int pid = Integer.parseInt(pidToString);
//        String pname = intent.getStringExtra("재생목록 이름");
//        TextView playListCount = (TextView) findViewById(R.id.playListCount);
//        playListId.setText(""+pid); //setText 에서 int형 파라미터는 리소스 id 값이지 그냥 int값이 아님. String 형태로 바꿔서 출력해야함 + setText는 charsequance 자료형임
        TextView playListName = (TextView) findViewById(R.id.playListName);

        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        playListEditViewModel = new ViewModelProvider(this).get(PlayListEditViewModel.class);
        playListEditViewModel.getPlayList(pid).observe(this, playList -> {
            String pname = playList.getpName();
            playListName.setText(pname);
        });

        vCount = findViewById(R.id.video_count);
        mCount = findViewById(R.id.mark_count);

        playListEditViewModel.getVideoRowCount(pid).observe(this, integer -> {
            VIDEO_COUNT = integer;
            vCount.setText(String.valueOf(integer));
        });
        playListEditViewModel.getMarkRowCount(pid).observe(this, integers -> {
            MARK_COUNT = integers;
            mCount.setText(String.valueOf(integers));
        });
        setVideoInPlaylist();
        setMarkInPlaylist();
        setFab();
    }

    public void setFab() {
        FloatingActionButton fab_main = findViewById(R.id.fab_main);
        fab_video = findViewById(R.id.fab_video);
        fab_mark = findViewById(R.id.fab_mark);

        fab_main.setOnClickListener(view -> {
            if (!IS_OPEN) {
                fab_video.setVisibility(View.VISIBLE);
                fab_mark.setVisibility(View.VISIBLE);
                IS_OPEN = true;
            } else {
                fab_video.setVisibility(View.GONE);
                fab_mark.setVisibility(View.GONE);
                IS_OPEN = false;
            }
        });

        fab_video.setOnClickListener(view -> {
            Intent videoIntent = new Intent(PlayListEditActivity.this, SelectActivity.class);
            videoIntent.putExtra("pid", pid);
            videoIntent.putExtra("VIEW_TYPE", 2001);
            startActivityForResult(videoIntent, SELECT_VIDEO_REQUEST_CODE);
        });

        fab_mark.setOnClickListener(view -> {
            Intent markIntent = new Intent(PlayListEditActivity.this, SelectActivity.class);
            markIntent.putExtra("pid", pid);
            markIntent.putExtra("VIEW_TYPE", 2002);
            startActivityForResult(markIntent, SELECT_MARK_REQUEST_CODE);
        });
    }

    public void setVideoInPlaylist() {
        RecyclerView recyclerView_video = findViewById(R.id.rv_PlaylistEdit_video);

        adapter_video = new PlayList_VideoAdapter(this, this, this, _mGlideRequestManager);
        Callback callback = new Callback(adapter_video);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView_video);

        //vCount = findViewById(R.id.video_count);

        playListEditViewModel.findVideoInPlayList(pid).observe(this, plRels -> {
            adapter_video.setPlRelv(plRels);
            //vCount.setText(""+plRels.size());
/*
            resultList = getStringArrayList(""+pid);

            if(resultList == null) {
                setStringArrayList(""+pid, plRels);
                adapter.setPlRels(plRels);
            } else {
                if(resultList.size() < plRels.size()) {
                    for(int i = 0; i < plRels.size(); i++) {
                        if (!resultList.contains(plRels.get(i))) {
                            resultList.add(plRels.get(i));
                            setStringArrayList(""+pid, resultList);
                            adapter.setPlRels(resultList);
                        }
                    }
                } else if(resultList.size() > plRels.size()){
                    for(int i = 0; i < resultList.size(); i++) {
                        if (!plRels.contains(resultList.get(i))) {
                            resultList.remove(i);
                            setStringArrayList(""+pid, resultList);
                            adapter.setPlRels(resultList);
                        }
                    }
                } else {
                    setStringArrayList(""+pid, resultList);
                    adapter.setPlRels(resultList);
                }
            }
*/
        });

        recyclerView_video.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView_video.setAdapter(adapter_video);
    }


    public void setMarkInPlaylist() {
        RecyclerView recyclerView_mark = findViewById(R.id.rv_PlaylistEdit_mark);
        adapter_mark = new PlayList_MarkAdapter(this, this, _mGlideRequestManager);

        //mCount = findViewById(R.id.mark_count);

        playListEditViewModel.findMarkInPlayList(pid).observe(this, plRelMarks -> {
            adapter_mark.setPlRelm(plRelMarks);
            //mCount.setText(""+plRelMarks.size());
        });
        recyclerView_mark.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView_mark.setAdapter(adapter_mark);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO_REQUEST_CODE) {
                assert data != null;
                ArrayList<Integer> selectedVidList = data.getIntegerArrayListExtra("vidlist");
                playlistViewModel.updateVideoCount(pid, VIDEO_COUNT);
                assert selectedVidList != null;
                for (int i = 0; i < selectedVidList.size(); i++) {
                    PlRel plRel = new PlRel();
                    plRel.setPid(pid);
                    plRel.setMid(-1);
                    plRel.setVid(selectedVidList.get(i));
                    playListEditViewModel.insertPlRelation(plRel);
                }
            } else if (requestCode == SELECT_MARK_REQUEST_CODE) {
                assert data != null;
                ArrayList<Integer> selectedMidList = data.getIntegerArrayListExtra("midlist");
                playlistViewModel.updateMarkCount(pid, MARK_COUNT);
                assert selectedMidList != null;
                for (int i = 0; i < selectedMidList.size(); i++) {
                    PlRel plRel = new PlRel();
                    plRel.setPid(pid);
                    plRel.setMid(selectedMidList.get(i));
                    playListEditViewModel.insertPlRelation(plRel);
                }
            }
        }
    }

    @Override
    public void clickItem(int id, String path) {
        Intent playerIntent = new Intent(this, PlayerActivity.class);
        playerIntent.putExtra("ContentID", id);
        playerIntent.putExtra("Path", path);
        playerIntent.putExtra("Start", 0L);
        homeViewModel.updateRecentVideo(id, System.currentTimeMillis());
        startActivity(playerIntent);
    }

    @Override
    public void clickLongItem(View v, int id, String path) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_playlist_video, menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.popup_delete_playlist) {
                playListEditViewModel.deleteVideoInPlaylist(id);
            }
            return false;
        });
        popupMenu.show();
    }


    @Override
    public void clickMark(int id, long start, String path) {
        Intent playerIntent = new Intent(this, PlayerActivity.class);
        playerIntent.putExtra("ContentID", id);
        playerIntent.putExtra("Path", path);
        playerIntent.putExtra("Start", start);
        startActivity(playerIntent);
    }

    @Override
    public void clickLongMark(View v, int id, String path) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_playlist_mark, menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.popup_delete_playlist) {
                playListEditViewModel.deleteMarkInPlaylist(id);
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onStartDrag(PlayList_VideoAdapter.PLEViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStringArrayList("" + pid, adapter_video.plRelList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStringArrayList("" + pid, adapter_video.plRelList);
    }

    public void setStringArrayList(String key, List<PlRelVideo> valueList) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        //Gson build gradle에 추가해야함
        Gson gson = new Gson();
        String json = gson.toJson(valueList);
        editor.putString(key, json);
        editor.apply();
    }

    public List<PlRelVideo> getStringArrayList(String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = pref.getString(key, null);
        Type type = new TypeToken<List<PlRelVideo>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}

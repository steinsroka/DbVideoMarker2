package com.example.dbvideomarker.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.InfoActivity;
import com.example.dbvideomarker.activity.PlayListEditViewModel;
import com.example.dbvideomarker.activity.SearchActivity;
import com.example.dbvideomarker.activity.setting.SettingActivity;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.Media;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.dialog.BottomSheetDialog;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.util.FileUtil;
import com.example.dbvideomarker.util.MediaStoreLoader;
import com.example.dbvideomarker.player.PlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemSelectedListener, OnItemClickListener {

    private String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private PlayListEditViewModel playListEditViewModel;
    private RequestManager mGlideRequestManager;
    private VideoAdapter videoAdapter;

    private int selectedSort = 0;
    private View v;
    private View normalView;
    private View selectView;
    private View bottomMenu;

    private ImageButton btn_add_playlist, btn_info, btn_delete;

    private ArrayList<Integer> idList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
//        Context context = v.getContext();
        mGlideRequestManager = Glide.with(getActivity());


        normalView = v.findViewById(R.id.video_normal_wrapper);
        selectView = v.findViewById(R.id.video_select_wrapper);
        bottomMenu = v.findViewById(R.id.home_bottom_menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수

            } else {

                //Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
            }
        }

        setVideoNormalView();
        normalView.setVisibility(View.VISIBLE);
        selectView.setVisibility(View.GONE);
        bottomMenu.setVisibility(View.GONE);


        setBottomMenu();
//        Button buttonMediaRoom = v.findViewById(R.id.media_room);
//        buttonMediaRoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<Integer> idArray = MediaStoreLoader.getIdArray(getActivity());
//                Log.d(TAG, "idArraySize ======" + idArray);
//                idArray.clear();
//            }
//        });
//
//        Button buttonMedia = v.findViewById(R.id.btn_media);
//        buttonMedia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RecyclerView recyclerView = v.findViewById(R.id.rv_Home);
//                DividerItemDecoration dividerItemDecoration =
//                        new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
//                recyclerView.addItemDecoration(dividerItemDecoration);
//                mediaAdapter = new MediaAdapter(context, ViewCase.MEDIA, datas);
//                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//                recyclerView.setAdapter(mediaAdapter);
//            }
//        });
        //최초실행 확인 + 최초실행시 Room에 데이터 추가
        SharedPreferences pref = getActivity().getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            addMediaDataToRoom();
        }else{
            Log.d("Is first Time?", "not first");
        }

        Button buttonSortDialog = v.findViewById(R.id.video_sort);
        buttonSortDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSort = sort();
//                SortRunnable runnable = new SortRunnable();
//                Thread thread = new Thread(runnable);
//                thread.setDaemon(true);
//                thread.start();

            }
        });

        return v;
    }

//    public void addToPlaylist(int pid) {
//       Toast.makeText(getActivity(), "재생목록: "+pid+" 리스트 크기"+idList.size(), Toast.LENGTH_SHORT).show();
//        for(int j=0; j<idList.size(); j++) {
//            PlRel plRel = new PlRel();
//            plRel.setPid(pid);
//            plRel.setVid(idList.get(j));
//            playListEditViewModel.insertPlRelation(plRel);
//        }
//    }

    public void setBottomMenu() {
        btn_add_playlist = v.findViewById(R.id.video_bottom_add_playlist);
        btn_info = v.findViewById(R.id.video_bottom_info);
        btn_delete = v.findViewById(R.id.video_bottom_delete);

        btn_add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog video_bottomSheetDialog = new BottomSheetDialog();
                video_bottomSheetDialog.show(getChildFragmentManager(), "bottomSheetDialog");
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(getContext(), InfoActivity.class);
                infoIntent.putExtra("ContentID", idList.get(0));
                getContext().startActivity(infoIntent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<idList.size(); i++) {
                    deleteVideo(idList.get(i));
                    Toast.makeText(getActivity(), idList.get(i) + "Deleted", Toast.LENGTH_SHORT).show();
                    setVideoNormalView();
                }
            }
        });
    }

    public void addMediaDataToRoom() {
        List<Media> mediaList = MediaStoreLoader.getContent(getActivity());
        for (int i = 0; i < mediaList.size(); i++) {
            Media media = mediaList.get(i);
            Video video = new Video();
            video.setContentId(media.getResId());
            video.setVdur(media.getDur());
            video.setVname(media.getName());
            video.setVpath(media.getPath());
            video.setVadded(media.getAdded());
            homeViewModel.insertVideo(video);
        }
    }

    public void setVideoNormalView() {

        normalView.setVisibility(View.VISIBLE);
        selectView.setVisibility(View.GONE);
        bottomMenu.setVisibility(View.GONE);

        VideoAdapter videoAdapter = new VideoAdapter(getActivity(), ViewCase.NORMAL, this, this);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(videoAdapter);
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        homeViewModel.getAllVideo(selectedSort).observe(getActivity(), new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                videoAdapter.setVideos(videos);
            }
        });
    }

    public void setVideoSelectView() {

        normalView.setVisibility(View.GONE);
        selectView.setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = v.findViewById(R.id.rv_Home_select);
        VideoAdapter adapter = new VideoAdapter(getActivity(), ViewCase.SELECT, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllVideo(selectedSort).observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                adapter.setVideos(videos);
            }
        });
    }

    public void deleteVideo(int id) {
        homeViewModel.deleteVideoWithMark(id);
        homeViewModel.deleteVideo(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.select:
                setVideoSelectView();
                break;
            case R.id.setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_search:
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.menu_sort:
                addMediaDataToRoom();
                break;
        }
        return true;
    }

    public int sort() {
        final String[] sort = new String[]{"영상 제목순", "추가된순(최근)", "추가된순(오래된)", "북마크된 수"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("정렬 순서")
                .setSingleChoiceItems(sort, selectedSort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedSort = which;
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homeViewModel.getAllVideo(selectedSort).observe(getActivity(), new Observer<List<Video>>() {
                            @Override
                            public void onChanged(List<Video> videos) {
                                videoAdapter.setVideos(videos);
                            }
                        });
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return selectedSort;
    }

    @Override
    public void clickLongItem(View v, int id, String path) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_video, menu);



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_info:
                        Intent infoIntent = new Intent(getContext(), InfoActivity.class);
                        infoIntent.putExtra("ContentID", id);
                        getContext().startActivity(infoIntent);
                        break;
                    case R.id.popup_edit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        EditText afterName = new EditText(getActivity());
                        builder.setView(afterName);
                        builder.setTitle("북마크 추가");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (afterName.getText().toString().trim().length() != 0) {
                                    Video video =  new Video();
                                    video.setVname(afterName.getText().toString());
                                    //File update, mediastore update
                                    homeViewModel.updateVideo(id, afterName.getText().toString());

                                    FileUtil fileUtil = new FileUtil();
                                    fileUtil.fileRename(path, video.getVname(), afterName.getText().toString());
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        break;
                    case (R.id.popup_delete):
                        //File delete
                        deleteVideo(id);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void clickItem(int id, String path) {
        Intent playerIntent = new Intent(getContext(), PlayerActivity.class);
        playerIntent.putExtra("ContentID", id);
        playerIntent.putExtra("Path", path);
        playerIntent.putExtra("Start", 0L);
        getContext().startActivity(playerIntent);
    }


    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        idList = new ArrayList<>();

        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            idList.add(sparseBooleanArray.keyAt(i));
            Log.d("text", "idList 길이 : " + sparseBooleanArray);
            if (idList.size() > 1) {
                btn_info.setBackgroundResource(R.drawable.ic_baseline_info_red_24);
                btn_info.setClickable(false);
            } else {
                btn_info.setBackgroundResource(R.drawable.ic_baseline_info_24);
                btn_info.setClickable(true);
            }
        }
    }


//    class SortRunnable implements Runnable {
//        public List<Video> videoList;
//
//        @Override
//        public void run() {
//            homeViewModel.getAllVideo(selectedSort).observe(getActivity(), new Observer<List<Video>>() {
//                @Override
//                public void onChanged(List<Video> videos) {
//                    videoList = videos;
//                    handler.sendEmptyMessage(0);
//                }
//            });
//
//        }
//
//        Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                if(msg.what == 0)
//                    videoAdapter.setVideos(videoList);
//            }
//        };
//    }
}
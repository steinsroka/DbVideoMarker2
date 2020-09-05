package com.example.dbvideomarker.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.InfoActivity;
import com.example.dbvideomarker.player.PlayerActivity;
import com.example.dbvideomarker.adapter.MediaAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Media;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.mediastore.MediaStoreLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemSelectedListener, OnItemClickListener {

    private HomeViewModel homeViewModel;
    private MediaAdapter mediaAdapter;
    private String TAG = HomeFragment.class.getSimpleName();
    public RequestManager mGlideRequestManager;
    public int selectedSort = 0;
    public VideoAdapter videoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = v.getContext();
        mGlideRequestManager = Glide.with(getActivity());

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

        List<Media> datas = MediaStoreLoader.getContent(getActivity());
        ArrayList<Integer> idArray = MediaStoreLoader.getIdArray(getActivity());
        videoAdapter = new VideoAdapter(context, ViewCase.NORMAL, this, this, mGlideRequestManager);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Home);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(videoAdapter);

        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        homeViewModel.getAllVideo(selectedSort).observe(getActivity(), new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                videoAdapter.setVideos(videos);
            }
        });


        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                EditText videoName = new EditText(context);
//                builder.setView(videoName);
//                builder.setTitle("임시 비디오 추가");
//                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (videoName.getText().toString().trim().length() != 0) {
//                            Video video = new Video();
//                            video.setvName(videoName.getText().toString());
//                            homeViewModel.insertVideo(video);
//                        }
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();


                /** insert only id */
//                ArrayList<Integer> idArray = MediaStoreLoader.getIdArray(getActivity());
//                for (int i = 0; i < idArray.size(); i++) {
//                    Video video = new Video();
//                    int ContentId = idArray.get(i);
//                    video.setContentId(ContentId);
//                    homeViewModel.insertVideo(video);
//                    Log.d(TAG, "insert ======" + ContentId);
//                }

                List<Media> mediaList = MediaStoreLoader.getContent(getActivity());
                for(int i = 0; i < mediaList.size(); i++) {
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
        });


        Button buttonMediaRoom = v.findViewById(R.id.media_room);
        buttonMediaRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> idArray = MediaStoreLoader.getIdArray(getActivity());
                Log.d(TAG, "idArraySize ======" + idArray);
                idArray.clear();
            }
        });

        Button buttonMedia = v.findViewById(R.id.btn_media);
        buttonMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = v.findViewById(R.id.rv_Home);
                DividerItemDecoration dividerItemDecoration =
                        new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                mediaAdapter = new MediaAdapter(context, ViewCase.MEDIA, datas);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(mediaAdapter);
            }
        });

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

    public int sort() {
        final String[] sort = new String[] {"영상 제목순", "추가된순(최근)", "추가된순(오래된)", "북마크된 수"};

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
    public void clickLongItem(View v, int id) {
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
                        //TODO: 데이터베이스 수정코드 -> 미디어스토어 수정코드로 변경할 필요있음
                        break;
                    case (R.id.popup_delete):
                        homeViewModel.deleteVideoWithMark(id);
                        homeViewModel.deleteVideo(id);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
//        Video_BottomSheetDialog video_bottomSheetDialog = new Video_BottomSheetDialog();
//        video_bottomSheetDialog.show(getChildFragmentManager(), "bottomSheetDialog");
//        video_bottomSheetDialog.onClick(v);
    }

    @Override
    public void clickItem(int id) {
        Intent playerIntent = new Intent(getContext(), PlayerActivity.class);
        playerIntent.putExtra("ContentID", id);
        playerIntent.putExtra("Start", 0L);
        getContext().startActivity(playerIntent);
    }

    @Override
    public void clickMark(int id, long start) {}

    @Override
    public void clickLongMark(View v, int id) {}

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {

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
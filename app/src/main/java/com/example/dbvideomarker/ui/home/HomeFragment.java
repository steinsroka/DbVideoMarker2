package com.example.dbvideomarker.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
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
import com.example.dbvideomarker.adapter.viewholder.VideoViewHolderDrag;
import com.example.dbvideomarker.callbacks.Toolbar_ActionMode;
import com.example.dbvideomarker.database.entitiy.Media;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.dialog.BottomSheetDialog;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.util.MediaStoreLoader;
import com.example.dbvideomarker.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemSelectedListener, OnItemClickListener, VideoAdapter.OnStartDragListener {

    private String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private PlayListEditViewModel playListEditViewModel;
    private VideoAdapter videoAdapter;
    private int selectedSort = 0;
    private View v;
    private View normalView;
    private View selectView;
    private View bottomMenu;
    private ImageButton btn_add_playlist, btn_info, btn_delete;
    private ArrayList<Integer> idList;
    private List<Video> videoList;
    private ActionMode mActionMode;
    private static RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        normalView = v.findViewById(R.id.video_normal_wrapper);
        selectView = v.findViewById(R.id.video_select_wrapper);
        bottomMenu = v.findViewById(R.id.home_bottom_menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수
            }

        }

        populateRecyclerView();
        setBottomMenu();
        setVideoNormalView();
        addMediaDataToRoom();

        normalView.setVisibility(View.VISIBLE);
        selectView.setVisibility(View.GONE);
        bottomMenu.setVisibility(View.GONE);

        playListEditViewModel = new ViewModelProvider(this).get(PlayListEditViewModel.class);
        //최초실행 확인 + 최초실행시 Room에 데이터 추가
        SharedPreferences pref = requireActivity().getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if (!first) {
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.apply();
        } else {
            Log.d("Is first Time?", "not first");
        }

        Button buttonSortDialog = v.findViewById(R.id.video_sort);
        buttonSortDialog.setOnClickListener(v -> {
            selectedSort = sort();
        });
        return v;
    }

    private void populateRecyclerView() {
        recyclerView = v.findViewById(R.id.rv_Home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void onClickListener(Video video, View view, int typeClick) {
        int position = recyclerView.getChildAdapterPosition(view);
        switch (typeClick) {
            case 0:
                if (mActionMode != null)
                    onListItemSelect(position);
                break;
            case 1:
                onListItemSelect(position);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setBottomMenu() {
        btn_add_playlist = v.findViewById(R.id.video_bottom_add_playlist);
        btn_info = v.findViewById(R.id.video_bottom_info);
        btn_delete = v.findViewById(R.id.video_bottom_delete);

        btn_add_playlist.setOnClickListener(view -> {
            BottomSheetDialog playerBottomSheetDialog = new BottomSheetDialog();
            Bundle args = new Bundle();
            args.putIntegerArrayList("idList", idList);
            args.putInt("code", 1100);
            playerBottomSheetDialog.setArguments(args);
            playerBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetDialog");
            mActionMode.finish();

//                Intent playlistIntent = new Intent(getContext(), SelectActivity.class);
//                playlistIntent.putExtra("pid", "");
//                playlistIntent.putExtra("VIEW_TYPE", 2003);
//                startActivityForResult(playlistIntent, SELECT_PLAYLIST_REQUEST_CODE);
        });

        btn_add_playlist.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_add_playlist.setImageResource(R.drawable.ic_baseline_playlist_add_red_24);
                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_add_playlist.setImageResource(R.drawable.ic_baseline_playlist_add_24);
                return false;
            }
            return false;
        });


        btn_info.setOnClickListener(view -> {
            Intent infoIntent = new Intent(getContext(), InfoActivity.class);
            infoIntent.putExtra("ContentID", idList.get(0));
            requireContext().startActivity(infoIntent);
            mActionMode.finish();
        });

        btn_delete.setOnClickListener(view -> {
            for (int i = 0; i < idList.size(); i++) {
                deleteVideo(idList.get(i));
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                setVideoNormalView();
            }
        });

        btn_delete.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_delete.setImageResource(R.drawable.ic_baseline_delete_outline_red_24);
                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_delete.setImageResource(R.drawable.ic_baseline_delete_outline_24);
                return false;
            }
            return false;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            int SELECT_PLAYLIST_REQUEST_CODE = 1003;
            if (requestCode == SELECT_PLAYLIST_REQUEST_CODE) {
                assert data != null;
                ArrayList<Integer> selectedPidList = data.getIntegerArrayListExtra("pidlist");
                assert selectedPidList != null;
                for (int i = 0; i < selectedPidList.size(); i++) {
                    for (int j = 0; j < idList.size(); j++) {
                        PlRel plRel = new PlRel();
                        plRel.setPid(selectedPidList.get(i));
                        plRel.setVid(idList.get(j));
                        playListEditViewModel.insertPlRelation(plRel);
                    }
                }
            }
        }
        setVideoNormalView();
        Toast.makeText(getActivity(), "재생목록에 추가됨", Toast.LENGTH_SHORT).show();
    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    public void addMediaDataToRoom() {
        List<Media> mediaList = MediaStoreLoader.getContent(requireActivity());
        for (int i = 0; i < mediaList.size(); i++) {
            Media media = mediaList.get(i);
            Video video = new Video();
            video.setContentId(media.getResId());
            video.setVdur(media.getDur());
            video.setVname(media.getName());
            video.setVpath(media.getPath());
            video.setVadded(media.getAdded());
            video.setVrecent(System.currentTimeMillis());
            homeViewModel.insertVideo(video);
        }
    }

    public void setVideoNormalView() {

        selectView.setVisibility(View.GONE);
        bottomMenu.setVisibility(View.GONE);
        normalView.setVisibility(View.VISIBLE);

        videoAdapter = new VideoAdapter(getActivity(), ViewCase.NORMAL, this, this, this);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(videoAdapter);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getAllVideo(selectedSort).observe(requireActivity(), videos -> videoAdapter.setVideos(videos));
    }

    public void setVideoSelectView() {

        normalView.setVisibility(View.GONE);
        selectView.setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.VISIBLE);

        VideoAdapter adapter = new VideoAdapter(getActivity(), ViewCase.SELECT, this, this, this);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Home_select);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllVideo(selectedSort).observe(this, adapter::setVideos);
    }

    public void deleteVideo(int id) {
        MediaStoreLoader loader = new MediaStoreLoader();
        loader.deleteFile(requireActivity(), id);
        homeViewModel.deleteVideoWithMark(id);
        homeViewModel.deleteVideo(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NonConstantResourceId")
    //@RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.select:
                setVideoSelectView();
                mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode(getActivity(), videoAdapter, null, videoList, null, true));
                break;
            case R.id.setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_search:
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.menu_scan:
                addMediaDataToRoom();
                break;
        }
        return true;
    }

    public int sort() {
        final String[] sort = new String[]{"영상 제목순", "추가된순(최근)", "추가된순(오래된)", "북마크된 수"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("정렬 순서")
                .setSingleChoiceItems(sort, selectedSort, (dialog1, which) -> selectedSort = which)
                .setPositiveButton("확인", (dialog12, which) -> homeViewModel.getAllVideo(selectedSort).observe(requireActivity(), videos -> videoAdapter.setVideos(videos)));
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return selectedSort;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void clickLongItem(View v, int id, String path) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_video, menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popup_info:
                    Intent infoIntent = new Intent(getContext(), InfoActivity.class);
                    infoIntent.putExtra("ContentID", id);
                    requireContext().startActivity(infoIntent);
                    break;
                case R.id.popup_edit:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    EditText afterName = new EditText(getActivity());
                    builder.setView(afterName);
                    builder.setTitle("동영상 제목 수정");
                    builder.setPositiveButton("확인", (dialogInterface, i) -> {
                        if (afterName.getText().toString().trim().length() != 0) {
                            Video video = new Video();
                            video.setVname(afterName.getText().toString());

//                            String newPath = video.getVpath().substring(video.getVpath().lastIndexOf("/")+1);
//                            newPath = newPath +


                            MediaStoreLoader loader = new MediaStoreLoader();
                            loader.updateFile(requireActivity(), id, afterName.getText().toString());
                            homeViewModel.updateVideo(id, afterName.getText().toString());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                case (R.id.popup_delete):
                    AlertDialog.Builder deletebuilder = new AlertDialog.Builder(getActivity());
                    deletebuilder.setMessage("동영상을 삭제하시겠습니까?");
                    deletebuilder.setPositiveButton("확인", (dialogInterface, i) -> {
                        deleteVideo(id);
                    });
                    deletebuilder.setNegativeButton("취소", (dialogInterface, i) -> {
                    });
                    AlertDialog deletedialog = deletebuilder.create();
                    deletedialog.show();

                    break;
            }
            return false;
        });
        popupMenu.show();
    }

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
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        idList = new ArrayList<>();

        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            idList.add(sparseBooleanArray.keyAt(i));
            Log.d("text", "idList 길이 : " + sparseBooleanArray);
            if (idList.size() > 1) {
                btn_info.setImageResource(R.drawable.ic_baseline_info_red_24);
                btn_info.setClickable(false);
            } else {
                btn_info.setImageResource(R.drawable.ic_baseline_info_24);
                btn_info.setClickable(true);
            }
        }
    }

    @Override
    public void onStartDrag(VideoViewHolderDrag mHolder) {
    }

    private void onListItemSelect(int position) {
        videoAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = videoAdapter.getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode(getActivity(), videoAdapter, null, videoList, null, true));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(videoAdapter
                    .getSelectedCount()) + " selected");
    }

    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        setVideoNormalView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mActionMode != null && !isVisibleToUser) {
            mActionMode.finish();
        }
    }
}
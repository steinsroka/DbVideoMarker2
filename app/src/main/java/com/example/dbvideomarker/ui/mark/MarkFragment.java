package com.example.dbvideomarker.ui.mark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.SearchActivity;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class MarkFragment extends Fragment implements OnMarkClickListener, OnItemSelectedListener {

    private MarkViewModel markViewModel;
    private RequestManager mGlideRequestManager;
    public int selectedSort;
    private MarkAdapter markAdapter;

    private View v;
    private View normalMarkView;
    private View selectMarkView;
    private View bottomMarkMenu;

    private ImageButton btn_add_playlist_mark, btn_delete_mark;

    private ArrayList<Integer> idList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mark, container, false);

        mGlideRequestManager = Glide.with(getActivity());

        normalMarkView = v.findViewById(R.id.mark_normal_wrapper);
        selectMarkView = v.findViewById(R.id.mark_select_wrapper);
        bottomMarkMenu = v.findViewById(R.id.mark_bottom_menu);

        setMarkNormalView();
        setBottomMarkMenu();

        return v;
    }

    public void setMarkNormalView() {

        normalMarkView.setVisibility(View.VISIBLE);
        selectMarkView.setVisibility(View.GONE);
        bottomMarkMenu.setVisibility(View.GONE);

        markAdapter = new MarkAdapter(getActivity(), ViewCase.NORMAL, this, this, mGlideRequestManager);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Mark);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(markAdapter);
        markViewModel = new ViewModelProvider(getActivity()).get(MarkViewModel.class);
        markViewModel.getAllMark().observe(getActivity(), new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                markAdapter.setMarks(marks);
            }
        });

        Button buttonSortDialog = v.findViewById(R.id.mark_sort);
        buttonSortDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort();
            }
        });
    }

    public void setMarkSelectView() {

        normalMarkView.setVisibility(View.GONE);
        selectMarkView.setVisibility(View.VISIBLE);
        bottomMarkMenu.setVisibility(View.VISIBLE);

        MarkAdapter markAdapter = new MarkAdapter(getActivity(), ViewCase.SELECT, this, this, mGlideRequestManager);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Mark_select);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(markAdapter);
        markViewModel = new ViewModelProvider(getActivity()).get(MarkViewModel.class);
        markViewModel.getAllMark().observe(getActivity(), new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                markAdapter.setMarks(marks);
            }
        });
    }

    public void setBottomMarkMenu() {
        btn_add_playlist_mark = v.findViewById(R.id.mark_bottom_add_playlist);
        btn_delete_mark = v.findViewById(R.id.mark_bottom_delete);

        btn_add_playlist_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //재생목록 추가
            }
        });

        btn_delete_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<idList.size(); i++) {
                    markViewModel.deleteMark(idList.get(i));
                    Toast.makeText(getActivity(), idList.get(i) + "Deleted", Toast.LENGTH_SHORT).show();
                    setMarkNormalView();
                }
            }
        });
    }

    public int sort() {
        final String[] sort = new String[] {"북마크 제목순", "추가된순(최근)", "추가된순(오래된)"};

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
                        markViewModel.getAllMark(selectedSort).observe(getActivity(), new Observer<List<Mark>>() {
                            @Override
                            public void onChanged(List<Mark> marks) {
                                markAdapter.setMarks(marks);
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
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        idList = new ArrayList<>();

        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            idList.add(sparseBooleanArray.keyAt(i));
            Log.d("text", "idList 길이 : " + sparseBooleanArray);
        }
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
                setMarkSelectView();
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
    public void clickMark(int id, long start, String path) {
        Intent playerIntent = new Intent(getContext(), PlayerActivity.class);
        playerIntent.putExtra("ContentID", id);
        playerIntent.putExtra("Path", path);
        playerIntent.putExtra("Start", start);
        getContext().startActivity(playerIntent);
    }

    @Override
    public void clickLongMark(View v, int id, String path) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_mark, menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_edit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        EditText et = new EditText(getActivity());
                        builder.setView(et);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (et.getText().toString().trim().length() != 0) {
                                    Mark mark = new Mark();
                                    mark.setmMemo(et.getText().toString());
                                    markViewModel.updateMark(id, et.getText().toString());
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //TODO: update Mark
                        break;
                    case(R.id.popup_delete):
                        markViewModel.deleteMark(id);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
package com.example.dbvideomarker.ui.mark;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.PlayListEditViewModel;
import com.example.dbvideomarker.activity.SearchActivity;
import com.example.dbvideomarker.activity.SelectActivity;
import com.example.dbvideomarker.activity.setting.SettingActivity;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.callbacks.Toolbar_ActionMode;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.dialog.BottomSheetDialog;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class MarkFragment extends Fragment implements OnMarkClickListener, OnItemSelectedListener {

    private MarkViewModel markViewModel;
    private PlayListEditViewModel playListEditViewModel;
    public int selectedSort;
    private int SELECT_PLAYLIST_REQUEST_CODE = 1003;
    private MarkAdapter markAdapter;
    private ActionMode mActionMode;
    private List<Mark> markList;
    private static RecyclerView recyclerView;
    private View v;
    private View normalMarkView;
    private View selectMarkView;
    private View bottomMarkMenu;

    private ImageButton btn_add_playlist_mark, btn_delete_mark;

    private ArrayList<Integer> idList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mark, container, false);

        normalMarkView = v.findViewById(R.id.mark_normal_wrapper);
        selectMarkView = v.findViewById(R.id.mark_select_wrapper);
        bottomMarkMenu = v.findViewById(R.id.mark_bottom_menu);

        setMarkNormalView();
        setBottomMarkMenu();
        populateRecyclerView();
        playListEditViewModel = new ViewModelProvider(this).get(PlayListEditViewModel.class);

        return v;
    }

    private void populateRecyclerView() {
        recyclerView = v.findViewById(R.id.rv_Mark_select);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void onListItemSelect(int position) {
        markAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = markAdapter.getSelectedCount() > 0;//Check if any items are already selected or not

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode(getActivity(), null, markAdapter, null, markList, true));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(markAdapter
                    .getSelectedCount()) + " selected");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PLAYLIST_REQUEST_CODE) {
                assert data != null;
                ArrayList<Integer> selectedPidList = data.getIntegerArrayListExtra("pidlist");
                assert selectedPidList != null;
                for (int i = 0; i < selectedPidList.size(); i++) {
                    for (int j = 0; j < idList.size(); j++) {
                        PlRel plRel = new PlRel();
                        plRel.setPid(selectedPidList.get(i));
                        plRel.setMid(idList.get(j));
                        playListEditViewModel.insertPlRelation(plRel);
                    }
                }
            }
        }
        setMarkNormalView();
        Toast.makeText(getActivity(), "재생목록에 추가됨", Toast.LENGTH_SHORT).show();
    }

    public void setMarkNormalView() {

        normalMarkView.setVisibility(View.VISIBLE);
        selectMarkView.setVisibility(View.GONE);
        bottomMarkMenu.setVisibility(View.GONE);

        markAdapter = new MarkAdapter(getActivity(), ViewCase.NORMAL, this, this);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Mark);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(markAdapter);
        markViewModel = new ViewModelProvider(requireActivity()).get(MarkViewModel.class);
        markViewModel.getAllMark().observe(requireActivity(), marks -> markAdapter.setMarks(marks));

        Button buttonSortDialog = v.findViewById(R.id.mark_sort);
        buttonSortDialog.setOnClickListener(v -> sort());
    }

    public void setMarkSelectView() {

        normalMarkView.setVisibility(View.GONE);
        selectMarkView.setVisibility(View.VISIBLE);
        bottomMarkMenu.setVisibility(View.VISIBLE);

        MarkAdapter markAdapter = new MarkAdapter(getActivity(), ViewCase.SELECT, this, this);
        RecyclerView recyclerView = v.findViewById(R.id.rv_Mark_select);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(markAdapter);
        markViewModel = new ViewModelProvider(requireActivity()).get(MarkViewModel.class);
        markViewModel.getAllMark().observe(requireActivity(), markAdapter::setMarks);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setBottomMarkMenu() {
        btn_add_playlist_mark = v.findViewById(R.id.mark_bottom_add_playlist);
        btn_delete_mark = v.findViewById(R.id.mark_bottom_delete);

        btn_add_playlist_mark.setOnClickListener(view -> {
            BottomSheetDialog playerBottomSheetDialog = new BottomSheetDialog();
            Bundle args = new Bundle();
            args.putIntegerArrayList("idList", idList);
            args.putInt("code", 1101);
            playerBottomSheetDialog.setArguments(args);
            playerBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetDialog");
        });


        btn_add_playlist_mark.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_add_playlist_mark.setImageResource(R.drawable.ic_baseline_playlist_add_red_24);
                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_add_playlist_mark.setImageResource(R.drawable.ic_baseline_playlist_add_24);
                return false;
            }
            return false;
        });

        btn_delete_mark.setOnClickListener(view -> {
            for (int i = 0; i < idList.size(); i++) {
                markViewModel.deleteMark(idList.get(i));
                Toast.makeText(getActivity(), idList.get(i) + "Deleted", Toast.LENGTH_SHORT).show();
                setMarkNormalView();
            }
        });

        btn_delete_mark.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_delete_mark.setImageResource(R.drawable.ic_baseline_delete_outline_red_24);
                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                btn_delete_mark.setImageResource(R.drawable.ic_baseline_delete_outline_24);
                return false;
            }
            return false;
        });
    }

    public int sort() {
        final String[] sort = new String[]{"북마크 제목순", "추가된순(최근)", "추가된순(오래된)"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("정렬 순서")
                .setSingleChoiceItems(sort, selectedSort, (dialog1, which) -> selectedSort = which)
                .setPositiveButton("확인", (dialog12, which) -> markViewModel.getAllMark(selectedSort).observe(requireActivity(), marks -> markAdapter.setMarks(marks)));
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.select:
                setMarkSelectView();
                mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode(getActivity(), null, markAdapter, null, markList, false));
                break;
            case R.id.setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
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
        requireContext().startActivity(playerIntent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void clickLongMark(View v, int id, String path) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup_mark, menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popup_edit:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    EditText et = new EditText(getActivity());
                    builder.setView(et);
                    builder.setPositiveButton("확인", (dialogInterface, i) -> {
                        if (et.getText().toString().trim().length() != 0) {
                            Mark mark = new Mark();
                            mark.setmMemo(et.getText().toString());
                            markViewModel.updateMark(id, et.getText().toString());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                case (R.id.popup_delete):
                    markViewModel.deleteMark(id);
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onMarkClickListener(Mark mark, View view, int typeClick) {
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

    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        setMarkNormalView();
    }
}
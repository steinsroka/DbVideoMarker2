package com.example.dbvideomarker.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.PlayListEditActivity;
import com.example.dbvideomarker.activity.SearchActivity;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.PlayList;

import java.util.List;

public class DashboardFragment extends Fragment implements OnItemClickListener {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootv = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Context context = rootv.getContext();

        RecyclerView recyclerView = rootv.findViewById(R.id.rv_Mark);
        MarkAdapter adapter = new MarkAdapter(context, this);

        dashboardViewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);

        dashboardViewModel.getAllMark().observe(getActivity(), new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                adapter.setMarks(marks);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        Button buttonSearch = rootv.findViewById(R.id.btn_Search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(intentSearch);
            }
        });

        return rootv;
    }

    @Override
    public void clickLongItem(View v, int id) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
        PopupMenu popupMenu = new PopupMenu(contextThemeWrapper, getView());
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup, menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.popup_edit:
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        EditText et = new EditText(getActivity());
                        builder.setView(et);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (et.getText().toString().trim().length() != 0) {
                                    Mark mark = new Mark();
                                    mark.setmMemo(et.getText().toString());
                                    mark.set
                                    mark.setmid(id);

                                    DashboardViewModel.update(playList);
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();*/
                        //TODO: update구문 특정 column만 변경할 수 있도록 수정필요
                        break;
                    case(R.id.popup_delete):
                        dashboardViewModel.deleteMark(id);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    @Override
    public void clickItem(int id) {
    }
}
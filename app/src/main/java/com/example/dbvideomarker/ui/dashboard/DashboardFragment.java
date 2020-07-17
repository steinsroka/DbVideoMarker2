package com.example.dbvideomarker.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.database.entitiy.Mark;

import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootv = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Context context = rootv.getContext();

        RecyclerView recyclerView = rootv.findViewById(R.id.rv_Mark);
        MarkAdapter adapter = new MarkAdapter(context);

        dashboardViewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);

        dashboardViewModel.getAllMark().observe(getActivity(), new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                adapter.setMarks(marks);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        return rootv;
    }
}
package com.example.dbvideomarker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.ui.home.HomeViewModel;
import com.example.dbvideomarker.ui.mark.MarkViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectMarkActivity extends AppCompatActivity implements OnItemSelectedListener, OnItemClickListener {

    private MarkViewModel markViewModel;
    private Button btnSelection;
    public RequestManager mGlideRequestManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mark);

        mGlideRequestManager = Glide.with(this);


        RecyclerView recyclerView = findViewById(R.id.rv_select_mark);
        MarkAdapter adapter = new MarkAdapter(this, ViewCase.SELECT, this, this, mGlideRequestManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        markViewModel = new ViewModelProvider(this).get(MarkViewModel.class);
        markViewModel.getAllMark().observe(this, new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                adapter.setMarks(marks);
            }
        });
    }

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
        ArrayList<Integer> idList = new ArrayList<>();
        for(int i=0; i<sparseBooleanArray.size(); i++) {
            idList.add(sparseBooleanArray.keyAt(i));
        }

        btnSelection = (Button) findViewById(R.id.btn_add_mark);
        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putIntegerArrayListExtra("midlist", idList);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void clickLongItem(View v, int id) {}

    @Override
    public void clickItem(int vid) {}

    @Override
    public void clickMark(int id, long start) {}
}

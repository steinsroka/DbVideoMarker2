package com.example.dbvideomarker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.PlayListEditAdapter;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.dao.PlayListDao;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlRelVideo;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.ui.notifications.NotificationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlayListEditActivity extends AppCompatActivity implements OnItemClickListener {

    private PlayListEditViewModel playListEditViewModel;
    TextView PlayListName, PlayListId;
    public int SELECT_REQUEST_CODE = 1001;
    private int pid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_playlistedit);

        Intent intent = getIntent();

        pid = intent.getIntExtra("재생목록 번호", -1);
//        int pid = Integer.parseInt(pidToString);
//        String pname = intent.getStringExtra("재생목록 이름");

        TextView playListName = (TextView) findViewById(R.id.playListName);
        TextView playListId = (TextView) findViewById(R.id.playListId);
        TextView playListCount = (TextView) findViewById(R.id.playListCount);

        // Get a new or existing ViewModel from the ViewModelProvider.
        playListEditViewModel = new ViewModelProvider(this).get(PlayListEditViewModel.class);

        playListEditViewModel.getPlayList(pid).observe(this, new Observer<PlayList>() {
            @Override
            public void onChanged(PlayList playList) {
                String pname = playList.getpName();
                playListName.setText(pname);
            }
        });


        playListId.setText(""+pid); //setText 에서 int형 파라미터는 리소스 id 값이지 그냥 int값이 아님. String 형태로 바꿔서 출력해야함

        RecyclerView recyclerView = findViewById(R.id.rv_PlaylistEdit);
        PlayListEditAdapter adapter = new PlayListEditAdapter(this, this, ViewCase.NORMAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);



        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        playListEditViewModel.findVideoInPlayList(pid).observe(this, new Observer<List<PlRelVideo>>() {
            @Override
            public void onChanged(List<PlRelVideo> plRels) {
                //Update the cached copy of the words in the adapter.
                adapter.setPlRels(plRels);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PlayListEditActivity.this, SelectActivity.class);
                intent1.putExtra("추가할 재생목록 번호", pid);
                startActivityForResult(intent1, SELECT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<Integer> selectedVidList = data.getIntegerArrayListExtra("vidlist");

            for(int i=0; i<selectedVidList.size(); i++) {
                PlRel plRel = new PlRel();
                plRel.setPid(pid);
                plRel.setVid(selectedVidList.get(i));
                playListEditViewModel.insertPlRelation(plRel);
            }
        }
    }

    @Override
    public void clickLongItem(View v, int id) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        Menu menu = popupMenu.getMenu();
        inflater.inflate(R.menu.menu_popup, menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case(R.id.popup_delete):
                        playListEditViewModel.deletePlRel(id);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    @Override
    public void clickItem(int pid) {

    }
}

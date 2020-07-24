package com.example.dbvideomarker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.PlayListEditAdapter;
import com.example.dbvideomarker.database.dao.PlayListDao;
import com.example.dbvideomarker.database.entitiy.PlRel;
import com.example.dbvideomarker.database.entitiy.PlayList;
import com.example.dbvideomarker.ui.notifications.NotificationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PlayListEditActivity extends AppCompatActivity {

    private PlayListEditViewModel playListEditViewModel;
    TextView PlayListName, PlayListId;
    public int SELECT_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_playlistedit);

        Intent intent = getIntent();

        int pid = intent.getIntExtra("재생목록 번호", -1);
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
        PlayListEditAdapter adapter = new PlayListEditAdapter(this);



        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        playListEditViewModel.getAllPlRel().observe(this, new Observer<List<PlRel>>() {
            @Override
            public void onChanged(List<PlRel> plRels) {
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
                startActivityForResult(intent1, SELECT_REQUEST_CODE);
            }
        });

        //TODO: intent가 아니라 pid로 값을 찾아오기
        // Repository 는 테이블당 하나 뷰모델은 여러개의 repository 갖고있어도괜찮음
        Button editPlayListName = (Button) findViewById(R.id.btn_EditPlayListName);
        editPlayListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayListEditActivity.this);
                EditText et = new EditText(PlayListEditActivity.this);
                builder.setView(et);
                builder.setTitle("재생목록 제목 수정");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (et.getText().toString().trim().length() != 0) {
                            PlayList playList = new PlayList();
                            playList.setpName(et.getText().toString());
                            playList.setPid(pid);

                            playListEditViewModel.update(playList);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}

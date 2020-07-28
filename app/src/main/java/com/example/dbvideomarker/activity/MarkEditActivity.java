package com.example.dbvideomarker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.repository.MarkRepository;

public class MarkEditActivity extends AppCompatActivity {

    private MarkRepository markRepository = new MarkRepository(getApplication());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markedit);
        Button button = (Button)findViewById(R.id.button);
        TextView textView = (TextView)findViewById(R.id.textView5);
        EditText editText1 = (EditText)findViewById(R.id.edit1);
        EditText editText2 = (EditText)findViewById(R.id.edit2);
        Intent intent = getIntent();

        int vid = intent.getIntExtra("동영상 번호", -1);
        textView.setText(""+vid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().trim().length() != 0
                        && editText2.getText().toString().trim().length() != 0) {
                    String memo = editText1.getText().toString().trim();
                    String start = editText2.getText().toString().trim();
                    Mark mark = new Mark();
                    mark.setmMemo(memo);
                    mark.setmStart(start);
                    mark.setvid(vid);
                    markRepository.insertMark(mark);
                    finish();
                }
            }
        });
    }
}

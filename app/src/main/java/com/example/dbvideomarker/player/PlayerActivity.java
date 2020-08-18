package com.example.dbvideomarker.player;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.repository.PlayListEditRepository;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class PlayerActivity extends AppCompatActivity implements OnItemClickListener {

    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;
    private int widthOfScreen, index;
    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;
    private static final String TAG = PlayerActivity.class.getSimpleName();
    private int id;
    private Uri contentUri;
    private PlayerViewModel playerViewModel;
    private RecyclerView recyclerView;
    private MarkAdapter markAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private Long CURRENT_POSITION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ContentID");
        String id_toString = String.valueOf(id);
        contentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id_toString);

        exoPlayerView = findViewById(R.id.video_view);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        recyclerView = findViewById(R.id.rv_getMark);


        markAdapter = new MarkAdapter(this, this);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(markAdapter);

        playerViewModel.getMarkByVideoId(id).observe(this, new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                markAdapter.setMarks(marks);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {
        if (player == null) {

            /*DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this.getApplicationContext());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DefaultLoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(
                    this.getApplicationContext(),
                    renderersFactory,
                    trackSelector,
                    loadControl);*/

            player = ExoPlayerFactory.newSimpleInstance(this.getApplicationContext());

            //플레이어 연결
            exoPlayerView.setPlayer(player);

            //컨트롤러 없애기
            //exoPlayerView.setUseController(false);

            //사이즈 조절
            //exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM); // or RESIZE_MODE_FILL

            //음량조절
            //player.setVolume(0);

            //프레임 포지션 설정
            //player.seekTo(currentWindow, playbackPosition);

        }

        MediaSource mediaSource = buildMediaSource(contentUri);
        //prepare
        player.prepare(mediaSource, true, false);
        //start,stop
        player.setPlayWhenReady(playWhenReady);
        seekToOnDoubleTap();
    }

//    //현재 동영상의 시간
//    public int getCurrentPosition(){
//        return (int) player.getCurrentPosition();
//    }
//
//    //동영상이 실행되고있는지 확인
//    public boolean isPlaying(){
//        return player.getPlayWhenReady();
//    }
//
//    //동영상 정지
//    public void pause(){
//        player.setPlayWhenReady(false);
//    }
//
//    //동영상 재생
//    public void start(){
//        player.setPlayWhenReady(true);
//    }
//
//    //동영상 시간 설정
//    public void seekTo(int position){
//        player.seekTo(position);
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("DATADATA","onResume");
//        player.setPlayWhenReady(true);
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.i("DATADATA","onPause");
//        player.setPlayWhenReady(false);
//    }
//
//    //동영상 해제
//    public void releasePlayer() {
//        player.release();
//        trackSelector = null;
//    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(this, null);

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {

            return new ProgressiveMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);

        } else {

            return new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent))
                    .createMediaSource(uri);
        }

    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();

            exoPlayerView.setPlayer(null);
            player.release();
            player = null;

        }
    }

    private void getWidthOfScreen() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        widthOfScreen = metrics.widthPixels;
    }

    public void seekToOnDoubleTap() {
        getWidthOfScreen();
        final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

//                        float positionOfDoubleTapX = e.getX();

//                        if (positionOfDoubleTapX < widthOfScreen / 2) {
//                            player.seekTo(player.getCurrentPosition() - 5000);
//                            Log.d("TEST", "onDoubleTap(): widthOfScreen >> " + widthOfScreen +
//                                    " positionOfDoubleTapX >>" + positionOfDoubleTapX);
//                        }
//                        else {
//                        player.seekTo(player.getCurrentPosition() + 5000);
//                        }
                        CURRENT_POSITION = player.getCurrentPosition();
                        Log.d(TAG, "onDoubleTap():  " + player.getCurrentPosition());
                        addMark(CURRENT_POSITION);
                        return true;
                    }
                });
        exoPlayerView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    public void addMark(Long currentPosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText mMemo = new EditText(this);
        builder.setView(mMemo);
        builder.setTitle("북마크 추가");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mMemo.getText().toString().trim().length() != 0) {
                    Mark mark = new Mark();
                    mark.setvid(id);
                    mark.setmMemo(mMemo.getText().toString());
                    mark.setmStart(currentPosition);

                    playerViewModel.insertMark(mark);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();



        //TODO: 여기 값 어떻게 채워넣을지 확인하기, 슬라이드 제스쳐디택터 추가
    }

    @Override
    public void clickLongItem(View v, int id) {
    }

    @Override
    public void clickItem(int id) {
    }
}

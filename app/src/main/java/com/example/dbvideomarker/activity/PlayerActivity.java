package com.example.dbvideomarker.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dbvideomarker.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayerActivity extends AppCompatActivity {

    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;
    private int widthOfScreen, index;
    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;
    private String id;
    private Uri contentUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        exoPlayerView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        id = String.valueOf(intent.getExtras().getInt("ContentID"));
        contentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
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

                        float positionOfDoubleTapX = e.getX();

                        if (positionOfDoubleTapX < widthOfScreen / 2)
                            player.seekTo(player.getCurrentPosition() - 5000);
                        else
                            player.seekTo(player.getCurrentPosition() + 5000);

                        Log.d("TEST", "onDoubleTap(): widthOfScreen >> " + widthOfScreen +
                                " positionOfDoubleTapX >>" + positionOfDoubleTapX);
                        return true;
                    }
                });

        exoPlayerView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    //TODO: 더블탭 해도 하단 메뉴바가 움직임, 시간이동 없음
}

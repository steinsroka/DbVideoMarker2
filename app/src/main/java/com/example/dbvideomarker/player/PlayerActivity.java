package com.example.dbvideomarker.player;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.dialog.Player_BottomSheetDialog;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PlayerActivity extends AppCompatActivity implements OnItemClickListener, OnItemSelectedListener, View.OnClickListener, PlayerController {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    private PlayerView playerView;
    private VideoPlayer player;
    private ImageButton mute, unMute, lock, unLock, nextBtn, preBtn, retry, back, full, unFull, add;
    private ProgressBar progressBar;
    private AudioManager mAudioManager;
    private boolean disableBackPress = false;

    private PlayerViewModel playerViewModel;

    private int id;
    private long start;
    private long CURRENT_POSITION;
    private Uri contentUri;

    private boolean forceLandscape = false;
    private ViewGroup.LayoutParams layoutParams;
    private RequestManager mGlideRequestManager;

    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (player != null)
                                //  player.getPlayer().setPlayWhenReady(true);
                                break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost audio focus, probably "permanently"
                            // Lost audio focus, but will gain it back (shortly), so note whether
                            // playback should resume
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                    }
                }
            };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        Intent intent = getIntent();
        id = intent.getExtras().getInt("ContentID");
        start = intent.getExtras().getLong("Start");

        if(start == -1) {
            startPlayer();
            player.setCurrentPosition(0);
        } else {
            startPlayer();
            player.setCurrentPosition(start);
        }

        initBottomView();
    }

    private void startPlayer() {
        getDataFromIntent();
        setupLayout();
        initSource();
    }

    private void initBottomView() {
        RecyclerView recyclerView = findViewById(R.id.rv_getMark);
        MarkAdapter markAdapter = new MarkAdapter(this, ViewCase.NORMAL, this, this, mGlideRequestManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(markAdapter);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getMarkByVideoId(id).observe(this, new Observer<List<Mark>>() {
            @Override
            public void onChanged(List<Mark> marks) {
                markAdapter.setMarks(marks);
            }
        });

        Button button = findViewById(R.id.bottom_sheet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player_BottomSheetDialog playerBottomSheetDialog = new Player_BottomSheetDialog();
                playerBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetDialog");
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt("ContentID");
        contentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
    }

    private void setupLayout() {
        playerView = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progress_bar);

        mute = findViewById(R.id.btn_mute);
        unMute = findViewById(R.id.btn_unMute);
        lock = findViewById(R.id.btn_lock);
        unLock = findViewById(R.id.btn_unLock);
        nextBtn = findViewById(R.id.btn_next);
        preBtn = findViewById(R.id.btn_prev);
        retry = findViewById(R.id.retry_btn);
        back = findViewById(R.id.btn_back);
        full = findViewById(R.id.btn_full);
        unFull = findViewById(R.id.btn_unFull);
        add = findViewById(R.id.add_mark);

        //optional setting
        playerView.getSubtitleView().setVisibility(View.GONE);

        mute.setOnClickListener(this);
        unMute.setOnClickListener(this);
        lock.setOnClickListener(this);
        unLock.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        preBtn.setOnClickListener(this);
        retry.setOnClickListener(this);
        back.setOnClickListener(this);
        full.setOnClickListener(this);
        unFull.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    private void initSource() {

        if (contentUri == null) {
            Toast.makeText(this, "can not play video", Toast.LENGTH_SHORT).show();
            return;
        }

        player = new VideoPlayer(playerView, getApplicationContext(), contentUri, this);

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        //optional setting
        player.seekToOnDoubleTap();

        playerView.setControllerVisibilityListener(visibility ->
        {
            Log.i(TAG, "onVisibilityChange: " + visibility);
            if (player.isLock())
                playerView.hideController();

            back.setVisibility(visibility == View.VISIBLE && !player.isLock() ? View.VISIBLE : View.GONE);
            add.setVisibility(visibility == View.VISIBLE && !player.isLock() ? View.VISIBLE : View.GONE);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (player != null)
            player.resumePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (player != null)
            player.resumePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null)
            player.releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            mAudioManager = null;
        }
        if (player != null) {
            player.releasePlayer();
            player = null;
        }
        //PlayerApplication.getRefWatcher(this).watch(this);
    }

    @Override
    public void onBackPressed() {
        if (disableBackPress)
            return;
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUi();
    }

    @Override
    public void onClick(View view) {
        int controllerId = view.getId();

        switch (controllerId) {
            case R.id.btn_mute:
                player.setMute(true);
                break;
            case R.id.btn_unMute:
                player.setMute(false);
                break;
            case R.id.btn_lock:
                updateLockMode(true);
                break;
            case R.id.btn_unLock:
                updateLockMode(false);
                break;
            case R.id.exo_rew:
                player.seekToSelectedPosition(0, true);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.retry_btn:
                initSource();
                showProgressBar(true);
                showRetryBtn(false);
                break;
            case R.id.btn_full:
                getFullScreen(playerView, true);
                break;
            case R.id.btn_unFull:
                getFullScreen(playerView,false);
                break;
            case R.id.add_mark:
                player.pausePlayer();
                addMark(player.getCurrentPosition());
            default:
                break;
        }
    }


    public void getFullScreen(PlayerView playerView, boolean forceLandscape) {

        PlayerView playerViewFullscreen = new PlayerView(this);
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        playerViewFullscreen.setLayoutParams(layoutParams);
        playerViewFullscreen.setVisibility(View.GONE);
        playerViewFullscreen.setBackgroundColor(Color.BLACK);

        ((ViewGroup)playerView.getRootView()).addView(playerViewFullscreen, 1);

        if(forceLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //시스템 버전별로 UI를 숨기는 View변수가 다른데 4102는 절대값이라 상관없음
            getWindow().getDecorView().setSystemUiVisibility(4102);
            playerView.setVisibility(View.GONE);
            playerViewFullscreen.setVisibility(View.VISIBLE);
            playerView.switchTargetView(player.getPlayer(), playerView, playerViewFullscreen);
        } else if(!forceLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
            playerView.setVisibility(View.VISIBLE);
            playerViewFullscreen.setVisibility(View.GONE);
            playerView.switchTargetView(player.getPlayer(), playerViewFullscreen, playerView);
        }
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        playerView.getPlayer();
        //TODO:전체화면은 되는데 나갈수가없음
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void setMuteMode(boolean mute) {
        if (player != null && playerView != null) {
            if (mute) {
                this.mute.setVisibility(View.GONE);
                unMute.setVisibility(View.VISIBLE);
            } else {
                unMute.setVisibility(View.GONE);
                this.mute.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void updateLockMode(boolean isLock) {
        if (player == null || playerView == null)
            return;

        player.lockScreen(isLock);

        if (isLock) {
            disableBackPress = true;
            playerView.hideController();
            unLock.setVisibility(View.VISIBLE);
            return;
        }

        disableBackPress = false;
        playerView.showController();
        unLock.setVisibility(View.GONE);

    }


    @Override
    public void showRetryBtn(boolean visible) {
        retry.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    @Override
    public void audioFocus() {
        mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

/*
    private void initializePlayer() {
        if (player == null) {

            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this.getApplicationContext());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DefaultLoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(
                    this.getApplicationContext(),
                    renderersFactory,
                    trackSelector,
                    loadControl);

            player = ExoPlayerFactory.newSimpleInstance(this.getApplicationContext());

            //플레이어 연결
            playerView.setPlayer(player);

            //컨트롤러 없애기
            exoPlayerView.setUseController(false);

            //사이즈 조절
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM); // or RESIZE_MODE_FILL

            //음량조절
            player.setVolume(0);

            //프레임 포지션 설정
            player.seekTo(currentWindow, playbackPosition);

        }

        MediaSource mediaSource = buildMediaSource(contentUri);
        //prepare
        player.prepare(mediaSource, true, false);
        player.seekTo(start);
        //start,stop
        player.setPlayWhenReady(playWhenReady);
        seekToOnDoubleTap();
        seekToOnSwipe();
    }

    //현재 동영상의 시간
    public int getCurrentPosition(){
        return (int) player.getCurrentPosition();
    }

    //동영상이 실행되고있는지 확인
    public boolean isPlaying(){
        return player.getPlayWhenReady();
    }

    //동영상 정지
    public void pause(){
        player.setPlayWhenReady(false);
    }

    //동영상 재생
    public void start(){
        player.setPlayWhenReady(true);
    }

    //동영상 시간 설정
    public void seekTo(int position){
        player.seekTo(position);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("DATADATA","onResume");
        player.setPlayWhenReady(true);
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.i("DATADATA","onPause");
        player.setPlayWhenReady(false);
    }

    //동영상 해제
    public void releasePlayer() {
        player.release();
        trackSelector = null;
    }

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

            playerView.setPlayer(null);
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
//                        player.setPlayWhenReady(false);


                        CURRENT_POSITION = player.getCurrentPosition();
                        Log.d(TAG, "onDoubleTap():  " + player.getCurrentPosition());
                        addMark(CURRENT_POSITION);
                        player.setPlayWhenReady(false);
                        return true;


                    }

//                    @Override
//                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                        //return super.onScroll(e1, e2, distanceX, distanceY);
//                        if (Math.abs(e1.getY() - e2.getY()) > Math.abs(e1.getX() - e2.getX())) {
//                            if(e1.getX() - e2.getX() > 100) {
//                                //Left swipe
//                                player.seekTo(player.getCurrentPosition() - 5000);
//                                Log.d(TAG, "왼쪽으로 스와이프" + e1.getX() + e2.getX());
//                            } else if(e2.getX() - e1.getX() > 100) {
//                                player.seekTo(player.getCurrentPosition() + 5000);
//                                Log.d(TAG, "오른쪽으로 스와이프" + e1.getX() + e2.getX());
//                                //Right swipe
//                            }
//                        }
//                        return true;
//                    }
                });
        playerView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }
//
//    public void seekToOnSwipe() {
//        final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
//                new GestureDetector.SimpleOnGestureListener() {
//
//                });
//    }
*/

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
                    mark.setmAdded(System.currentTimeMillis());

                    playerViewModel.insertMark(mark);
                    player.resumePlayer();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

        //TODO: 여기 값 어떻게 채워넣을지 확인하기, 슬라이드 제스쳐디택터 추가
        //TODO: 북마크 시점 시간형변환


    @Override
    public void clickLongItem(View v, int id) {
    }

    @Override
    public void clickItem(int id) {
    }

    @Override
    public void clickMark(int id, long start) {
        player.setCurrentPosition(start);
    }

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {

    }
}

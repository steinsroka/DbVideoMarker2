package com.example.dbvideomarker.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.dialog.BottomSheetDialog;
import com.example.dbvideomarker.listener.OnItemClickListener;
import com.example.dbvideomarker.listener.OnItemSelectedListener;
import com.example.dbvideomarker.listener.OnMarkClickListener;
import com.example.dbvideomarker.ui.home.HomeViewModel;
import com.example.dbvideomarker.util.MediaStoreLoader;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity implements OnItemClickListener, OnMarkClickListener, OnItemSelectedListener, View.OnClickListener {

    private PlayerViewModel playerViewModel;
    private MediaStoreLoader mediaStoreLoader;

    private int CONTENT_ID;
    private long CONTENT_START;
    private String CONTENT_PATH;

    private boolean isVer = false;

    private final String TAG = "PlayerActivity";
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private ComponentListener componentListener;
    private AudioManager mAudioManager;
    private FrameLayout playerViewLayout;
    private TextView gesture_info;
    private TextView video_name_top;
    private TextView video_name;
    private GestureDetector gestureDetector;

    private ArrayList<Integer> numList = null;
    private Uri uri;

    private Button change_view;
    private ImageButton btn_next, btn_prev, btn_back, btn_full, btn_lock, btn_unlock;
    private int index;

    private float initX, initY, changedX, changedY, diffX, diffY;
    private int widthOfScreen;
    private boolean isLock = false;
    private boolean fullscreen = false;
    private boolean unLockIsGone = false;

    //Volume, Brightness
    private float mVol;
    private float mBrightness;

    //Touch Events
    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_VOL = 1;
    private static final int TOUCH_BRI = 2;
    private static final int TOUCH_SEEK = 3;

    //touch
    private int mTouchAction = TOUCH_NONE;
    private int mSurfaceYDisplayRange = 0;
    private int mSurfaceXDisplayRange = 0;


    //오디오 포커스 요청
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (simpleExoPlayer != null)
                                //  player.getPlayer().setPlayWhenReady(true);
                                break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                            if (simpleExoPlayer != null)
                                simpleExoPlayer.setPlayWhenReady(false);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost audio focus, probably "permanently"
                            // Lost audio focus, but will gain it back (shortly), so note whether
                            // playback should resume
                            if (simpleExoPlayer != null)
                                simpleExoPlayer.setPlayWhenReady(false);
                            break;
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mediaStoreLoader = new MediaStoreLoader();


        FloatingActionButton fab = findViewById(R.id.fab_add_mark);
        fab.setOnClickListener(view -> addMark(simpleExoPlayer.getCurrentPosition()));

        //재생목록에서 리스트 생성 -> 전달(인덱스를 줌)
        int vid = getIntent().getIntExtra("ContentID", -1);
        String vpath = getIntent().getStringExtra("Path");
        long vstart = getIntent().getLongExtra("Start", -0L);
        index = getIntent().getIntExtra("index", -1);
        numList = getIntent().getIntegerArrayListExtra("numList");

        if(numList == null) {
            index = -1;
            CONTENT_ID = vid;
            CONTENT_PATH = vpath;
            CONTENT_START = vstart;
        } else {
            CONTENT_ID = numList.get(index);
            CONTENT_PATH = mediaStoreLoader.getPathById(this, CONTENT_ID);
        }

        if(CONTENT_PATH != null) {
            String videoName = CONTENT_PATH.substring(CONTENT_PATH.lastIndexOf("/") +1);
        }

        //wake lock(화면안꺼짐)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //numList = getIntent().getStringArrayListExtra("ids");
        componentListener = new ComponentListener();
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        video_name = findViewById(R.id.video_name);
        video_name_top = findViewById(R.id.exo_tv_name);
        gesture_info = findViewById(R.id.exo_center_wrapper_data);
        playerViewLayout = findViewById(R.id.playerView_layout);
        playerView = findViewById(R.id.exo_playerView);
        btn_back = findViewById(R.id.exo_back);
        btn_full = findViewById(R.id.btn_full);
        btn_next = findViewById(R.id.btn_next);
        btn_prev = findViewById(R.id.btn_prev);
        btn_lock = findViewById(R.id.btn_lock);
        btn_unlock = findViewById(R.id.btn_unlock);

        btn_back.setOnClickListener(this);
        btn_full.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_lock.setOnClickListener(this);
        btn_unlock.setOnClickListener(this);
        video_name.setText(CONTENT_PATH+"");
    }

    private void initBottomView(int id) {
        RecyclerView recyclerView = findViewById(R.id.rv_getMark);
        MarkAdapter markAdapter = new MarkAdapter(this, ViewCase.NORMAL, this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(markAdapter);

        RecyclerView recyclerViewVer = findViewById(R.id.rv_getMarkVer);
        MarkAdapter markAdapterVer = new MarkAdapter(this, ViewCase.VERTICAL, this, this);
        recyclerViewVer.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewVer.setAdapter(markAdapterVer);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewVer.setVisibility(View.GONE);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getMarkByVideoId(id).observe(this, markAdapter::setMarks);
        playerViewModel.getMarkByVideoId(id).observe(this, markAdapterVer::setMarks);

        Button button = findViewById(R.id.bottom_sheet);
        button.setOnClickListener(view -> {
            BottomSheetDialog playerBottomSheetDialog = new BottomSheetDialog();
            Bundle args = new Bundle();
            args.putInt("vid", id);
            playerBottomSheetDialog.setArguments(args);
            playerBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetDialog");
        });

        Button changeView = findViewById(R.id.change_view);
        changeView.setOnClickListener(view -> {
            if(!isVer) {
                //isVer = false  그리드뷰이면, 버티컬뷰로 바꿈
                isVer = true;
                recyclerView.setVisibility(View.GONE);
                recyclerViewVer.setVisibility(View.VISIBLE);
            } else {
                //isVer = true  리니어뷰이면, 그리드뷰로 바꿈
                isVer = false;
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewVer.setVisibility(View.GONE);
            }

        });

        change_view = findViewById(R.id.change_view);
        change_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(!isVer) {
                    change_view.setBackgroundResource(R.drawable.ic_baseline_view_module_24);
                } else {
                    change_view.setBackgroundResource(R.drawable.ic_baseline_view_list_24);
                }

                return false;
            }
        });
    }



    public void addMark(long position) {
        //Log.d("DOUBLETAP", "DOUBLETAPTIME : " + videoView.getCurrentPosition());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText mMemo = new EditText(this);
        builder.setView(mMemo);
        builder.setTitle(mediaStoreLoader.getReadableDuration(position) + "에 북마크 추가");
        builder.setPositiveButton("확인", (dialogInterface, i) -> {
            if (mMemo.getText().toString().trim().length() != 0) {
                Mark mark = new Mark();
                mark.setvid(CONTENT_ID);
                mark.setmMemo(mMemo.getText().toString());
                mark.setmStart(position);
                mark.setmAdded(System.currentTimeMillis());
                mark.setMpath(CONTENT_PATH);

                playerViewModel.insertMark(mark);
                simpleExoPlayer.setPlayWhenReady(true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializePlayer() {
//        if (numList != null) {
//            initializePlayer(String.valueOf(numList.get(index)));
//        } else {
//            initializePlayer(String.valueOf(CONTENT_ID));
//        }
        initializePlayer(String.valueOf(CONTENT_ID));
        initBottomView(CONTENT_ID);
    }

    private void initializePlayer(String id) {
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();

        uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

        MediaItem item = MediaItem.fromUri(uri);

        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setMediaItem(item, false);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(componentListener);
        simpleExoPlayer.play();
        setDisableBtn(index);
        seekToOnDoubleTap();
        enableGesture();
    }

    private void setNextVideo() {
        if(numList !=null) {
            if (index == 0 || index < numList.size() - 1) {
                index++;
                CONTENT_ID = numList.get(index);
                releasePlayer();
                initializePlayer();
            }
        }
    }

    private void setPrevVideo() {
        if(numList != null) {
            if (index > 0 && index < numList.size()) {
                index--;
                CONTENT_ID = numList.get(index);
                releasePlayer();
                initializePlayer();
            }
        }
    }

    private void setDisableBtn(int index) {
        if (index == -1) {
            //동영상 하나 재생
            btn_prev.setImageResource(R.drawable.ic_baseline_skip_previous_disable_32);
            btn_prev.setEnabled(false);
            btn_next.setImageResource(R.drawable.ic_baseline_skip_next_disable_32);
            btn_next.setEnabled(false);
        }
        if (index != -1) {
            if (index == 0) {
                btn_prev.setImageResource(R.drawable.ic_baseline_skip_previous_disable_32);
                btn_prev.setEnabled(false);
            } else if (index == numList.size() - 1) {
                btn_next.setImageResource(R.drawable.ic_baseline_skip_next_disable_32);
                btn_next.setEnabled(false);
            } else {
                btn_next.setImageResource(R.drawable.ic_baseline_skip_next_32);
                btn_next.setEnabled(true);
                btn_prev.setImageResource(R.drawable.ic_baseline_skip_previous_32);
                btn_prev.setEnabled(true);
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public void seekToOnDoubleTap() {
        gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        //addMark()
                        gesture_info.setText(simpleExoPlayer.getCurrentPosition() + "에 북마크 추가됨");
                        addMark(simpleExoPlayer.getCurrentPosition());
                        simpleExoPlayer.setPlayWhenReady(false);
                        Log.d(TAG, "onDoubleTap(): " + simpleExoPlayer.getCurrentPosition());
                        return true;
                    }
                });
    }


    private void enableGesture() {
        if (isLock) {
            return;
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics screen = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(screen);
        if (mSurfaceYDisplayRange == 0) {
            mSurfaceYDisplayRange = Math.min(screen.widthPixels, screen.heightPixels);
        }
        if (mSurfaceXDisplayRange == 0) {
            mSurfaceXDisplayRange = Math.max(screen.widthPixels, screen.heightPixels);
        }

        if (!isLock) {
            playerView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); //터치 시작시점의 볼륨
                            mBrightness = getWindow().getAttributes().screenBrightness;

                            WindowManager wm = (WindowManager) PlayerActivity.this.getSystemService(Context.WINDOW_SERVICE);
                            DisplayMetrics metrics = new DisplayMetrics();
                            wm.getDefaultDisplay().getMetrics(metrics);
                            widthOfScreen = metrics.widthPixels;

                            initX = motionEvent.getRawX();
                            initY = motionEvent.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            mTouchAction = TOUCH_NONE;
                            changedX = motionEvent.getRawX();
                            changedY = motionEvent.getRawY();

                            diffX = (int) (initX - changedX);
                            diffY = (int) (initY - changedY);
                            if (Math.abs(diffX + diffY) > 100) {
                                //움직인 좌표의 Y값을 X값 으로 나눴을때, 2이상(Y가 많이 움직임) 이면 볼륨, 밝기
                                if (diffX != 0 && diffY != 0) {
                                    if (Math.abs(diffY / diffX) > 2 && mTouchAction != TOUCH_SEEK) {
                                        //볼륨, 밝기 증가+,감소-
                                        //최초 터치 지점이 스크린 크기의 절반 이하 부분에서 발생했을때
                                        if ((int) initX < widthOfScreen / 2) {
                                            mTouchAction = TOUCH_BRI;
                                            Log.d(TAG, "onTouch: " + initX + "<" + widthOfScreen / 2);
                                            doBrightnessTouch(diffY);
                                        }
                                        if ((int) initX > widthOfScreen / 2) {
                                            mTouchAction = TOUCH_VOL;
                                            //볼륨 diffY값 볼륨줄임: 음수, 볼륨높임 양수
                                            //gesture_info.setText("화면 높이: " + heightOfScreen + " 볼륨이" + initY + "에서" + changedY + "까지 변함 | 화면 백분율 기준" + diffY/heightOfScreen*100);
                                            Log.d(TAG, "onTouch: " + initX + ">" + widthOfScreen / 2);
                                            doVolumeTouch(diffY);
                                        }

                                    } else {
                                        //do seek
                                        mTouchAction = TOUCH_SEEK;
                                        doSeekTouch(diffY, diffX, false);
                                    }
                                }
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            if (mTouchAction == TOUCH_SEEK) {
                                //gesture_info.setText("화면 넓이: " + widthOfScreen + "|" + initX + "에서" + changedX + "까지 변함 | 화면 백분율 기준" + diffX/widthOfScreen*100);
                                //앞으로 -, 뒤로 +
                                Log.d(TAG, "onTouch: 앞뒤이동 " + initX + "|" + initY + "시작 /////" + changedX + "|" + changedY + "종료 /////" + "diffY 값 : " + diffX);
                                doSeekTouch(diffY, diffX, true);
                            }
                            if (mTouchAction != TOUCH_NONE) {
                                noGesture();
                            }

                            //diffx값 앞으로: 음수, 뒤로: 양수

                            break;
                        default:
                            break;
                    }
                    gestureDetector.onTouchEvent(motionEvent);
                    return mTouchAction != TOUCH_NONE;

                }

            });
        }
    }


    private void setCenterText(boolean isGesture, int type, int data1) {
        if (!isGesture) {
            gesture_info.setVisibility(View.GONE);
        } else {
            gesture_info.setVisibility(View.VISIBLE);
            switch (type) {
                case 101:
                    //밝기
                    gesture_info.setText("Brightness: " + data1 + "%");
                    break;
                case 102:
                    //볼륨
                    gesture_info.setText("Volume: " + data1);
                    break;
                case 103:
                    //seek
                    String data;
                    if (data1 > 0) {
                        data = "+" + data1;
                    } else {
                        data = "" + data1;
                    }
                    gesture_info.setText(data + "[" + mediaStoreLoader.getReadableDuration(simpleExoPlayer.getCurrentPosition()) + "/" + mediaStoreLoader.getReadableDuration(simpleExoPlayer.getDuration()) + "]");
            }
        }
    }

    public void noGesture() {
        if (gesture_info == null) {
            return;
        }
        gesture_info.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        gesture_info.setVisibility(View.GONE);
    }

    private void doSeekTouch(float deltaY, float deltaX, boolean seek) {
        long time = simpleExoPlayer.getCurrentPosition();
        long duration = simpleExoPlayer.getDuration();

        if (seek) {
            if (Math.abs(diffY / diffX) > 0.5 || Math.abs(deltaX) / mSurfaceXDisplayRange * 100 < 10) {
                Log.d(TAG, "doSeekTouch: Failed! =====================");
                Log.d(TAG, "doSeekTouch: 기울기 value = " + Math.abs(deltaY / deltaX));
                Log.d(TAG, "doSeekTouch: 이동값 백분율 value = " + deltaX / mSurfaceXDisplayRange * 100);
                return;
            }

            int jump = -(Math.round((deltaX / mSurfaceXDisplayRange) * 100000));
            Log.d(TAG, "doSeekTouch: Succeed! =====================");
            Log.d(TAG, "doSeekTouch: 기울기 value = " + Math.abs(deltaY / deltaX));
            Log.d(TAG, "doSeekTouch: 이동값 백분율 value = " + deltaX / mSurfaceXDisplayRange * 100);
            Log.d(TAG, "doSeekTouch: millisecond value = " + jump);

            if ((jump > 0) && ((time + jump) > duration)) {
                //ffwd
                simpleExoPlayer.seekTo(duration);
                setCenterText(true, 103, (int) duration);
            } else if ((jump < 0) && ((time - jump) < 0)) {
                //rewind
                simpleExoPlayer.seekTo(0);
                setCenterText(true, 103, 0);
            } else {
                simpleExoPlayer.seekTo(time + jump);
                setCenterText(true, 103, jump);
            }

            // 진행하는 터치가 아닐때
        } else {
            if (Math.abs(diffY / diffX) > 0.5 || Math.abs(deltaX) / mSurfaceXDisplayRange * 100 < 10) {
                return;
            }

            int jump = -(Math.round((deltaX / mSurfaceXDisplayRange) * 100000));

            if ((jump > 0) && ((time + jump) > duration)) {
                setCenterText(true, 103, (int) duration);
            } else if ((jump < 0) && ((time - jump) < 0)) {
                setCenterText(true, 103, 0);
            } else {
                setCenterText(true, 103, jump);
            }
        }
    }

    private void doBrightnessTouch(float deltaY) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        float maxBrightness = 255.0f;

        float delta = ((deltaY / mSurfaceYDisplayRange) * 255.0f);
        int brightness = (int) (mBrightness + delta);
        if (brightness < 0) {
            params.screenBrightness = 0f;
            setCenterText(true, 101, 0);
        } else if (brightness > maxBrightness) {
            params.screenBrightness = maxBrightness;
            setCenterText(true, 101, 100);
        } else {
            params.screenBrightness = brightness;
            setCenterText(true, 101, brightness * 100 / 255);
        }

    }

    private void doVolumeTouch(float deltaY) {
        int maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        float delta = ((deltaY / mSurfaceYDisplayRange) * maxVol);
        int vol = (int) (mVol + delta);
        if (vol < 0) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
            setCenterText(true, 102, 0);
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.min(vol, maxVol), AudioManager.FLAG_SHOW_UI);
            setCenterText(true, 102, Math.min(vol, maxVol));
        }
    }

    private void updateLockMode(boolean isLock) {
        if (simpleExoPlayer == null || playerView == null)
            return;

        if (isLock) {
            playerView.setUseController(false);
            btn_unlock.setVisibility(View.VISIBLE);

            playerViewLayout.setClickable(true);
            playerViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (unLockIsGone) {
                        btn_unlock.setVisibility(View.VISIBLE);
                        unLockIsGone = false;
                    } else {
                        btn_unlock.setVisibility(View.GONE);
                        unLockIsGone = true;
                    }
                }
            });
            return;
        }

        playerViewLayout.setClickable(false);
        playerView.setUseController(true);
        playerView.showController();
        btn_unlock.setVisibility(View.GONE);

    }

    public void pause() {
        simpleExoPlayer.setPlayWhenReady(false);
    }

    public void resume() {
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void releasePlayer() {
        if (simpleExoPlayer != null) {

            playerView.setPlayer(null);
            simpleExoPlayer.release();
            simpleExoPlayer.removeListener(componentListener);
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        int controllerId = view.getId();

        switch (controllerId) {
            case R.id.btn_next:
                setNextVideo();
                break;
            case R.id.btn_prev:
                setPrevVideo();
                break;
            case R.id.exo_back:
                onBackPressed();
                index = 0;
                break;
            case R.id.btn_full:
                doFullScreen();
                break;
            case R.id.btn_lock:
                updateLockMode(true);
                break;
            case R.id.btn_unlock:
                updateLockMode(false);

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isLock)
            return;
        super.onBackPressed();
    }

    private void doFullScreen() {
        if (fullscreen) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            playerView.setLayoutParams(params);
            fullscreen = false;

        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            fullscreen = true;

        }
    }


    private class ComponentListener implements Player.EventListener {
        @Override
        public void onPlaybackStateChanged(int state) {
            switch (state) {
                case Player.STATE_IDLE:
                    break;
                case Player.STATE_BUFFERING:
                    break;
                case Player.STATE_READY:
                    break;
                case Player.STATE_ENDED:
                    if (index != -1) {
                        setNextVideo();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void clickItem(int id, String path) {
    }

    @Override
    public void clickLongItem(View v, int id, String path) {
    }

    @Override
    public void clickMark(int id, long start, String path) {
        simpleExoPlayer.seekTo(start);
    }

    @Override
    public void clickLongMark(View v, int id, String path) {
    }

    @Override
    public void onItemSelected(View v, SparseBooleanArray sparseBooleanArray) {
    }

}


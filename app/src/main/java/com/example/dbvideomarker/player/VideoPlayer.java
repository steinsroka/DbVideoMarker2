package com.example.dbvideomarker.player;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayer {

    private final String CLASS_NAME = VideoPlayer.class.getName();
    private static final String TAG = "VideoPlayer";
    private SimpleExoPlayer exoPlayer;
    private Context context;
    private PlayerController playerController;

    private PlayerActivity playerActivity;
    private PlayerView playerView;
    private MediaSource mediaSource;
    private DefaultTrackSelector trackSelector;
    private int widthOfScreen, index;
    private ComponentListener componentListener;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private Uri contentUri;
    private boolean isLock = false;


    public VideoPlayer(PlayerView playerView,
                       Context context,
                       Uri contentUri,
                       PlayerController mView) {

        this.playerView = playerView;
        this.context = context;
        this.playerController = mView;
        this.contentUri = contentUri;
        initializePlayer();
    }



    private void initializePlayer() {
        playerView.requestFocus();

        componentListener = new ComponentListener();

//        cacheDataSourceFactory = new CacheDataSourceFactory(context, 100 * 1024 * 1024, 5 * 1024 * 1024)

        trackSelector = new DefaultTrackSelector(context);
//        trackSelector.setParameters(trackSelector
//                .buildUponParameters());

        exoPlayer = new SimpleExoPlayer.Builder(context)
                .setTrackSelector(trackSelector)
                .build();

        playerView.setPlayer(exoPlayer);
        playerView.setKeepScreenOn(true);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(componentListener);
        //build mediaSource depend on video type (Regular, HLS, DASH, etc)

        //mediaSource = buildMediaSource(videoSource.getVideos().get(index), cacheDataSourceFactory);
        mediaSource = buildMediaSource(contentUri);
        exoPlayer.prepare(mediaSource);
        //resume video


        //seekToSelectedPosition(videoSource.getVideos().get(index).getWatchedLength(), false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(context, null);
        return new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(context, userAgent)).createMediaSource(uri);
    }

    /*
    private MediaSource buildMediaSource(Uri contentUri) {
        Uri source = contentUri;
        @C.ContentType int type = Util.inferContentType(source);
        switch (type) {
            case C.TYPE_SS:
                Log.d(TAG, "buildMediaSource() C.TYPE_SS = [" + C.TYPE_SS + "]");
                return new SsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(source);

            case C.TYPE_DASH:
                Log.d(TAG, "buildMediaSource() C.TYPE_DASH = [" + C.TYPE_DASH + "]");
                return new DashMediaSource.Factory(cacheDataSourceFactory).createMediaSource(source);

            case C.TYPE_HLS:
                Log.d(TAG, "buildMediaSource() C.TYPE_HLS = [" + C.TYPE_HLS + "]");
                return new HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(source);

            case C.TYPE_OTHER:
                Log.d(TAG, "buildMediaSource() C.TYPE_OTHER = [" + C.TYPE_OTHER + "]");
                return new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(source);

            default: {
                throw new IllegalStateException("Unsupported type: " + source);
            }
        }
    }
     */

    public void pausePlayer() {
        exoPlayer.setPlayWhenReady(false);
    }

    public void resumePlayer() {
        exoPlayer.setPlayWhenReady(true);
    }

    public void releasePlayer() {
        if (exoPlayer == null)
            return;
        playerView.setPlayer(null);
        exoPlayer.release();
        exoPlayer.removeListener(componentListener);
        exoPlayer = null;
    }

    public void setCurrentPosition(long pos) {
        exoPlayer.seekTo(pos);
    }

    public long getCurrentPosition() {
        long cur_pos = exoPlayer.getCurrentPosition();
        return cur_pos;
    }

    public SimpleExoPlayer getPlayer() {
        return exoPlayer;
    }


    /************************************************************
     mute, unMute
     ***********************************************************/
    public void setMute(boolean mute) {
        float currentVolume = exoPlayer.getVolume();
        if (currentVolume > 0 && mute) {
            exoPlayer.setVolume(0);
            playerController.setMuteMode(true);
        } else if (!mute && currentVolume == 0) {
            exoPlayer.setVolume(1);
            playerController.setMuteMode(false);
        }
    }


    /***********************************************************
     double tap event and seekTo
     ***********************************************************/

    public void seekToSelectedPosition(long millisecond, boolean rewind) {
        if (rewind) {
            exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 15000);
            return;
        }
        exoPlayer.seekTo(millisecond * 1000);
    }

    public void seekToOnDoubleTap() {
        //getWidthOfScreen();
        final GestureDetector gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
/*
                        float positionOfDoubleTapX = e.getX();

                        if (positionOfDoubleTapX < widthOfScreen / 2)
                            exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 5000);
                        else
                            exoPlayer.seekTo(exoPlayer.getCurrentPosition() + 5000);

                        Log.d(TAG, "onDoubleTap(): widthOfScreen >> " + widthOfScreen +
                                " positionOfDoubleTapX >>" + positionOfDoubleTapX);



                        Long cur_pos = exoPlayer.getCurrentPosition();
                        Log.d(TAG, "onDoubleTap():  " + exoPlayer.getCurrentPosition());
                        playerActivity = new PlayerActivity();
                        playerActivity.addMark(cur_pos);
                        resumePlayer();
*/
                        return true;
                    }
                });
        playerView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    /**
    아마 슬라이드할때 쓸듯
     */
    private void getWidthOfScreen() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        widthOfScreen = metrics.widthPixels;
    }





    /***********************************************************
     playerView listener for lock and unlock screen
     ***********************************************************/
    public void lockScreen(boolean isLock) {
        this.isLock = isLock;
    }

    public boolean isLock() {
        return isLock;
    }



    /***********************************************************
     Listeners
     ***********************************************************/
    private class ComponentListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d(TAG, "onPlayerStateChanged: playWhenReady: " + playWhenReady + " playbackState: " + playbackState);
            switch (playbackState) {
                case Player.STATE_IDLE:
                    playerController.showProgressBar(false);
                    playerController.showRetryBtn(true);
                    break;
                case Player.STATE_BUFFERING:
                    playerController.showProgressBar(true);
                    break;
                case Player.STATE_READY:
                    playerController.showProgressBar(false);
                    playerController.audioFocus();
                    break;
                case Player.STATE_ENDED:
                    playerController.showProgressBar(false);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            playerController.showProgressBar(false);
            playerController.showRetryBtn(true);
        }
    }
}

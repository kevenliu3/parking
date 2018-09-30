package com.hibox.carclient;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.hibox.carclient.media.IVideoView;
import com.hibox.carclient.media.MyMediaController;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2017/11/22 09:45
 * desc:    全屏播放视频， 目前作废不需要播放商品视频
 */
public class PlayerActivity extends AppCompatActivity {

    RelativeLayout llParent;
    //    @BindView(R.id.iv_cover)
//    AppCompatImageView ivCover;
    private IVideoView video;
    private int lastPosition = 0;
    private MyMediaController controller;

    private static final String TAG = "PlayerActivity";

    //    private OrientationEventListener mOrientationListener;
    private boolean isLandScape;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        llParent = findViewById(R.id.ll_parent);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }


    public void initView() {

        VideoView mVideoView = findViewById(R.id.video_view);


//        Picasso.with(this)
//                .load(getIntent().getStringExtra("coverUrl"))
//                .into(ivCover);

        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra("url"))) {
            ToastUtil.show(this, "无法播放此视频");
            return;
        }

        String url = getIntent().getStringExtra("url");
        mVideoView.setVideoURI(Uri.parse(Uri.decode(url)));
        mVideoView.start();
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//        video = new IVideoView(this);
//        video.setVideoUrl(url);
//        controller = new MyMediaController(this);
//        controller.setListener(new MyMediaController.OnVideoListener() {
//            @Override
//            public void onPause(boolean pause) {
//
//            }
//
//            @Override
//            public void onBack() {
//                finish();
//            }
//
//            @Override
//            public void onFullScreen(boolean full) {
//                if (full) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                } else {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                }
//
//            }
//        });
////        controller.attach(video);
////        video.setMediaController(controller);
//        video.setBackgroundColor(getResources().getColor(R.color.black));
//        video.setLoop(false);
//        video.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        llParent.addView(video);
//        video.start();
//        video.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
////                finish();
//            }
//        });
//        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
//
//            @Override
//            public void onOrientationChanged(int orientation) {
//                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
//                    return;  //手机平放时，检测不到有效的角度
//                }
//                //只检测是否有四个角度的改变
//                if (orientation > 350 || orientation < 10) { //0度
//                    orientation = 0;
//                } else if (orientation > 80 && orientation < 100) { //90度
//                    orientation = 90;
//                } else if (orientation > 170 && orientation < 190) { //180度
//                    orientation = 180;
//                } else if (orientation > 260 && orientation < 280) { //270度
//                    orientation = 270;
//                } else {
//                    return;
//                }
//                if (orientation == 90) {
//                    if (isLandScape) {
//                        return;
//                    }
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    isLandScape = true;
//                } else if (orientation == 270) {
//                    if (isLandScape) {
//                        return;
//                    }
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    isLandScape = true;
//                } else {
//                    if (isLandScape) {
//                        isLandScape = false;
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    }
//                }
//            }
//        };
//
//        if (mOrientationListener.canDetectOrientation()) {
//            Log.e(TAG, "Can detect orientation");
//            mOrientationListener.enable();
//        } else {
//            Log.e(TAG, "Cannot detect orientation");
//            mOrientationListener.disable();
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (video != null) {
            video.seekTo(lastPosition);
            video.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video != null && video.isPlaying()) {
            lastPosition = video.getCurrentPosition();
            video.pause();
        }

    }


    @Override
    public void onBackPressed() {
        if (controller.isFull()) {
            controller.switchFull(false);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        if (video != null && video.isDrawingCacheEnabled()) {
            video.destroyDrawingCache();
        }
        if (controller != null) {
            controller.disAttach(video);
        }
        IjkMediaPlayer.native_profileEnd();
//        mOrientationListener.disable();
        super.onDestroy();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (controller != null) {
            controller.switchFull(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
        }
    }

}

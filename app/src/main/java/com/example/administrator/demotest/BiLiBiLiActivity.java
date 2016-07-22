package com.example.administrator.demotest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.administrator.demotest.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class BiLiBiLiActivity extends AppCompatActivity
        implements View.OnClickListener {
    IjkVideoView mIjkVideoView;

    private boolean mBackPressed;
    private Button start, stop, pre;
    TextView tv_title;
    LinearLayout linaer1;
    Button start2;
    SeekBar skbProgress;
    boolean isperson = false;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bi_li_bi_li);

        mIjkVideoView = (IjkVideoView) findViewById(R.id.video_view);
        init();
    }


    private void init() {
        //全屏播放
        mIjkVideoView.toggleAspectRatio();

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        pre = (Button) findViewById(R.id.pre);
        start2 = (Button) findViewById(R.id.start2);
        linaer1 = (LinearLayout) findViewById(R.id.linaer1);

        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        tv_title = (TextView) findViewById(R.id.tv_title);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pre.setOnClickListener(this);

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mIjkVideoView.setVideoPath("http://media.assets.bdqn.cn/test/1102/test/segments.m3u8");
        mIjkVideoView.start();





        //获取当前的缓冲量
        new Thread() {
            @Override public void run() {

                while (true) {

                    int duration = mIjkVideoView.getDuration();
                    int bufferPercentage = mIjkVideoView.getBufferPercentage();
                    int currentPosition = mIjkVideoView.getCurrentPosition();
                    int   secondaryProgress = (int) (skbProgress.getMax() * bufferPercentage / 100);;


                    skbProgress.setMax(duration);

                    skbProgress.setProgress(currentPosition);

                    skbProgress.setSecondaryProgress(secondaryProgress);
/*
                    Log.e("duration", duration + "");
                    Log.e("currentPosition", currentPosition + "");
                    Log.e("bufferPercentage", bufferPercentage + "");
                    Log.e("secondaryProgress", secondaryProgress + "");*/
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override public void onBackPressed() {
        mBackPressed = true;

        super.onBackPressed();
    }


    @Override protected void onStop() {
        super.onStop();

        if (mBackPressed || !mIjkVideoView.isBackgroundPlayEnabled()) {
            mIjkVideoView.stopPlayback();
            mIjkVideoView.release(true);
            mIjkVideoView.stopBackgroundPlay();
        }
        else {
            mIjkVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                tv_title.setText("正在播放加密视频");

                mIjkVideoView.stopPlayback();
                mIjkVideoView.release(true);
                mIjkVideoView.stopBackgroundPlay();

                mIjkVideoView.setVideoURI(Uri.parse(
                        "https://media.assets.bdqn.cn/test/1102/hybrid/segments.m3u8"));
                mIjkVideoView.start();

                break;
            case R.id.start2://播放未加密地址
                tv_title.setText("正在播放未加密视频");

                mIjkVideoView.stopPlayback();
                mIjkVideoView.release(true);
                mIjkVideoView.stopBackgroundPlay();

                mIjkVideoView.setVideoURI(Uri.parse("http://media.assets.bdqn" +
                        ".cn/test/1102/unencrypted/segments.m3u8"));
                mIjkVideoView.start();
                break;
            case R.id.pre:  //暂定或者播放
                if (mIjkVideoView.isPlaying()) {
                    mIjkVideoView.pause();
                }
                else {
                    mIjkVideoView.start();
                }
                break;
            case R.id.stop:  //重播
                mIjkVideoView.togglePlayer();

                break;

            //点击全屏
            case R.id.surface1:
                if (linaer1.isShown()) {
                    linaer1.setVisibility(View.GONE);
                }
                else {
                    linaer1.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }


    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }


        @Override public void onStopTrackingTouch(SeekBar seekBar) {
            int currentPlay = seekBar.getProgress();
            mIjkVideoView.seekTo(currentPlay);
            isperson = false;
        }


        @Override public void onStartTrackingTouch(SeekBar arg0) {
            isperson = true;
        }
    }
}

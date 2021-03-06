package com.example.administrator.demotest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.administrator.demotest.constant.Directory;
import com.example.administrator.demotest.nohttp.UAsetHttpServer;
import com.github.lazylibrary.util.FileUtils;
import com.github.lazylibrary.util.ZipUtil;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 实现http
 */
public class MainHostHttpActivity extends AppCompatActivity
        implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener {
    public String DOWNLOAD_URI="http://media.assets.bdqn" +
            ".cn/test/1102/test1/test1.tar.gz";
    String path = Directory.DOWN_LODE_APF_PATH.toString();

    private SurfaceView surface1;
    private Button start, stop, pre;
    private MediaPlayer mediaPlayer1;
    TextView tv_title;
    private int postion = 0;
    LinearLayout linaer1;
    Button start2;
    SeekBar skbProgress;
    boolean isperson = false;
    UAsetHttpServer myHttp;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surface1 = (SurfaceView) findViewById(R.id.surface1);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        pre = (Button) findViewById(R.id.pre);
        start2 = (Button) findViewById(R.id.start2);
        linaer1 = (LinearLayout) findViewById(R.id.linaer1);

        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        tv_title = (TextView) findViewById(R.id.tv_title);
        mediaPlayer1 = new MediaPlayer();
        //设置播放时打开屏幕
        surface1.getHolder().setKeepScreenOn(true);
        surface1.getHolder().addCallback(new SurfaceViewLis());
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pre.setOnClickListener(this);
        start2.setOnClickListener(this);
        surface1.setOnClickListener(this);
        tv_title.setText("正在播放缓存的加密视频");

        mediaPlayer1.setOnBufferingUpdateListener(this);

        openSersice();
        downLoade();
    }


    /**
     * 下载需要缓存的视频文件压缩包
     */
    private void downLoade() {
        start.setEnabled(false);
        new Thread() {
            @Override public void run() {
                super.run();
                copayAssetsToSdCard();
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        start.setEnabled(true);
                    }
                });
            }
        }.start();

       /* HttpUtils mhttpUtils = new HttpUtils();
        boolean fileExist = FileUtils.isFileExist(path + "a.zip");
        if (fileExist){
            Toast.makeText(MainHostHttpActivity.this,"视频已经缓存",Toast
                    .LENGTH_SHORT).show();
            return;
        }

        mhttpUtils.download(DOWNLOAD_URI, path+"a.tar.gz", true, false, new
                RequestCallBack<File>() {
            @Override
            public void onStart() {
                Toast.makeText(MainHostHttpActivity.this,"开始下载视频文件",Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                Log.e("已经下载",current + "/" + total);
            }
            @Override public void onSuccess(ResponseInfo<File> responseInfo) {
                try {
                    GZipUtils.decompress(new File(path+"a.tar.gz"));
                } catch (Exception e) {
                    Log.e("GZipUtils",e.getMessage());

                }
                Toast.makeText(MainHostHttpActivity.this,"视频下载成功,开始解压。",Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(HttpException e, String s) {
                Toast.makeText(MainHostHttpActivity.this,s,Toast
                        .LENGTH_SHORT).show();
                Log.e("onFailure",s);
            }
        });*/
    }


    /**
     * 拷贝并且解压
     */
    private void copayAssetsToSdCard() {
        boolean fileExist = FileUtils.isFileExist(path+"a.zip");
        if(fileExist!=true){
            FileUtils.makeDirs(path);
            Log.e("拷贝","目录不存在创建完毕");
            boolean b = FileUtils.copyAssetToSDCard(getAssets(), "a.zip", path + "a.zip");
            if (b) {
                Log.e("拷贝", "拷贝压缩文件到sd卡成功！");
                ZipUtil.upZipFile(new File(path + "a.zip"), path);
                Log.e("拷贝", "解压完毕");
            }
        }
    }


    /**
     * 开启本地服务
     */
    private void openSersice() {
        myHttp = new UAsetHttpServer(MainHostHttpActivity.this);
        try {
            myHttp.start();
        } catch (IOException e) {
            Log.e("http", "http服务运行错误" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                try {
                    tv_title.setText("正在播放缓存的加密视频");

                    play();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.start2://播放未加密地址
                tv_title.setText("正在播放未加密视频");
                try {
                    mediaPlayer1.reset();
                    mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer1.setDataSource(
                            "http://media.assets.bdqn.cn/test/1102/unencrypted/segments.m3u8");
                    // 把视频输出到SurfaceView上
                    mediaPlayer1.setDisplay(surface1.getHolder());
                    mediaPlayer1.prepare();
                    mediaPlayer1.start();

                    initDation();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.pre:
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.pause();
                }
                else {
                    mediaPlayer1.start();
                }
                break;
            case R.id.stop:  //重播
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.seekTo(0);
                    mediaPlayer1.start();
                    initDation();
                }

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


    public void play() throws IllegalArgumentException, SecurityException,
                              IllegalStateException, IOException {
        mediaPlayer1.reset();
        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);

//        mediaPlayer1.setDataSource("http://localhost:4477");

        //mediaPlayer1.setDataSource("http://test.bdqn:4477/test/1102/test/segments.key");

        mediaPlayer1.setDataSource("http://127.0.0.1:4477/test/1102/test/segments.m3u8");

        // 把视频输出到SurfaceView上
        mediaPlayer1.setDisplay(surface1.getHolder());
        mediaPlayer1.prepare();
        mediaPlayer1.start();

        initDation();
    }


    private void initDation() {
        int dur = mediaPlayer1.getDuration();
        skbProgress.setMax(dur);
        //更新进度
        new Timer().schedule(new TimerTask() {

            @Override public void run() {
                if (isperson == true) return;
                skbProgress.setProgress(mediaPlayer1.getCurrentPosition());
            }
        }, 0, 10);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int bufferingProgress) {
        Log.e("onbuffer", "onBuffer: " + bufferingProgress);
        int secondaryProgress = (int) (
                skbProgress.getMax() * bufferingProgress / 100);
        skbProgress.setSecondaryProgress(secondaryProgress);
    }


    private class SurfaceViewLis implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }


        @Override public void surfaceCreated(SurfaceHolder holder) {
            if (postion == 0) {
                try {
                    //play();
                    //mediaPlayer1.seekTo(postion);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        @Override public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }


        @Override public void onStopTrackingTouch(SeekBar seekBar) {
            int currentPlay = seekBar.getProgress();
            mediaPlayer1.seekTo(currentPlay);
            isperson = false;
        }


        @Override public void onStartTrackingTouch(SeekBar arg0) {
            isperson = true;
        }
    }


    @Override protected void onPause() {
        if (mediaPlayer1.isPlaying()) {
            // 保存当前播放的位置
            postion = mediaPlayer1.getCurrentPosition();
            mediaPlayer1.stop();
        }
        super.onPause();
    }


    @Override protected void onDestroy() {
        if (mediaPlayer1.isPlaying()) {
            mediaPlayer1.stop();
            mediaPlayer1.release();
        }
        myHttp.stop();
        Log.e("http", "http服务停止");
        super.onDestroy();
    }
}

package com.example.administrator.demotest;

import android.content.Intent;
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
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 实现https
 */
public class MainHostHttpsActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener {
    private SurfaceView surface1;
    private Button start, stop, pre;
    private MediaPlayer mediaPlayer1;
    TextView tv_title;
    private int postion = 0;
    LinearLayout linaer1;
    Button start2;
    SeekBar skbProgress;
    boolean isperson = false;
    Intent mIntent;

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
        tv_title.setText("正在播放加密视频");

        mediaPlayer1.setOnBufferingUpdateListener(this);

        try {
            openSersice();
        } catch (IOException e) {
            Log.e("IOException", "Couldn't start server:\n" + e.getMessage());
        }
    }

    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier(){

                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost")) {
                            return true;
                        }
                        return false;
                    }
                });
    }
    /**
     * 开启本地服务
     */
    private void openSersice() throws IOException {

      /*  try {
            AssetManager am = getAssets();
            //InputStream ins1 = am.open("server.cer");
            InputStream ins2 = am.open("android.kbs");
            MyHttpd myHttpd = new MyHttpd();

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(ins2, null);

            //读取证书,注意这里的密码必须设置
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "android".toCharArray());

            myHttpd.makeSecure(NanoHTTPD.makeSSLSocketFactory(keyStore, keyManagerFactory), null);
            myHttpd.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);


        } catch (IOException e) {
            Log.e("IOException", "Couldn't start server:\n" + e.getMessage());
        } catch (NumberFormatException e) {
            Log.e("NumberFormatException", e.getMessage());
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            Log.e("HTTPSException", "HTTPS certificate error:\n " + e.getMessage());
        } catch (UnrecoverableKeyException e) {
            Log.e("UnrecoverableKeyException", "UnrecoverableKeyException" + e.getMessage());
        }   catch (CertificateException e) {
            e.printStackTrace();
        }*/
    }




    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                try {
                    tv_title.setText("正在播放加密视频");

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


       mediaPlayer1.setDataSource("https://localhost:4477");

       //mediaPlayer1.setDataSource("http://test.bdqn:4477/test/1102/test/segments.key");

        //mediaPlayer1.setDataSource("http://media.assets.bdqn.cn/test/1102/test/segments.m3u8");

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
        if (mediaPlayer1.isPlaying()) mediaPlayer1.stop();
        mediaPlayer1.release();
        super.onDestroy();
    }


}

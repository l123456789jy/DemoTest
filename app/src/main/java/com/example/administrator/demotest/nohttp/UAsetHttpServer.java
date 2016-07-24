package com.example.administrator.demotest.nohttp;

import android.util.Log;

import com.example.administrator.demotest.MainHostHttpActivity;
import com.example.administrator.demotest.constant.Directory;

import java.io.FileInputStream;

import fi.iki.elonen.NanoHTTPD;

/**
 * 作者：Administrator on 2016/7/21 17:58
 * 邮箱：906514731@qq.com
 */
public class UAsetHttpServer extends NanoHTTPD {
    MainHostHttpActivity mainHostHttpActivity;
    public UAsetHttpServer(MainHostHttpActivity mainHostHttpActivity)  {
        super(4477);
        this.mainHostHttpActivity=mainHostHttpActivity;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String path = Directory.DOWN_LODE_APF_PATH.toString();
        String uri = session.getUri();
        Log.e("访问的地址",uri);

        try {
            //代表是访问加密的key,把视频加密的key反给播放器
            if (uri.endsWith("segments.key")){
                FileInputStream fis = new FileInputStream("/storage/emulated/0/kgc/apk/segments.key");
                return new NanoHTTPD.Response(Response.Status.OK, "video/mp4", fis);
            }else {
                //然后在反播放的视频，每当播放完一个片段播放器就会请求拿去下一个视频片段
                FileInputStream fis = new FileInputStream("/storage/emulated/0/kgc/apk/segments1.ts");
                return new NanoHTTPD.Response(Response.Status.OK, "video/mp4", fis);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
            return new Response("Error");
        }
    }
}
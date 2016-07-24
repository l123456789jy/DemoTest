package com.example.administrator.demotest.nohttp;

import android.util.Log;

import com.example.administrator.demotest.MainHostHttpActivity;
import com.example.administrator.demotest.constant.Directory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        Log.e("视频路径",path);
        Log.e("http",session.getUri());
        try {
            FileInputStream fis = new FileInputStream("/storage/emulated/0/kgc/apk/segments0.ts");
            return new NanoHTTPD.Response(Response.Status.OK, "video/mp4", fis);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
            return new Response("Error");
        }
    }
}
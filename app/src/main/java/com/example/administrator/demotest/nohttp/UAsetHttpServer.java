package com.example.administrator.demotest.nohttp;

import android.util.Log;
import com.example.administrator.demotest.MainHostHttpActivity;
import com.example.administrator.demotest.constant.Directory;
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


       /* if(session.getUri().equals("/")){
            String ua = session.getHeaders().get("user-agent");
            try{

                FileOutputStream fs = new FileOutputStream(path);
                OutputStreamWriter ow = new OutputStreamWriter(fs);
                ow.write(ua);
                ow.close();
                Log.e("读取文件完毕","UAsetHttpServer, save useragent to file " +
                        "successfully.");

            }catch(Exception e){
                Log.e("读取文件出错",e.getMessage());
            }
        }*/

        Log.e("http",session.getUri());

            return new Response(Response.Status.OK,NanoHTTPD.MIME_PLAINTEXT,
                    path+"segments.m3u8");
    }
}
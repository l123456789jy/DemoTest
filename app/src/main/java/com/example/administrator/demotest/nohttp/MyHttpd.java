package com.example.administrator.demotest.nohttp;

import android.util.Log;
import fi.iki.elonen.NanoHTTPD;

/**
 * 作者：Administrator on 2016/7/21 17:58
 * 邮箱：906514731@qq.com
 */
public class MyHttpd extends NanoHTTPD{

    public MyHttpd() {
        super(4477);
            //start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.e("MyHttpd","服务运行");
    }
    @Override
    public Response serve(IHTTPSession session) {
      /*  Method method = session.getMethod();
        String uri = session.getUri();
        Log.e("jltxgcy", method+"  "+uri);
        return newFixedLengthResponse("Hello server");*/
        return null;
    }

}
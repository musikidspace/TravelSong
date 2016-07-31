package com.lg.travelsong.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http工具类
 *
 * @author LuoYi on 2016/7/30
 */
public class MyHttpUtils {

    private static final int MSG_HTTPGET_SUCCESS = 100;
    private static final int MSG_HTTPGET_FAILURE = 101;

    private MyHandler mHandler;

    public MyHttpUtils() {
        mHandler = new MyHandler();
    }

    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                throw new IllegalArgumentException("msg must not be null!");
            }
            HttpGetCallBack callBack = null;
            String result = null;
            if (msg.obj instanceof HttpGetCallBack) {
                callBack = (HttpGetCallBack) msg.obj;
                result = msg.getData().getString("result");
                MyLogUtils.logCatch("MyHttpUtils", result);
            }
            switch (msg.what) {
                case MSG_HTTPGET_SUCCESS:
                    callBack.onSuccess(result);
                    break;
                case MSG_HTTPGET_FAILURE:
                    callBack.onFailure(result);
                    break;
            }
        }
    }

    /**
     * 通过get方式请求网络
     * @param url 请求地址
     * @param callBack 回调方法
     */
    public void httpGet(final String url, final HttpGetCallBack callBack) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Message mMessage = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                HttpURLConnection conn = null;
                StringBuffer sb = null;
                try {
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5 * 1000);
                    conn.setReadTimeout(5 * 1000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line = null;
                        sb = new StringBuffer();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        mMessage.what = MSG_HTTPGET_SUCCESS;
                        bundle.putString("result", sb.toString());
                    } else {
                        mMessage.what = MSG_HTTPGET_FAILURE;
                        bundle.putString("result", "Http response state wrong : " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    mMessage.what = MSG_HTTPGET_FAILURE;
                    bundle.putString("result", e.getMessage());
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    mMessage.obj = callBack;
                    mMessage.setData(bundle);
                    mHandler.sendMessage(mMessage);
                }
            }
        }).start();
    }

    /**
     * HttpGet的回调接口
     * 要求实现onSuccess(),onFailure()
     */
    public interface HttpGetCallBack {
        void onSuccess(String result);

        void onFailure(String failMsg);
    }
}
package com.lg.travelsong.utils;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lg.travelsong.global.AppProperty;
import com.lg.travelsong.manager.ThreadPool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    private static final int MSG_HTTPPOST_SUCCESS = 102;
    private static final int MSG_HTTPPOST_FAILURE = 103;

    private MyHandler mHandler;
    private int mConnectTimeout = 5 * 1000;
    private int mReadTimeout = 5 * 1000;
    public static boolean sGetCookies = false;//默认不读取cookies，外部

    public MyHttpUtils() {
        mHandler = new MyHandler();
    }

    /**
     * 设置超时的构造方法
     *
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     */
    public MyHttpUtils(int connectTimeout, int readTimeout) {
        this();
        mConnectTimeout = connectTimeout;
        mReadTimeout = readTimeout;
    }

    /**
     * 设置开启解析cookies的方法
     *
     * @param getCookies 是否从返回的输入流解析cookies
     */
    public MyHttpUtils(boolean getCookies) {
        this();
        sGetCookies = getCookies;
    }

    /**
     * 设置开启解析cookies,并设置超时的方法
     *
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @param getCookies     是否从返回的输入流解析cookies
     */
    public MyHttpUtils(boolean getCookies, int connectTimeout, int readTimeout) {
        this();
        sGetCookies = getCookies;
        mConnectTimeout = connectTimeout;
        mReadTimeout = readTimeout;
    }


    /**
     * 使用内部类创建Handler，Handler对象会隐式地持有一个外部类对象（通常是一个Activity）的引用
     * 静态类不持有外部类的对象，所以Activity可以随意被回收
     */
    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                throw new IllegalArgumentException("msg must not be null!");
            }
            HttpCallBack callBack = null;
            String result = null;
            if (msg.obj instanceof HttpCallBack) {
                callBack = (HttpCallBack) msg.obj;
                result = msg.getData().getString("result");
                if (sGetCookies){
                    String cookies = msg.getData().getString("cookies");
                    result = result + ";getCookiesWhenNeed:" + cookies;
                }
                MyLogUtils.logi("MyHttpUtils-->result", result);
            }
            switch (msg.what) {
                case MSG_HTTPGET_SUCCESS:
                    callBack.onSuccess(result);
                    break;
                case MSG_HTTPGET_FAILURE:
                    callBack.onFailure(result);
                    break;
                case MSG_HTTPPOST_SUCCESS:
                    callBack.onSuccess(result);
                    break;
                case MSG_HTTPPOST_FAILURE:
                    callBack.onFailure(result);
                    break;
            }
        }
    }

    /**
     * 通过get方式请求网络
     *
     * @param url      请求地址
     * @param param    参数，以name1=value1&name2=value2形式传入，无则传null
     * @param callBack 回调方法
     */
    public void httpGet(final String url, final String param, final HttpCallBack callBack) {
        ThreadPool.getInstance().execute(new Runnable() {

            @Override
            public void run() {
                String urlName = param == null ? url : url + "?" + param;
                Message mMessage = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                HttpURLConnection conn = null;
                StringBuffer sb;
                try {
                    // 打开和URL之间的连接
                    conn = (HttpURLConnection) new URL(urlName).openConnection();
                    conn.setRequestMethod("GET");
                    // 设置超时时间
                    conn.setConnectTimeout(mConnectTimeout);
                    conn.setReadTimeout(mReadTimeout);
                    // 设置通用的请求属性
                    conn.setRequestProperty("user-agent", Build.MODEL + ";" + Build.VERSION.RELEASE + ";" + AppProperty.versionCode);
                    // 建立实际的连接
                    conn.connect();
                    // 判断响应状态
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        sb = new StringBuffer();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        mMessage.what = MSG_HTTPGET_SUCCESS;
                        bundle.putString("result", sb.toString());
                        MyCloseUtils.doClose(is, isr, br);
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
                    if (sGetCookies) {
                        bundle.putString("cookies", conn.getHeaderField("cookies"));
                    }
                    mMessage.obj = callBack;
                    mMessage.setData(bundle);
                    mHandler.sendMessage(mMessage);
                }
            }
        });
    }

    /**
     * 通过post方式请求网络
     *
     * @param url      请求地址
     * @param param    参数，以name1=value1&name2=value2形式传入，无则传null
     * @param callBack 回调方法
     */
    public void httpPost(final String url, final String param, final HttpCallBack callBack) {
        ThreadPool.getInstance().execute(new Runnable() {

            @Override
            public void run() {
                String noNullParam = param == null ? "" : param;
                Message mMessage = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                HttpURLConnection conn = null;
                StringBuffer sb;
                try {
                    // 打开和URL之间的连接
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("POST");
                    // 设置超时时间
                    conn.setConnectTimeout(mConnectTimeout);
                    conn.setReadTimeout(mReadTimeout);
                    // 设置通用的请求属性
                    //设置contentType为application/x-www-form-urlencoded，
                    // servlet就可以直接使用request.getParameter("username");直接得到所需要信息
                    conn.setRequestProperty("contentType", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("user-agent", Build.MODEL + ";" + Build.VERSION.RELEASE + ";" + AppProperty.versionCode);
                    // 发送POST请求必须设置允许输出
                    conn.setDoOutput(true);
                    // 发送POST请求必须设置允许输入
                    conn.setDoInput(true);
                    // 建立实际的连接
                    conn.connect();
                    // 获取输出流
                    OutputStream os = conn.getOutputStream();
                    // 写入参数
                    os.write(noNullParam.getBytes());
                    os.flush();
                    // 判断响应状态
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        sb = new StringBuffer();
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        mMessage.what = MSG_HTTPPOST_SUCCESS;
                        bundle.putString("result", sb.toString());
                        MyCloseUtils.doClose(is, isr, br);
                    } else {
                        mMessage.what = MSG_HTTPPOST_FAILURE;
                        bundle.putString("result", "Http response state wrong : " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    mMessage.what = MSG_HTTPGET_FAILURE;
                    bundle.putString("result", e.getMessage());
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (sGetCookies) {
                        bundle.putString("cookies", conn.getHeaderField("cookies"));
                    }
                    mMessage.obj = callBack;
                    mMessage.setData(bundle);
                    mHandler.sendMessage(mMessage);
                }
            }
        });
    }

    /**
     * Http的回调接口
     * 要求实现onSuccess(),onFailure()
     */
    public interface HttpCallBack {
        void onSuccess(String result);

        void onFailure(String failMsg);
    }
}
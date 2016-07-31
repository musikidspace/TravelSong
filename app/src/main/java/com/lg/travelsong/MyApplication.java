package com.lg.travelsong;

import android.app.Application;

import com.lg.travelsong.utils.MyLogUtils;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 自定义Application
 *
 * @author LuoYi on 2016/7/29
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置捕获全局为捕获的异常捕获器
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                MyLogUtils.logCatch("MyApplication", "");
            }
        });

    }
}

package com.lg.travelsong;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.lg.travelsong.global.AppProperty;

/**
 * 自定义Application
 *
 * @author LuoYi on 2016/7/29
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppProperty.initVersionCode(this);
        //在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //设置捕获全局为捕获的异常捕获器
//        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable throwable) {
//                MyLogUtils.logCatch("MyApplication", "");
//            }
//        });

    }
}

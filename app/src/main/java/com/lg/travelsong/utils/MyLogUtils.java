package com.lg.travelsong.utils;

import android.util.Log;

/**
 * 日志工具类
 *
 * @author LuoYi on 2016/7/29
 */
public class MyLogUtils {
    private static boolean flag = true;

    /**
     * 调试日志打印，调试时将flag设为true，发布则将flag改为false
     *
     * @param tag 建议传类名-->方法名，方便调试
     * @param msg 输出的日志信息
     */
    public static void logi(String tag, String msg) {
        if (flag) {
            tag = tag != null ? tag : "MyLogUtilsTagNull";
            msg = msg != null ? msg : "MyLogUtilsMsgNull";
            Log.i("++++++" + tag + "++++++", msg);
        }
    }

    /**
     * 运行异常日志打印收集
     *
     * @param tag 建议传类名-->方法名，方便定位问题
     * @param msg 输出的日志信息
     */
    public static void logCatch(String tag, String msg) {
        tag = tag != null ? tag : "MyLogUtilsLogCatchTagNull";
        msg = msg != null ? msg : "MyLogUtilsLogCatchMsgNull";
        Log.i("++++++" + tag + "++++++", msg);
    }
}

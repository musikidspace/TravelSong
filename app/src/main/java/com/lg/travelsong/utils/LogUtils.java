package com.lg.travelsong.utils;

import android.util.Log;

/**
 * 日志工具类
 *
 * @author LuoYi on 2016/7/29
 */
public class LogUtils {
    private static boolean flag = true;

    /**
     * 日志打印工具类，调试时将flag设为true，发布则将flag改为false
     *
     * @param tag 建议传类名方便调试
     * @param msg 输出的日志信息
     */
    public static void logi(String tag, String msg) {
        if (flag) {
            Log.i("++++++" + tag + "++++++", msg);
        }
    }
}

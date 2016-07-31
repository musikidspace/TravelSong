package com.lg.travelsong.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭流等得工具类
 * @author LuoYi on 2016/7/31
 */
public class MyCloseUtils {
    /**
     * 关闭流等
     * @param closeable 要关闭的Closeable
     */
    public static void doClose(Closeable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                MyLogUtils.logi("MyCloseUtils", e.getMessage());
            }
        }
    }
}

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
     * @param closeables 要关闭的Closeables,可变参数
     */
    public static void doClose(Closeable... closeables){
        for (Closeable closeable : closeables) {
            if (closeable != null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    MyLogUtils.logi("MyCloseUtils", e.getMessage());
                }
            }
        }
    }
}

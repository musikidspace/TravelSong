package com.lg.travelsong.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 常用的工具类
 *
 * @author LuoYi on 2016/8/7
 */
public class MyCommonUtils {
    /**
     * 将px转化为dp
     * @param context 上下文
     * @param px px值
     * @return dp值
     */
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 将dp转化为px
     * @param context 上下文
     * @param dp dp值
     * @return px值
     */
    public static int dp2px(Context context, float dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}

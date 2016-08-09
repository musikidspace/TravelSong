package com.lg.travelsong.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

/**
 * 常用的工具类
 *
 * @author LuoYi on 2016/8/7
 */
public class MyDisplayUtils {
    /**
     * 将px转化为dp
     *
     * @param context 上下文
     * @param px      px值
     * @return dp值
     */
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 将dp转化为px
     *
     * @param context 上下文
     * @param dp      dp值
     * @return px值
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 将px转换为sp
     *
     * @param context 上下文
     * @param px      px值
     * @return sp值
     */
    public static int px2sp(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * 将sp转换为px
     *
     * @param context 上下文
     * @param sp      sp值
     * @return px值
     */
    public static int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 从asset创建字体
     *
     * @param context  上下文
     * @param typeface 字体名称
     * @return 字体
     */
    public static Typeface getTypeface(Context context, String typeface) {
        if (typeface == null) {
            typeface = "fonts/BRI293.TTF";
        }
        return Typeface.createFromAsset(context.getAssets(), typeface);
    }
}

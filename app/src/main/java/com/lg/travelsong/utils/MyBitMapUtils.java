package com.lg.travelsong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * BitMap相关方法
 * @author LuoYi on 2016/7/30
 */
public class MyBitMapUtils {

    public static Bitmap readBitMap(Context context, int resId){
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is);
    }
}

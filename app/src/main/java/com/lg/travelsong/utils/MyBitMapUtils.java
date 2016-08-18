package com.lg.travelsong.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * BitMap相关方法
 *
 * @author LuoYi on 2016/8/4
 */
public class MyBitmapUtils {

    private static MyBitmapUtils sMyBitmapUtils;

    private Context mContext;

    private MyBitmapUtils(Context context) {
        mContext = context;
    }

    /**
     * 单例模式 懒汉式，双重校验锁
     *
     * @return MyBitmapUtils对象
     */
    public static MyBitmapUtils getInstance(Context context) {
        if (sMyBitmapUtils == null) {
            synchronized (MyBitmapUtils_no.class) {
                if (sMyBitmapUtils == null) {
                    sMyBitmapUtils = new MyBitmapUtils(context);
                }
            }
        }
        return sMyBitmapUtils;
    }

    /**
     * 根据id获取原图片
     *
     * @param resId 图片id
     * @return 原图
     */
    public Bitmap readBitMap(int resId) {
        InputStream is = mContext.getResources().openRawResource(resId);
        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
        MyCloseUtils.doClose(is);
        return bm;
    }

    /**
     * 根据id按高度获取压缩图片
     *
     * @param resId  图片id
     * @param height 需要返回图片的高度
     * @return 压缩后的图片
     */
    public Bitmap readSampleBitMap(int resId, int height) {
        //1.不加载图片获取图片参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds设为true那么将不返回实际的bitmap
        options.inJustDecodeBounds = true;
        InputStream is = mContext.getResources().openRawResource(resId);
        BitmapFactory.decodeStream(is, null, options);
        //2.计算缩放比，重新加载图片
        options.inSampleSize = options.outHeight / height > 0 ? options.outHeight / height : 1;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeStream(is, null, options);
        MyCloseUtils.doClose(is);
        return bm;
    }

    /**
     * 根据文件名从assets中读取图片
     *
     * @param name 图片名
     * @return 返回bitmap
     */
    public Bitmap readBitmapFromAssets(String name) throws Exception {
        AssetManager am = mContext.getResources().getAssets();
        InputStream is = am.open(name);
        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
        MyCloseUtils.doClose(is);
        return bm;
    }
}

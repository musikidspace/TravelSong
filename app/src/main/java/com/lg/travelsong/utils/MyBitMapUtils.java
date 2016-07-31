package com.lg.travelsong.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * BitMap相关方法
 *
 * @author LuoYi on 2016/7/30
 */
public class MyBitmapUtils {

    private static MyBitmapUtils sMyBitmapUtils;

    private Context mContext;
    private int type;

    private MyBitmapUtils(Context context) {
        mContext = context;
    }

    /**
     * 单例模式 懒汉式
     *
     * @return MyBitmapUtils对象
     */
    public static MyBitmapUtils getInstance(Context context) {
        if (sMyBitmapUtils == null) {
            synchronized (MyBitmapUtils.class) {
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
     * @param context 上下文
     * @param resId   图片id
     * @return 原图
     */
    public Bitmap readBitMap(Context context, int resId) {
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
        MyCloseUtils.doClose(is);
        return bm;
    }

    /**
     * 根据id按高度获取压缩图片
     *
     * @param context 上下文
     * @param resId   图片id
     * @param height  需要返回图片的高度
     * @return 压缩后的图片
     */
    public Bitmap readSampleBitMap(Context context, int resId, int height) {
        //1.不加载图片获取图片参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds设为true那么将不返回实际的bitmap
        options.inJustDecodeBounds = true;
        InputStream is = context.getResources().openRawResource(resId);
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
    public Bitmap readBitmapFromAssets(Context context, String name) throws IOException {
        AssetManager am = context.getResources().getAssets();
        InputStream is = am.open(name);
        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
        MyCloseUtils.doClose(is);
        return bm;
    }

    /**
     * 根据id获取异步加载原图片
     * @param iv ImageView
     * @param resId 资源Id
     */
    public void setBitmap(ImageView iv, int resId){
        type = 0;
        BitmapTaskFactory.getInstance().create(type).execute(mContext, iv, resId);
    }

    /**
     * 根据文件名从assets中读取图片，异步加载
     * @param iv ImageView
     * @param name 图片名
     */
    public void setBitmapFromAssets(ImageView iv, String name){
        type = 1;
        BitmapTaskFactory.getInstance().create(type).execute(mContext, iv, name);
    }
}
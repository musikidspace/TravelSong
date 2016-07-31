package com.lg.travelsong.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.lg.travelsong.utils.bean.BitmapTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * BitmapTask工厂类
 * @author LuoYi on 2016/7/31
 */
public class BitmapTaskFactory {
    private static BitmapTaskFactory sBitmapTaskFactory;
    private static BitmapTask bitmapTask;
    private static LruCache<String, Bitmap> lruCache;
    private static ImageView iv;
    private static int resId;
    private static String name;

    private BitmapTaskFactory() {
    }

    /**
     * 单例模式， 懒汉式
     *
     * @return BitmapTaskFactory对象
     */
    public static BitmapTaskFactory getInstance() {
        if (sBitmapTaskFactory == null) {
            synchronized (BitmapTaskFactory.class) {
                if (sBitmapTaskFactory == null) {
                    sBitmapTaskFactory = new BitmapTaskFactory();
                }
            }
        }
        return sBitmapTaskFactory;
    }

    public BitmapTask create(int type) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        lruCache = new LruCache<String, Bitmap>((int) maxMemory / 8) {
            // 返回每个对象的大小，需重写
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        switch (type) {
            case 0:
                bitmapTask = new BitmapTask() {
                    @Override
                    protected LruCache<String, Bitmap> doInBackground(Object... objects) {
                        Context context = (Context) objects[0];
                        iv = (ImageView) objects[1];
                        resId = (int) objects[2];
                        InputStream is = context.getResources().openRawResource(resId);
                        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
                        lruCache.put(resId + "", bm);
                        MyCloseUtils.doClose(is);
                        return lruCache;
                    }

                    @Override
                    protected void onPostExecute(LruCache<String, Bitmap> lruCache) {
                        iv.setImageBitmap(lruCache.get(resId + ""));
                    }
                };
                break;
            case 1:
                bitmapTask = new BitmapTask() {
                    @Override
                    protected LruCache<String, Bitmap> doInBackground(Object... objects) {
                        Context context = (Context) objects[0];
                        iv = (ImageView) objects[1];
                        name = (String) objects[2];
                        AssetManager am = context.getResources().getAssets();
                        InputStream is = null;
                        try {
                            is = am.open(name);
                            Bitmap bm = BitmapFactory.decodeStream(is, null, null);
                            lruCache.put(name, bm);
                        } catch (Exception e) {
                            MyLogUtils.logCatch("MyBitmapUtils-->setBitmapFromAssets", e.getMessage());
                        }
                        MyCloseUtils.doClose(is);
                        return lruCache;
                    }

                    @Override
                    protected void onPostExecute(LruCache<String, Bitmap> lruCache) {
                        iv.setImageBitmap(lruCache.get(name));
                    }
                };
                break;
            case 2:
                break;
        }
        return bitmapTask;
    }
}
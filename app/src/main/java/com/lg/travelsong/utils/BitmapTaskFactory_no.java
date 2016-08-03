package com.lg.travelsong.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.lg.travelsong.manager.ThreadPool;
import com.lg.travelsong.utils.bean.BitmapTask_no;

import java.io.InputStream;

/**
 * BitmapTask工厂类
 * @author LuoYi on 2016/7/31
 */
public class BitmapTaskFactory_no {
    private static BitmapTaskFactory_no sBitmapTaskFactory;
    private static BitmapTask_no bitmapTask;
    private static LruCache<String, Bitmap> lruCache;
    private static ImageView iv;
    private static int resId;
    private static String name;

    private BitmapTaskFactory_no() {
    }

    /**
     * 单例模式， 懒汉式,双重校验锁
     *
     * @return BitmapTaskFactory对象
     */
    public static BitmapTaskFactory_no getInstance() {
        if (sBitmapTaskFactory == null) {
            synchronized (BitmapTaskFactory_no.class) {
                if (sBitmapTaskFactory == null) {
                    sBitmapTaskFactory = new BitmapTaskFactory_no();
                }
            }
        }
        return sBitmapTaskFactory;
    }

    public BitmapTask_no create(int type) {
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
                bitmapTask = new BitmapTask_no() {
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
                        MyLogUtils.logi("onPostExecute", resId + "");
                    }
                };
                break;
            case 1:
                bitmapTask = new BitmapTask_no() {
                    boolean flag = true;
                    boolean reFlag = false;

                    @Override
                    protected LruCache<String, Bitmap> doInBackground(Object... objects) {
                        MyLogUtils.logi("doInBackground", "执行了");
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
                            MyLogUtils.logCatch("MyBitmapUtils_no-->setBitmapFromAssets", e.getMessage());
                        }
                        MyCloseUtils.doClose(is);
                        return lruCache;
                    }

                    @Override
                    protected void onPostExecute(final LruCache<String, Bitmap> lruCache) {
                        MyLogUtils.logi("onPostExecute", "执行了");
                        iv.setImageBitmap(lruCache.get(name));
                        //回收bitmap
                        ThreadPool.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                while (flag){
                                    MyLogUtils.logi("onPostExecutewhilewhilewhile", "执行了");
                                    SystemClock.sleep(4000);
                                    if(reFlag){
                                        recyleBitmap(lruCache);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void notifyRecyle() {
                        reFlag = true;
                    }

                    private void recyleBitmap(LruCache<String, Bitmap> lruCache){
                        flag = false;
                        if (!lruCache.get(name).isRecycled()){
                            MyLogUtils.logi("recyleBitmap", "执行了");
                            lruCache.get(name).recycle();
                        }
                        this.cancel(false);
                    }

                };
                break;
            case 2:
                break;
        }
        return bitmapTask;
    }
}
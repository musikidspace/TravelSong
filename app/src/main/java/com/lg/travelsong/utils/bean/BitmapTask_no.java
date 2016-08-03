package com.lg.travelsong.utils.bean;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;

/**
 * BitmapTask类型
 *
 * @author LuoYi on 2016/7/31
 */

public class BitmapTask_no extends AsyncTask<Object, Integer, LruCache<String, Bitmap>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LruCache<String, Bitmap> doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(LruCache<String, Bitmap> lruCache) {
        super.onPostExecute(lruCache);
    }

    public void notifyRecyle() {
    }
}

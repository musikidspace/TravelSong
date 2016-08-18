package com.lg.travelsong.dao.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.lg.travelsong.utils.MyLogUtils;

import java.io.File;
import java.io.IOException;

/**
 * SQLiteOpenHeelper 传入context对象，定义后可以创建带有目录的数据库
 * @author LuoYi on 2016/8/18
 */
public class MyContextWrapper extends ContextWrapper {
    private Context mContext;
    private String dirPath;

    public MyContextWrapper(Context base) {
        super(base);
        mContext = base;
    }

    public MyContextWrapper(Context base, String dirPath) {
        super(base);
        mContext = base;
        this.dirPath = dirPath;
    }

    /**
     * 获得数据库路径，如果不存在，则创建
     *
     * @param name
     */
    @Override
    public File getDatabasePath(String name) {
        //获取应用包名最后一段
        String pn = this.getPackageName();
        String appName = pn.substring(pn.lastIndexOf(".") + 1);
        MyLogUtils.logi("MyContextWrapper-->appName", appName);
        // 获取/data/data/youPackageName/appName的文件夹路径
        String dbDir = mContext.getDir(appName, MODE_PRIVATE).getAbsolutePath();
        dbDir += "/" + ((dirPath == null || "".equals(dirPath)) ? mContext.getPackageName() : dirPath);// 数据库所在目录
        String dbPath = dbDir + "/" + name;// 数据库路径
        // 判断目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        // 数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        // 判断文件是否存在，不存在则创建该文件
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
            } catch (IOException e) {
                MyLogUtils.logCatch("MyContextWrapper-->getDatabasePath", e.getMessage());
                e.printStackTrace();
            }
        } else {
            isFileCreateSuccess = true;
        }
        // 返回数据库文件对象
        if (isFileCreateSuccess) {
            return dbFile;
        } else {
            return null;
        }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
     * int, android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }

}

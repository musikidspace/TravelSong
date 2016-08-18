package com.lg.travelsong.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lg.travelsong.bean.User;
import com.lg.travelsong.utils.MyLogUtils;
import com.lg.travelsong.utils.MyStringUtils;

/**
 * 应用相关信息
 * @author LuoYi on 2016/8/1
 */
public class AppProperty {

    public static User currentUser;

    public static int versionCode = -1;
    /**
     * 初始化版本号
     * @param context
     * @return 版本号，异常为-1
     */
    public static void initVersionCode(Context context){
        try {
            PackageManager mPM = context.getPackageManager();
            PackageInfo pi = mPM.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            MyLogUtils.logCatch("AppProperty-->initVersionCode", e.getMessage());
        }
    }

    /**
     * 获取数据库路径
     * @return 数据库路径
     */
    public static String dirPath(){
        if (currentUser != null){
            return MyStringUtils.md5(currentUser.usercode);
        } else {
            return "00000000";
        }
    }
}

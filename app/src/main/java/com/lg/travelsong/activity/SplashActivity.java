package com.lg.travelsong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.lg.travelsong.R;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyLogUtils;
import com.lg.travelsong.utils.MySPUtils;

import java.io.IOException;

/**
 * 闪屏界面
 *
 * @author LuoYi on 2016/7/29
 */
public class SplashActivity extends BaseActivity {
    private Context mContext;
    private ImageView iv_ad;
    private MyBitmapUtils mMyBitmapUtils;
    private Intent mIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
    }

    //初始化界面
    private void initView() {
        mContext = this;
        iv_ad = (ImageView) findViewById(R.id.iv_ad);
    }

    //初始化数据
    private void initData() {
        mMyBitmapUtils = MyBitmapUtils.getInstance(mContext);
        String name = "ad.png";
        boolean exist = false;
        try {
            String[] names = getAssets().list("ad");
            for (int i = 0; i < names.length; i ++){
                if (name.equals(names[i])){
                    mMyBitmapUtils.setBitmapFromAssets(iv_ad, "ad/" + name);
                    exist = true;
                    break;
                }
            }
        } catch (IOException e) {
            MyLogUtils.logCatch("SplashActivity-->getAssets", e.getMessage());
        } finally {
            if (!exist){
                mMyBitmapUtils.setBitmap(iv_ad, R.drawable.login_bg);
            }
        }

        //检查版本名是否存在了已读引导的SP
        String versionName = "0";
        try {
            PackageManager mPM = getPackageManager();
            PackageInfo pi = mPM.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            MyLogUtils.logCatch("SplashActivity-->versionName", e.getMessage());
        }
        String guidedVersion = MySPUtils.getString(mContext, "guidedVersion");

        if (!guidedVersion.equals(versionName)){
            mIntent.setClass(mContext, GuideActivity.class);//新版本，跳转到引导页面
        } else {
            int lockType = MySPUtils.getInt(mContext, "lockType");
            if (lockType != 0){
                mIntent.setClass(mContext, LockActivity.class);//设置了程序锁，跳转到解锁页面
                mIntent.putExtra("lockType", lockType);
            } else {
                mIntent.setClass(mContext, MainActivity.class);//跳转到主页面
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);//平移动画
                finish();
            }
        }, 3 * 1000);
    }
}
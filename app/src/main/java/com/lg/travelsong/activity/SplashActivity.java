package com.lg.travelsong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.lg.travelsong.R;
import com.lg.travelsong.global.AppProperty;
import com.lg.travelsong.global.HttpConfig;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyHttpUtils;
import com.lg.travelsong.utils.MyLogUtils;
import com.lg.travelsong.utils.MySPUtils;

/**
 * 闪屏界面
 *
 * @author LuoYi on 2016/7/29
 */
public class SplashActivity extends BaseActivity {
    private Context mContext;
    private ImageView iv_ad;
    private MyBitmapUtils mMyBitmapUtils;
    private Bitmap adBitmap;
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
        new MyHttpUtils(true).httpPost(HttpConfig.HOSTURL + HttpConfig.CONFIG, null, new MyHttpUtils.HttpCallBack() {

            @Override
            public void onSuccess(String result) {
                MyLogUtils.logi("SplashActivity-->onSuccess", result);
            }

            @Override
            public void onFailure(String failMsg) {
                MyLogUtils.logi("SplashActivity-->onFailure", failMsg);
            }
        });

        mMyBitmapUtils = MyBitmapUtils.getInstance(mContext);
        String name = "ad.png";
        boolean exist = false;
        try {
            String[] names = getAssets().list("ad");
            for (int i = 0; i < names.length; i++) {
                if (name.equals(names[i])) {
                    adBitmap = mMyBitmapUtils.readBitmapFromAssets("ad/" + name);
                    iv_ad.setImageBitmap(adBitmap);
                    exist = true;
                    break;
                }
            }
        } catch (Exception e) {
            MyLogUtils.logCatch("SplashActivity-->getAssets", e.getMessage());
        } finally {
            if (!exist) {
                adBitmap = mMyBitmapUtils.readBitMap(R.drawable.login_bg);
                iv_ad.setImageBitmap(adBitmap);
            }
        }

        //检查版本名是否存在了已读引导的SP
        int versionCode = AppProperty.versionCode;
        int guidedVersionCode = MySPUtils.getInt(mContext, "guidedVersionCode");
        if (guidedVersionCode != versionCode) {
            mIntent.setClass(mContext, GuideActivity.class);//新版本，跳转到引导页面
        } else {
            // TODO: 2016/8/1 登录页面
            int lockType = MySPUtils.getInt(mContext, "lockType");
            if (lockType != 0) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adBitmap != null && !adBitmap.isRecycled()){
            adBitmap.recycle();
            adBitmap = null;
        }
    }
}
package com.lg.travelsong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.lg.travelsong.R;

/**
 * 闪屏界面
 *
 * @author LuoYi on 2016/7/29
 */
public class SplashActivity extends BaseActivity {

    private Context mContext;
    private ImageView iv_ad;
    private Bitmap adBitMap;

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
//        adBitMap = MyBitMapUtils.readBitMap(mContext, R.drawable.ad_default);
//        iv_ad.setImageBitmap(adBitMap);

        Intent intent = new Intent(mContext, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adBitMap != null){
            adBitMap.recycle();
            System.gc();
        }
    }
}

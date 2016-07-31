package com.lg.travelsong.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lg.travelsong.R;
import com.lg.travelsong.utils.MyBitmapUtils;

import java.util.ArrayList;

/**
 * 引导页
 *
 * @author LuoYi on 2016/7/29
 */
public class GuideActivity extends BaseActivity {

    private final int[] mGuideImgs = new int[]{R.drawable.start_i0, R.drawable.start_i1, R.drawable.start_i2,
            R.drawable.start_i3, R.drawable.start_i4, R.drawable.start_i5, R.drawable.start_i6, R.drawable.start_i7};
    private ArrayList<ImageView> mIVList;
    private Context mContext;
    private ViewPager vp_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        initView();
        initData();
    }

    //初始化界面
    private void initView() {
        mContext = this;
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
    }

    //初始化数据
    private void initData() {
        mIVList = new ArrayList<>();
        MyBitmapUtils myBitmapUtils = MyBitmapUtils.getInstance(mContext);
        for (int guideImg : mGuideImgs) {
            ImageView iv = new ImageView(mContext);
            iv.setImageBitmap(myBitmapUtils.readBitMap(mContext, guideImg));
            mIVList.add(iv);
        }
        vp_guide.setAdapter(new GuideAdapter());
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mIVList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public ImageView instantiateItem(ViewGroup container, int position) {
            ImageView iv = mIVList.get(position);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

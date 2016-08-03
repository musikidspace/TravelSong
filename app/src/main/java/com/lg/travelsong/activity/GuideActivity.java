package com.lg.travelsong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
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
    private LruCache<String, Bitmap> mLruCache;
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
        mLruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()) / 8){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        Bitmap bitmap;
        MyBitmapUtils myBitmapUtils = MyBitmapUtils.getInstance(mContext);
        for (int i = 0; i < mGuideImgs.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bitmap = myBitmapUtils.readBitMap(mGuideImgs[i]);
            iv.setImageBitmap(bitmap);
            mLruCache.put(i + "", bitmap);
            mIVList.add(iv);
        }
        vp_guide.setAdapter(new GuideAdapter());

        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mIVList.size() - 1) {
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

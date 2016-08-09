package com.lg.travelsong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lg.travelsong.R;
import com.lg.travelsong.global.AppProperty;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyDisplayUtils;
import com.lg.travelsong.utils.MySPUtils;

import java.util.ArrayList;

/**
 * 引导页
 *
 * @author LuoYi on 2016/7/29
 */
public class GuideActivity extends BaseActivity implements View.OnTouchListener{

    private final int[] mGuideImgs = new int[]{R.drawable.start_i1, R.drawable.start_i2, R.drawable.start_i3,
            R.drawable.start_i4, R.drawable.start_i5, R.drawable.start_i6, R.drawable.start_i7};
    private ArrayList<ImageView> mIVList;
    private LruCache<String, Bitmap> mLruCache;
    private Context mContext;
    private ViewPager vp_guide;
    private LinearLayout ll_points;
    private int lastPosition = 0;
    private float downX;

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
        ll_points = (LinearLayout) findViewById(R.id.ll_points);
    }

    //初始化数据
    private void initData() {
        mIVList = new ArrayList<>();
        mLruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()) / 8) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        MyBitmapUtils myBitmapUtils = MyBitmapUtils.getInstance(mContext);
        Bitmap bitmap;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                MyDisplayUtils.dp2px(mContext, 8), MyDisplayUtils.dp2px(mContext, 8));
        int margin = MyDisplayUtils.dp2px(mContext, 2);
        params.setMargins(margin, 0, margin, 0);
        View point;
        for (int i = 0; i < mGuideImgs.length; i++) {
            //添加图片
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bitmap = myBitmapUtils.readBitMap(mGuideImgs[i]);
            iv.setImageBitmap(bitmap);
            if (i == mGuideImgs.length - 1){
                iv.setOnTouchListener(this);
            }
            mLruCache.put(i + "", bitmap);
            mIVList.add(iv);
            //添加指示器
            point = new View(mContext);
            point.setBackgroundResource(R.drawable.selector_point);
            if (i != 0) {
                point.setEnabled(false);
            }
            ll_points.addView(point, params);
        }
        vp_guide.setAdapter(new GuideAdapter());

        //滑动时，变动小白点状态
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ll_points.getChildAt(lastPosition).setEnabled(false);
                ll_points.getChildAt(position).setEnabled(true);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == mIVList.get(mIVList.size() - 1)){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN :
                    downX = motionEvent.getX();//按下时的x值
                    break;
                case MotionEvent.ACTION_UP ://当向上下拉了才会触发这个
                    if (downX - motionEvent.getX()> MyDisplayUtils.getScreenWidth(mContext) / 4){
                        skipTo();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL ://直接向左右话，触发这个（因为这是最后一个vp）
                    if (downX - motionEvent.getX() > 0){//向右滑动
                        skipTo();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * 跳转界面
     */
    private void skipTo(){
        int lockType = MySPUtils.getInt(mContext, "lockType");
        Intent mIntent = new Intent();
        if ("".equals(MySPUtils.getString(mContext, "cookie")) || "null".equals(MySPUtils.getString(mContext, "cookie"))) {
            mIntent.setClass(mContext, LoginActivity.class);//cookie为空，跳转到登录界面
        } else if (lockType != 0) {
            mIntent.setClass(mContext, LockActivity.class);//设置了程序锁，跳转到解锁页面
            mIntent.putExtra("lockType", lockType);
        } else {
            mIntent.setClass(mContext, MainActivity.class);//跳转到主页面
        }
        startActivity(mIntent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);//平移动画
        finish();
        //设置已读本版本引导
        MySPUtils.putInt(mContext, "guidedVersionCode", AppProperty.versionCode);
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

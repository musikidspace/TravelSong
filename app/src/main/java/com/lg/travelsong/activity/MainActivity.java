package com.lg.travelsong.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.lg.travelsong.R;
import com.lg.travelsong.utils.MyDisplayUtils;

/**
 * @author LuoYi on 2016/8/1
 */
public class MainActivity extends BaseActivity implements View.OnTouchListener{

    private Context mContext;
    private FragmentTabHost fth_tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView(){
        mContext = this;
        fth_tabhost = (FragmentTabHost) findViewById(R.id.fth_tabhost);

        //使FragementTabHost和FrameLayout关联
        fth_tabhost.setup(mContext, getSupportFragmentManager(), R.id.sv_tabcontent);
        //不显示分隔线
        fth_tabhost.getTabWidget().setShowDividers(0);
        //添加对应Fragment
        initTabs();
    }

    /**
     * 添加tab到FragmentTabHost
     */
    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        for (int i = 0; i < tabs.length; i ++){
            TabHost.TabSpec ts = fth_tabhost.newTabSpec(getString(tabs[i].resName));
            // 自定义选项卡
            View viewIndicator = View.inflate(mContext, R.layout.tab_indicator, null);
            TextView tv_tab_title = (TextView) viewIndicator.findViewById(R.id.tv_tab_title);
            Drawable drawable = this.getResources().getDrawable(tabs[i].resIcon);
            drawable.setBounds(0, 0, MyDisplayUtils.dp2px(mContext, 32), MyDisplayUtils.dp2px(mContext, 32));
            tv_tab_title.setCompoundDrawables(null, drawable, null, null);
            tv_tab_title.setText(getString(tabs[i].resName));
            // 设置自定义选项卡
            ts.setIndicator(viewIndicator);
            ts.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });

            Bundle bundle = new Bundle();
            bundle.putString("host", "content: " + getString(tabs[i].resName));
            // 2. 把新的选项卡添加到TabHost中
            fth_tabhost.addTab(ts, tabs[i].clz, bundle);

            fth_tabhost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}

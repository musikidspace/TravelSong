package com.lg.travelsong.activity;

import com.lg.travelsong.fragment.ChatFragment;
import com.lg.travelsong.fragment.MapFragment;
import com.lg.travelsong.fragment.MeFragment;
import com.lg.travelsong.fragment.MomentFragment;
import com.lg.travelsong.R;

/**
 * TabHost TabSpec的枚举
 * @author LuoYi on 2016/8/10
 */
public enum MainTab {
    MAP(0, R.string.map, R.drawable.selector_tab_icon_map,
            MapFragment.class),

    CHAT(1, R.string.chat, R.drawable.selector_tab_icon_chat,
            ChatFragment.class),

    MOMENT(3, R.string.moment, R.drawable.selector_tab_icon_moment,
            MomentFragment.class),

    ME(4, R.string.me, R.drawable.selector_tab_icon_me,
            MeFragment.class);

    public int idx;
    public int resName;
    public int resIcon;
    public Class<?> clz;

    private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }
}

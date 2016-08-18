package com.lg.travelsong.bean;

/**
 * 图片和文字内部类
 *
 * @author LuoYi on 2016/8/17
 */
public class ResIdAndName {
    public int resId;//图片id
    public String rname;//要显示的文字

    public ResIdAndName(int resId, String rname) {
        this.resId = resId;
        this.rname = rname;
    }
}

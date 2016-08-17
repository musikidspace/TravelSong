package com.lg.travelsong.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lg.travelsong.R;

/**
 * @author LuoYi on 2016/8/16
 */
public class CustomPopupWindow extends PopupWindow {

    public CustomPopupWindow(final Activity context, int resId, final OnItemClickListener onItemClickListener) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(resId, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Style_PopupAnim_Right);
        RelativeLayout[] relativeLayouts = new RelativeLayout[]{
                (RelativeLayout) conentView.findViewById(R.id.re_item1),
                (RelativeLayout) conentView.findViewById(R.id.re_item2),
                (RelativeLayout) conentView.findViewById(R.id.re_item3),
                (RelativeLayout) conentView.findViewById(R.id.re_item4),
                (RelativeLayout) conentView.findViewById(R.id.re_item5)

        };
        for (int i = 0; i < relativeLayouts.length; i++) {
            final int finalI = i;
            relativeLayouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomPopupWindow.this.dismiss();
                    onItemClickListener.onClick(finalI);
                }
            });
        }


    }

    /**
     * 显示popupWindow
     *
     * @param parent 基于此控件弹出
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    //点击事件回调
    public interface OnItemClickListener {
        void onClick(int position);
    }

}
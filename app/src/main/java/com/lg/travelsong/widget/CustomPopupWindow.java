package com.lg.travelsong.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lg.travelsong.R;
import com.lg.travelsong.bean.ResIdAndName;
import com.lg.travelsong.utils.MyBitmapUtils;

import java.util.List;

/**
 * @author LuoYi on 2016/8/16
 */
public class CustomPopupWindow extends PopupWindow {

    public CustomPopupWindow(final Context context, List<ResIdAndName> list, final OnItemClickListener onItemClickListener) {
        View view = View.inflate(context, R.layout.custompopup, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
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
        RelativeLayout[] rls = new RelativeLayout[]{
                (RelativeLayout) view.findViewById(R.id.re_item1),
                (RelativeLayout) view.findViewById(R.id.re_item2),
                (RelativeLayout) view.findViewById(R.id.re_item3),
                (RelativeLayout) view.findViewById(R.id.re_item4),
                (RelativeLayout) view.findViewById(R.id.re_item5),
                (RelativeLayout) view.findViewById(R.id.re_item6)
        };
        ImageView[] ivs = new ImageView[]{
                (ImageView) view.findViewById(R.id.iv_item1),
                (ImageView) view.findViewById(R.id.iv_item2),
                (ImageView) view.findViewById(R.id.iv_item3),
                (ImageView) view.findViewById(R.id.iv_item4),
                (ImageView) view.findViewById(R.id.iv_item5),
                (ImageView) view.findViewById(R.id.iv_item6)
        };
        TextView[] tvs = new TextView[]{
                (TextView) view.findViewById(R.id.tv_item1),
                (TextView) view.findViewById(R.id.tv_item2),
                (TextView) view.findViewById(R.id.tv_item3),
                (TextView) view.findViewById(R.id.tv_item4),
                (TextView) view.findViewById(R.id.tv_item5),
                (TextView) view.findViewById(R.id.tv_item6)
        };

        for (int i = 0; i < rls.length; i++) {
            final int fi = i;
            if (i < list.size()) {
                rls[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomPopupWindow.this.dismiss();
                        onItemClickListener.onClick(fi);
                    }
                });
                ivs[i].setImageBitmap(MyBitmapUtils.getInstance(context).readBitMap(list.get(i).resId));
                tvs[i].setText(list.get(i).rname);
            } else {
                rls[i].setVisibility(View.GONE);
            }
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

    /**
     * 点击事件回调
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }

}

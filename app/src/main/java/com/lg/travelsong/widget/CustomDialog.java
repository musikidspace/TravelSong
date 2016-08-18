package com.lg.travelsong.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lg.travelsong.R;

/**
 * 自定义弹出进度条
 *
 * @author LuoYi on 2016/8/9
 */

public class CustomDialog extends Dialog {

    private static int default_width = 220;
    private static int default_height = 60;

    public CustomDialog(Context context, int width, int height, int layout, int style) {
        //父类构造方法
        super(context, style);

        setContentView(layout);
        //获取lp对象
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        //获取屏幕密度
        //Resources resources = context.getResources();
        //DisplayMetrics displayMetrics = resources.getDisplayMetrics();//方法一
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        float density = displayMetrics.density;
        //设置lp属性
        layoutParams.width = (int) (width * density);
        layoutParams.height = (int) (height * density);
        layoutParams.gravity = Gravity.CENTER;
        //lp设置进window
        window.setAttributes(layoutParams);
    }

    /*
     * 缺省状态
     */
    public CustomDialog(Context context) {
        this(context, default_width, default_height, R.layout.dialog_layout, R.style.DialogTheme);
    }

    /*
     * 设置宽高
     */
    public CustomDialog(Context context, int width, int height) {
        this(context, width, height, R.layout.dialog_layout, R.style.DialogTheme);
    }

    /*
     * 设置样式
     */
    public CustomDialog(Context context, int style) {
        this(context, default_width, default_height, R.layout.dialog_layout, style);
    }

    /*
     * 设置文字
     */
    public void setText(String text) {
        TextView textView = (TextView) findViewById(R.id.tv_dialog);
        if (textView != null) {
            textView.setText(text);
        }
    }

}

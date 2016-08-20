package com.lg.travelsong.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lg.travelsong.R;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyDisplayUtils;
import com.lg.travelsong.widget.CustomDialog;

/**
 * @author LuoYi on 2016/8/9
 */
public class ForgotActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private Intent intent;
    private EditText et_user;
    private EditText et_account;
    private Button btn_find_password;
    private ImageView iv_back;
    private CustomDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        initView();
    }

    private void initView() {
        mContext = this;
        //ActionBar自定义布局
        ActionBar ab = getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //界面布局
        LinearLayout ll_actionbar = new LinearLayout(mContext);
        ll_actionbar.setOrientation(LinearLayout.HORIZONTAL);
        ll_actionbar.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        iv_back = new ImageView(mContext);
        iv_back.setImageBitmap(MyBitmapUtils.getInstance(mContext).readBitMap(R.drawable.actionbar_back));
        iv_back.setPadding(MyDisplayUtils.dp2px(mContext, 8), 0, MyDisplayUtils.dp2px(mContext, 8), 0);
        iv_back.setOnClickListener(this);
        TextView tv_back = new TextView(mContext);
        tv_back.setText(R.string.find_password);
        tv_back.setTextColor(getResources().getColor(R.color.gray));
        tv_back.setTextSize(18);
        ll_actionbar.addView(iv_back, lp);
        ll_actionbar.addView(tv_back, lp);
        //设置进actionbar
        ab.setCustomView(ll_actionbar);

        //设置EditText的Drawable
        et_user = (EditText) findViewById(R.id.et_user);
        et_account = (EditText) findViewById(R.id.et_account);
        final Drawable drawableLeft_account = getResources().getDrawable(R.drawable.login_account);
        drawableLeft_account.setBounds(0, 0, MyDisplayUtils.dp2px(mContext, 25), MyDisplayUtils.dp2px(mContext, 25));
        et_user.setCompoundDrawables(drawableLeft_account, null, null, null);
        et_account.setCompoundDrawables(drawableLeft_account, null, null, null);

        btn_find_password = (Button) findViewById(R.id.btn_find_password);
        btn_find_password.setOnClickListener(this);

        mCustomDialog = new CustomDialog(this);
        mCustomDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 按返回键，逆动画销毁
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_top_in, R.anim.slide_bottom_out);//平移动画
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_find_password:
                mCustomDialog.show();
                break;
        }
        if (view == iv_back) {
            onBackPressed();
        }
    }
}

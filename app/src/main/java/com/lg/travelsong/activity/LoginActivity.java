package com.lg.travelsong.activity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.lg.travelsong.R;
import com.lg.travelsong.utils.MyCommonUtils;

/**
 * @author LuoYi on 2016/8/4
 */
public class LoginActivity extends BaseActivity implements View.OnTouchListener {
    private Context mContext;
    private EditText et_account;
    private EditText et_password;

    private boolean isCloseShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        ActionBar ab = getActionBar();
        ab.setTitle(R.string.login);
        mContext = this;

        //设置EditText的Drawable
        et_account = (EditText) findViewById(R.id.et_account);
        final Drawable drawableLeft_account = getResources().getDrawable(R.drawable.login_account);
        final Drawable drawableRight_account = getResources().getDrawable(R.drawable.login_close);
        drawableLeft_account.setBounds(0, 0, MyCommonUtils.dp2px(mContext, 25), MyCommonUtils.dp2px(mContext, 25));
        drawableRight_account.setBounds(0, 0, MyCommonUtils.dp2px(mContext, 20), MyCommonUtils.dp2px(mContext, 20));
        et_account.setCompoundDrawables(drawableLeft_account, null, null, null);
        //设置触摸监听（drawableRight_account清空文字）
        et_account.setOnTouchListener(this);
        //设置文字改变监听（没有文字不显示drawableRight_account）
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && !isCloseShow){
                    et_account.setCompoundDrawables(drawableLeft_account, null, drawableRight_account, null);
                    isCloseShow = true;
                } else if (charSequence.length() == 0 && isCloseShow) {
                    et_account.setCompoundDrawables(drawableLeft_account, null, null, null);
                    isCloseShow = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //设置焦点改变监听（没有焦点不显示drawableRight_account）
        et_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && ((EditText) view).getText().length() > 0){
                    et_account.setCompoundDrawables(drawableLeft_account, null, drawableRight_account, null);
                    isCloseShow = true;
                } else if (!hasFocus){
                    et_account.setCompoundDrawables(drawableLeft_account, null, null, null);
                    isCloseShow = false;
                }
            }
        });

        et_password = (EditText) findViewById(R.id.et_password);
        Drawable drawableLeft_password = getResources().getDrawable(R.drawable.login_password);
        drawableLeft_password.setBounds(0, 0, MyCommonUtils.dp2px(mContext, 25), MyCommonUtils.dp2px(mContext, 25));
        et_password.setCompoundDrawables(drawableLeft_password, null, null, null);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == et_account) {//et_account设置清除事件
            // et_account.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
            Drawable drawable = et_account.getCompoundDrawables()[2];
            //如果右边没有图片，不再处理
            if (drawable == null)
                return false;
            //如果不是按下事件，不再处理
            if (motionEvent.getAction() != MotionEvent.ACTION_UP)
                return false;
            //找到位置
            if (motionEvent.getX() > et_account.getWidth()
                    - et_account.getPaddingRight()
                    - drawable.getIntrinsicWidth()) {
                et_account.setText("");
            }
            return false;
        }
        return false;
    }
}

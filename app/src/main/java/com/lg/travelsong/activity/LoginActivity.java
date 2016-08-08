package com.lg.travelsong.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lg.travelsong.R;
import com.lg.travelsong.global.HttpConfig;
import com.lg.travelsong.utils.MyCommonUtils;
import com.lg.travelsong.utils.MyHttpUtils;
import com.lg.travelsong.utils.MyLogUtils;
import com.lg.travelsong.utils.MySPUtils;
import com.lg.travelsong.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author LuoYi on 2016/8/4
 */
public class LoginActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {
    private Context mContext;
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private CustomDialog mCustomDialog;
    private String usercode;
    private String userpassword;

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
                    // TODO: 2016/8/9 失去焦点，显示头像 
                }
            }
        });

        et_password = (EditText) findViewById(R.id.et_password);
        Drawable drawableLeft_password = getResources().getDrawable(R.drawable.login_password);
        drawableLeft_password.setBounds(0, 0, MyCommonUtils.dp2px(mContext, 25), MyCommonUtils.dp2px(mContext, 25));
        et_password.setCompoundDrawables(drawableLeft_password, null, null, null);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        mCustomDialog = new CustomDialog(this);
        mCustomDialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login :
                checkLogin();
                break;
        }
    }

    /**
     * 登录检验
     */
    private void checkLogin() {
        usercode = et_account.getText().toString();
        userpassword = et_password.getText().toString();

        if (usercode.equals("")) {
            CharSequence html1 = Html
                    .fromHtml("<font color='#83CFFC'>账号不能为空</color>");
            et_account.setError(html1);
            return;
        } else if (userpassword.equals("")) {
            Spanned html2 = Html
                    .fromHtml("<font color='#83CFFC'>密码不能为空</color>");
            et_password.setError(html2);
            return;
        } else {
            mCustomDialog.show();
            login();
        }
    }
    /**
     * 登录
     */
    private void login(){
        String param = "usercode=" + usercode + "&userpassword=" + userpassword;
        new MyHttpUtils(mContext, true).httpPost(HttpConfig.HOSTURL + HttpConfig.LOGIN, param, new MyHttpUtils.HttpCallBack() {

            @Override
            public void onSuccess(String result) {
                MyLogUtils.logi("SplashActivity-->onSuccess", result);
                String[] results = result.split(";getCookieWhenNeed:");
                try {
                    JSONObject jo = new JSONObject(results[0]);
                    if(jo.getBoolean("success")){
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("user", jo.getJSONObject("data").toString());
                        startActivity(intent);
                        MySPUtils.putString(mContext, "cookie", results[1].substring(7));
                    } else {
                        Toast.makeText(mContext, R.string.login_fail,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, R.string.login_wrong,Toast.LENGTH_SHORT).show();
                    MyLogUtils.logCatch("LoginActivity-->JSONObject", e.getMessage());
                }
                mCustomDialog.dismiss();
            }

            @Override
            public void onFailure(String failMsg) {
                Toast.makeText(mContext, R.string.login_wrong,Toast.LENGTH_SHORT).show();
                MyLogUtils.logi("SplashActivity-->onFailure", failMsg);
                mCustomDialog.dismiss();
            }
        });
    }
}
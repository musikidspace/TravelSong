package com.lg.travelsong.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.travelsong.R;
import com.lg.travelsong.global.HttpConfig;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyDisplayUtils;
import com.lg.travelsong.utils.MyHttpUtils;
import com.lg.travelsong.utils.MyLogUtils;
import com.lg.travelsong.utils.MySPUtils;
import com.lg.travelsong.utils.MyStringUtils;
import com.lg.travelsong.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册界面
 *
 * @author LuoYi on 2016/8/9
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private Intent intent;
    private EditText et_user;
    private EditText et_account;
    private EditText et_password;
    private EditText et_password_again;
    private Button btn_register;
    private ImageView iv_back;
    private CustomDialog mCustomDialog;
    private String user;
    private String usercode;
    private String userpassword;
    private String userpassword_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        tv_back.setText(R.string.register);
        tv_back.setTextColor(getResources().getColor(R.color.gray));
        tv_back.setTextSize(MyDisplayUtils.sp2px(mContext, 6));
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

        et_password = (EditText) findViewById(R.id.et_password);
        Drawable drawableLeft_password = getResources().getDrawable(R.drawable.login_password);
        drawableLeft_password.setBounds(0, 0, MyDisplayUtils.dp2px(mContext, 25), MyDisplayUtils.dp2px(mContext, 25));
        et_password.setCompoundDrawables(drawableLeft_password, null, null, null);

        et_password_again = (EditText) findViewById(R.id.et_password_again);
        et_password_again.setCompoundDrawables(drawableLeft_password, null, null, null);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        mCustomDialog = new CustomDialog(this);
        mCustomDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 按返回键，回到登录界面
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_top_in, R.anim.slide_bottom_out);//平移动画
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (checkRegister()) {
                    register();
                }
                break;
        }
        if (view == iv_back) {
            onBackPressed();
        }
    }

    /**
     * 注册前校验
     */
    private boolean checkRegister() {
        user = et_user.getText().toString();
        usercode = et_account.getText().toString();
        userpassword = et_password.getText().toString();
        userpassword_again = et_password_again.getText().toString();
        if (user.equals("")) {
            et_user.setError(Html
                    .fromHtml("<font color='#7FA2C2'>用户名不能为空</color>"));
            return false;
        }
        if ((!MyStringUtils.validPhone(usercode)) && (!MyStringUtils.validEmail(usercode))) {
            et_account.setError(Html
                    .fromHtml("<font color='#7FA2C2'>请输入正确的手机号或邮箱</color>"));
            return false;
        }
        if (userpassword.length() < 6) {
            et_password.setError(Html
                    .fromHtml("<font color='#7FA2C2'>密码不能小于6位</color>"));
            return false;
        }
        if (!userpassword.equals(userpassword_again)) {
            et_password_again.setError(Html
                    .fromHtml("<font color='#7FA2C2'>两次密码不一致</color>"));
            return false;
        }
        mCustomDialog.show();
        return true;
    }

    /**
     * 注册，成功后跳转至MainActivity
     */
    private void register() {
        // TODO: 2016/8/9 userpassword jni加密后提交
        String param = "user=" + user + "&usercode=" + usercode + "&userpassword=" + userpassword;
        new MyHttpUtils(mContext, true).httpSend(HttpConfig.HOSTURL + HttpConfig.REGISTER, MyHttpUtils.POST, param, new MyHttpUtils.HttpCallBack() {

            @Override
            public void onSuccess(String result) {
                MyLogUtils.logi("RegisterActivity-->onSuccess", result);
                String[] results = result.split(";getCookieWhenNeed:");
                try {
                    JSONObject jo = new JSONObject(results[0]);
                    if (jo.getBoolean("success")) {
                        intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("user", jo.getJSONObject("data").toString());
                        startActivity(intent);
                        finish();
                        MySPUtils.putString(mContext, "cookie", results[1].substring(7));
                    } else if (jo.getString("msg").contains("same usercode")) {
                        Toast.makeText(mContext, R.string.register_samecode, Toast.LENGTH_SHORT).show();
                    } else if (jo.getString("msg").contains("same userphone")) {
                        Toast.makeText(mContext, R.string.register_samephone, Toast.LENGTH_SHORT).show();
                    } else if (jo.getString("msg").contains("same useremail")) {
                        Toast.makeText(mContext, R.string.register_sameemial, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, R.string.register_fail, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, R.string.connect_wrong, Toast.LENGTH_SHORT).show();
                    MyLogUtils.logCatch("RegisterActivity-->JSONObject", e.getMessage());
                }
                mCustomDialog.dismiss();
            }

            @Override
            public void onFailure(String failMsg) {
                Toast.makeText(mContext, R.string.connect_wrong, Toast.LENGTH_SHORT).show();
                MyLogUtils.logi("RegisterActivity-->onFailure", failMsg);
                mCustomDialog.dismiss();
            }
        });
    }
}